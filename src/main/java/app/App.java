package app;

import io.jooby.Jooby;
import io.jooby.OpenAPIModule;
import static io.jooby.ExecutionMode.EVENT_LOOP;

public class App extends Jooby {
  App()
  {
    install(new OpenAPIModule());

    mvc(new Controller());
  }

  public static void main(final String[] args) {
    runApp(args, EVENT_LOOP, App::new);
  }

}
