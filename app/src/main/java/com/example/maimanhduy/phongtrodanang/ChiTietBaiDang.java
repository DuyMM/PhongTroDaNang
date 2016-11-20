package com.example.maimanhduy.phongtrodanang;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChiTietBaiDang extends Activity {
    public String key, diachi;
    DatabaseReference mData;
    TextView txtTieuDe, txtGiaCa, txtDienTich, txtThongTin;
    Button btCall, btDanDuong;
    ImageView imgAnh;
    String Sdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_bai_dang);
        mData = FirebaseDatabase.getInstance().getReference("BaiDang");
        Anhxa();
        Intent myIntent = getIntent();
        key = myIntent.getStringExtra("key");
        getValueData();
        btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Sdt));
                startActivity(callIntent);
            }
        });
        btDanDuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(ChiTietBaiDang.this, MapsActivity.class);
                mapIntent.putExtra("origin", diachi);
                startActivity(mapIntent);
            }
        });
    }

    public void getValueData() {
        mData.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                txtTieuDe.setText(dataSnapshot.child("TieuDe").getValue().toString());
                txtDienTich.setText(dataSnapshot.child("dientich").getValue().toString() + " m2");
                txtGiaCa.setText(dataSnapshot.child("GiaCa").getValue().toString() + " VND");
                txtThongTin.setText(dataSnapshot.child("ThongTinChung").getValue().toString());
                Sdt = dataSnapshot.child("SDT").getValue().toString();
                diachi = dataSnapshot.child("DiaChi").getValue().toString();
                Glide.with(ChiTietBaiDang.this).load(dataSnapshot.child("linkPicture").getValue().toString()).into(imgAnh);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void Anhxa() {
        txtTieuDe = (TextView) findViewById(R.id.txtTieuDeCt);
        txtDienTich = (TextView) findViewById(R.id.txtDienTichCt);
        txtGiaCa = (TextView) findViewById(R.id.txtGiaCaCt);
        txtThongTin = (TextView) findViewById(R.id.txtThongTinChungCt);
        btCall = (Button) findViewById(R.id.btGoiNgay);
        btDanDuong = (Button) findViewById(R.id.btDanDuong);
        imgAnh = (ImageView) findViewById(R.id.imgHinhAnhct);
    }
}