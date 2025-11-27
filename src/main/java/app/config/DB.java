package app.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

/**
 * Central database configuration. Exposes a pooled{@link javax.sql.DataSource}
 * backed by HikariCP. Reads env vars and falls back to sane local defaults.
 */
public final class DB {
    private static final HikariDataSource DS;

    static {
        HikariConfig c = new HikariConfig();
        String url  = System.getenv().getOrDefault("INVEXUS_DB_URL",
                "jdbc:mysql://localhost:3306/invexus_dms?useSSL=true&serverTimezone=UTC");
        String user = System.getenv().getOrDefault("INVEXUS_DB_USER", "invexus");
        String pass = System.getenv().getOrDefault("INVEXUS_DB_PASS", "Admin123!");

        c.setJdbcUrl(url);
        c.setUsername(user);
        c.setPassword(pass);
        c.setMaximumPoolSize(5);
        c.setPoolName("InvexusPool");
        DS = new HikariDataSource(c);
    }

    private DB() {}

    /**
     * Provides the shared Hikari {@link javax.sql.DataSource}.
     * @return configured data source
     */
    public static DataSource dataSource() { return DS; }
}
