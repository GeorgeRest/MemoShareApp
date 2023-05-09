package com.george.memoshareapp.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.george.memoshareapp.R;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.Fragment
 * @className: CalendarTripFragment
 * @author: George
 * @description: TODO
 * @date: 2023/5/8 21:24
 * @version: 1.0
 */
public class CalendarTripFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar_trip_page, container, false);
        return view;
    }
}
