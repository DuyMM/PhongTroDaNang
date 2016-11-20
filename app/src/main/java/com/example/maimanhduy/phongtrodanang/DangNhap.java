package com.example.maimanhduy.phongtrodanang;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class DangNhap extends Activity {
    public static final String STRING_KEY_NAME = "MyKeyStogare";
    EditText edTen, edMatKhau;
    Button btDangNhap, btDK;
    DatabaseReference mData;
    String userName, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        mData = FirebaseDatabase.getInstance().getReference();
        Anhxa();
        btDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangNhap();
            }
        });
        btDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(DangNhap.this, DangKy.class);
                startActivity(myIntent);
            }
        });
    }

    public void Anhxa() {
        edTen = (EditText) findViewById(R.id.edtTaiKhoan);
        edMatKhau = (EditText) findViewById(R.id.edtPassword);
        btDangNhap = (Button) findViewById(R.id.btDangNhap);
        btDK = (Button) findViewById(R.id.btDKonDangNhap);
    }

    public void getEditText() {
        userName = edTen.getText().toString();
        pass = edMatKhau.getText().toString();
    }

    public void dangNhap() {
        if (edTen.getText().toString().equals("") || edMatKhau.getText().toString().equals("")) {
            Toast.makeText(this, "Không được để trống các trường", Toast.LENGTH_SHORT).show();
        } else {
            getEditText();
            mData.child("NguoiDung").child(userName).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshotCha) {
                    if (dataSnapshotCha.getValue() != null) {
                        mData.child("NguoiDung").child(userName).child("password").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String data = dataSnapshot.getValue().toString();
                                if (pass.equals(data)) {
                                    Toast.makeText(DangNhap.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor saveEdit = getSharedPreferences(STRING_KEY_NAME, MODE_PRIVATE).edit();
                                    saveEdit.putString("name", userName);
                                    saveEdit.putString("sdt", dataSnapshotCha.child("sdt").getValue().toString());
                                    saveEdit.putString("diachi", dataSnapshotCha.child("diachi").getValue().toString());
                                    saveEdit.apply();
                                    Intent myIntent = new Intent(DangNhap.this, QuanLyThongTinCaNhan.class);
                                    startActivity(myIntent);
                                } else {
                                    Toast.makeText(DangNhap.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Toast.makeText(DangNhap.this, "Tài khoản hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
