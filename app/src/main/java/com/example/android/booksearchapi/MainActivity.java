package com.example.android.booksearchapi;

import android.app.LoaderManager;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<String> {

    private static final String URL_STRING_BUNDLE_KEY = "search_string_bundle_key"; //gets the bundle key

    private static final int SEARCH_LOADER_ID = 4;      //loader id

    private static final String PROGRESS_BAR_STATUS = "progress_bar_status";    //status for progressBar used in savedInstanceState

    TextView mDisplaySearchResultTextView, mDisplayErrorMessageTextView;
    EditText mSearchEditText;
    Button mSearchButton;
    ProgressBar mLoadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDisplaySearchResultTextView = findViewById(R.id.tv_display_search_result_main);
        mSearchEditText = findViewById(R.id.et_book_title_main);
        mSearchButton = findViewById(R.id.btn_search_main);
        mDisplayErrorMessageTextView = findViewById(R.id.tv_error_message_main);
        mLoadingProgressBar = findViewById(R.id.pb_loading_main);

        //this gives the progress bar the same visibility as the previous activity in the new one eg when screen is rotated
        if(savedInstanceState != null){
            int progressBarStatus = savedInstanceState.getInt(PROGRESS_BAR_STATUS);

            mLoadingProgressBar.setVisibility(progressBarStatus);
        }

        getSupportLoaderManager().initLoader(SEARCH_LOADER_ID, null, this);


    }

    public void searchButtonClick(View view){
        makeSearchQuery();
    }

    public void makeSearchQuery(){

        // THIS CODE Hides the keyboard when the button is pushed.
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        String messageFromEditText = mSearchEditText.getText().toString();

        //checks if the user inputted anything before clicking search
        if(messageFromEditText == null || TextUtils.isEmpty(messageFromEditText)){
            mDisplaySearchResultTextView.setText("Enter a Book Title to search");
            return;
        }

        //builds the url and returns it
        URL searchUrl = NetworkUtils.buildUrl(messageFromEditText);

        Bundle bundle = new Bundle();
        //adds the url as a string bundle for the loader
        bundle.putString(URL_STRING_BUNDLE_KEY, searchUrl.toString());

        android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();

        //TODO 1 Change this to initloader and observe if there's a difference

        loaderManager.restartLoader(SEARCH_LOADER_ID, bundle, this);

        mDisplaySearchResultTextView.setText("");

    }

    //this method displays the error message
    private void showErrorMessage(){

        mDisplayErrorMessageTextView.setVisibility(View.VISIBLE);
        mDisplaySearchResultTextView.setVisibility(View.INVISIBLE);
        mLoadingProgressBar.setVisibility(View.INVISIBLE);

    }

    //this method makes the textview for the query visible
    private void showResult(){
        mDisplaySearchResultTextView.setVisibility(View.VISIBLE);
        mDisplayErrorMessageTextView.setVisibility(View.INVISIBLE);
        mLoadingProgressBar.setVisibility(View.INVISIBLE);

    }
    //this method displays the progress bar
    private void progressLoading(){
        mDisplaySearchResultTextView.setVisibility(View.INVISIBLE);
        mDisplayErrorMessageTextView.setVisibility(View.INVISIBLE);
        mLoadingProgressBar.setVisibility(View.VISIBLE);
    }






    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<String>(this) {

            String dataToDeliver = null;
            @Override
            protected void onStartLoading() {
            //if nothing is inthe bundle, there is no need to query or go to doinBackround, so just return
                if(args == null){
                    return;
                }

                progressLoading();


                if (dataToDeliver != null) {
                    deliverResult(dataToDeliver);

                }
                else {
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {

               // String searchQuery = args.getString(URL_STRING_BUNDLE_KEY);

                //if url is empty return null
                if(args.getString(URL_STRING_BUNDLE_KEY) == null || TextUtils.isEmpty(args.getString(URL_STRING_BUNDLE_KEY))){
                    return null;
                }

                //this gets the url and performs the http connection it return the result of the query in a string format

                try{

                    URL url = new URL(args.getString(URL_STRING_BUNDLE_KEY));
                    String searchResult =  NetworkUtils.getResponseFromHttpUrlBuffer(url);
                    return searchResult;

                }
                catch (IOException e){
                    e.printStackTrace();
                    return null;
                }
            }

            //this method stores the value of the result in case another app was opened, on returning to your app, it won't
            //query again but just delivers the result it had
            @Override
            public void deliverResult(String data) {
                dataToDeliver = data;
                showResult();   //this helps to hide the progress bar, else it would be showing cos it was saved in onsavedinstance
                                //and displays the result textview, without this, the loading bar will cover the textview, cos they
                                // are in a FrameLayout parent
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {


        //if there is no result, display an error message
        if(data == null || TextUtils.isEmpty(data)){
            showErrorMessage();
        }
        else {
            //display the result
            mDisplaySearchResultTextView.setText(data);
            showResult();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //this gets the current visibility state of the progress bar to display it in the new activity if device configuratio
        //changes
       int progressBarVisibility =  mLoadingProgressBar.getVisibility();

       outState.putInt(PROGRESS_BAR_STATUS, progressBarVisibility);
    }
}
