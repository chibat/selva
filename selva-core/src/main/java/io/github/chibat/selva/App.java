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

import io.github.chibat.selva.response.Forward;
import io.github.chibat.selva.response.JsonResponse;
import io.github.chibat.selva.response.RedirectResponse;
import io.github.chibat.selva.response.Response;
import io.github.chibat.selva.response.TemplateResponse;
import io.github.chibat.selva.response.TextResponse;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

public interface App {

  public static final Validator validator = Validation.buildDefaultValidatorFactory()
      .getValidator();

  public void init();

  public default WebResource resource(String path) {
    WebResource resource = new WebResource(path);
    Router.getInstance().add(resource);
    return resource;
  }

  public default Response text(CharSequence charSequence) {
    return new TextResponse(charSequence);
  }

  public default Response redirect(String path) {
    return new RedirectResponse(path);
  }

  public default Response forward(String path) {
    return new Forward(path);
  }

  public default <T> Response json(T model) {
    return new JsonResponse<>(model);
  }

  public default <T> Response json(String rootName, T model) {
    return new JsonResponse<>(rootName, model);
  }

  public default <T> Response template(String path) {
    return new TemplateResponse<T>(path);
  }

  public default <T> Response template(String path, T model) {
    return new TemplateResponse<>(path, model);
  }

  public default <T> Response template(String path, T model, Set<ConstraintViolation<T>> errors) {
    return new TemplateResponse<>(path, model, errors);
  }

  public default <T> Set<ConstraintViolation<T>> validate(T model) {
    return validator.validate(model);
  }
}
