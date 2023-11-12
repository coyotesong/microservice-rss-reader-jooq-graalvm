package com.coyotesong.microservice.rss.repo.jooq;

import com.coyotesong.microservice.rss.repo.Repository;
import org.jetbrains.annotations.NotNull;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Table;
import org.jooq.UpdatableRecord;
import org.jooq.exception.IntegrityConstraintViolationException;
import org.jooq.impl.DAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Optional;

/**
 * Abstract generic extension to the standard DAOImpl class.
 *
 * @param <R> The generic record type.
 * @param <P> The generic POJO type.
 * @param <T> The generic primary key type. This is a regular &lt;T&gt; type for single-column keys, or a Record subtype for composite keys.
 */
public abstract class AbstractRepositoryImpl<R extends UpdatableRecord<R>, P, T> extends DAOImpl<R, P, T> implements Repository<P, T> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRepositoryImpl.class);

    @NotNull
    protected final DSLContext dsl;
    @NotNull
    protected final Table<R> table;

    protected AbstractRepositoryImpl(@NotNull DSLContext dsl, @NotNull Table<R> table, Class<P> type) {
        super(table, type);
        this.dsl = dsl;
        this.table = table;
    }

    protected AbstractRepositoryImpl(@NotNull DSLContext dsl, @NotNull Table<R> table, Class<P> type, Configuration configuration) {
        super(table, type, configuration);
        this.dsl = dsl;
        this.table = table;
    }

    @Override
    public abstract T getId(P p);

    @Override
    public abstract void clear();

    @Override
    public long count() {
        return super.count();
    }

    @Override
    public boolean existsById(T id) {
        return super.existsById(id);
    }

    @Override
    public P findById(T id) {
        return super.findById(id);
    }

    @Override
    public @NotNull Optional<@NotNull P> findOptionalById(@NotNull T id) {
        return super.findOptionalById(id);
    }

    /**
     * Convenience method that records information about an integrity constraint violation
     * closer to the source - it reduces the size of the stacktrace.
     *
     * @param e IntegrityConstraintViolationException
     */
    protected void logIntegrityException(IntegrityConstraintViolationException e) {
        try (Writer w = new FileWriter("/tmp/integrity.txt", true);
             PrintWriter pw = new PrintWriter(w)) {
            pw.printf("%s\n\n--\n\n", e.getMessage());
        } catch (IOException ex) {
            LOG.error("error logging error! {}", ex.getMessage());
        }
    }
}