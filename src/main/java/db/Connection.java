package db;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import com.github.jasync.sql.db.Configuration;
import com.github.jasync.sql.db.ConnectionPoolConfiguration;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.github.jasync.sql.db.postgresql.PostgreSQLConnection;
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory;
import com.typesafe.config.Config;

import io.jooby.Environment;
import io.jooby.EnvironmentOptions;

public class Connection {
  private static ConnectionPool<PostgreSQLConnection> connection;
  private static Config config = Environment.loadEnvironment(new EnvironmentOptions()).getConfig();

  static {
    final Configuration configuration = new Configuration(
      config.getString("database.username"),
      config.getString("database.host"),
      config.getInt("database.port"),
      config.getString("database.password"),
      config.getString("database.dbname")
    );
    final ConnectionPoolConfiguration connectionPoolConfiguration = new ConnectionPoolConfiguration(
      config.getString("database.host"),
      config.getInt("database.port"),
      config.getString("database.dbname"),
      config.getString("database.username"),
      config.getString("database.password"),
      20, // maxActiveConnections,
      TimeUnit.MINUTES.toMillis(15), // maxIdle,
      10_000, // maxQueueSize,
      TimeUnit.SECONDS.toMillis(30) // validationInterval
    );
    connection = new ConnectionPool<>(
        new PostgreSQLConnectionFactory(configuration), connectionPoolConfiguration);
  }

  public static CompletableFuture<com.github.jasync.sql.db.Connection> getConnection() {
    return connection.connect();
  }

  private Connection(){}
}
