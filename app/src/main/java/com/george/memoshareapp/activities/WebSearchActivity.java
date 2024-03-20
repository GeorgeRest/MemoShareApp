package com.george.memoshareapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.george.memoshareapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.just.agentweb.AgentWeb;

import es.dmoral.toasty.Toasty;

public class WebSearchActivity extends BaseActivity {

    private AgentWeb agentWeb;
    private static String uri = "https://www.baidu.com/s?wd=" ;
    private static String errorToast = "搜索内容获取失败，请于跳转后手动搜索" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_search);

        FloatingActionButton iv_back = (FloatingActionButton) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v->{
            finish();
        });

        Intent intent = getIntent();
        String searchContent = intent.getStringExtra("searchcontent");

        LinearLayout ll_web_search_root = (LinearLayout) findViewById(R.id.ll_web_search_root);

        if(searchContent == null || searchContent.equals("")){
            Toasty.info(this,errorToast);
        }
        searchContent = searchContent == null || searchContent.equals("") ? "" : searchContent;

        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(ll_web_search_root,
                        new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(uri + searchContent);

    }

    @Override
    protected void onResume() {
        agentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }



    @Override
    protected void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        agentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }
}