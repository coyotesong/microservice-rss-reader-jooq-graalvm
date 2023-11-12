package com.coyotesong.microservice.rss.repo.jooq.bindings;

import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DefaultBinding;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * jOOQ binding between Timestamp (database type) and Instant (java type)
 * <p>
 * Per the author of jOOQ we must use a Binding for this. If we use a Converter
 * the data will be truncated into a LocalDateTime internally at some point
 * prior to the Converter.
 * </p><p>
 * From https://www.jooq.org/doc/latest/manual/code-generation/custom-data-types/
 * From https://github.com/jOOQ/jOOQ/issues/5539
 * </p>
 */
public class TimestampToInstantBinding implements Binding<Timestamp, Instant> {
    private static final long serialVersionUID = 1L;

    private static final TimeZone UTC = TimeZone.getTimeZone(ZoneId.of(ZoneOffset.UTC.getId()));

    private final Converter<Timestamp, Instant> converter;
    private final Binding<Timestamp, Instant> delegate;

    public static class TimestampToInstantConverter implements Converter<Timestamp, Instant> {
        private static final long serialVersionUID = 1L;

        /**
         * Convert from {@code LocalDateTime} to {@code Instant}
         */
        @Override
        public Instant from(Timestamp databaseObject) {
            if (databaseObject == null) {
                return null;
            }
            return databaseObject.toInstant();
        }

        /**
         * Convert from {@code Instant} to {@code Timestamp}
         */
        @Override
        public Timestamp to(Instant userObject) {
            if (userObject == null) {
                return null;
            }
            return Timestamp.from(userObject);
        }

        /**
         * Return the 'from' Type Class (Database Type Class)
         */
        @Override
        @NotNull
        public Class<Timestamp> fromType() {
            return Timestamp.class;
        }

        /**
         * Return the 'to' Type Class (User type Class)
         */
        @Override
        @NotNull
        public Class<Instant> toType() {
            return Instant.class;
        }
    }

    public TimestampToInstantBinding() {
        this.converter = new TimestampToInstantConverter();
        this.delegate = DefaultBinding.binding(converter);
    }

    // The converter does all the work
    @Override
    @NotNull
    public Converter<Timestamp, Instant> converter() {
        return converter;
    }

    // Rending a bind variable for the binding context's value and casting it to the user type
    @Override
    public void sql(BindingSQLContext<Instant> ctx) throws SQLException {
        delegate.sql(ctx);
    }

    // Registering TIMESTAMP types for JDBC CallableStatement OUT parameters
    @Override
    public void register(BindingRegisterContext<Instant> ctx) throws SQLException {
        delegate.register(ctx);
    }

    // Getting a TIMESTAMP value from a JDBC ResultSet and converting that to an Instant
    @Override
    @SuppressWarnings("all") // we shouldn't close resultSet!
    public void get(BindingGetResultSetContext<Instant> ctx) throws SQLException {
        final Calendar calendar = Calendar.getInstance(UTC);

        final ResultSet resultSet = ctx.resultSet();
        final Timestamp timestamp = resultSet.getTimestamp(ctx.index(), calendar);

        if (timestamp == null) {
            ctx.value(null);
        } else {
            ctx.value(converter.from(timestamp));
        }
    }

    // Getting a TIMESTAMP value from a JDBC CallableStatement and converting that to an Instant
    @Override
    @SuppressWarnings("all") // we shouldn't close callableStatement!
    public void get(BindingGetStatementContext<Instant> ctx) throws SQLException {
        final Calendar calendar = Calendar.getInstance(UTC);

        final CallableStatement statement = ctx.statement();
        final Timestamp timestamp = statement.getTimestamp(ctx.index(), calendar);

        if (timestamp == null) {
            ctx.value(null);
        } else {
            ctx.value(converter.from(timestamp));
        }
    }

    // Converting the Instant to a TIMESTAMP value and setting that on a JDBC PreparedStatement
    @Override
    @SuppressWarnings("all") // we shouldn't close preparedStatement
    public void set(BindingSetStatementContext<Instant> ctx) throws SQLException {
        final Calendar calendar = Calendar.getInstance(UTC);
        final Instant value = ctx.value();
        final PreparedStatement statement = ctx.statement();

        if (value == null) {
            statement.setNull(ctx.index(), Types.TIMESTAMP);
        } else {
            final Timestamp timestamp = converter.to(value);
            statement.setTimestamp(ctx.index(), timestamp, calendar);
        }
    }

    // Getting a value from a JDBC SQLInput
    @Override
    public void get(BindingGetSQLInputContext<Instant> ctx) throws SQLException {
        final SQLInput input = ctx.input();
        final Timestamp timestamp = input.readTimestamp();

        if (timestamp == null) {
            ctx.value(null);
        } else {
            ctx.value(converter.from(timestamp));
        }
    }

    // Setting a value on a JDBC SQLOutput
    @Override
    public void set(BindingSetSQLOutputContext<Instant> ctx) throws SQLException {
        final SQLOutput output = ctx.output();
        final Instant instant = ctx.value();

        if (instant == null) {
            output.writeTimestamp(null);
        } else {
            output.writeTimestamp(converter.to(instant));
        }
    }
}
