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

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface EmbeddedServer {

  public static final Logger logger = LoggerFactory.getLogger(EmbeddedServer.class);

  void listen(ServerConfig serverConfig);

  public default void openWebBrowser(int port) {
    String url = "http://localhost:" + port;
    try {
      Desktop.getDesktop().browse(new URI(url));
    } catch (IOException | URISyntaxException ignore) {
      logger.warn("Can not opened Web browser.", ignore);
    }
  }
}
