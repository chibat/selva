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

public class FirstApp implements App {
  public void init() {
    // resource("/").get(req -> text("Hello, World!"));

    //@formatter:off
    resource("/users/(.+)")
    .get(req -> text("Show " + req.pathParam()))
    .post(req -> text("Update " + req.pathParam()));
    //@formatter:on
  }

  public static void main(String[] args) {
    Server server = new Server();
    server.add(FirstApp.class);
    server.listen();
  }
}
