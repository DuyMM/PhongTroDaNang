package com.example.maimanhduy.phongtrodanang;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static com.example.maimanhduy.phongtrodanang.DangBai.RESULT_LOAD_IMG;
import static com.example.maimanhduy.phongtrodanang.DangNhap.STRING_KEY_NAME;

public class UpdateBaiDang extends AppCompatActivity {
    DatabaseReference mData;
    FirebaseStorage storage;
    StorageReference storageRef;
    String key, linkImage;
    private EditText edtTieuDe, edtDientich, edtGiaCa, edtThongtin, edtDiaChi;
    private ImageView imgAnh;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bai_dang);
        storage = FirebaseStorage.getInstance();
        SharedPreferences shared = getSharedPreferences(STRING_KEY_NAME, MODE_PRIVATE);
        mData = FirebaseDatabase.getInstance().getReference("BaiDang");
        init();
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        getValueData();
        imgAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImages();
            }
        });
    }

    public void getValueData() {
        mData.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("TieuDe").getValue().toString() == null) {

                } else {
                    edtTieuDe.setText(dataSnapshot.child("TieuDe").getValue().toString());
                    edtDientich.setText(dataSnapshot.child("dientich").getValue().toString());
                    edtGiaCa.setText(dataSnapshot.child("GiaCa").getValue().toString());
                    edtThongtin.setText(dataSnapshot.child("ThongTinChung").getValue().toString());
                    edtDiaChi.setText(dataSnapshot.child("DiaChi").getValue().toString());
                    Glide.with(UpdateBaiDang.this).load(dataSnapshot.child("linkPicture").getValue().toString()).into(imgAnh);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void upDateData() {
        mData.child(key).child("TieuDe").setValue(edtTieuDe.getText().toString());
        mData.child(key).child("GiaCa").setValue(edtGiaCa.getText().toString());
        mData.child(key).child("ThongTinChung").setValue(edtThongtin.getText().toString());
        mData.child(key).child("DiaChi").setValue(edtDiaChi.getText().toString());
        mData.child(key).child("dientich").setValue(edtDientich.getText().toString());
        if (linkImage.equals("")){
        }else {
            mData.child(key).child("linkPicture").setValue(linkImage);
        }
        Intent myIntent = new Intent(this, MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(myIntent);
    }

    public void init() {
        edtTieuDe = (EditText) findViewById(R.id.edtUpdateTieuDe);
        edtDiaChi = (EditText) findViewById(R.id.edtUpdateDiaChi);
        edtThongtin = (EditText) findViewById(R.id.edtUpdateThongTinChung);
        edtGiaCa = (EditText) findViewById(R.id.edtUpdateGia);
        edtDientich = (EditText) findViewById(R.id.edtUpdateDienTich);
        imgAnh = (ImageView) findViewById(R.id.imgUpdate);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgAnh.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImages() {
        storageRef = storage.getReferenceFromUrl("gs://demoduan2-a526a.appspot.com");
        Calendar calendar = Calendar.getInstance();
        StorageReference mountainsRef = storageRef.child("image" + key + calendar.getTimeInMillis() + ".jpeg");
        boolean hasDrawable = (imgAnh.getDrawable() != null);
        if(hasDrawable) {
            Bitmap bitmap = ((GlideBitmapDrawable) imgAnh.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            // so 50 bi la chat luong hinh anh.
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(UpdateBaiDang.this, "Hình ảnh bị lỗi" + exception, Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    linkImage = downloadUrl.toString();
                    upDateData();
                }
            });
        }
        else {
            linkImage = "";
            upDateData();
        }

    }
}
