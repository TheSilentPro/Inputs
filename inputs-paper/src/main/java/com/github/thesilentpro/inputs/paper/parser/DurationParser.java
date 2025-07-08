package com.github.thesilentpro.inputs.paper.parser;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * A utility class that provides methods for parsing durations from Adventure {@link Component} representations.
 * The supported formats include combinations of years, months, weeks, days,
 * hours, minutes, seconds, etc., e.g. "1d 2h 30m".
 * <p>
 * Each method accepts a Component, extracts plain text, and attempts to parse into a Duration,
 * handling invalid parts via optional callbacks or returning an Optional.
 * </p>
 */
public final class DurationParser {

    @SuppressWarnings("SpellCheckingInspection")
    private static final Map<String, ChronoUnit> UNIT_MAP = Map.ofEntries(
            Map.entry("w", ChronoUnit.WEEKS),
            Map.entry("week", ChronoUnit.WEEKS),
            Map.entry("weeks", ChronoUnit.WEEKS),
            Map.entry("d", ChronoUnit.DAYS),
            Map.entry("day", ChronoUnit.DAYS),
            Map.entry("days", ChronoUnit.DAYS),
            Map.entry("halfdays", ChronoUnit.HALF_DAYS),
            Map.entry("halfday", ChronoUnit.HALF_DAYS),
            Map.entry("h", ChronoUnit.HOURS),
            Map.entry("hour", ChronoUnit.HOURS),
            Map.entry("hours", ChronoUnit.HOURS),
            Map.entry("m", ChronoUnit.MINUTES),
            Map.entry("minute", ChronoUnit.MINUTES),
            Map.entry("minutes", ChronoUnit.MINUTES),
            Map.entry("s", ChronoUnit.SECONDS),
            Map.entry("second", ChronoUnit.SECONDS),
            Map.entry("seconds", ChronoUnit.SECONDS),
            Map.entry("millis", ChronoUnit.MILLIS),
            Map.entry("milliseconds", ChronoUnit.MILLIS),
            Map.entry("micros", ChronoUnit.MICROS),
            Map.entry("microseconds", ChronoUnit.MICROS),
            Map.entry("nanos", ChronoUnit.NANOS),
            Map.entry("nanoseconds", ChronoUnit.NANOS),
            Map.entry("mo", ChronoUnit.MONTHS),
            Map.entry("month", ChronoUnit.MONTHS),
            Map.entry("months", ChronoUnit.MONTHS),
            Map.entry("y", ChronoUnit.YEARS),
            Map.entry("year", ChronoUnit.YEARS),
            Map.entry("years", ChronoUnit.YEARS),
            Map.entry("decade", ChronoUnit.DECADES),
            Map.entry("decades", ChronoUnit.DECADES),
            Map.entry("century", ChronoUnit.CENTURIES),
            Map.entry("centuries", ChronoUnit.CENTURIES),
            Map.entry("millennium", ChronoUnit.MILLENNIA),
            Map.entry("millennia", ChronoUnit.MILLENNIA),
            Map.entry("era", ChronoUnit.ERAS),
            Map.entry("forever", ChronoUnit.FOREVER)
    );

    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final PlainTextComponentSerializer SERIALIZER = PlainTextComponentSerializer.plainText();

    private DurationParser() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Parse a duration from a Component, returning null on failure or zero duration.
     */
    @Nullable
    public static Duration parse(@Nullable Component component, @Nullable Consumer<String> invalidUnitHandler, @Nullable Consumer<String> invalidNumberHandler) {
        String input = extract(component);
        if (input == null || input.isBlank()) {
            return null;
        }
        String[] parts = WHITESPACE.split(input.trim());
        Duration duration = Duration.ZERO;
        for (String part : parts) {
            int idx = 0;
            while (idx < part.length() && Character.isDigit(part.charAt(idx))) {
                idx++;
            }
            if (idx == 0) {
                if (invalidNumberHandler != null) invalidNumberHandler.accept(part);
                continue;
            }
            String numStr = part.substring(0, idx);
            String unitStr = part.substring(idx).toLowerCase();
            int amount;
            try {
                amount = Integer.parseInt(numStr);
            } catch (NumberFormatException e) {
                if (invalidNumberHandler != null) invalidNumberHandler.accept(numStr);
                continue;
            }
            ChronoUnit unit = UNIT_MAP.get(unitStr);
            if (unit != null) {
                duration = duration.plus(unit.getDuration().multipliedBy(amount));
            } else {
                if (invalidUnitHandler != null) invalidUnitHandler.accept(unitStr);
            }
        }
        return duration.isZero() ? null : duration;
    }

    public static Duration parse(@Nullable Component component, @Nullable Consumer<String> invalidUnitHandler) {
        return parse(component, invalidUnitHandler, null);
    }

    public static Duration parse(@Nullable Component component) {
        return parse(component, null, null);
    }

    /**
     * Safely parse into Optional, catching any unexpected exceptions.
     */
    public static Optional<Duration> parseSafely(@Nullable Component component, @Nullable Consumer<String> invalidUnitHandler, @Nullable Consumer<String> invalidNumberHandler) {
        try {
            return Optional.ofNullable(parse(component, invalidUnitHandler, invalidNumberHandler));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static Optional<Duration> parseSafely(@Nullable Component component, @Nullable Consumer<String> invalidUnitHandler) {
        return parseSafely(component, invalidUnitHandler, null);
    }

    public static Optional<Duration> parseSafely(@Nullable Component component) {
        return parseSafely(component, null, null);
    }

    @Nullable
    private static String extract(@Nullable Component component) {
        return component == null ? null : SERIALIZER.serialize(component);
    }

}
