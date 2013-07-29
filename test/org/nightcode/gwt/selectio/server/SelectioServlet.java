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

package org.nightcode.gwt.selectio.server;

import org.nightcode.gwt.selectio.server.domain.Item;
import org.nightcode.gwt.selectio.server.service.SelectioService;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
@Singleton
public class SelectioServlet extends HttpServlet {

  private final SelectioService selectioService;

  @Inject SelectioServlet(SelectioService selectioService) {
    this.selectioService = selectioService;
  }

  private String getContent(HttpServletRequest request) throws IOException {
    int contentLength = request.getContentLength();
    byte[] content = new byte[contentLength];
    BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
    try {
      int readBytes = 0;
      while (bis.read(content, readBytes, contentLength - readBytes) > 0) {
        // read the contents
      }
      return new String(content);
    } finally {
      bis.close();
    }
  }

  public static final String PARAMETER_QUERY = "query";
  public static final String PARAMETER_START = "start";
  public static final String PARAMETER_LENGTH = "length";

  @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    try {
      response.setStatus(HttpServletResponse.SC_OK);
      response.setContentType("text/json");
      response.setCharacterEncoding("UTF-8");
      PrintWriter writer = response.getWriter();

      JSONObject topLevelJsonObject = new JSONObject(getContent(request));
      JSONObject result = new JSONObject();

      String query = topLevelJsonObject.getString(PARAMETER_QUERY);
      int start = topLevelJsonObject.getInt(PARAMETER_START);
      int length = topLevelJsonObject.getInt(PARAMETER_LENGTH);

      List<Item> items = selectioService.getItems(query, start, length);

      JSONArray jsonArray = new JSONArray();
      for (Item item : items) {
        JSONObject obj = new JSONObject();
        obj.put("id", item.getId());
        obj.put("displayName", item.getDisplayName());
        obj.put("description", item.getDescription());
        jsonArray.put(obj);
      }

      result.put("count", items.size());
      result.put("items", jsonArray);

      writer.print(result.toString());
      writer.flush();
    } catch (SecurityException e) {
      throw new IllegalArgumentException(e);
    } catch (JSONException e) {
      throw new IllegalArgumentException(e);
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
  }
}
