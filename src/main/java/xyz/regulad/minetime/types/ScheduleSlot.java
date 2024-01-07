package xyz.regulad.minetime.types;

import com.cronutils.model.Cron;
import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Optional;

public record ScheduleSlot(@NotNull String cron, @NotNull String time) {
    private final static @NotNull CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
    private final static @NotNull CronParser cronParser = new CronParser(cronDefinition);

    public @NotNull Cron getCron() {
        return cronParser.parse(cron).validate();
    }

    public @NotNull ExecutionTime getExecutionTime() {
        return ExecutionTime.forCron(getCron());
    }

    public @NotNull Duration getDuration() {
        return Duration.parse(time);
    }

    public boolean timeIsInSlot(final @NotNull ZonedDateTime dateTime) {
        // if a cronjob occurs after (current time - duration) and before (current time), then it is in the slot
        final @NotNull ZonedDateTime before = dateTime.minus(getDuration());
        // an execution of the cron must occur between before and the datetime
        return getExecutionTime().nextExecution(before).map(zonedDateTime -> zonedDateTime.isBefore(dateTime)).orElse(false);
        // note: map only runs if the optional is present, making this code slightly more efficient
    }
}
