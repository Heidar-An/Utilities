package com.example.notesapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class noteRecyclerView extends RecyclerView.Adapter<noteRecyclerView.ViewHolder> {
    private List<Note> localDataSet = new ArrayList<>();
    private final LayoutInflater mInflater;
    private final OnNoteListener mOnNoteListener;
    private Note mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private String mRecentlyDeletedTitle;
    private final Activity mActivity;
    private final Context mContext;
    private final NoteViewModel mNoteViewModel;

    public noteRecyclerView(Context context, OnNoteListener onNoteListener, Activity mainActivity,
                            NoteViewModel noteViewModel){
        mActivity = mainActivity;
        mContext = context;
        mNoteViewModel = noteViewModel;
        mInflater = LayoutInflater.from(context);
        this.mOnNoteListener = onNoteListener;
    }

    // used to delete item from data set with swipe
    public void deleteItem(int position) {
        mRecentlyDeletedItem = localDataSet.get(position);
        mRecentlyDeletedTitle = mRecentlyDeletedItem.getTitle();
        mRecentlyDeletedItemPosition = position;
        localDataSet.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        mNoteViewModel.delete(mRecentlyDeletedItem);
        showUndoSnackbar();
    }

    // undo delete using a snackbar
    private void showUndoSnackbar() {
        View mView = mActivity.findViewById(R.id.mainPageLayout);
        Snackbar snackbar = Snackbar.make(mView, mRecentlyDeletedTitle + " has been deleted", Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", v-> undoDelete());
        snackbar.show();
    }

    private void undoDelete(){
        localDataSet.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    public Context getContext() {
        return mContext;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private static List<Note> mDataSet;
        private final TextView textView;

        private final OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, List<Note> dataSet, OnNoteListener onNoteListener) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.txtTitle);
            mDataSet = dataSet;
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View itemView){
            onNoteListener.onNoteClick(mDataSet.get(getAdapterPosition()));
        }
        public static void setmDataSet(List<Note> dataset){
            mDataSet = dataset;
        }

    }

    public interface OnNoteListener{
        void onNoteClick(Note note);
    }

    public void setLocalDataSet(List<Note> dataSet){
        this.localDataSet = dataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = mInflater.inflate(R.layout.text_row_item, viewGroup, false);
        return new ViewHolder(view, localDataSet, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note current = localDataSet.get(position);
        holder.textView.setText(current.getTitle());
    }

    @Override
    public int getItemCount() {
        if (localDataSet != null) {
            return localDataSet.size();
        }else{
            return 0;
        }
    }

}
