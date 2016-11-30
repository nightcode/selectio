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

import org.nightcode.gwt.selectio.client.Selector;
import org.nightcode.gwt.selectio.client.SelectorMessages;
import org.nightcode.gwt.selectio.client.jso.ItemJso;
import org.nightcode.gwt.selectio.client.requestfactory.SelectorRequestFactory;
import org.nightcode.gwt.selectio.shared.ItemPageProxy;
import org.nightcode.gwt.selectio.shared.ItemProxy;
import org.nightcode.gwt.selectio.shared.SelectionProxy;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CellPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.web.bindery.requestfactory.shared.Receiver;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class ItemSelector extends Composite implements ClickHandler {

  /**
   *
   */
  @CssResource.ImportedWithPrefix("cl")
  public interface CellListStyle extends CellList.Style {
  }

  /**
   *
   */
  public interface CellListResources extends CellList.Resources {
    @ClientBundle.Source("CellList.css")
    CellListStyle cellListStyle();
  }

  private static final ProvidesKey<ItemProxy> KEY_PROVIDER = new ProvidesKey<ItemProxy>() {
    public Object getKey(ItemProxy item) {
      return item != null ? item.getId() : null;
    }
  };

  private Receiver<ItemPageProxy> itemsReceiver = new Receiver<ItemPageProxy>() {
    @Override public void onSuccess(ItemPageProxy response) {
      int numSearch = response.getItems().size() == response.getLength() + 1
          ? Integer.MAX_VALUE : response.getStart() + response.getItems().size();
      if (queryField.getText() != null && queryField.getText().equals(response.getQuery())) {
        dataProvider.updateRowCount(numSearch, numSearch == 0);
        dataProvider.updateRowData(response.getStart(), response.getItems());
      }
    }
  };

  private final AsyncDataProvider<ItemProxy> dataProvider;

  private final SearchBox queryField = new SearchBox();
  private final CellList<ItemProxy> display;

  private final ItemSelectionModel selectionModel = new ItemSelectionModel(KEY_PROVIDER);

  private final FlowPanel selectedDescription = new FlowPanel();

  private final Anchor all;
  private final Anchor none;

  private int height;

  private final EventListener selectedItemsEventListener = new EventListener() {
    @Override public void onBrowserEvent(Event event) {
      String id = Element.as(event.getEventTarget()).getId();
      ItemJso item = ItemJso.create();
      item.setId(id);
      selectionModel.setSelected(item, !selectionModel.isSelected(item));
    }
  };

  private final EventListener selectedSearchQueriesEventListener = new EventListener() {
    @Override public void onBrowserEvent(Event event) {
      String id = Element.as(event.getEventTarget()).getId();
      selectionModel.removeSearchQuery(id);
    }
  };

  private final EventListener selectedTypeEventListener = new EventListener() {
    @Override public void onBrowserEvent(Event event) {
      String id = Element.as(event.getEventTarget()).getId();
      selectionModel.setType(id);
    }
  };

  public ItemSelector(final SelectorRequestFactory requestFactory, int height, int pageSize) {
    this.height = height;

    final CellList.Resources cellListResources = GWT.create(CellListResources.class);

    final SelectorMessages messages = Selector.MESSAGES;

    display = new CellList<ItemProxy>(new ItemCell(), cellListResources, pageSize);
    display.setStyleName("slt-items");
    Widget noDataWidget = new Label(messages.noData());
    noDataWidget.setStyleName("slt-nodata");
    display.setEmptyListWidget(noDataWidget);
    Widget loadingWidget = new Label(messages.loading());
    loadingWidget.setStyleName("slt-loading");
    display.setLoadingIndicator(loadingWidget);
    display.setSelectionModel(selectionModel);
    display.setValueUpdater(new ValueUpdater<ItemProxy>() {
      @Override public void update(ItemProxy value) {
        selectionModel.setSelected(value, !selectionModel.isSelected(value));
      }
    });

    dataProvider = new AsyncDataProvider<ItemProxy>() {
      @Override protected void onRangeChanged(HasData<ItemProxy> itemHasData) {
        Range searchRange = itemHasData.getVisibleRange();
        requestFactory.itemRequest()
            .getItems(queryField.getText(), searchRange.getStart(), searchRange.getLength() + 1)
            .fire(itemsReceiver); // ask one more item to examine should we enable next button
      }
    };
    dataProvider.addDataDisplay(display);

    final VerticalPanel mainPanel = createVerticalPanel("slt-container");
    initWidget(mainPanel);
    mainPanel.setSpacing(7);

    final Timer requestTimer = new Timer() {
      @Override public void run() {
        selectionModel.setSearch(queryField.getText());
        Range[] searchRanges = dataProvider.getRanges();
        if (searchRanges == null || searchRanges.length == 0) {
          return;
        }
        display.setPageStart(0);
        requestFactory.itemRequest()
            .getItems(queryField.getText(), 0, searchRanges[0].getLength() + 1)
            .fire(itemsReceiver); // ask one more item to examine should we enable next button
      }
    };
    queryField.addKeyUpHandler(new KeyUpHandler() {
      @Override public void onKeyUp(KeyUpEvent event) {
        requestTimer.schedule(300);
      }
    });

    Anchor search = createAnchor(ItemSelectionModel.Type.SEARCH, messages.search());
    all = createAnchor(ItemSelectionModel.Type.ALL, messages.all());
    none = createAnchor(ItemSelectionModel.Type.NONE, messages.none());

    FlowPanel selectionPanel = new FlowPanel();
    selectionPanel.setStyleName("slt-btn-group");
    selectionPanel.add(search);
    selectionPanel.add(all);
    selectionPanel.add(none);

    FlowPanel itemsPanel = new FlowPanel();
    itemsPanel.setStyleName("slt-frame slt-items-box");
    itemsPanel.add(display);

    final ItemPager pager = new ItemPager(messages);
    pager.setDisplay(display);
    mainPanel.setCellHorizontalAlignment(pager, HasHorizontalAlignment.ALIGN_RIGHT);

    selectedDescription.setStyleName("slt-frame slt-selected");
    Element selectedUl = DOM.createElement("ul");
    selectedUl.setClassName("slt-selected-box");
    appendItem(selectedUl, ItemSelectionModel.Type.ALL, messages.none());
    insertTitle(selectedUl, messages.selected());
    DOM.appendChild(selectedDescription.getElement(), selectedUl);

    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
      @Override public void onSelectionChange(SelectionChangeEvent event) {
        selectedDescription.clear();
        Element selectedUl = DOM.createElement("ul");
        selectedUl.setClassName("slt-selected-box");
        Element deselectedUl = DOM.createElement("ul");
        deselectedUl.setClassName("slt-selected-box");
        Map<ItemProxy, Boolean> exceptions = selectionModel.getExceptions();
        if (ItemSelectionModel.Type.ALL.equals(selectionModel.getType())) {
          appendItem(selectedUl, ItemSelectionModel.Type.NONE, messages.all());
        }

        List<String> searchQueries = selectionModel.getSearchQueries();
        for (int i = searchQueries.size() - 1; i >= 0; i--) {
          appendItem(selectedUl, searchQueries.get(i));
        }
        for (Map.Entry<ItemProxy, Boolean> entry : exceptions.entrySet()) {
          Element parent = (entry.getValue()) ? selectedUl : deselectedUl;
          appendItem(parent, entry.getKey());
        }

        if (selectedUl.getChildCount() == 0) {
          appendItem(selectedUl, ItemSelectionModel.Type.ALL, messages.none());
       }

        insertTitle(selectedUl, messages.selected());
        DOM.appendChild(selectedDescription.getElement(), selectedUl);

        if (deselectedUl.getChildCount() != 0) {
          insertTitle(deselectedUl, messages.except());
          DOM.appendChild(selectedDescription.getElement(), deselectedUl);
        }
        updateButtonsStyle();
      }
    });

    attachPanel(mainPanel, queryField, 5);
    attachPanel(mainPanel, selectionPanel, 5);
    attachPanel(mainPanel, itemsPanel, 50);
    attachPanel(mainPanel, pager, 5);
    attachPanel(mainPanel, selectedDescription, 25);
  }

  public SelectionProxy fillSelection(SelectionProxy selection) {
    selection.setExceptions(selectionModel.getExceptions());
    selection.setSearchQueries(selectionModel.getSearchQueries());
    selection.setType(selectionModel.getType().name());
    return selection;
  }

  @Override public void onClick(ClickEvent event) {
    String id = ((Widget) event.getSource()).getElement().getId();
    selectionModel.setType(id);
    updateButtonsStyle();
  }

  public void setSelection(SelectionProxy selection) {
    selectionModel.setType(selection.getType());
    selectionModel.addSearchQueries(selection.getSearchQueries());
    Map<ItemProxy, Boolean> exceptions = selection.getExceptions();
    for (Map.Entry<ItemProxy, Boolean> entry : exceptions.entrySet()) {
      selectionModel.setSelected(entry.getKey(), entry.getValue());
    }
  }

  private void appendItem(Element parent, final String search) {
    Element li = DOM.createElement("li");
    li.setClassName("slt-selected-item");
    li.setId(search);
    li.setInnerText("*" + search + "*");
    DOM.appendChild(parent, li);
    Event.setEventListener(li, selectedSearchQueriesEventListener);
    Event.sinkEvents(li, Event.ONCLICK);
  }

  private void appendItem(Element parent, ItemSelectionModel.Type type, String text) {
    Element li = DOM.createElement("li");
    li.setClassName("slt-selected-item");
    li.setId(type.name());
    li.setInnerText(text);
    DOM.appendChild(parent, li);
    Event.setEventListener(li, selectedTypeEventListener);
    Event.sinkEvents(li, Event.ONCLICK);
  }

  private void appendItem(Element parent, final ItemProxy item) {
    Element li = DOM.createElement("li");
    li.setClassName("slt-selected-item");
    li.setId(item.getId());
    li.setInnerText(item.getDisplayName());
    DOM.appendChild(parent, li);
    Event.setEventListener(li, selectedItemsEventListener);
    Event.sinkEvents(li, Event.ONCLICK);
  }

  private void insertTitle(Element parent, String message) {
    Element selectedTitle = DOM.createElement("li");
    selectedTitle.setClassName("slt-selected-label");
    selectedTitle.setInnerText(message);
    DOM.insertChild(parent, selectedTitle, 0);
  }

  private void attachPanel(CellPanel parent, Widget child, double heightPercent) {
    parent.add(child);
    parent.setCellHeight(child, Math.round(height * heightPercent / 100.0) + "px");
  }

  private Anchor createAnchor(ItemSelectionModel.Type type, String text) {
    Anchor anchor = new Anchor(text);
    anchor.getElement().setId(type.name());
    anchor.setStyleName("slt-btn slt-btn-link");
    anchor.addClickHandler(this);
    return anchor;
  }

  private VerticalPanel createVerticalPanel(String style) {
    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.setStyleName(style);
    return verticalPanel;
  }

  private void updateButtonsStyle() {
    boolean hasSelection = selectionModel.getExceptions().size() > 0
        || selectionModel.getSearchQueries().size() > 0;
    if (hasSelection) {
      all.setStyleName("slt-btn slt-btn-link");
      none.setStyleName("slt-btn slt-btn-link");
    } else {
      switch (selectionModel.getType()) {
        case ALL:
          all.setStyleName("slt-btn slt-btn-link slt-btn-active");
          none.setStyleName("slt-btn slt-btn-link");
          break;
        case NONE:
        default:
          all.setStyleName("slt-btn slt-btn-link");
          none.setStyleName("slt-btn slt-btn-link slt-btn-active");
          break;
      }
    }
  }
}
