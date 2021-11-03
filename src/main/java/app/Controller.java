package app;

import io.jooby.annotations.Path;

import com.github.jasync.sql.db.Configuration;
import com.github.jasync.sql.db.ConnectionPoolConfiguration;
import com.github.jasync.sql.db.QueryResult;
import com.github.jasync.sql.db.pool.ConnectionPool;
import com.github.jasync.sql.db.postgresql.PostgreSQLConnection;
import com.github.jasync.sql.db.postgresql.pool.PostgreSQLConnectionFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jooby.annotations.GET;

@Path("/")
public class Controller {

  private static Logger logger = LoggerFactory.getLogger(Controller.class);

  @GET
  public CompletableFuture<String> sayHi() throws ExecutionException, InterruptedException {
    Configuration configuration = new Configuration("postgres", "localhost", 5432, "password", "jooby_development");
    ConnectionPoolConfiguration connectionPoolConfiguration = new ConnectionPoolConfiguration("localhost", 5432,
        "jooby_development", "postgres", "password", 20, // maxActiveConnections,
        TimeUnit.MINUTES.toMillis(15), // maxIdle,
        10_000, // maxQueueSize,
        TimeUnit.SECONDS.toMillis(30) // validationInterval
    );
    ConnectionPool<PostgreSQLConnection> connection = new ConnectionPool<>(
        new PostgreSQLConnectionFactory(configuration), connectionPoolConfiguration);
    connection.connect().get();
    final CompletableFuture<QueryResult> future = connection.sendPreparedStatement("SELECT * FROM users LIMIT 2");
    return future.thenApply(t -> {
      logger.debug("Name: " + t.getRows().get(0).get(0) + " Email: " + t.getRows().get(0).get(1));
      return "Name: " + t.getRows().get(0).get(0) + " Email: " + t.getRows().get(0).get(1);
    });
  }
}
