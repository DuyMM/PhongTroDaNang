package com.example.maimanhduy.phongtrodanang;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by MaiManhDuy on 10/22/2016.
 */

public class ArrayAdapterCustomListBaiDang extends ArrayAdapter<BaiDangObject> {
    Activity context = null;
    ArrayList<BaiDangObject> myArray = null;
    int layoutId;

    public ArrayAdapterCustomListBaiDang(Activity context, int layoutId, ArrayList<BaiDangObject> arr) {
        super(context, layoutId, arr);
        this.context = context;
        this.layoutId = layoutId;
        this.myArray = arr;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);
        //chỉ là test thôi, bạn có thể bỏ If đi
        if (myArray.size() > 0 && position >= 0) {
            //dòng lệnh lấy TextView ra để hiển thị Mã và tên lên
            final TextView txtTieuDe = (TextView)
                    convertView.findViewById(R.id.cstTieuDe);
            final TextView txtGiaCa = (TextView)
                    convertView.findViewById(R.id.cstGia);
            final TextView txtDienTich = (TextView)
                    convertView.findViewById(R.id.cstDienTich);
            final ImageView imgHinhanh = (ImageView)
                    convertView.findViewById(R.id.cstImg);
            //lấy ra nhân viên thứ position
            final BaiDangObject emp = myArray.get(position);
            txtTieuDe.setText(emp.TieuDe.toString());
            txtGiaCa.setText(emp.GiaCa.toString() + " VND");
            txtDienTich.setText(emp.dientich.toString() + "m2");
            try {
                if (emp.linkPicture == null) {
                    Toast.makeText(context, "Mạng Yếu", Toast.LENGTH_SHORT).show();
                } else {
                    // Picasso.with(context).load(emp.linkPicture).into(imgHinhanh);
                    Glide.with(context)
                            .load(emp.linkPicture).override(150, 150)
                            .into(imgHinhanh);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Vì View là Object là dạng tham chiếu đối tượng, nên
        //mọi sự thay đổi của các object bên trong convertView
        //thì nó cũng biết sự thay đổi đó
        return convertView;//trả về View này, tức là trả luôn
        //về các thông số mới mà ta vừa thay đổi
    }
}
