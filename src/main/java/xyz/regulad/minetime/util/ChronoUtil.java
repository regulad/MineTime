package xyz.regulad.minetime.util;

import com.cronutils.model.Cron;
import com.cronutils.model.field.CronFieldName;
import org.jetbrains.annotations.NotNull;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.Duration;
import java.util.Collection;

public final class ChronoUtil {
    private ChronoUtil() {}

    public static final @NotNull ZonedDateTime NEVER_PAST = ZonedDateTime.of(0, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
    public static final @NotNull ZonedDateTime NEVER_FUTURE = ZonedDateTime.of(9999, 12, 31, 23, 59, 59, 999999999, ZoneId.of("UTC"));

    /**
     * Returns whether or not a given time zone is valid.
     * @param timeZone the time zone to check
     * @return whether or not a given time zone is valid
     */
    public static boolean timeZoneIsValid(final @NotNull String timeZone) {
        try {
            ZoneId.of(timeZone);
            return true;
        } catch (final @NotNull Exception exception) {
            return false;
        }
    }

    public static @NotNull String describeDuration(final @NotNull Duration duration) {
        final @NotNull StringBuilder stringBuilder = new StringBuilder();

        final long days = duration.toDays();
        final long hours = duration.toHours() - (days * 24);
        final long minutes = duration.toMinutes() - (days * 24 * 60) - (hours * 60);
        final long seconds = duration.getSeconds() - (days * 24 * 60 * 60) - (hours * 60 * 60) - (minutes * 60);

        if (days > 0) {
            stringBuilder.append(days).append(" day").append(days == 1 ? "" : "s");
        }
        if (hours > 0) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(hours).append(" hour").append(hours == 1 ? "" : "s");
        }
        if (minutes > 0) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(minutes).append(" minute").append(minutes == 1 ? "" : "s");
        }
        if (seconds > 0) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(", ");
            }
            stringBuilder.append(seconds).append(" second").append(seconds == 1 ? "" : "s");
        }
        return stringBuilder.toString();
    }
}
