package com.example.android.booksearchapi;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ETORO on 19/08/2018.
 */

public final class BookSearchJson {

    public static String[] getSimpleBookSearchJson(String bookSearchResult) throws JSONException {

        final String LOG_TAG = BookSearchJson.class.getSimpleName();

        String [] parsedBookData = null;

        final String BD_RESULT = "book_search_result";
        final String ITEMS = "items";
        final String SELF_LINK = "selfLink";
        final String TITLE = "title";
        final String SUB_TITLE = "subtitle";
        final String PUBLISHER = "publisher";
        final String DATE_PUBLISHED = "publishedDate";
        final String DESCRIPTION = "description";
        final String IMAGE_LINK = "imageLinks";
        final String THUMBNAIL = "thumbnail";
        final String SMALL_THUMNAIL = "smallThumbnail";
        final String AUTHOR = "authors";
        final String COUNT = "totalItems";
        final String CATEGORY = "categories";

        final String VOLUME_INFO = "volumeInfo";


        JSONObject  bookSearchJson = new JSONObject(bookSearchResult);
        int count = bookSearchJson.getInt(COUNT);

        JSONArray bookResultArray = bookSearchJson.getJSONArray(ITEMS);

        parsedBookData = new String[bookResultArray.length()];

        for(int i = 0; i < bookResultArray.length(); i++){

            String bookSubTitle= "no subtitle";

            JSONObject eachBookObject = bookResultArray.getJSONObject(i);


            /*THIS DOESN'T THROW AN EXCEPTION IF KEY DOSE'T EXIST RATHER RETURNS AN EMPTY RESULT

            *WE could also us if(eachBookObject.has(SELF_LINK){ eachObject.getString(SELF_LINK)} else{"no self link"}
            *                                   OR
            *                  if(!eachBookObject.isNull(SELF_LINK){ eachObject.getString(SELF_LINK)}
            */
            String selfLink = eachBookObject.optString(SELF_LINK);

            JSONObject bookVolumeObject = eachBookObject.getJSONObject(VOLUME_INFO);


            String bookTitle = bookVolumeObject.getString(TITLE);
            Log.d(LOG_TAG,  "Book Title: "+ bookTitle);

            bookSubTitle = bookVolumeObject.optString(SUB_TITLE);

            String publisher = bookVolumeObject.optString(PUBLISHER);
           String publishedDate = bookVolumeObject.optString(DATE_PUBLISHED);
            String description = bookVolumeObject.optString(DESCRIPTION);

         JSONObject imageLinkObject = bookVolumeObject.optJSONObject(IMAGE_LINK);

        String thumbnail = imageLinkObject.optString(THUMBNAIL);
        String smallThumbnail = imageLinkObject.optString(SMALL_THUMNAIL);

        String authors = "";
        if(bookVolumeObject.has(AUTHOR)){
            JSONArray authorsArray = bookVolumeObject.getJSONArray(AUTHOR);

            for(int x= 0; x<authorsArray.length(); x++){
                authors = authors + ", " +authorsArray.get(x);
                Log.d(LOG_TAG, "Author found: " + authors +  " : " + authorsArray.length());
            }
        }



        String categories = "";
        if(!bookVolumeObject.isNull(CATEGORY)){
            JSONArray categoryArray = bookVolumeObject.getJSONArray(CATEGORY);

            for(int x= 0; x<categoryArray.length(); x++){
                categories = categories + ", "+ categoryArray.get(x);
                Log.d(LOG_TAG, "Category found: "+ categories + " : "+ categoryArray.length());
            }
        }


           parsedBookData[i] =  selfLink+ ": -- :" + bookTitle + ": -- :" + authors + ": -- :" + categories  + ": -- :" + bookSubTitle + ": -- :"+ publisher  + ": -- :"
                   +publishedDate + ": -- :" + description +  ": -- :" + thumbnail + ": -- :" + smallThumbnail;


            parsedBookData[i] =  ": -- :" + bookTitle + ": -- :" + authors + ": -- :" + categories;
        }

        return parsedBookData;

    }
}
