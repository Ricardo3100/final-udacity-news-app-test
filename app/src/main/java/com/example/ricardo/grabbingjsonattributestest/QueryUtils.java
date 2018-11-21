/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.ricardo.grabbingjsonattributestest;

import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Accessibility> fetchAcessibilityData(String requestUrl) {

        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Accessibility> accessibilities  = extractFeatureFromJson(jsonResponse);

        return accessibilities;
    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List <Accessibility> extractFeatureFromJson(String newsjson) {
        if (TextUtils.isEmpty(newsjson)) {
            return null;
        }

        List<Accessibility> accessibilities = new ArrayList<>();


        try {

            JSONObject baseJson = new JSONObject(newsjson);
            JSONArray acess = baseJson.getJSONObject("response").getJSONArray("results");
JSONObject object = new JSONObject( newsjson);

            for (int i = 0; i < acess.length(); i++) {

                JSONObject jsonObject = acess.getJSONObject(i);
                String webtitle = jsonObject.getString("webTitle");
                JSONArray tag = jsonObject.getJSONArray("tags");

                // Extract the value for the key called "place"
                String section = jsonObject.getString("sectionName");

                // Extract the value for the key called "time"
                String publication = jsonObject.getString("webPublicationDate");

                // Extract the value for the key called "url"
                String url = jsonObject.getString("apiUrl");

                String author = "";
                if (tag.length() != 0) {
                    JSONObject currenttagsauthor = tag.getJSONObject(0);
                    author = currenttagsauthor.getString("webTitle");
                }
                else {
                    author = "No Author ..";
                }

                Accessibility accessibility = new Accessibility(webtitle, section, publication, url,tag,author);

                accessibilities.add(accessibility);
            }
            long publication = 1454124312220L;
            Date dateObject = new Date(publication);
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM DD, yyyy");
            String dateToDisplay = dateFormatter.format(dateObject);
        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return accessibilities;
    }

}
