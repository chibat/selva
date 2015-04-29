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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StaticResourceHandler {

  protected static final String STATIC_RESOURCE_ROOT = "/META-INF/resources";
  protected static final long DEFAULT_EXPIRE_TIME_MS = 86400000L; // 1 day
  protected static final long DEFAULT_EXPIRE_TIME_S = 86400L; // 1 day

  protected final boolean disableCache;

  public StaticResourceHandler(boolean disableCache) {
    this.disableCache = disableCache;
  }

  protected boolean handle(HttpServletRequest request, HttpServletResponse response)
      throws IOException {

    String contextPath = request.getContextPath();

    String resourceURI = STATIC_RESOURCE_ROOT
        + request.getRequestURI().replaceFirst(contextPath == null ? "" : contextPath, "");

    if (resourceURI.charAt(resourceURI.length() - 1) == '/') {
      resourceURI += "index.html";
    }

    try (InputStream inputStream = this.getClass().getResourceAsStream(resourceURI)) {
      if (inputStream == null) {
        return false;
      }

      if (!disableCache) {
        prepareCacheHeaders(response, resourceURI);
      }
      String filename = getFileName(resourceURI);
      String mimeType = request.getSession().getServletContext().getMimeType(filename);
      response.setContentType(mimeType != null ? mimeType : "application/octet-stream");
      copy(inputStream, response.getOutputStream());
    }
    return true;
  }

  /**
   *
   * @param resourceURI
   * @return
   */
  protected String getFileName(String resourceURI) {
    String[] tokens = resourceURI.split("/");
    return tokens[tokens.length - 1];
  }

  /**
   *
   * @param response
   * @param resourceURI
   */
  protected void prepareCacheHeaders(HttpServletResponse response, String resourceURI) {
    String[] tokens = resourceURI.split("/");
    String fileName = tokens[tokens.length - 1];

    String eTag = fileName;
    response.setHeader("ETag", eTag);
    response.setDateHeader("Expires", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME_MS);
    response.addHeader("Cache-Control", "private, max-age=" + DEFAULT_EXPIRE_TIME_S);
  }

  /**
   * The default buffer size ({@value} ) to use
   */
  protected static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

  protected static final int EOF = -1;

  /**
   * Copy bytes from an <code>InputStream</code> to an <code>OutputStream</code>
   * .
   * <p>
   * This method buffers the input internally, so there is no need to use a
   * <code>BufferedInputStream</code>.
   * <p>
   * Large streams (over 2GB) will return a bytes copied value of
   * <code>-1</code> after the copy has completed since the correct number of
   * bytes cannot be returned as an int. For large streams use the
   * <code>copyLarge(InputStream, OutputStream)</code> method.
   *
   * @param input
   *          the <code>InputStream</code> to read from
   * @param output
   *          the <code>OutputStream</code> to write to
   * @return the number of bytes copied, or -1 if &gt; Integer.MAX_VALUE
   * @throws NullPointerException
   *           if the input or output is null
   * @throws IOException
   *           if an I/O error occurs
   */
  protected static int copy(InputStream input, OutputStream output) throws IOException {
    long count = 0;
    int n = 0;
    byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
    while (EOF != (n = input.read(buffer))) {
      output.write(buffer, 0, n);
      count += n;
    }
    if (count > Integer.MAX_VALUE) {
      return -1;
    }
    return (int) count;
  }
}
