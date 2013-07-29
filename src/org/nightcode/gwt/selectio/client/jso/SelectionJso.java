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

import org.nightcode.gwt.selectio.shared.ItemProxy;
import org.nightcode.gwt.selectio.shared.SelectionProxy;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Selection java script object.
 */
public class SelectionJso extends JavaScriptObject implements SelectionProxy {

  // Overlay type zero argument constructor.
  protected SelectionJso() {
    // do nothing
  }

  public static native SelectionJso create() /*-{
    return {};
  }-*/;

  @Override public final Map<ItemProxy, Boolean> getExceptions() {
    JsArray<ItemExceptionJso> exc = getItemExceptions();
    Map<ItemProxy, Boolean> exceptions = new HashMap<ItemProxy, Boolean>();
    for (int i = exc.length() - 1; i >= 0; i--) {
      ItemExceptionJso item = exc.get(i);
      exceptions.put(item.getItem(), item.isSelected());
    }
    return exceptions;
  }

  public final native JsArray<ItemExceptionJso> getItemExceptions() /*-{
    return this.exceptions;
  }-*/;

  public final native JsArrayString getQueries() /*-{
    return this.queries;
  }-*/;

  @Override public final List<String> getSearchQueries() {
    JsArrayString queries = getQueries();
    List<String> searchQueries = new ArrayList<String>();
    for (int i = queries.length() - 1; i >= 0; i--) {
      searchQueries.add(queries.get(i));
    }
    return searchQueries;
  }

  public final native String getType() /*-{
    return this.type;
  }-*/;

  @SuppressWarnings("unchecked")
  @Override public final void setExceptions(Map<ItemProxy, Boolean> exceptions) {
    JsArray<ItemExceptionJso> array = (JsArray<ItemExceptionJso>) JsArray.createArray();
    for (Map.Entry<ItemProxy, Boolean> entry : exceptions.entrySet()) {
      ItemExceptionJso itemExceptionJso = ItemExceptionJso.create();
      ItemJso itemJso = ItemJso.create();
      itemJso.setId(entry.getKey().getId());
      itemJso.setDisplayName(entry.getKey().getDisplayName());
      itemExceptionJso.setItem(itemJso);
      itemExceptionJso.setSelected(entry.getValue());
      array.push(itemExceptionJso);
    }
    setSelected(array);
  }

  @Override public final void setSearchQueries(List<String> searchQueries) {
    JsArrayString array = (JsArrayString) JsArrayString.createArray();
    for (int i = searchQueries.size() - 1; i >= 0; i--) {
      array.push(searchQueries.get(i));
    }
    setQueries(array);
  }

  public final native void setQueries(JsArrayString value) /*-{
    this['queries'] = value;
  }-*/;

  public static native SelectionJso selectionFromJson(String json) /*-{
    return eval('(' + json + ')');
  }-*/;

  public final native void setSelected(JsArray array) /*-{
    this['exceptions'] = array;
  }-*/;

  @Override public final native void setType(String value) /*-{
    this['type'] = value;
  }-*/;
}
