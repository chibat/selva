
# Selva

* selva-core [ ![Download](https://api.bintray.com/packages/chibat/maven/selva-core/images/download.svg) ](https://bintray.com/chibat/maven/selva-core/_latestVersion)
* selva-jetty [ ![Download](https://api.bintray.com/packages/chibat/maven/selva-jetty/images/download.svg) ](https://bintray.com/chibat/maven/selva-jetty/_latestVersion)

Selva is a tiny Web framework for Java 8.

## Minimal Example 

```java
public class FirstApp implements App {
  public void init() {
    resource("/users/(.+)")
    .get(req -> text("Show " + req.pathParam()))
    .post(req -> text("Update " + req.pathParam()));
  }

  public static void main(String[] args) {
    Server server = new Server();
    server.add(FirstApp.class);
    server.listen();
  }
}
```

## More Example

```java
public class ExampleApp implements App {

  public void init() {

    resource("/").get(req -> template("index.html"));

    resource("/users/(.+)")
    .get(req -> text("Show " + req.pathParam()))
    .post(req -> text("Update " + req.pathParam()));

    resource("/blog/(.+)/(.+)/(.+)")
    .get(req -> text("Show - Year: " + req.pathParams(0) + ", Month: " + req.pathParams(1) + ", Day: " + req.pathParams(2)))
    .post(req -> text("Update - Year: " + req.pathParams(0) + ", Month: " + req.pathParams(1) + ", Day: " + req.pathParams(2)));

    resource("/template")
    .get(req -> template("/template.html", new Model()))
    .post(req -> {
      Model model = req.bean(Model.class);
      Set<ConstraintViolation<Model>> errors = validate(model);
      if (errors.isEmpty()) {
        model.result = Integer.parseInt(model.arg1) + Integer.parseInt(model.arg2);
      }
      return template("/template.html", model, errors);
    });

    resource("/json/map").get(req -> {
      Map<String, String> map = new HashMap<>();
      map.put("name", "chiba");
      map.put("age", "10");
      return json(map);
    });

    resource("/json/bean").get(req -> json(new Bean()));
    resource("/forward").get(req -> forward("/json/bean"));
    resource("/redirect/outer").get(req -> redirect("http://www.google.com"));
    resource("/redirect/inner").get(req -> redirect("/json/bean"));

    resource("/session").post(req -> {
      req.session("sessionData", req.params("sessionData"));
      return text("setted to session: " + req.session("sessionData"));
    }).get(req -> text(req.session("sessionData")));

    resource("/filter").get(req -> text("Hello, World!"), new LogFilter());
  }

  public static class Bean {

    private String name = "chiba";
    private int age = 30;

    public String getName() {
      return name;
    }

    public int getAge() {
      return age;
    }
  }

  public static class Model {

    @NotBlank
    @Digits(fraction = 0, integer = Integer.MAX_VALUE)
    public String arg1 = "";

    @NotBlank
    @Digits(fraction = 0, integer = Integer.MAX_VALUE)
    public String arg2 = "";

    public Integer result;
  }

  public static void main(String[] args) {
    Server server = new Server();
    server.add(ExampleApp.class);
    server.listen();
  }
}
```

## Selva Example

### Run

```
$ gradlew selva-example:run
```

This command will start up server.
Also it will open the web browser.

### Make a executable jar and Run

Unix
```
$ gradlew selva-example:jar
$ java -Dfile.encoding=UTF-8 -jar selva-example/build/libs/selva-example-0.0.1.jar
```
Windows
```
> gradlew selva-example:jar
> java -Dfile.encoding=UTF-8 -jar selva-example\build\libs\selva-example-0.0.1.jar
```

Also, see the [wiki](https://github.com/chibat/selva/wiki).

That's it.

