package com.example.ricardo.grabbingjsonattributestest;

import org.json.JSONArray;

public class Accessibility {

    private String mWebTitle;

    private String mSectionName;
    private String mWebPublicationDate;
    private String mUrl;
    private JSONArray mTag;
    private String mAuthor;

    /**
     * Constructs a new {@link Accessibility} object.
     * @param webtitle
     * @param sectionname
     * @param webpublicationdate
     * @param url is the website URL to find more details about the earthquake
     * @param  tag
     * @param author
     */
    public Accessibility(String webtitle, String sectionname, String webpublicationdate, String url, JSONArray tag,
                         String author) {
        mWebTitle = webtitle;
        mSectionName = sectionname;
        mWebPublicationDate = webpublicationdate;
        mUrl = url;
        mTag = tag;
        mAuthor=author;

    }

    public String getWebTitle() {
        return mWebTitle;
    }

    public String getSectionName(){
      return mSectionName;
    }
    public  String getmWebPublicationDate() {
        return  mWebPublicationDate;

        }
    public String getUrl() {
        return mUrl;
    }

    public JSONArray getTag(){
        return mTag;
    }
    public String getAuthor() {
        return mAuthor;
    }

}





