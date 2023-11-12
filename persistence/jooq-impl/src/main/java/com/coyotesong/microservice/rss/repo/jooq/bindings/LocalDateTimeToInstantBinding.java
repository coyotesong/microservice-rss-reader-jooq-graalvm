package com.coyotesong.microservice.rss.repo.jooq.bindings;

import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DefaultBinding;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * jOOQ binding between Timestamp (database type) and LocalDateTime (java type)
 */
public class LocalDateTimeToInstantBinding implements Binding<LocalDateTime, Instant> {
    private static final long serialVersionUID = 1L;

    private static final ZoneId ZONE_ID_UTC = ZoneOffset.UTC;
    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone(ZONE_ID_UTC);

    private final Converter<LocalDateTime, Instant> converter;
    private final Binding<LocalDateTime, Instant> delegate;

    public static class LocalDateTimeToInstantConverter implements ContextConverter<LocalDateTime, Instant> {
        private static final long serialVersionUID = 1L;

        @Override
        public Instant from(LocalDateTime localDateTime) {
            if (localDateTime == null) {
                return null;
            }
            return localDateTime.atZone(ZONE_ID_UTC).toInstant();
        }

        @Override
        public Instant from(LocalDateTime localDateTime, ConverterContext converterContext) {
            return from(localDateTime);
        }

        @Override
        public LocalDateTime to(Instant instant) {
            if (instant == null) {
                return null;
            }
            return LocalDateTime.ofInstant(instant, ZONE_ID_UTC);
        }

        @Override
        public LocalDateTime to(Instant instant, ConverterContext converterContext) {
            return to(instant);
        }

        @Override
        public @NotNull Class<LocalDateTime> fromType() {
            return LocalDateTime.class;
        }

        @Override
        public @NotNull Class<Instant> toType() {
            return Instant.class;
        }
    }

    public LocalDateTimeToInstantBinding() {
        this.converter = new LocalDateTimeToInstantConverter();
        this.delegate = DefaultBinding.binding(converter);
    }

    // The converter does all the work
    @Override
    @NotNull
    public Converter<LocalDateTime, Instant> converter() {
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
    @SuppressWarnings("all") // we shouldn't close CallableStatement!
    public void get(BindingGetResultSetContext<Instant> ctx) throws SQLException {
        final ResultSet resultSet = ctx.resultSet();
        final Calendar calendar = Calendar.getInstance(TIMEZONE_UTC);
        final Timestamp timestamp = resultSet.getTimestamp(ctx.index(), calendar);

        if (timestamp == null) {
            ctx.value(null);
        } else {
            ctx.value(converter.from(timestamp.toLocalDateTime()));
        }
    }

    // Getting a TIMESTAMP value from a JDBC CallableStatement and converting that to an Instant
    @Override
    @SuppressWarnings("all") // we shouldn't close CallableStatement!
    public void get(BindingGetStatementContext<Instant> ctx) throws SQLException {
        final CallableStatement statement = ctx.statement();
        final Calendar calendar = Calendar.getInstance(TIMEZONE_UTC);
        final Timestamp timestamp = statement.getTimestamp(ctx.index(), calendar);

        if (timestamp == null) {
            ctx.value(null);
        } else {
            ctx.value(converter.from(timestamp.toLocalDateTime()));
        }
    }

    // Converting the Instant to a TIMESTAMP value and setting that on a JDBC PreparedStatement
    @Override
    @SuppressWarnings("all") // we shouldn't close PreparedStatement!
    public void set(BindingSetStatementContext<Instant> ctx) throws SQLException {
        final Instant instant = ctx.value();
        final PreparedStatement statement = ctx.statement();

        if (instant == null) {
            statement.setNull(ctx.index(), Types.TIMESTAMP);
        } else {
            final Calendar calendar = Calendar.getInstance(TIMEZONE_UTC);
            statement.setTimestamp(ctx.index(), Timestamp.valueOf(converter.to(instant)), calendar);
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
            ctx.value(converter.from(timestamp.toLocalDateTime()));
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
            output.writeTimestamp(Timestamp.valueOf(converter.to(instant)));
        }
    }
}
