package com.example.maimanhduy.phongtrodanang;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.maimanhduy.phongtrodanang.DangNhap.STRING_KEY_NAME;

public class TabThongTinChiTiet extends AppCompatActivity {
    String name, sdt, diachi;
    EditText edName, edDiaChi, edSoDienThoai;
    Button btUpdate;
    DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_thong_tin_chi_tiet);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mData = FirebaseDatabase.getInstance().getReference().child("NguoiDung");
        init();
        getDataFromStogare();
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInformation();
            }
        });
    }

    public void getDataFromStogare() {
        SharedPreferences prefs = getSharedPreferences(STRING_KEY_NAME, MODE_PRIVATE);
        name = prefs.getString("name", "");
        sdt = prefs.getString("sdt", "");
        diachi = prefs.getString("diachi", "");
        edName.setText(name);
        edDiaChi.setText(diachi);
        edSoDienThoai.setText(sdt);
    }

    public void init() {
        edName = (EditText) findViewById(R.id.edtChiTietName);
        edDiaChi = (EditText) findViewById(R.id.edtChitietDiaChi);
        edSoDienThoai = (EditText) findViewById(R.id.edtChiTietSDT);
        btUpdate = (Button) findViewById(R.id.btUpdate);
    }

    public void updateInformation() {
        mData.child(name).child("diachi").setValue(edDiaChi.getText().toString());
        mData.child(name).child("sdt").setValue(edSoDienThoai.getText().toString());
        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
    }
}
