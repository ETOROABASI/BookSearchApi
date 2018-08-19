package com.example.android.booksearchapi.model;

/**
 * Created by ETORO on 19/08/2018.
 */

public class BookListItem {

    String bookData;
    boolean favourite = false;



    public String getBookData() {
        return bookData;
    }

    public void setBookData(String bookData) {
        this.bookData = bookData;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }


}
