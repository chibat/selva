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

import io.github.chibat.selva.response.Response;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class AbstractServletFilter implements Filter {

  protected final Router router = Router.getInstance();
  protected final StaticResourceHandler staticResourceHandler = new StaticResourceHandler(false);
  protected final Config config = Config.getInstance();

  protected ServletContext servletContext;
  Logger logger = LoggerFactory.getLogger(AbstractServletFilter.class);

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    this.servletContext = filterConfig.getServletContext();
    registerApp();
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    request.setCharacterEncoding(config.getRequestCharacterEncoding());
    try {
      execute((HttpServletRequest) request, (HttpServletResponse) response, chain);
    } catch (Exception e) {
      logger.error("filter cached exception.", e);
      throw Exceptions.wrap(e);
    }
  }

  protected void execute(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws Exception {

    Optional<Executor> executor = router.getExecutor(request);

    executor.ifPresent(it -> {
      try {
        Response res = it.filterChain.execute(new Request(request, it.pathParams));
        if (res != null) {
          res.execute(request, response, this.servletContext);
        }
      } catch (Exception e) {
        throw Exceptions.wrap(e);
      }
    });

    if (response.isCommitted()) {
      return;
    }

    if (this.staticResourceHandler.handle(request, response)) {
      return;
    }

    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
  }

  abstract protected void registerApp();

  /**
   * Super class の init メソッドでコールされる。
   * 
   * @param clazz
   *          App class
   * @return this
   */
  protected AbstractServletFilter register(Class<? extends App> clazz) {
    try {
      clazz.newInstance().init();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    return this;
  }

}
