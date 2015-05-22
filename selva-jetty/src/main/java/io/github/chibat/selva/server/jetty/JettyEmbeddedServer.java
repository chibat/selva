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

package io.github.chibat.selva.server.jetty;

import io.github.chibat.selva.Exceptions;
import io.github.chibat.selva.server.DefaultServletFilter;
import io.github.chibat.selva.server.EmbeddedServer;
import io.github.chibat.selva.server.ServerConfig;

import java.net.BindException;
import java.util.EnumSet;
import java.util.stream.IntStream;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;

public class JettyEmbeddedServer implements EmbeddedServer {

  public void listen(ServerConfig serverConfig) {

    Server server = new Server();

    ServletHandler servletHandler = new ServletHandler();

    servletHandler.addFilterWithMapping(DefaultServletFilter.class, "/*", EnumSet.of(
        DispatcherType.REQUEST, DispatcherType.INCLUDE, DispatcherType.ERROR, DispatcherType.ASYNC,
        DispatcherType.FORWARD));

    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");
    context.setHandler(servletHandler);
    server.setHandler(context);

    ServerConnector connector = new ServerConnector(server);
    server.setConnectors(new Connector[] { connector });

    try {
      if (serverConfig.getPort() == null) {
        IntStream.range(0, SERACH_PORT_TIMES).anyMatch(i -> {
          serverConfig.setPort(EmbeddedServer.DEFAULT_PORT + i);
          connector.setPort(serverConfig.getPort());
          try {
            server.start();
          } catch (BindException ignore) {
            return false;
          } catch (Exception e) {
            throw Exceptions.wrap(e);
          }
          return true;
        });

      } else {
        connector.setPort(serverConfig.getPort());
        server.start();
      }
    } catch (Exception e) {
      throw Exceptions.wrap(e);
    }
    openWebBrowser(serverConfig.getPort());
    try {
      server.join();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
