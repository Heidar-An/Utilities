package com.example.notesapplication.alarmFiles;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapplication.R;

public class AlarmSwipeToDelete extends ItemTouchHelper.SimpleCallback{

    private final alarmRecyclerView mRecyclerAdapter;

    // used for background and icon
    private final Drawable mIcon;
    private final ColorDrawable mBackground;

    public AlarmSwipeToDelete(alarmRecyclerView recyclerAdapter){
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mRecyclerAdapter = recyclerAdapter;
        mIcon = ContextCompat.getDrawable(mRecyclerAdapter.getmContext(), R.drawable.delete_bin);
        mBackground = new ColorDrawable(Color.RED);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mRecyclerAdapter.deleteItem(position);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20; //so background is behind the rounded corners of itemView

        int iconMargin = (itemView.getHeight() - mIcon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - mIcon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + mIcon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + mIcon.getIntrinsicWidth();
            mIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            mBackground.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - mIcon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            mIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            mBackground.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            mIcon.setBounds(0, 0, 0, 0);
            mBackground.setBounds(0, 0, 0, 0);
        }

        mBackground.draw(c);
        mIcon.draw(c);
    }
}
