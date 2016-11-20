package com.example.maimanhduy.phongtrodanang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ListBaiDang extends Activity {
    ArrayList<BaiDangObject> arrBaiDang = new ArrayList<BaiDangObject>();
    ArrayAdapterCustomListBaiDang myArrayAdapter = null;
    ListView listBaiDang = null;
    DatabaseReference mData;
    String TieuDe, GiaCa, linkPicture, dientich;
    ArrayList<String> listKeyArr = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bai_dang);
        mData = FirebaseDatabase.getInstance().getReference();
        listBaiDang = (ListView) findViewById(R.id.idListViewBaidang);
        arrBaiDang = new ArrayList<BaiDangObject>();
        myArrayAdapter = new ArrayAdapterCustomListBaiDang(this, R.layout.custom_layout_listview, arrBaiDang);
        listBaiDang.setAdapter(myArrayAdapter);
        getDataAddArrayList();
        listBaiDang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //arrBaiDang.remove(position);
                String keyBaiViet = listKeyArr.get(position);
                Intent myIntent = new Intent(ListBaiDang.this, ChiTietBaiDang.class);
                myIntent.putExtra("key", keyBaiViet);
                startActivity(myIntent);
                myArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getDataAddArrayList() {
        mData.child("BaiDang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrBaiDang.clear();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    listKeyArr.add(messageSnapshot.getKey());
                    TieuDe = (String) messageSnapshot.child("TieuDe").getValue();
                    GiaCa = (String) messageSnapshot.child("GiaCa").getValue();
                    dientich = (String) messageSnapshot.child("dientich").getValue();
                    if (GiaCa == null) {
                        continue;
                    }
                    if (TieuDe == null) {
                        continue;
                    }
                    if (dientich == null) {
                        continue;
                    }
                    linkPicture = (String) messageSnapshot.child("linkPicture").getValue();
                    BaiDangObject baidang = new BaiDangObject(TieuDe, GiaCa, "", "", "", linkPicture, dientich);
                    arrBaiDang.add(baidang);
                }
                Collections.reverse(listKeyArr);
                Collections.reverse(arrBaiDang);
                //Đảo ngược mảng.
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
