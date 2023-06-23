package com.george.memoshareapp.view;

import android.content.Context;
import android.graphics.Canvas;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.view
 * @className: MeiZuMonthView
 * @author: George
 * @description: TODO
 * @date: 2023/6/22 22:36
 * @version: 1.0
 */
public class MeiZuMonthView extends MonthView {
    public MeiZuMonthView(Context context) {
        super(context);
    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {

    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {

    }
}
