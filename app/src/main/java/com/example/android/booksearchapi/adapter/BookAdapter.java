package com.example.android.booksearchapi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.booksearchapi.R;

/**
 * Created by ETORO on 19/08/2018.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookAdapterViewHolder> {


    String [] mBookData;



    @Override
    public BookAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.search_result_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new BookAdapterViewHolder(view);
    }



    @Override
    public void onBindViewHolder(BookAdapterViewHolder holder, int position) {

        String bookData = mBookData[position];
        holder.mBookDataTextView.setText(bookData);
    }



    @Override
    public int getItemCount() {
        if(null == mBookData){
            return 0;
        }
        return mBookData.length;
    }

    public void setBookData(String[] bookData){
        mBookData = bookData;
        notifyDataSetChanged();
    }






    public class BookAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView mBookDataTextView;

        public BookAdapterViewHolder(View itemView) {
            super(itemView);
            mBookDataTextView = itemView.findViewById(R.id.tv_result_data);
        }
    }
}
