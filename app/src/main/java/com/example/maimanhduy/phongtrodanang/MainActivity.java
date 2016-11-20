package com.example.maimanhduy.phongtrodanang;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TabHost;

import static com.example.maimanhduy.phongtrodanang.DangNhap.STRING_KEY_NAME;

public class MainActivity extends TabActivity {
    String data;
    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        tabHost = getTabHost();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("", getResources().getDrawable(R.drawable.ic_format_list_bulleted_red_500_24dp)).setContent(
                new Intent(this, ListBaiDang.class)));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("", getResources().getDrawable(R.drawable.ic_pin_drop_red_500_24dp)).setContent(
                new Intent(this, MapsActivityMarker.class)));
        getDataFromStogare();
    }

    public void getDataFromStogare() {
        SharedPreferences prefs = getSharedPreferences(STRING_KEY_NAME, MODE_PRIVATE);
        data = prefs.getString("name", "");
        if (data.equals("")) {
            tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("", getResources().getDrawable(R.drawable.login_01)).setContent(
                    new Intent(this, DangNhap.class)));
            //Dang Nhap
        } else {
            tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("", getResources().getDrawable(R.drawable.quanly)).setContent(
                    new Intent(this, QuanLyThongTinCaNhan.class)));
            //Quan ly
        }
    }
}

   /* Button btDangNhap = (Button) findViewById(R.id.btDangNhap);
    Button btDk = (Button) findViewById(R.id.btDK);
    TextView btdangBai = (TextView) findViewById(R.id.idDangBaiMain);
btDangNhap.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        Intent myIntent = new Intent(MainActivity.this, DangNhap.class);
        startActivity(myIntent);
        }
        });
        btDk.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        Intent myIntent = new Intent(MainActivity.this, DangKy.class);
        startActivity(myIntent);
        }
        });
        btdangBai.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {
        Intent myIntent = new Intent(MainActivity.this, DangBai.class);
        startActivity(myIntent);
        }
        });*/