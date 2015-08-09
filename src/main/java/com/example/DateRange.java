package com.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.seasar.doma.Domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Domain(valueType = String.class, factoryMethod = "of")
public class DateRange {
    private static final Pattern REGEXP = Pattern.compile("^(?:\\[([0-9]{4}-[0-9]{2}-[0-9]{2})?,|\\(,)([0-9]{4}-[0-9]{2}-[0-9]{2})?\\)$");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    public static final DateRange empty = new DateRange("empty", Optional.empty(), Optional.empty());

    private final String value;
    private final Optional<LocalDate> begin;
    private final Optional<LocalDate> end;

    private DateRange(String value, Optional<LocalDate> begin, Optional<LocalDate> end) {
        this.value = value;
        this.begin = begin;
        this.end = end;
    }
 
    @JsonValue
    public String getValue() {
        return value;
    }
    public Optional<LocalDate> getBegin() {
        return begin;
    }
    public Optional<LocalDate> getEnd() {
        return end;
    }

    @JsonCreator
    public static DateRange of(String value) {
        if (value == null) return null;

        if (value.equals("empty")) return empty;

        Matcher matcher = REGEXP.matcher(value);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(value + " is not a date range.");
        }
        Optional<LocalDate> begin = Optional.ofNullable(matcher.group(1)).map(c -> LocalDate.parse(c, FORMATTER));
        Optional<LocalDate> end = Optional.ofNullable(matcher.group(2)).map(c -> LocalDate.parse(c, FORMATTER));

        if (isIllegalRange(begin.orElse(null), end.orElse(null))) {
            throw new IllegalArgumentException(String.format("Illegal DateRange specified: %s, %s", begin, end));
        }

        if (isEmpty(begin.orElse(null), end.orElse(null))) {
            return empty;
        }

        return new DateRange(value, begin, end);
    }

    private static boolean isIllegalRange(LocalDate begin, LocalDate end) {
        if (begin == null || end == null) return false;
        return begin.compareTo(end) > 0;
    }

    private static boolean isEmpty(LocalDate begin, LocalDate end) {
        if (begin == null && end == null) return false;
        return Objects.equals(begin, end);
    }
}
