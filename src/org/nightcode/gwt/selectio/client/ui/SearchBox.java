/*
 * Copyright (C) 2012 The NightCode Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.nightcode.gwt.selectio.client.ui;

import org.nightcode.gwt.selectio.client.Selector;

import com.google.gwt.event.dom.client.HasKeyUpHandlers;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Search box.
 */
public class SearchBox extends ComplexPanel implements HasText, HasKeyUpHandlers {

  private final TextBox queryField = new TextBox();

  public SearchBox() {
    setElement(DOM.createDiv());

    setStyleName("input-group");

    Element spanImage = DOM.createSpan();
    spanImage.setClassName("input-group-addon");
    DOM.appendChild(getElement(), spanImage);

    Element image = DOM.createSpan();
    image.setClassName("icon-search");
    DOM.appendChild(spanImage, image);

    queryField.setStyleName("form-control");
    queryField.setFocus(true);
    queryField.selectAll();
    queryField.setText("");
    DOM.setElementAttribute(queryField.getElement(), "id", "appendedPrependedInput");
    DOM.setElementAttribute(queryField.getElement(), "placeholder", Selector.MESSAGES.search());
    add(queryField, getElement());
  }

  @Override public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
    return queryField.addKeyUpHandler(handler);
  }

  @Override public String getText() {
    return queryField.getText();
  }

  @Override public void setText(String text) {
    queryField.setText(text);
  }
}
