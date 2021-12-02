package com.ducle.learnengforkids.Module;

import java.util.HashMap;

public class CauDo {
    private int id;
    private String trueAns;
    private String wrongAns;
    private String imgUrl;

    public CauDo() {
    }

    public CauDo( String trueAns, String wrongAns, String imgUrl) {
        this.trueAns = trueAns;
        this.wrongAns = wrongAns;
        this.imgUrl = imgUrl;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrueAns() {
        return trueAns;
    }

    public void setTrueAns(String trueAns) {
        this.trueAns = trueAns;
    }

    public String getWrongAns() {
        return wrongAns;
    }

    public void setWrongAns(String wrongAns) {
        this.wrongAns = wrongAns;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }


    public HashMap<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("trueAns",trueAns);
        result.put("wrongAns",wrongAns);
        result.put("imgUrl",imgUrl);
        return result;
    }
}
