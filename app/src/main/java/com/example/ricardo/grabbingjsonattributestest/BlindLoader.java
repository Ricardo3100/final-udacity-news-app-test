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

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class BlindLoader extends AsyncTaskLoader<List<Accessibility>> {

    public BlindLoader(Context context) {
        super(context);
    }

    @Override
    public List <Accessibility> loadInBackground() {
        return null;
    }

    public static class Loader extends AsyncTaskLoader <List <Accessibility>> {

        private static final String LOG_TAG = BlindLoader.class.getName();

        private String mUrl;

        /**
         * Constructs a new {@link Loader}.
         *
         * @param context of the activity
         * @param url     to load data from
         */
        public Loader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List <Accessibility> loadInBackground() {
            if (mUrl == null) {
                return null;
            }

            List <Accessibility> accessibilities = QueryUtils.fetchAcessibilityData(mUrl);
            return accessibilities;
        }
    }
}