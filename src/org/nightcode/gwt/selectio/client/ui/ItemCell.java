/*
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

import org.nightcode.gwt.selectio.shared.ItemProxy;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import java.util.Collections;
import java.util.Set;

/**
 *
 */
public class ItemCell implements Cell<ItemProxy> {

  interface Template extends SafeHtmlTemplates {
    @Template("<div class=\"slt-item-box\"><span class=\"slt-item-name\">{0}</span></div>")
    SafeHtml item(String value);

    @Template("<div class=\"slt-item-box\"><span class=\"slt-item-name\">{0}</span>"
        + "<span class=\"slt-item-description\">{1}</span></div>")
    SafeHtml itemAndDescription(String value, String description);
  }

  private static Template template;

  private Set<String> consumedEvents;

  public ItemCell() {
    if (template == null) {
      template = GWT.create(Template.class);
    }
    consumedEvents = Collections.unmodifiableSet(Collections.singleton("click"));
  }

  @Override public boolean dependsOnSelection() {
    return true;
  }

  @Override public Set<String> getConsumedEvents() {
    return consumedEvents;
  }

  @Override public boolean handlesSelection() {
    return true;
  }

  @Override public boolean isEditing(Context context, Element parent, ItemProxy value) {
    return false;
  }

  @Override public void onBrowserEvent(Context context, Element parent, ItemProxy value,
      NativeEvent event, ValueUpdater<ItemProxy> itemValueUpdater) {
    if (itemValueUpdater != null) {
      String type = event.getType();
      if ("click".equals(type)) {
        itemValueUpdater.update(value);
      }
    }
  }

  @Override public void render(Context context, ItemProxy value, SafeHtmlBuilder sb) {
    if (value == null || value.getDisplayName().length() == 0) {
      return;
    }
    sb.appendHtmlConstant("<div class=\"slt-item\"><i class=\"icon-ok\"></i>");
    if (value.getDescription() != null) {
      sb.append(template.itemAndDescription(value.getDisplayName(), value.getDescription()));
    } else {
      sb.append(template.item(value.getDisplayName()));
    }
    sb.appendHtmlConstant("</div>");
  }

  @Override public boolean resetFocus(Context context, Element parent, ItemProxy value) {
    return false;
  }

  @Override public void setValue(Context context, Element parent, ItemProxy value) {
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    render(context, value, sb);
    parent.setInnerHTML(sb.toSafeHtml().asString());
  }
}
