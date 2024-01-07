package xyz.regulad.minetime;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.regulad.minetime.config.MineTimeConfig;
import xyz.regulad.minetime.listener.MineTimeListener;
import xyz.regulad.minetime.runnable.KickRunnable;
import xyz.regulad.minetime.types.ScheduleSlot;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Optional;

import static xyz.regulad.minetime.util.ChronoUtil.NEVER_FUTURE;
import static xyz.regulad.minetime.util.ChronoUtil.NEVER_PAST;
import static xyz.regulad.minetime.util.PlaceholderAPIUtil.isPlaceholderAPIInstalled;

public class MineTimeBukkit extends JavaPlugin {
    public final @NotNull MineTimeConfig config = new MineTimeConfig(this);
    public final @NotNull MineTimeListener listener = new MineTimeListener(this);

    public final @NotNull KickRunnable kickRunnable = new KickRunnable(this);
    public @Nullable BukkitTask kickTask = null;

    public volatile boolean scheduleEnabled = true;

    @Override
    public void onEnable() {
        config.reloadConfig();
        getServer().getPluginManager().registerEvents(listener, this);
        loadPlaceholderAPI();
        loadCommands();
        kickTask = getServer().getScheduler().runTaskTimer(this, kickRunnable, 0, 20 * 60);
    }

    public void loadPlaceholderAPI() {
        if (!isPlaceholderAPIInstalled()) {
            getLogger().warning("PlaceholderAPI not found, disabling placeholders.");
            return;
        }

        getLogger().info("PlaceholderAPI found, enabling placeholders.");
        final @NotNull xyz.regulad.minetime.placeholders.MineTimePlaceholderExpansion placeholderExpansion = new xyz.regulad.minetime.placeholders.MineTimePlaceholderExpansion(this);
        placeholderExpansion.register();
    }

    public void loadCommands() {
        final @Nullable PluginCommand mtreload = getServer().getPluginCommand("mtreload");
        if (mtreload != null) {
            mtreload.setExecutor(new xyz.regulad.minetime.commands.MineTimeReloadCommand(this));
        }

        final @Nullable PluginCommand mtschedule = getServer().getPluginCommand("mtschedule");
        if (mtschedule != null) {
            mtschedule.setExecutor(new xyz.regulad.minetime.commands.MineTimeScheduleCommand(this));
        }

        final @Nullable PluginCommand mton = getServer().getPluginCommand("mton");
        if (mton != null) {
            mton.setExecutor(new xyz.regulad.minetime.commands.MineTimeOnCommand(this));
        }

        final @Nullable PluginCommand mtoff = getServer().getPluginCommand("mtoff");
        if (mtoff != null) {
            mtoff.setExecutor(new xyz.regulad.minetime.commands.MineTimeOffCommand(this));
        }
    }

    /**
     * Returns whether or not a player can join at a given time according to the rules set in the configuration.
     * @param time the time to check
     * @return whether or not a player can join at a given time
     */
    public boolean loginPermittedDuringTime(final @NotNull ZonedDateTime time) {
        if (!scheduleEnabled) {
            return true;
        }

        final boolean result = config.getScheduleSlots().stream().anyMatch(scheduleSlot -> scheduleSlot.timeIsInSlot(time));

        return getConfig().getBoolean("invert") != result; // invert if necessary
    }

    public @NotNull ZonedDateTime getNow() {
        final @Nullable ZoneId timeZone = config.getTimeZone();
        return timeZone != null ? ZonedDateTime.now(timeZone) : ZonedDateTime.now();
    }

    public boolean loginPermittedNow() {
        return loginPermittedDuringTime(getNow());
    }

    private @NotNull Optional<@NotNull ScheduleSlot> getNextScheduleSlotForTime(final @NotNull ZonedDateTime time) {
        return config.getScheduleSlots().stream().min(Comparator.comparing(s -> s.getExecutionTime().nextExecution(time).orElse(NEVER_FUTURE))).filter(s -> s.getExecutionTime().nextExecution(time).isPresent());
    }

    private @NotNull Optional<@NotNull Duration> getTimeRemainingUntilNextScheduleSlotForTime(final @NotNull ZonedDateTime time) {
        return getNextScheduleSlotForTime(time).map(scheduleSlot -> Duration.between(time, scheduleSlot.getExecutionTime().nextExecution(time).orElse(NEVER_FUTURE)));
    }

    public @NotNull Optional<@NotNull Duration> getTimeRemainingUntilNextScheduleSlot() {
        return getTimeRemainingUntilNextScheduleSlotForTime(getNow());
    }

    private @NotNull Optional<@NotNull ScheduleSlot> getCurrentScheduleSlotForTime(final @NotNull ZonedDateTime time) {
        return config.getScheduleSlots().stream().max(Comparator.comparing(s -> s.getExecutionTime().lastExecution(time).orElse(NEVER_PAST))).filter(s -> s.getExecutionTime().lastExecution(time).isPresent());
    }

    private @NotNull Optional<@NotNull Duration> getTimeRemainingForCurrentScheduleSlotForTime(final @NotNull ZonedDateTime time) {
        return getCurrentScheduleSlotForTime(time).map(scheduleSlot -> Duration.between(scheduleSlot.getExecutionTime().lastExecution(time).orElse(NEVER_PAST), time));
    }

    public @NotNull Optional<@NotNull Duration> getTimeRemainingForCurrentScheduleSlot() {
        return getTimeRemainingForCurrentScheduleSlotForTime(getNow());
    }
}
