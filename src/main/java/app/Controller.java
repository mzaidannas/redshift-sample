package app;

import io.jooby.annotations.Path;
import utils.ZonedDateTimeAdapter;
import io.jooby.annotations.GET;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.github.jasync.sql.db.QueryResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;

import db.Connection;
import entities.User;

@Path("/")
public class Controller {

  // private static Logger logger = LoggerFactory.getLogger(Controller.class);

  @GET
  public CompletableFuture<List<String>> sayHi() {
    final CompletableFuture<QueryResult> future = Connection.getConnection()
        .thenCompose(conn -> conn.sendPreparedStatement("SELECT * FROM users"));

    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
        .enableComplexMapKeySerialization()
        .create();
    return future.thenApply(t -> t.getRows().stream()
        .map(row -> new User(row.getInt("id"), row.getString("name"), row.getString("email"),
            ((OffsetDateTime) row.getAs("created_at")).atZoneSameInstant(ZoneId.systemDefault()),
            ((OffsetDateTime) row.getAs("updated_at")).atZoneSameInstant(ZoneId.systemDefault())))
        .map(gson::toJson).toList());
  }
}
