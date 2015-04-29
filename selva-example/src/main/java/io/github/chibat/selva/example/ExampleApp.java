/*
 * Copyright 2015- Tomofumi Chiba
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.chibat.selva.example;

import io.github.chibat.selva.App;
import io.github.chibat.selva.server.Server;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.NotBlank;

public class ExampleApp implements App {

  public void init() {

    resource("/").get(req -> template("index.html"));

    //@formatter:off

    resource("/users/(.+)")
    .get(req -> text("Show " + req.pathParam()))
    .post(req -> text("Update " + req.pathParam()));

    resource("/blog/(.+)/(.+)/(.+)")
    .get(req -> text("Show - Year: " + req.pathParams(0) + ", Month: " + req.pathParams(1) + ", Day: " + req.pathParams(2)))
    .post(req -> text("Update - Year: " + req.pathParams(0) + ", Month: " + req.pathParams(1) + ", Day: " + req.pathParams(2)));

    resource("/template")
    .get(req -> template("template.html", new Model()))
    .post(req -> {
      Model model = req.bean(Model.class);
      Set<ConstraintViolation<Model>> errors = validate(model);
      if (errors.isEmpty()) {
        model.result = Integer.parseInt(model.arg1) + Integer.parseInt(model.arg2);
      }
      StringBuilder builder = new StringBuilder();
      for (ConstraintViolation<Model> error : errors) {
        builder.append(error.getPropertyPath() + ": " + error.getMessage()
            + System.lineSeparator());
      }
      System.out.println(builder);
      return template("template.html", model, errors);
    });

    //@formatter:on

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
