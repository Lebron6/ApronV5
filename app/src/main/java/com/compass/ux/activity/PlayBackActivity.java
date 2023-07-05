package com.compass.ux.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.compass.ux.R;
import com.compass.ux.adapter.FileListAdapter;
import com.compass.ux.base.BaseActivity;
import com.compass.ux.manager.PlayBackManager;
import com.compass.ux.tools.RecyclerViewHelper;

public class PlayBackActivity extends BaseActivity {

    private RecyclerView rv_playback;
    private FileListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);
        initView();
    }

    private void initView() {
        rv_playback = findViewById(R.id.rv_media);
        mListAdapter = new FileListAdapter(this);
        RecyclerViewHelper.initRecyclerViewG(this, rv_playback, mListAdapter, 3);
        PlayBackManager.getInstance().loadMediaFileList(this,mListAdapter);
    }

    @Override
    public boolean useEventBus() {
        return false;
    }
}
