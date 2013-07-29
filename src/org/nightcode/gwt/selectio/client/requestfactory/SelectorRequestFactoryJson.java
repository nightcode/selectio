/*
 * Copyright (C) 2012 The NightCode Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.nightcode.gwt.selectio.client.requestfactory;

import com.google.web.bindery.requestfactory.gwt.client.DefaultRequestTransport;
import com.google.web.bindery.requestfactory.shared.RequestTransport;

/**
 * Json implementation of SelectorRequestFactory.
 */
public class SelectorRequestFactoryJson implements SelectorRequestFactory {

  private final DefaultRequestTransport requestTransport;

  public SelectorRequestFactoryJson(String requestUrl) {
    requestTransport = new DefaultRequestTransport();
    requestTransport.setRequestUrl(requestUrl);
  }

  @Override public RequestTransport getRequestTransport() {
    return requestTransport;
  }

  @Override public ItemRequest itemRequest() {
    return new ItemRequestJson(this);
  }
}
