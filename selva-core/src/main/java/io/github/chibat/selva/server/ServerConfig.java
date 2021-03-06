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

public class ServerConfig {
  protected Integer port = null;
  private boolean webBrowserOpenFlag = true;

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public boolean isWebBrowserOpenFlag() {
    return webBrowserOpenFlag;
  }

  public void setWebBrowserOpenFlag(boolean webBrowserOpenFlag) {
    this.webBrowserOpenFlag = webBrowserOpenFlag;
  }
}
