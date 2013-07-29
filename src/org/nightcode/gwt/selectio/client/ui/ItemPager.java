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

package org.nightcode.gwt.selectio.client.ui;

import org.nightcode.gwt.selectio.client.SelectorMessages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.AbstractPager;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;

/**
 * A pager for controlling a {@link com.google.gwt.user.cellview.client.CellList}
 * that only supports simple page navigation.
 */
public class ItemPager extends AbstractPager {

  private final SelectorMessages messages;

  private final Element label = DOM.createSpan();

  /**
   * Set to true when the next and last buttons are disabled.
   */
  private boolean nextDisabled;

  private final Anchor nextPage;

  /**
   * Set to true when the prev and first buttons are disabled.
   */
  private boolean prevDisabled;

  private final Anchor prevPage;

  public ItemPager(SelectorMessages messages) {
    this.messages = messages;

    // Create the links.
    nextPage = new Anchor(messages.older());
    prevPage = new Anchor(messages.newer());

    nextPage.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        nextPage();
      }
    });
    prevPage.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        previousPage();
      }
    });

    // Construct the widget.
    FlowPanel layout = new FlowPanel();
    initWidget(layout);
    setStyleName("slt-pager");

    layout.add(prevPage);
    appendInnerText(layout.getElement(), " ");
    DOM.appendChild(layout.getElement(), label);
    appendInnerText(layout.getElement(), " ");
    layout.add(nextPage);

    // Add style names to the cells.
    prevPage.setStyleName("btn btn-link");
    label.addClassName("label");
    nextPage.setStyleName("btn btn-link");
  }

  @Override protected boolean hasNextPage() {
    HasRows display = getDisplay();
    if (display == null) {
      return false;
    }
    Range range = display.getVisibleRange();
    return range.getStart() + range.getLength() < display.getRowCount();
  }

  @Override protected void onRangeOrRowCountChanged() {
    label.setInnerText(createText());

    // Update the prev and first buttons.
    boolean hasPrev = hasPreviousPage();
    if (hasPrev && prevDisabled) {
      prevDisabled = false;
      prevPage.getElement().removeClassName("btn-disabled");
    } else if (!hasPrev && !prevDisabled) {
      prevDisabled = true;
      prevPage.getElement().addClassName("btn-disabled");
    }

    // Update the next and last buttons.
    if (isRangeLimited()) {
      boolean hasNext = hasNextPage();
      if (hasNext && nextDisabled) {
        nextDisabled = false;
        nextPage.getElement().removeClassName("btn-disabled");
      } else if (!hasNext && !nextDisabled) {
        nextDisabled = true;
        nextPage.getElement().addClassName("btn-disabled");
      }
    }
  }

  /**
   * Get the text to display in the pager that reflects the state of the pager.
   *
   * @return the text
   */
  protected String createText() {
    HasRows display = getDisplay();
    int dataSize = display.getRowCount();
    int pageStart = 0;
    int endIndex = 0;
    if (dataSize != 0) {
      Range range = display.getVisibleRange();
      pageStart = range.getStart() + 1;
      int pageSize = range.getLength();
      endIndex = Math.min(dataSize, pageStart + pageSize - 1);
      endIndex = Math.max(pageStart, endIndex);
    }
    return messages.label(pageStart, endIndex);
  }

  private static native void appendInnerText(Element element, String text) /*-{
    element.appendChild(element.ownerDocument.createTextNode(text));
  }-*/;
}
