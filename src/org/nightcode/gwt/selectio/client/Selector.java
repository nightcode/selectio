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

package org.nightcode.gwt.selectio.client;

import org.nightcode.gwt.selectio.client.jso.SelectionJso;
import org.nightcode.gwt.selectio.client.requestfactory.SelectorRequestFactory;
import org.nightcode.gwt.selectio.client.requestfactory.SelectorRequestFactoryJson;
import org.nightcode.gwt.selectio.client.ui.ItemSelector;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 *
 */
public class Selector implements EntryPoint {

  public static final SelectorMessages MESSAGES = GWT.create(SelectorMessages.class);

  public static native void onChange(String func, Element element) /*-{
    eval("$wnd." + func + "('" + element.id + "');");
  }-*/;

  public static native void publish() /*-{
    $wnd.showSelector = $entry(@org.nightcode.gwt.selectio.client.Selector::showSelector(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;));
  }-*/;

  /**
   * Shows selector dialog panel.
   *
   * @param inputId id of html element which will contain user input
   * @param entityName name of entity
   * @param server server name
   * @param function js function which will be executed if OK button pushed
   */
  public static void showSelector(String inputId, String entityName, String server, String function,
      String selection) {
    String url = server + "/selectio?q=" + entityName;

    final DialogBox dialogBox = createDialogBox(url, RootPanel.get(inputId), function, selection);
    dialogBox.setGlassEnabled(true);
    dialogBox.setAnimationEnabled(true);
    dialogBox.center();
    dialogBox.show();
  }

  private static DialogBox createDialogBox(final String url, final RootPanel input,
      final String function, final String selection) {
    final DialogBox dialogBox = new DialogBox(new SelectorHeader());
    dialogBox.setStyleName("modal");
    dialogBox.getCaption().setText("Title");

    SelectorRequestFactory requestFactory = new SelectorRequestFactoryJson(url);
    final ItemSelector itemSelector = new ItemSelector(requestFactory, 490);

    if (selection != null) {
      SelectionJso selectionJso = SelectionJso.selectionFromJson(selection);
      itemSelector.setSelection(selectionJso);
    }

    VerticalPanel dialogContents = new VerticalPanel();
    dialogContents.setSize("300px", "500px");
    dialogContents.setStyleName("selector");
    dialogBox.setWidget(dialogContents);

    dialogContents.add(itemSelector);
    dialogContents.setCellHeight(itemSelector, "490px");

    Button doneButton = new Button(MESSAGES.done(), new ClickHandler() {
      @Override public void onClick(ClickEvent event) {
        final SelectionJso selection = SelectionJso.create();
        itemSelector.fillSelection(selection);
        input.getElement().setAttribute("value", new JSONObject(selection).toString());
        onChange(function, input.getElement());
        dialogBox.hide();
      }
    });
    doneButton.setStyleName("btn btn-primary");

    Button cancelButton = new Button(MESSAGES.cancel(), new ClickHandler() {
      @Override public void onClick(ClickEvent clickEvent) {
        dialogBox.hide();
      }
    });
    cancelButton.setStyleName("btn");

    Panel buttonPanel = new FlowPanel();
    buttonPanel.setStyleName("btn-toolbar pull-right");
    buttonPanel.add(doneButton);
    buttonPanel.add(cancelButton);

    dialogContents.add(buttonPanel);
    dialogContents.setCellHeight(buttonPanel, "20px");
    dialogContents.setCellHorizontalAlignment(buttonPanel, HasHorizontalAlignment.ALIGN_RIGHT);

    return dialogBox;
  }

  @Override public void onModuleLoad() {
    publish();
  }
}
