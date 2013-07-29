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

package org.nightcode.gwt.selectio.client.jso;

import org.nightcode.gwt.selectio.shared.ItemPageProxy;
import org.nightcode.gwt.selectio.shared.ItemProxy;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

import java.util.ArrayList;
import java.util.List;

/**
 * ItemPage java script object.
 */
public class ItemPageJso extends JavaScriptObject implements ItemPageProxy {

  public static native ItemPageJso itemPageFromJson(String json) /*-{
    return eval('(' + json + ')');
  }-*/;

  // Overlay type zero argument constructor.
  protected ItemPageJso() {
  }

  public final native int getCount() /*-{
    return this.count;
  }-*/;

  public final native JsArray<ItemJso> items() /*-{
    return this.items;
  }-*/;

  @Override public final List<ItemProxy> getItems() {
    JsArray<ItemJso> items = items();
    List<ItemProxy> valueList = new ArrayList<ItemProxy>(items.length());
    for (int i = 0; i < items.length(); i++) {
      ItemJso jso = items.get(i);
      valueList.add(jso);
    }
    return valueList;
  }

  @Override public final native int getLength() /*-{
    return this.length;
  }-*/;

  @Override public final native String getQuery() /*-{
    return this.query;
  }-*/;

  @Override public final native int getStart() /*-{
    return this.start;
  }-*/;

  @Override public final native void setLength(int value) /*-{
    this['length'] = value;
  }-*/;

  @Override public final native void setQuery(String value) /*-{
    this['query'] = value;
  }-*/;

  @Override public final native void setStart(int value) /*-{
    this['start'] = value;
  }-*/;
}
