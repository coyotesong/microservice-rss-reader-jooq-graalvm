package com.coyotesong.microservice.rss.service.rome.converters;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Converter interface modeled on jOOQ's Converter.
 * <p>
 * We don't use the jOOQ interface in order to eliminate an unnecessary dependency.
 * </p><p>
 * Note: the 'to' and 'from' are backwards from what we would expect since it's database-oriented.
 * </p>
 *
 * @param <T>
 * @param <U>
 */
public interface SyndConverter<T, U> {
    @Nullable
    U from(@Nullable T t);

    @Nullable
    default T to (@Nullable U u) {
        throw new AssertionError("unimplemented");
    }

    /**
     * Return the 'from' Type Class
     */
    @NotNull
    Class<T> fromType();

    /**
     * Return the 'to' Type Class
     */
    @NotNull
    Class<U> toType();
}