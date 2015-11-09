package com.example.tenxingqq;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tenxingqq.view.MyLinearLayout;
import com.example.tenxingqq.view.SlideLayout_1;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {


    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SlideLayout_1 slideLayout = (SlideLayout_1) findViewById(R.id.slideLayout);
        slideLayout.setOnDragStateChangeListener(new SlideLayout_1.OnDragStateChangedListener() {
            @Override
            public void open() {

                show("开");
            }

            @Override
            public void close() {
                show("关");
            }

            @Override
            public void dragging(int offset) {
//                System.out.println(offset);
            }
        });

        list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("数据： "+i);
        }

        ListView listView = (ListView) findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        MyLinearLayout myLl = (MyLinearLayout) findViewById(R.id.ll_main_container);
        myLl.setSlideLayout(slideLayout);


    }

    private void show(String info){
        Toast.makeText(this,info,Toast.LENGTH_SHORT).show();

        System.out.println(~100);
    }




}
