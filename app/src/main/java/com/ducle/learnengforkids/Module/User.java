package com.ducle.learnengforkids.Module;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private String username;
    private String matKhau;
    private int Diem;

    public User() {
    }

    public User( String username,String matKhau) {
        this.username = username;
        this.matKhau = matKhau;
        Diem = 0;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getDiem() {
        return Diem;
    }

    public void setDiem(int diem) {
        Diem = diem;
    }

    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("diem",Diem);
        result.put("matKhau",matKhau);
        return result;
    }
}
