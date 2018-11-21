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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An {@link Accessibility} knows how to create a list item layout for each earthquake
 * in the data source (a list of {@link } objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class AcessibilityAdapter extends ArrayAdapter<Accessibility> {

    /**
     * The part of the location string from the USGS service that we use to determine
     * whether or not there is a location offset present ("5km N of Cairo, Egypt").
     */

    /**
     * Constructs a new {@link }.
     *  @param context         of the app
     * @param accessibilities is the list of earthquakes, which is the data source of the adapter
     */
    public AcessibilityAdapter(Context context, ArrayList <Accessibility> accessibilities) {
        super(context, 0, accessibilities);
    }

    /**
     * Returns a list item view that displays information about the earthquake at the given position
     * in the list of earthquakes.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Find the earthquake at the given position in the list of earthquakes
        Accessibility accessibility = getItem(position);

        TextView nameview = (TextView) listItemView.findViewById(R.id.web);

        nameview.setText(accessibility.getWebTitle());

        TextView sectionname = (TextView) listItemView.findViewById(R.id.section);

        sectionname.setText(accessibility.getSectionName());

        TextView webpublication = (TextView) listItemView.findViewById(R.id.publication);

        webpublication.setText(accessibility.getmWebPublicationDate());

        TextView url = (TextView) listItemView.findViewById(R.id.url);

        url.setText(accessibility.getUrl());

        TextView author = (TextView) listItemView.findViewById(R.id.author);

        author.setText(accessibility.getAuthor());



        return listItemView;
    }

    public void addAll(List<AcessibilityAdapter> accessibility) {
    }
}

