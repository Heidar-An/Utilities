package com.example.notesapplication.alarmFiles;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapplication.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

public class alarmRecyclerView extends RecyclerView.Adapter<alarmRecyclerView.AlarmViewHolder>{

    private List<Alarm> mLocalDataSet = new ArrayList<>();
    private final Context mContext;
    private final Activity mAlarmActivity;
    private Alarm mRecentlyDeletedItem;
    private int mRecentlyDetletedItemPosition;
    private String mRecentlyDeletedItemTitle;
    private final LayoutInflater mInflator;
    private final AlarmViewModel mAlarmViewModel;
    private final OnAlarmListener mOnAlarmListener;

    public alarmRecyclerView(Context context, Activity alarmActivity, AlarmViewModel alarmViewModel, OnAlarmListener onAlarmListener){
        mContext = context;
        mAlarmActivity = alarmActivity;
        mInflator = LayoutInflater.from(context);
        mAlarmViewModel = alarmViewModel;
        mOnAlarmListener = onAlarmListener;
    }

    public void deleteItem(int position){
        mRecentlyDeletedItem = mLocalDataSet.get(position);
        mRecentlyDeletedItemTitle = mRecentlyDeletedItem.getName();
        mRecentlyDetletedItemPosition = position;
        mLocalDataSet.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        mAlarmViewModel.delete(mRecentlyDeletedItem);
        showUndoSnackbar();
    }

    private void showUndoSnackbar(){
        View mView = mAlarmActivity.findViewById(R.id.mainClockLayout);
        Snackbar snackbar = Snackbar.make(mView, mRecentlyDeletedItemTitle + " has been deleted", Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", v-> undoDelete());
        snackbar.show();
    }

    private void undoDelete(){
        mLocalDataSet.add(mRecentlyDetletedItemPosition, mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDetletedItemPosition);
    }

    public Context getmContext(){
        return mContext;
    }

    public static class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final SwitchMaterial mSwitchAlarm;
        private final TextView mTimeTextView;
        private final TextView mNameTextView;
        private static List<Alarm> mDataSet;
        private final OnAlarmListener mOnAlarmListener;

        public AlarmViewHolder(@NonNull View itemView, List<Alarm> dataSet, OnAlarmListener onAlarmListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            mSwitchAlarm = itemView.findViewById(R.id.materialSwitch);
            mTimeTextView = itemView.findViewById(R.id.alarmTime);
            mNameTextView = itemView.findViewById(R.id.alarmName);
            mDataSet = dataSet;

            mSwitchAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mOnAlarmListener.onSlideChanged(mDataSet.get(getAdapterPosition()), isChecked);
                }
            });
            mOnAlarmListener = onAlarmListener;
        }

        @Override
        public void onClick(View v) {
            mOnAlarmListener.onAlarmClick(mDataSet.get(getAdapterPosition()));
        }

        public static void setmDataSet(List<Alarm> dataset){
            mDataSet = dataset;
        }
    }

    public interface OnAlarmListener{
        void onAlarmClick(Alarm alarm);
        void onSlideChanged(Alarm alarm, boolean isChecked);
    }

    public void setmLocalDataSet(List<Alarm> mLocalDataSet) {
        this.mLocalDataSet = mLocalDataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = mInflator.inflate(R.layout.alarm_row_item, viewGroup, false);
        return new AlarmViewHolder(view, mLocalDataSet, mOnAlarmListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        Alarm current = mLocalDataSet.get(position);
        holder.mNameTextView.setText(current.getName());
        String alarmTime = current.getTimeHour() + ":" + current.getTimeMinute();
        holder.mTimeTextView.setText(alarmTime);
        holder.mSwitchAlarm.setChecked(current.getActiveAlarm());
    }

    @Override
    public int getItemCount() {
        if (mLocalDataSet != null) {
            return mLocalDataSet.size();
        }else{
            return 0;
        }
    }
}
