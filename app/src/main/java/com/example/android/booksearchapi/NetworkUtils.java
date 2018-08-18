package com.example.android.booksearchapi;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by ETORO on 17/08/2018.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();


    private final static String BOOK_BASE_URL = "https://www.googleapis.com/books/v1/volumes?";





    final static String QUERY_PARAM = "q";
    final static String PRINT_TYPTE_PARAM = "printType";
    final static String MAX_RESULT__PARAM = "maxResults";

    private static final int maxResults = 20;

    private static final String printType = "books";


//This method gets the string from the editText and uses it to build the full query url
    public static URL buildUrl(String searchQuery) {
        Uri builtUri = Uri.parse(BOOK_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, searchQuery)
                .appendQueryParameter(MAX_RESULT__PARAM, Integer.toString(maxResults))
                .appendQueryParameter(PRINT_TYPTE_PARAM, printType)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


//This method gets the url and connects to the internet, returning the string value of the query result
    public static String getResponseFromHttpUrlBuffer(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        //GET METHOD from the api
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        String bookJSONString = null;
        BufferedReader reader = null;

        try {

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
   /* Since it's JSON, adding a newline isn't necessary (it won't affect
      parsing) but it does make debugging a *lot* easier if you print out the
      completed buffer for debugging. */
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            bookJSONString = buffer.toString();
            return bookJSONString;
        } finally {
            urlConnection.disconnect();

            if(reader != null){
                reader.close();
            }
        }


    }

    //This method gets the url and connects to the internet, returning the string value of the query  with scanner

    public static String getResponseFromHttpUrlScanner(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
