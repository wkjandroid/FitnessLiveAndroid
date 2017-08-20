package com.example.wkj_pc.fitnesslive.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.fragment.MainPageFragment;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.home_main_content_fragment)
    LinearLayout homeMainContentFragment;
    private MainPageFragment mainPgaeFragment;
    private FragmentManager fragmentManager;
    public static android.support.v4.app.FragmentManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        manager=getSupportFragmentManager();
        fragmentManager = getFragmentManager();
        FragmentTransaction tran = fragmentManager.beginTransaction();
        mainPgaeFragment=new MainPageFragment();
        tran.add(R.id.home_main_content_fragment,mainPgaeFragment);
        tran.addToBackStack(null);
        tran.commit();
    }

}
