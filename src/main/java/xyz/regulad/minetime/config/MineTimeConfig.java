package xyz.regulad.minetime.config;

import com.cronutils.descriptor.CronDescriptor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.regulad.minetime.MineTimeBukkit;
import xyz.regulad.minetime.types.ScheduleSlot;

import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;

import static xyz.regulad.minetime.util.ChronoUtil.timeZoneIsValid;
import static xyz.regulad.minetime.util.PlaceholderAPIUtil.substitutePlaceholders;

public class MineTimeConfig {
    private final @NotNull MineTimeBukkit plugin;

    @Getter
    private @Nullable ZoneId timeZone = null;
    @Getter
    private @Nullable Locale locale = null;

    public MineTimeConfig(final @NotNull MineTimeBukkit plugin) {
        this.plugin = plugin;
    }

    // Config validity & loading

    public void reloadConfig() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        // Verify locale
        locale = getLocaleFromConfig();
        plugin.getLogger().info("Using locale \"" + locale.getDisplayName() + "\".");

        // Verify time zone
        timeZone = getTimeZoneFromConfig();
        plugin.getLogger().info("Using time zone \"" + timeZone.getDisplayName(TextStyle.FULL, locale) + "\".");
    }

    /**
     * Returns the time zone used to determine if a player can join. This can be set in the configuration, or it can be obtained from the system.
     * @return the time zone used to determine if a player can join
     */
    private @NotNull ZoneId getTimeZoneFromConfig() {
        final @Nullable String timezoneString = plugin.getConfig().getString("timezone");
        if (timezoneString == null) {
            return ZoneId.systemDefault();
        } else if (timeZoneIsValid(timezoneString)) {
            return ZoneId.of(timezoneString);
        } else {
            plugin.getLogger().warning("Invalid time zone \"" + timezoneString + "\" specified in configuration, using system default.");
            return ZoneId.systemDefault();
        }
    }

    /**
     * Returns the locale used to determine if a player can join. This can be set in the configuration, or it can be obtained from the system.
     * @return the locale used to determine if a player can join
     */
    private @NotNull Locale getLocaleFromConfig() {
        final @Nullable String localeString = plugin.getConfig().getString("locale");
        if (localeString == null) {
            return Locale.getDefault();
        } else {
            final @NotNull Locale locale = Locale.forLanguageTag(localeString);
            if (locale.getLanguage().isEmpty()) {
                plugin.getLogger().warning("Invalid locale \"" + localeString + "\" specified in configuration, using system default.");
                return Locale.getDefault();
            } else {
                return locale;
            }
        }
    }

    public @NotNull Locale localeOrDefault() {
        if (locale == null) {
            return Locale.getDefault();
        } else {
            return locale;
        }
    }

    public @NotNull CronDescriptor getCronDescriptor() {
        return CronDescriptor.instance(localeOrDefault());
    }

    // Kick message
    public @NotNull Component getKickMessage(final @NotNull Player player) {
        final @NotNull JoinConfiguration joiner = JoinConfiguration.separator(Component.newline());
        final @NotNull LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();

        final @NotNull ArrayList<@NotNull Component> components = new ArrayList<>();

        for (final @NotNull String line : plugin.getConfig().getStringList("message")) {
            components.add(serializer.deserialize(substitutePlaceholders(line, player)));
        }

        return Component.join(joiner, components.toArray(new Component[0]));
    }

    // Schedule slots
    public @NotNull List<ScheduleSlot> getScheduleSlots() {
        final @NotNull ArrayList<ScheduleSlot> slots = new ArrayList<>();

        for (final Map<?, ?> slotString : plugin.getConfig().getMapList("schedule")) {
            final @NotNull String cron = (String) slotString.get("cron");
            final @NotNull String time = (String) slotString.get("time");
            slots.add(new ScheduleSlot(cron, time));
        }

        return Collections.unmodifiableList(slots);
    }
}
