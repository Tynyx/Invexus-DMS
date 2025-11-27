// app/db/Db.java
package app.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.util.Properties;
import javax.sql.DataSource;

/**
 * Provides access to the shared application {@link javax.sql.DataSource}.
 * <p>
 * This utility class is not instantiable.
 */
public final class Db {
    private static HikariDataSource ds;

    /**
     * Returns the configured application {@link javax.sql.DataSource}.
     *
     * @return the shared data source instance
     */
    public static DataSource get() {
        if (ds != null) return ds;
        try {
            Properties p = new Properties();
            try (InputStream in = Db.class.getResourceAsStream("/app.properties")) {
                p.load(in);
            }
            HikariConfig cfg = new HikariConfig();
            cfg.setJdbcUrl(p.getProperty("db.url"));
            cfg.setUsername(p.getProperty("db.user"));
            cfg.setPassword(p.getProperty("db.pass"));
            cfg.setMaximumPoolSize(Integer.parseInt(p.getProperty("db.pool.max","5")));
            cfg.setPoolName("InvexusPool");
            // sensible MySQL tweaks
            cfg.addDataSourceProperty("cachePrepStmts","true");
            cfg.addDataSourceProperty("prepStmtCacheSize","250");
            cfg.addDataSourceProperty("prepStmtCacheSqlLimit","2048");
            ds = new HikariDataSource(cfg);
            return ds;
        } catch (Exception e) {
            throw new RuntimeException("DB init failed", e);
        }
    }

    private Db() {}
}
