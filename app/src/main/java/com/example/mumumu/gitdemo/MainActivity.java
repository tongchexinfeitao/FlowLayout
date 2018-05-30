package com.example.mumumu.gitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private FlowLayout flowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flowLayout = findViewById(R.id.flow_layout);

        //动态添加子view
        TextView textView = new TextView(this);
        textView.setText("成也风云，败也风云");
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        marginLayoutParams.leftMargin = 50;
        textView.setPadding(20,20,20,20);
        textView.setLayoutParams(marginLayoutParams);
        flowLayout.addView(textView);
    }
}
