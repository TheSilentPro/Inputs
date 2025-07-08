package com.github.thesilentpro.inputs.paper.parser;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Optional;

/**
 * A utility class that provides methods for parsing different numeric types from an Adventure {@link Component}.
 * The parsing methods return an {@link Optional} to handle invalid inputs gracefully, returning an
 * empty {@link Optional} if parsing fails.
 * <p>
 * This class includes methods for parsing {@link Number}, {@link Integer}, {@link Long}, {@link Float},
 * {@link Double}, and {@link Byte}. Each method attempts to extract plain text from a given {@link Component}
 * and returns an {@link Optional} containing the parsed value if successful, or an empty {@link Optional}
 * if the component is null or the text cannot be parsed.
 * </p>
 *
 * @author TheSilentPro (Silent)
 */
public final class NumberParser {

    // Private constructor to prevent instantiation
    private NumberParser() {
        throw new UnsupportedOperationException("Utility class.");
    }

    /**
     * Attempts to parse the given {@link Component} into a {@link Number}. This method can parse any number type,
     * such as integers, floating-point numbers, etc.
     *
     * @param component the component to parse
     * @return an {@link Optional} containing the parsed number if successful, or an empty {@link Optional}
     *         if the component is null or parsing fails
     */
    @NotNull
    public static Optional<Number> parse(@Nullable Component component) {
        String text = extract(component);
        if (text == null) {
            return Optional.empty();
        }

        try {
            return Optional.ofNullable(NumberFormat.getInstance().parse(text));
        } catch (ParseException e) {
            return Optional.empty();
        }
    }

    @NotNull
    public static Optional<Integer> parseInteger(@Nullable Component component) {
        String text = extract(component);
        if (text == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(Integer.parseInt(text));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @NotNull
    public static Optional<Long> parseLong(@Nullable Component component) {
        String text = extract(component);
        if (text == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(Long.parseLong(text));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @NotNull
    public static Optional<Float> parseFloat(@Nullable Component component) {
        String text = extract(component);
        if (text == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(Float.parseFloat(text));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @NotNull
    public static Optional<Double> parseDouble(@Nullable Component component) {
        String text = extract(component);
        if (text == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(Double.parseDouble(text));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @NotNull
    public static Optional<Byte> parseByte(@Nullable Component component) {
        String text = extract(component);
        if (text == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(Byte.parseByte(text));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Extracts plain string text from a {@link Component}, or returns null if the component is null.
     */
    @Nullable
    private static String extract(@Nullable Component component) {
        if (component == null) {
            return null;
        }
        return PlainTextComponentSerializer.plainText().serialize(component).trim();
    }

}
