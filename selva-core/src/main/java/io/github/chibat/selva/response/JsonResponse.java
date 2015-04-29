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

package io.github.chibat.selva.response;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonResponse<T> extends Response {

  protected final Object model;

  public JsonResponse(T model) {
    this.model = model;
  }

  public JsonResponse(String rootName, T model) {
    Map<String, T> map = new HashMap<>();
    map.put(rootName, model);
    this.type("text/javascript; charset=utf-8");
    this.model = map;
  }

  public void execute(HttpServletRequest request, HttpServletResponse response,
      ServletContext servletContext) throws Exception {

    response.setStatus(this.status());
    response.setContentType(this.type());
    new ObjectMapper().writeValue(response.getWriter(), model);
    response.flushBuffer();
  }
}
