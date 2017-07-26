package com.example.saber.slidinglistview;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MyRecyclerView recyclerView;
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
        recyclerView=(MyRecyclerView) findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        slidingAdapter = new SlidingAdapter(list,this, new SlidingAdapter.OnClickListenerEditOrDelete() {
            @Override
            public void OnClickListenerEdit(int position) {
                Snackbar.make(recyclerView,"编辑:"+position, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void OnClickListenerDelete(int position) {
                list.remove(position);
                //这里使用notifyItemRemoved(position)，如果使用notifyDataChanged，会因为复用问题，闪屏，原因不明。
                slidingAdapter.notifyItemRemoved(position);
                Snackbar.make(recyclerView,"删除:"+position, Snackbar.LENGTH_SHORT).show();
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
