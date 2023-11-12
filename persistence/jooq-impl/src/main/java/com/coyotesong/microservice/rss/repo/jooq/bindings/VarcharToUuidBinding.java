package com.coyotesong.microservice.rss.repo.jooq.bindings;

import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.impl.DefaultBinding;

import java.sql.*;
import java.util.UUID;

/**
 * jOOQ binding between String (database type) and UUID (java type)
 * <p>
 * Some, but not all, databases have a native UUID type. This provides a mapping
 * for the databases that do not have a native type.
 * </p><p>
 * From https://www.jooq.org/doc/latest/manual/code-generation/custom-data-types/
 * From https://github.com/jOOQ/jOOQ/issues/5539
 * </p>
 */
public class VarcharToUuidBinding implements Binding<String, UUID> {
    private static final long serialVersionUID = 1L;

    private final Converter<String, UUID> converter;
    private final Binding<String, UUID> delegate;

    public static class VarcharToUuidConverter implements Converter<String, UUID> {
        private static final long serialVersionUID = 1L;

        /**
         * Convert from {@code String} to {@code UUID}
         */
        @Override
        public UUID from(String databaseObject) {
            if (databaseObject == null) {
                return null;
            }
            return UUID.fromString(databaseObject);
        }

        /**
         * Convert from {@code UUID} to {@code String}
         */
        @Override
        public String to(UUID userObject) {
            if (userObject == null) {
                return null;
            }
            return userObject.toString();
        }

        /**
         * Return the 'from' Type Class (Database Type Class)
         */
        @Override
        @NotNull
        public Class<String> fromType() {
            return String.class;
        }

        /**
         * Return the 'to' Type Class (User type Class)
         */
        @Override
        @NotNull
        public Class<UUID> toType() {
            return UUID.class;
        }
    }

    @SuppressWarnings("unused")
    public VarcharToUuidBinding() {
        this.converter = new VarcharToUuidConverter();
        this.delegate = DefaultBinding.binding(converter);
    }

    // The converter does all the work
    @Override
    @NotNull
    public Converter<String, UUID> converter() {
        return converter;
    }

    // Rending a bind variable for the binding context's value and casting it to the user type
    @Override
    public void sql(BindingSQLContext<UUID> ctx) throws SQLException {
        delegate.sql(ctx);
    }

    // Registering VARCHAR types for JDBC CallableStatement OUT parameters
    @Override
    public void register(BindingRegisterContext<UUID> ctx) throws SQLException {
        delegate.register(ctx);
    }

    // Getting a VARCHAR value from a JDBC ResultSet and converting that to a UUID
    @Override
    @SuppressWarnings("all") // we shouldn't close resultSet!
    public void get(BindingGetResultSetContext<UUID> ctx) throws SQLException {
        final ResultSet resultSet = ctx.resultSet();
        final String value = resultSet.getString(ctx.index());

        if (value == null) {
            ctx.value(null);
        } else {
            ctx.value(converter.from(value));
        }
    }

    // Getting a VARCHAR value from a JDBC CallableStatement and converting that to a UUID
    @Override
    @SuppressWarnings("all") // we shouldn't close callableStatement!
    public void get(BindingGetStatementContext<UUID> ctx) throws SQLException {
        final CallableStatement statement = ctx.statement();
        final String value = statement.getString(ctx.index());

        if (value == null) {
            ctx.value(null);
        } else {
            ctx.value(converter.from(value));
        }
    }

    // Converting the UUID to a VARCHAR value and setting that on a JDBC PreparedStatement
    @Override
    @SuppressWarnings("all") // we shouldn't close preparedStatement!
    public void set(BindingSetStatementContext<UUID> ctx) throws SQLException {
        final UUID value = ctx.value();
        final PreparedStatement statement = ctx.statement();

        if (value == null) {
            statement.setNull(ctx.index(), Types.VARCHAR);
        } else {
            statement.setString(ctx.index(), converter.to(value));
        }
    }

    // Getting a value from a JDBC SQLInput
    @Override
    public void get(BindingGetSQLInputContext<UUID> ctx) throws SQLException {
        final SQLInput input = ctx.input();
        final String value = input.readString();

        if (value == null) {
            ctx.value(null);
        } else {
            ctx.value(converter.from(value));
        }
    }

    // Setting a value on a JDBC SQLOutput
    @Override
    public void set(BindingSetSQLOutputContext<UUID> ctx) throws SQLException {
        final SQLOutput output = ctx.output();
        final UUID uuid = ctx.value();

        if (uuid == null) {
            output.writeString(null);
        } else {
            output.writeString(converter.to(uuid));
        }
    }
}
