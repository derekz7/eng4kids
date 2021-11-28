package com.ducle.learnengforkids.Module;

public class TuVung {
    private String noiDung;
    private LoaiTu loaiTu;
    private String imgUrl;

    public TuVung() {

    }

    public TuVung(String noiDung, LoaiTu loaiTu, String imgUrl) {
        this.noiDung = noiDung;
        this.loaiTu = loaiTu;
        this.imgUrl = imgUrl;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public LoaiTu getLoaiTu() {
        return loaiTu;
    }

    public void setLoaiTu(LoaiTu loaiTu) {
        this.loaiTu = loaiTu;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}
