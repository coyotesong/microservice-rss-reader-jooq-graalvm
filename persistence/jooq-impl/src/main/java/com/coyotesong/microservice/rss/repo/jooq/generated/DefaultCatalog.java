/*
 * This file is generated by jOOQ.
 */
package com.coyotesong.microservice.rss.repo.jooq.generated;


import com.coyotesong.microservice.rss.repo.jooq.generated.rome.Rome;
import com.coyotesong.microservice.rss.repo.jooq.generated.rss.Rss;
import org.jooq.Constants;
import org.jooq.Schema;
import org.jooq.impl.CatalogImpl;

import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class DefaultCatalog extends CatalogImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_CATALOG</code>
     */
    public static final DefaultCatalog DEFAULT_CATALOG = new DefaultCatalog();

    /**
     * The schema <code>rome</code>.
     */
    public final Rome ROME = Rome.ROME;

    /**
     * The schema <code>rss</code>.
     */
    public final Rss RSS = Rss.RSS;

    /**
     * No further instances allowed
     */
    private DefaultCatalog() {
        super("");
    }

    @Override
    public final List<Schema> getSchemas() {
        return Arrays.asList(
                Rome.ROME,
                Rss.RSS
        );
    }

    /**
     * A reference to the 3.18 minor release of the code generator. If this
     * doesn't compile, it's because the runtime library uses an older minor
     * release, namely: 3.18. You can turn off the generation of this reference
     * by specifying /configuration/generator/generate/jooqVersionReference
     */
    private static final String REQUIRE_RUNTIME_JOOQ_VERSION = Constants.VERSION_3_18;
}
