package com.example.teh_k.ChoreMate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;

import java.lang.reflect.Field;

public class BottomNavigationViewHelper extends BottomNavigationView {

    public BottomNavigationViewHelper(Context context) {
        this(context, null);
    }

    public BottomNavigationViewHelper(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public BottomNavigationViewHelper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setItemIconTintList(null);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);

        final ViewGroup bottomMenu = (ViewGroup)getChildAt(0);
        final int bottomMenuChildCount = bottomMenu.getChildCount();
        BottomNavigationItemView item;

        try {
            Field shiftingMode = bottomMenu.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(bottomMenu, false);
            shiftingMode.setAccessible(false);
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }

        for(int i=0; i<bottomMenuChildCount; i++){
            item = (BottomNavigationItemView)bottomMenu.getChildAt(i);
            //this shows all titles of items
            item.setChecked(true);
        }
    }
}