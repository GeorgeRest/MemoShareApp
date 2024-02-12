package com.george.memoshareapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.george.memoshareapp.R;
import com.just.agentweb.AgentWeb;

public class WebSearchActivity extends AppCompatActivity {

    private AgentWeb agentWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_search);

        Intent intent = getIntent();
        String searchcontent = intent.getStringExtra("searchcontent");

        LinearLayout ll_web_search_root = (LinearLayout) findViewById(R.id.ll_web_search_root);

        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(ll_web_search_root, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go("https://www.baidu.com/s?wd=" + searchcontent);

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