package com.aros.apron.activity;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import com.aros.apron.R;
import com.aros.apron.adapter.FileListAdapter;
import com.aros.apron.base.BaseActivity;
import com.aros.apron.manager.PlayBackManager;
import com.aros.apron.tools.RecyclerViewHelper;

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
