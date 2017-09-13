package com.example.wkj_pc.fitnesslive.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import com.example.wkj_pc.fitnesslive.R;
import com.example.wkj_pc.fitnesslive.adapter.UploadNativeVideoAdapter;
import com.example.wkj_pc.fitnesslive.po.UploadVideo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UploadNativeVideoActivity extends AppCompatActivity {

    List<UploadVideo> nativeVideos;
    @BindView(R.id.upload_native_video_choose_recycler_view)
    RecyclerView uploadNativeVideoChooseRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_native_video);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.user_upload_native_video_cancel_text_view, R.id.user_upload_native_video_choose_text_view,R.id.upload_native_video_choose_recycler_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.user_upload_native_video_choose_text_view:

                break;
            case R.id.upload_native_video_choose_recycler_view:

                break;
            case R.id.user_upload_native_video_cancel_text_view:
                finish();
                break;
        }
    }
    /** 显示本地视频 （单选进行上传，跳转到上传页面）*/
    public void initRecyclerView(){

        UploadNativeVideoAdapter adapter=new UploadNativeVideoAdapter(nativeVideos);
        StaggeredGridLayoutManager manager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        uploadNativeVideoChooseRecyclerView.setAdapter(adapter);
        uploadNativeVideoChooseRecyclerView.setLayoutManager(manager);
    }
}
