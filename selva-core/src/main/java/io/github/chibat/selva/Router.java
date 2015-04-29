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

package io.github.chibat.selva;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

/**
 * Router
 * 
 * Singleton
 * 
 * @author tomofumi
 */
public class Router {

  private final static Router instance = new Router();

  private final List<WebResource> webResources = new ArrayList<>();

  private Router() {
  }

  public static Router getInstance() {
    return instance;
  }

  synchronized public void add(WebResource webResource) {
    webResources.add(webResource);
  }

  // -------------------------

  public Optional<Executor> getExecutor(HttpServletRequest req) {
    return webResources.stream().map(wr -> wr.parse(req)).filter(pr -> pr.isPresent()).findFirst()
        .orElse(Optional.empty());
  }
}
