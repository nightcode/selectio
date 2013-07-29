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

import com.google.gwt.i18n.client.Messages;

/**
 * Selector messages.
 */
public interface SelectorMessages extends Messages {

  String all();

  String cancel();

  String done();

  String except();

  String label(int pageStart, int endIndex);

  String labelOf(int pageStart, int endIndex, int dataSize);

  String loading();

  String newer();

  String newest();

  String noData();

  String none();

  String older();

  String oldest();

  String search();

  String selected();
}
