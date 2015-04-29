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

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

public class TemplateResponse<T> extends Response {

  protected static final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
  protected static final TemplateEngine engine = new TemplateEngine();

  protected final String path;
  protected final T model;
  protected final Set<ConstraintViolation<T>> errors;

  static {
    templateResolver.setTemplateMode("HTML5");
    templateResolver.setPrefix("META-INF/template/");
    templateResolver.setCharacterEncoding("UTF-8");
    templateResolver.setCacheable(false);
    engine.setTemplateResolver(templateResolver);
  }

  public TemplateResponse(String path) {
    this(path, null);
  }

  public TemplateResponse(String path, T model) {
    this(path, model, new HashSet<>());
  }

  public TemplateResponse(String path, T model, Set<ConstraintViolation<T>> errors) {
    this.path = path;
    this.model = model;
    this.errors = errors;
    type("text/html");
  }

  public void execute(HttpServletRequest request, HttpServletResponse response,
      ServletContext servletContext) throws Exception {
    WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
    if (this.model != null) {
      ctx.setVariable("model", this.model);
    }
    ctx.setVariable("errors", this.errors);
    response.setContentType(type());
    response.setStatus(HttpServletResponse.SC_OK);
    OutputStream os = response.getOutputStream();
    try (PrintWriter pw = new PrintWriter(os)) {
      engine.process(path, ctx, pw);
      os.flush();
    }
  }
}
