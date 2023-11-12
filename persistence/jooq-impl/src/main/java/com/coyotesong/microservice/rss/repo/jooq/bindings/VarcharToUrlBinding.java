package com.coyotesong.microservice.rss.repo.jooq.bindings;

import org.jetbrains.annotations.NotNull;
import org.jooq.*;
import org.jooq.exception.DataTypeException;
import org.jooq.impl.DefaultBinding;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;

/**
 * jOOQ binding between String (database type) and URL (java type)
 */
public class VarcharToUrlBinding implements Binding<String, URL> {
    private static final long serialVersionUID = 1L;

    private final Converter<String, URL> converter;
    private final Binding<String, URL> delegate;

    public static class VarcharToUrlConverter implements ContextConverter<String, URL> {
        private static final long serialVersionUID = 1L;

        /**
         * Convert from {@code String} to {@code URL}
         */
        @Override
        public URL from(String url) {
            if (url == null) {
                return null;
            }
            try {
                return new URL(url);
            } catch (MalformedURLException e) {
                throw new DataTypeException(String.format(
                        "Content is mot a valid URL: '%s' : %s", url, e.getMessage()));
            }
        }

        @Override
        public URL from(String url, ConverterContext converterContext) {
            return from(url);
        }

        /**
         * Convert from {@code URL} to {@code String}
         */
        @Override
        public String to(URL userObject) {
            if (userObject == null) {
                return null;
            }
            return userObject.toString();
        }

        @Override
        public String to(URL url, ConverterContext converterContext) {
            return to(url);
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
        public Class<URL> toType() {
            return URL.class;
        }
    }

    @SuppressWarnings("unused")
    public VarcharToUrlBinding() {
        this.converter = new VarcharToUrlConverter();
        this.delegate = DefaultBinding.binding(converter);
    }

    // The converter does all the work
    @Override
    @NotNull
    public Converter<String, URL> converter() {
        return converter;
    }

    // Rending a bind variable for the binding context's value and casting it to the user type
    @Override
    public void sql(BindingSQLContext<URL> ctx) throws SQLException {
        delegate.sql(ctx);
    }

    // Registering VARCHAR types for JDBC CallableStatement OUT parameters
    @Override
    public void register(BindingRegisterContext<URL> ctx) throws SQLException {
        delegate.register(ctx);
    }

    // Getting a VARCHAR value from a JDBC ResultSet and converting that to a URL
    @Override
    @SuppressWarnings("all") // we shouldn't close resultSet!
    public void get(BindingGetResultSetContext<URL> ctx) throws SQLException {
        final ResultSet resultSet = ctx.resultSet();
        final String value = resultSet.getString(ctx.index());

        if (value == null) {
            ctx.value(null);
        } else {
            ctx.value(converter.from(value));
        }
    }

    // Getting a VARCHAR value from a JDBC CallableStatement and converting that to a URL
    @Override
    @SuppressWarnings("all") // we shouldn't close callableStatement!
    public void get(BindingGetStatementContext<URL> ctx) throws SQLException {
        final CallableStatement statement = ctx.statement();
        final String value = statement.getString(ctx.index());

        if (value == null) {
            ctx.value(null);
        } else {
            ctx.value(converter.from(value));
        }
    }

    // Converting the URL to a VARCHAR value and setting that on a JDBC PreparedStatement
    @Override
    @SuppressWarnings("all") // we shouldn't close preparedStatement!
    public void set(BindingSetStatementContext<URL> ctx) throws SQLException {
        final URL value = ctx.value();
        final PreparedStatement statement = ctx.statement();

        if (value == null) {
            statement.setNull(ctx.index(), Types.VARCHAR);
        } else {
            statement.setString(ctx.index(), converter.to(value));
        }
    }

    // Getting a value from a JDBC SQLInput
    @Override
    public void get(BindingGetSQLInputContext<URL> ctx) throws SQLException {
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
    public void set(BindingSetSQLOutputContext<URL> ctx) throws SQLException {
        final SQLOutput output = ctx.output();
        final URL uuid = ctx.value();

        if (uuid == null) {
            output.writeString(null);
        } else {
            output.writeString(converter.to(uuid));
        }
    }
}
