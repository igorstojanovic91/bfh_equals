package ch.bfh.cassd2021.gruppe1.equals.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class EqualsDataSource {

  private static final HikariConfig config = new HikariConfig();
  private static final HikariDataSource ds;

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
    ds = new HikariDataSource(config);
  }

  private EqualsDataSource() {
  }

  public static Connection getConnection() throws SQLException {
    return ds.getConnection();
  }
}
