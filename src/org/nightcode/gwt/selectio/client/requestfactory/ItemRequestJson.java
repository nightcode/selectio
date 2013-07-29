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

import org.nightcode.gwt.selectio.client.jso.ItemPageJso;
import org.nightcode.gwt.selectio.shared.ItemPageProxy;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.google.web.bindery.requestfactory.shared.RequestTransport;
import com.google.web.bindery.requestfactory.shared.ServerFailure;

/**
 * Json implementation of ItemRequest.
 */
public class ItemRequestJson implements SelectorRequestFactory.ItemRequest {

  private final SelectorRequestFactory selectorRequestFactory;

  public ItemRequestJson(SelectorRequestFactory selectorRequestFactory) {
    this.selectorRequestFactory = selectorRequestFactory;
  }

  @Override public Request<ItemPageProxy> getItems(final String query, final int start,
      final int length) {
    final JSONObject requestObject = new JSONObject();

    if (query != null) {
      requestObject.put("query", new JSONString(query));
    }
    requestObject.put("start", new JSONNumber(start));
    requestObject.put("length", new JSONNumber(length));

    return new Request<ItemPageProxy>() {
      @Override public void fire(final Receiver<? super ItemPageProxy> receiver) {
        selectorRequestFactory.getRequestTransport()
            .send(requestObject.toString(), new RequestTransport.TransportReceiver() {
              @Override public void onTransportSuccess(String payload) {
                ItemPageProxy itemPage = ItemPageJso.itemPageFromJson(payload);
                itemPage.setQuery(query);
                itemPage.setStart(start);
                itemPage.setLength(length);
                receiver.onSuccess(itemPage);
              }

              @Override public void onTransportFailure(ServerFailure failure) {
                receiver.onFailure(failure);
              }
            });
      }
    };
  }
}
