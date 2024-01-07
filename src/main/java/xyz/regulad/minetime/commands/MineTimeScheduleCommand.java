package xyz.regulad.minetime.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import xyz.regulad.minetime.MineTimeBukkit;
import xyz.regulad.minetime.types.ScheduleSlot;
import xyz.regulad.minetime.util.MinetimePermissions;

import java.time.Duration;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Optional;

import static xyz.regulad.minetime.util.ChronoUtil.describeDuration;

public class MineTimeScheduleCommand implements CommandExecutor {
    private final @NotNull MineTimeBukkit plugin;

    public MineTimeScheduleCommand(final @NotNull MineTimeBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final @NotNull JoinConfiguration joiner = JoinConfiguration.separator(Component.newline());

        final @NotNull ArrayList<@NotNull Component> components = new ArrayList<>();

        if (!plugin.scheduleEnabled) {
            components.add(Component.text("The schedule is currently ").color(NamedTextColor.YELLOW).append(plugin.scheduleEnabled ? Component.text("enabled").color(NamedTextColor.GREEN) : Component.text("disabled").color(NamedTextColor.RED)));
        }
        if (sender.hasPermission(MinetimePermissions.BYPASS_PERMISSION)) {
            components.add(Component.text("The server is currently ").color(NamedTextColor.YELLOW).append(plugin.loginPermittedNow() ? Component.text("open").color(NamedTextColor.GREEN) : Component.text("closed").color(NamedTextColor.RED)));
        }
        if (plugin.loginPermittedNow()) {
            final @NotNull Optional<@NotNull Duration> timeUntilClose = plugin.getTimeRemainingForCurrentScheduleSlot();
            timeUntilClose.ifPresent(duration -> components.add(Component.text("The server will close in ").color(NamedTextColor.YELLOW).append(Component.text(describeDuration(duration)).color(NamedTextColor.RED))));
        } else {
            final @NotNull Optional<@NotNull Duration> timeUntilOpen = plugin.getTimeRemainingUntilNextScheduleSlot();
            timeUntilOpen.ifPresent(duration -> components.add(Component.text("The server will open in ").color(NamedTextColor.YELLOW).append(Component.text(describeDuration(duration)).color(NamedTextColor.GREEN))));
        }
        components.add(Component.text("Schedule:").color(NamedTextColor.YELLOW));
        components.add(Component.text("All times are in " + plugin.getNow().getZone().getDisplayName(TextStyle.FULL, plugin.config.localeOrDefault()) + ".").color(NamedTextColor.RED));
        // see https://github.com/jmrozanec/cron-utils/blob/d31697ec4c30d4f43a1b528e5781d1d00ea1c967/src/main/java/com/cronutils/model/time/ExecutionTime.java#L122
        for (final @NotNull ScheduleSlot slot : plugin.config.getScheduleSlots()) {
            // every 45 minutes, for 15 minutes
            // cron describe, duration describe
            final @NotNull String cronDescription = plugin.config.getCronDescriptor().describe(slot.getCron());
            final @NotNull String durationDescription = describeDuration(slot.getDuration());

            final @NotNull Component componentDash = Component.text(" - ").color(NamedTextColor.GRAY);
            final @NotNull Component componentDescription = Component.text(cronDescription + "; for " + durationDescription).color(NamedTextColor.WHITE);

            components.add(componentDash.append(componentDescription));
        }

        sender.sendMessage(Component.join(joiner, components.toArray(new Component[0])));
        return true;
    }
}
