package com.example.wkj_pc.fitnesslive.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.activity.LiveActivity;
import com.example.wkj_pc.fitnesslive.activity.PreparedLiveActivity;
import com.example.wkj_pc.fitnesslive.activity.TakePicActivity;

public class BottomSheetDialogFrag extends BottomSheetDialogFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.bottom_sheet_dialog_fragment, container, false);
        LinearLayout beginlive = (LinearLayout) view.findViewById(R.id.beginLiveBtn);
        LinearLayout beginShootBtn = (LinearLayout) view.findViewById(R.id.beginShootBtn);
        TextView closeBottomSheet = (TextView) view.findViewById(R.id.close_bottom_sheet_text_view);
        beginlive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PreparedLiveActivity.class));
                getDialog().cancel();
            }
        });
        beginShootBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TakePicActivity.class));
                getDialog().cancel();
            }
        });
        closeBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
            }
        });
        return view;
    }

}
