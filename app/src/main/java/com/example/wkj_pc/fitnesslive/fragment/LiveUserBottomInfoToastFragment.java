package com.example.wkj_pc.fitnesslive.fragment;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.wkj_pc.fitnesslive.R;

public class LiveUserBottomInfoToastFragment extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live_user_bottom_info_toast, container, false);
        return view;
    }
}
