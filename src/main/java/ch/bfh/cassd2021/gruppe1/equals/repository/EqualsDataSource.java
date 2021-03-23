package ch.bfh.cassd2021.gruppe1.equals.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Datasource for accessing DB.
 *
 * provided by Peter Feuz
 */
public class EqualsDataSource {

    private static final HikariConfig config = new HikariConfig();
    private static final HikariDataSource ds;

    // Infos zu statischem Initializer siehe https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
    static {
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/equalsdb");
        config.setUsername("root");
        config.setPassword("root");
        config.setPoolName("equalsdb");
        config.addDataSourceProperty("autoCommit", "true");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("characterEncoding", "UTF-8");
        config.addDataSourceProperty("useUnicode", "true");
        config.addDataSourceProperty("serverTimezone", "UTC");
        ds = new HikariDataSource(config);
    }

    private EqualsDataSource() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
