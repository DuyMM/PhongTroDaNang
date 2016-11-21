package com.example.maimanhduy.phongtrodanang;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.maimanhduy.phongtrodanang.DangNhap.STRING_KEY_NAME;

public class TabListBaiDangCuaToi extends Activity {
    ArrayList<BaiDangObject> arrBaiDang = new ArrayList<BaiDangObject>();
    ArrayAdapterCustomListBaiDang myArrayAdapter = null;
    String TieuDe, GiaCa, linkPicture, dientich, Username;
    ListView listView;
    DatabaseReference mData;
    ArrayList<String> keynguoiDung = new ArrayList<String>();
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_list_bai_dang_cua_toi);
        mData = FirebaseDatabase.getInstance().getReference();
        SharedPreferences prefs = getSharedPreferences(STRING_KEY_NAME, MODE_PRIVATE);
        Username = prefs.getString("name", "");
        listView = (ListView) findViewById(R.id.litMyPost);
        myArrayAdapter = new ArrayAdapterCustomListBaiDang(this, R.layout.custom_layout_listview, arrBaiDang);
        listView.setAdapter(myArrayAdapter);
        getMyKeyAddArrayList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int position = i;
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getParent());
                builder1.setMessage("Your Choose");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mData.child("BaiDang").child(keynguoiDung.get(position).toString()).removeValue();
                                mData.child("NguoiDung").child(Username).child("BaiDangCuaToi").child(keynguoiDung.get(position).toString()).removeValue();
                                Intent myIntent = new Intent(TabListBaiDangCuaToi.this, QuanLyThongTinCaNhan.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        });

                builder1.setNegativeButton(
                        "Edit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent myIntent = new Intent(TabListBaiDangCuaToi.this, UpdateBaiDang.class);
                                myIntent.putExtra("key", keynguoiDung.get(position).toString());
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
                return false;
            }
        });
    }

    public void getMyKeyAddArrayList() {
        mData.child("NguoiDung").child(Username).child("BaiDangCuaToi").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                keynguoiDung.add(dataSnapshot.getKey().toString());
                getMyPostAddArrayList();
                i++;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getMyPostAddArrayList() {
        mData.child("BaiDang").child(keynguoiDung.get(i).toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TieuDe = (String) dataSnapshot.child("TieuDe").getValue();
                GiaCa = (String) dataSnapshot.child("GiaCa").getValue();
                dientich = (String) dataSnapshot.child("dientich").getValue();
                linkPicture = (String) dataSnapshot.child("linkPicture").getValue();
                if (TieuDe == null || GiaCa == null || dientich == null) {
                } else {
                    BaiDangObject baidang = new BaiDangObject(TieuDe, GiaCa, "", "", "", linkPicture, dientich);
                    arrBaiDang.add(baidang);
                    myArrayAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
