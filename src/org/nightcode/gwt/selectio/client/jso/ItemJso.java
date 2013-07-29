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

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Item java script object.
 */
public class ItemJso extends JavaScriptObject implements ItemProxy {

  // Overlay type zero argument constructor.
  protected ItemJso() {
    // do nothing
  }

  public static native ItemJso create() /*-{
    return { };
  }-*/;

  public final native String getDescription() /*-{
    return this.description;
  }-*/;

  public final native String getDisplayName() /*-{
    return this.displayName;
  }-*/;

  public final native String getId() /*-{
    return this.id;
  }-*/;

  public final native void setDescription(String value) /*-{
    this['description'] = value;
  }-*/;

  public final native void setDisplayName(String value) /*-{
    this['displayName'] = value;
  }-*/;

  public final native void setId(String value) /*-{
    this['id'] = value;
  }-*/;
}
