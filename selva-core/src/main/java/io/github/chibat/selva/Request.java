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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Request {

  private final List<String> pathParams;

  private final HttpServletRequest raw;

  public Request(HttpServletRequest raw, List<String> pathParams) {
    this.raw = raw;
    this.pathParams = pathParams;
  }

  public String pathParam() {
    return pathParams.get(0);
  }

  public String pathParams(int index) {
    return pathParams.get(index);
  }

  public String params(String name) {
    return raw.getParameter(name);
  }

  public Map<String, String> params() {
    Map<String, String> map = new HashMap<>();
    Enumeration<String> parameterNames = raw.getParameterNames();
    while (parameterNames.hasMoreElements()) {
      String name = parameterNames.nextElement();
      map.put(name, raw.getParameter(name));
    }
    return map;
  }

  public void session(String name, Object value) {
    raw.getSession().setAttribute(name, value);
  }

  @SuppressWarnings("unchecked")
  public <T> T session(String name) {
    return (T) raw.getSession().getAttribute(name);
  }

  @SuppressWarnings("unchecked")
  public <T> T session(Class<T> clazz) {
    return (T) raw.getSession().getAttribute(clazz.getName());
  }

  public void attribute(String name, Object value) {
    raw.setAttribute(name, value);
  }

  @SuppressWarnings("unchecked")
  public <T> T attribute(String name) {
    return (T) raw.getAttribute(name);
  }

  @SuppressWarnings("unchecked")
  public <T> T attribute(Class<T> clazz) {
    return (T) raw.getAttribute(clazz.getName());
  }

  public HttpServletRequest raw() {
    return raw;
  }

  public <T> T bean(Class<T> clazz) {
    return new ObjectMapper().convertValue(this.params(), clazz);
  }
}
