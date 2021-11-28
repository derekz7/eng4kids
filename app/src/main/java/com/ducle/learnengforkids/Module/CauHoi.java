package com.ducle.learnengforkids.Module;

import java.util.List;

public class CauHoi {
    private List<TuVung> listLuachon;
    private TuVung dapan;

    public CauHoi() {
    }

    public CauHoi(List<TuVung> listLuachon, TuVung dapan) {
        this.listLuachon = listLuachon;
        this.dapan = dapan;
    }

    public List<TuVung> getListLuachon() {
        return listLuachon;
    }

    public void setListLuachon(List<TuVung> listLuachon) {
        this.listLuachon = listLuachon;
    }

    public TuVung getDapan() {
        return dapan;
    }

    public void setDapan(TuVung dapan) {
        this.dapan = dapan;
    }
}
