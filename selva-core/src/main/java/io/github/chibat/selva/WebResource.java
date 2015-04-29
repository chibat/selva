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
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;

public class WebResource {

  protected final Pattern pathlPattern;
  protected final Map<String, FilterChain> map = new ConcurrentHashMap<>();

  public WebResource(String pathPattern) {
    this.pathlPattern = Pattern.compile(pathPattern);
  }

  public WebResource get(Handler handler, Filter... fs) {
    map.put(HttpMethod.get.name(), createFilterChain(handler, fs));
    return this;
  }

  public WebResource post(Handler handler, Filter... fs) {
    map.put(HttpMethod.post.name(), createFilterChain(handler, fs));
    return this;
  }

  public WebResource put(Handler handler) {
    map.put(HttpMethod.put.name(), createFilterChain(handler));
    return this;
  }

  public WebResource delete(Handler handler) {
    map.put(HttpMethod.delete.name(), createFilterChain(handler));
    return this;
  }

  public WebResource connect(Handler handler) {
    map.put(HttpMethod.connect.name(), createFilterChain(handler));
    return this;
  }

  public WebResource head(Handler handler) {
    map.put(HttpMethod.head.name(), createFilterChain(handler));
    return this;
  }

  public WebResource options(Handler handler) {
    map.put(HttpMethod.options.name(), createFilterChain(handler));
    return this;
  }

  public WebResource patch(Handler handler) {
    map.put(HttpMethod.patch.name(), createFilterChain(handler));
    return this;
  }

  public WebResource trace(Handler handler) {
    map.put(HttpMethod.trace.name(), createFilterChain(handler));
    return this;
  }

  public Optional<Executor> parse(HttpServletRequest req) {
    final String path = req.getServletPath();
    final Matcher m = pathlPattern.matcher(path);
    if (m.matches()) {
      FilterChain filterChain = map.get(req.getMethod().toLowerCase());
      if (filterChain != null) {
        final List<String> params = new ArrayList<>();
        IntStream.range(1, m.groupCount() + 1).forEach(i -> {
          params.add(m.group(i));
        });
        return Optional.of(new Executor(params, filterChain));
      }
    }
    return Optional.empty();
  }

  protected FilterChain createFilterChain(Handler handler, Filter... fs) {
    FilterChain lastFilterChain = new FilterChain(handler, null, null);
    FilterChain chain = lastFilterChain;
    for (int i = fs.length - 1; i >= 0; i--) {
      chain = new FilterChain(handler, fs[i], chain);
    }
    return chain;
  }
}