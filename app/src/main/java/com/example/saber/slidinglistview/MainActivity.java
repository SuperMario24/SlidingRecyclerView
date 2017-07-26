package com.example.saber.slidinglistview;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<String> list = new ArrayList<String>();
    private SlidingAdapter slidingAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData();
        initView();
    }

    private void initView() {
        recyclerView=(RecyclerView) findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        slidingAdapter = new SlidingAdapter(list,this, new SlidingAdapter.OnClickListenerEditOrDelete() {
            @Override
            public void OnClickListenerEdit(int position) {
                Snackbar.make(recyclerView,"编辑",Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void OnClickListenerDelete(int position) {
                list.remove(position);
                slidingAdapter.notifyDataSetChanged();
                Snackbar.make(recyclerView,"删除",Snackbar.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(slidingAdapter);
    }

    public void getData() {
        for (int i=0;i<20;i++){
            list.add(new String("第"+i+"个item"));
        }
    }
}
