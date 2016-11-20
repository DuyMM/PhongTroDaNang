package com.example.maimanhduy.phongtrodanang;

/**
 * Created by MaiManhDuy on 10/20/2016.
 */

public class BaiDangObject {
    public String TieuDe, GiaCa, DiaChi, ThongTinChung, SDT, linkPicture, dientich;

    public BaiDangObject(String tieuDe, String giaCa, String diaChi, String thongTinChung, String SDT, String linkPicture, String dientich) {
        TieuDe = tieuDe;
        GiaCa = giaCa;
        DiaChi = diaChi;
        ThongTinChung = thongTinChung;
        this.SDT = SDT;
        this.linkPicture = linkPicture;
        this.dientich = dientich;
    }

    public BaiDangObject() {
    }
}
