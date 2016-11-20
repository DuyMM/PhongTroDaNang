package com.example.maimanhduy.phongtrodanang;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static com.example.maimanhduy.phongtrodanang.DangNhap.STRING_KEY_NAME;

public class DangBai extends Activity {
    public static final int RESULT_LOAD_IMG = 1;
    Button btDangBai;
    EditText edTieuDe, edgiaca, edThongTin, edDiaChi, edDienTich;
    DatabaseReference mData;
    String User, key, linkImage, sdt;
    ImageView images1;
    FirebaseStorage storage;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_bai);
        SharedPreferences shared = getSharedPreferences(STRING_KEY_NAME, MODE_PRIVATE);
        User = (shared.getString("name", ""));
        sdt = shared.getString("sdt", "");
        //User sau nay se lay tu bo nho
        storage = FirebaseStorage.getInstance();
        mData = FirebaseDatabase.getInstance().getReference("BaiDang");
        Anhxa();
        btDangBai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImages();
            }
        });
        images1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
// Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
    }

    public void Anhxa() {
        btDangBai = (Button) findViewById(R.id.btDangBai);
        edTieuDe = (EditText) findViewById(R.id.edtTieuDe);
        edgiaca = (EditText) findViewById(R.id.edtGia);
        edThongTin = (EditText) findViewById(R.id.edtThongTinChung);
        edDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edDienTich = (EditText) findViewById(R.id.edtDienTich);
        images1 = (ImageView) findViewById(R.id.imgSO1);
    }

    public void PushBaiDang() {
        String tieude = edTieuDe.getText().toString();
        String giaca = edgiaca.getText().toString();
        String thongtin = edThongTin.getText().toString();
        String diachi = edDiaChi.getText().toString();
        String dientich = edDienTich.getText().toString();
        BaiDangObject baiDangObject = new BaiDangObject(tieude, giaca, diachi, thongtin, sdt, linkImage, dientich);
        DatabaseReference myData = mData.push();
        myData.setValue(baiDangObject);
        key = myData.getKey();
        Toast.makeText(this, "Đăng bài thành công", Toast.LENGTH_LONG).show();
        LuuKeyNguoiDung();
        finish();
    }

    public void LuuKeyNguoiDung() {
        DatabaseReference mData = FirebaseDatabase.getInstance().getReference("NguoiDung");
        String datetimes = Calendar.getInstance().getTime().toString();
        mData.child(User).child("BaiDangCuaToi").child(key).setValue(datetimes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                images1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadImages() {
        storageRef = storage.getReferenceFromUrl("gs://demoduan2-a526a.appspot.com");
        Calendar calendar = Calendar.getInstance();
        StorageReference mountainsRef = storageRef.child("image" + User + calendar.getTimeInMillis() + ".jpeg");
        Bitmap bitmap = ((BitmapDrawable) images1.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        // so 50 bi la chat luong hinh anh.
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(DangBai.this, "Hình ảnh bị lỗi" + exception, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                linkImage = downloadUrl.toString();
                PushBaiDang();
            }
        });
    }
}
