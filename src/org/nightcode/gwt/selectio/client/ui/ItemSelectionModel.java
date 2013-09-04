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

import org.nightcode.gwt.selectio.shared.ItemProxy;

import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 */
public class ItemSelectionModel extends SelectionModel.AbstractSelectionModel<ItemProxy> {

  /**
   * Selection types.
   */
  public enum Type {
    ALL, NONE, SEARCH;

    public static Type of(String type) {
      return TYPES.get(type);
    }
  }

  private final class ItemWrapper implements ItemProxy, Comparable<ItemProxy> {

    private final ItemProxy item;

    private ItemWrapper(ItemProxy item) {
      this.item = item;
    }

    @Override public String getDescription() {
      return item.getDescription();
    }

    @Override public String getDisplayName() {
      return item.getDisplayName();
    }

    @Override public String getId() {
      return item.getId();
    }

    @Override public int hashCode() {
      return getKey(this).hashCode();
    }

    @Override public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (!(obj instanceof ItemProxy)) {
        return false;
      }
      return getKey(this).equals(getKey((ItemProxy) obj));
    }

    @Override public int compareTo(ItemProxy obj) {
      return item.getDisplayName().compareTo(obj.getDisplayName());
    }
  }

  protected ItemSelectionModel(ProvidesKey<ItemProxy> keyProvider) {
    super(keyProvider);
  }

  private static final Map<String, Type> TYPES = new HashMap<String, Type>();

  static {
    for (int i = Type.values().length - 1; i >= 0; i--) {
      Type type = Type.values()[i];
      TYPES.put(type.name(), type);
    }
  }

  private final Map<ItemProxy, Boolean> exceptions = new HashMap<ItemProxy, Boolean>();

  private Type type = Type.NONE;
  private String search;
  private List<String> searchQueries = new ArrayList<String>();

  public void addSearchQueries(List<String> queries) {
    for (int i = queries.size() - 1; i >= 0; i--) {
      searchQueries.add(queries.get(i));
    }
  }

  public Map<ItemProxy, Boolean> getExceptions() {
    Map<ItemProxy, Boolean> exceptions = new TreeMap<ItemProxy, Boolean>();
    getExceptions(exceptions);
    return exceptions;
  }

  protected void getExceptions(Map<ItemProxy, Boolean> output) {
    output.clear();
    output.putAll(exceptions);
  }

  public List<String> getSearchQueries() {
    return searchQueries;
  }

  public Type getType() {
    return type;
  }

  public boolean isDefaultSelected(ItemProxy object) {
    switch (type) {
      case ALL:
        return true;
      case NONE:
        return false;
      case SEARCH:
        for (int i = searchQueries.size() - 1; i >= 0; i--) {
          if (canonicalize(object.getDisplayName()).contains(searchQueries.get(i))) {
            return true;
          }
        }
        return false;
      default:
        throw new IllegalStateException("type [" + type + "]");
    }
  }

  public boolean isSelected(ItemProxy object) {
    //    Object key = getKey(object);
    if (object == null) {
      return false;
    }
    ItemWrapper wrapper = new ItemWrapper(object);
    Boolean exception = exceptions.get(wrapper);
    if (exception != null) {
      return exception.booleanValue();
    }
    return isDefaultSelected(object);
  }

  public void setSearch(String search) {
    this.search = search;
    scheduleSelectionChangeEvent();
  }

  public void setSelected(ItemProxy object, boolean selected) {
    //    Object key = getKey(object);
    ItemWrapper wrapper = new ItemWrapper(object);
    Boolean currentlySelected = exceptions.get(wrapper);
    if (currentlySelected != null && currentlySelected.booleanValue() != selected) {
      exceptions.remove(wrapper);
    } else {
      exceptions.put(wrapper, selected);
    }
    scheduleSelectionChangeEvent();
  }

  public void setType(String type) {
    Type tmpType = this.type;
    this.type = Type.of(type);
    if (Type.SEARCH.equals(this.type)) {
      if (search != null && search.length() > 0 && !searchQueries.contains(canonicalize(search))) {
        searchQueries.add(canonicalize(search));
      }
      if (!this.type.equals(tmpType)) {
        clearExceptions();
      }
    } else {
      searchQueries.clear();
      clearExceptions();
    }
    scheduleSelectionChangeEvent();
  }

  void removeSearchQuery(final String query) {
    searchQueries.remove(query);
    for (Map.Entry<ItemProxy, Boolean> entry : exceptions.entrySet()) {
      if (entry.getKey().getDisplayName().contains(query)) {
        exceptions.remove(entry.getKey());
      }
    }
    scheduleSelectionChangeEvent();
  }

  private String canonicalize(String input) {
    return input.toUpperCase();
  }

  private void clearExceptions() {
    exceptions.clear();
    scheduleSelectionChangeEvent();
  }
}
