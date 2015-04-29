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

package io.github.chibat.selva.server;

import io.github.chibat.selva.App;

public class Server {

  protected final EmbeddedServer embeddedServer;

  public Server() {
    Class<EmbeddedServer> clazz = null;

    //
    // for Jetty
    //
    try {
      // TODO TomcatEmbeddedServer, UndertowEmbeddedServer
      clazz = this.forName("io.github.chibat.selva.server.jetty.JettyEmbeddedServer");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    try {
      this.embeddedServer = clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  public Server add(Class<? extends App> clazz) {
    try {
      clazz.newInstance().init();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

  public void listen(ServerConfig serverConfig) {
    this.embeddedServer.listen(serverConfig);
  }

  public void listen() {
    this.listen(new ServerConfig());
  }

  public Server staticFilePath(String path) {
    throw new RuntimeException("Not implement.");
    // return this;
  }

  @SuppressWarnings("unchecked")
  protected Class<EmbeddedServer> forName(String name) throws ClassNotFoundException {
    return (Class<EmbeddedServer>) Class.forName(name);
  }

}
