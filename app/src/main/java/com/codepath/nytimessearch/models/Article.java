package com.codepath.nytimessearch.models;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vmiha on 10/20/16.
 */
public class Article implements Parcelable {

    String webURL;
    String headline;
    String thumbNail;

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getThumbNail() {
        return thumbNail;
    }

    public void setThumbNail(String thumbNail) {
        this.thumbNail = thumbNail;
    }

    private Article(Parcel in) {
        webURL = in.readString();
        headline = in.readString();
        thumbNail = in.readString();
    }

    public Article(JSONObject jsonObject) {
        try {
            this.webURL = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            JSONArray multimedia = jsonObject.getJSONArray("multimedia");

            if(multimedia.length() > 0){
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
            } else{
                this.thumbNail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<Article> fromJsonArray(JSONArray jsonArray) {
        List<Article> results = new ArrayList<>();

        for(int i = 0; i <jsonArray.length(); i++) {
            try {
                results.add(new Article(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(webURL);
        dest.writeString(headline);
        dest.writeString(thumbNail);
    }

    public static final Parcelable.Creator<Article> CREATOR
            = new Parcelable.Creator<Article>() {

        // This simply calls our new constructor (typically private) and
        // passes along the unmarshalled `Parcel`, and then returns the new object!
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        // We just need to copy this and change the type to match our class.
        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
