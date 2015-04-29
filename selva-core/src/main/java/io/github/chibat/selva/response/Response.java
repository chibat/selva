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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

abstract public class Response {

  protected int status = HttpServletResponse.SC_OK;
  protected String type = "text/plain; charset=utf-8";

  abstract public void execute(HttpServletRequest request, HttpServletResponse response,
      ServletContext servletContext) throws Exception;

  public Response status(int status) {
    this.status = status;
    return this;
  }

  public int status() {
    return this.status;
  }

  public Response type(String type) {
    this.type = type;
    return this;
  }

  public String type() {
    return this.type;
  }
}
