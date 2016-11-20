package com.example.maimanhduy.phongtrodanang;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;

public class QuanLyThongTinCaNhan extends ActivityGroup {
    TabHost tabHost;
    Button btDangbai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_thong_tin_ca_nhan);
        Anhxa();
        addTab();
        btDangbai = (Button) findViewById(R.id.idDangBaiMoi);
        btDangbai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(QuanLyThongTinCaNhan.this, DangBai.class));
            }
        });
    }

    public void Anhxa() {
        tabHost = (TabHost) findViewById(android.R.id.tabhost);
    }

    public void addTab() {
        tabHost.setup(this.getLocalActivityManager());
        TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        tab1.setIndicator("Bài Đăng");
        tab1.setContent(new Intent(this, TabListBaiDangCuaToi.class));
        tab2.setIndicator("Thông Tin");
        tab2.setContent(new Intent(this, TabThongTinChiTiet.class));
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
    }
}
