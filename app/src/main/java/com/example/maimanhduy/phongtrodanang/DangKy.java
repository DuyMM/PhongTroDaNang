package com.example.maimanhduy.phongtrodanang;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangKy extends Activity {
    Button btDangky;
    EditText edtTenTaiKhoan, edtMatKhau, edtSDT, edtDiachi;
    DatabaseReference mData;
    String Username;
    String pass;
    String sdt;
    String diachi;
    boolean check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        mData = FirebaseDatabase.getInstance().getReference();
        Anhxa();
        btDangky.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dangkytk();
            }
        });
    }

    public void Anhxa() {
        btDangky = (Button) findViewById(R.id.btDangky);
        edtTenTaiKhoan = (EditText) findViewById(R.id.edtTenTaiKhoan);
        edtMatKhau = (EditText) findViewById(R.id.edtMatKhau);
        edtSDT = (EditText) findViewById(R.id.edtSDT);
        edtDiachi = (EditText) findViewById(R.id.edtDiaChi);
    }

        public void Dangkytk() {
            check = true;
            Username = edtTenTaiKhoan.getText().toString();
            pass = edtMatKhau.getText().toString();
            sdt = edtSDT.getText().toString();
            diachi = edtDiachi.getText().toString();
            if (Username.equals("")|| pass.equals("")||sdt.equals("")||diachi.equals("")){
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }else {
            mData.child("NguoiDung").child(Username).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() == null) {
                        NguoiDung user = new NguoiDung(diachi, pass, sdt);
                        mData.child("NguoiDung").child(Username).setValue(user);
                        Toast.makeText(DangKy.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        check = false;
                        finish();
                    }
                    if (check) {
                        Toast.makeText(DangKy.this, "Tên này đã có người đăng ký !!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }}
}
