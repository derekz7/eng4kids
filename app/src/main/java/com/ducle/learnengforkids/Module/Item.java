package com.ducle.learnengforkids.Module;

public class Item {
    private int resoureId;
    private String name;

    public Item(int resoureId, String name) {
        this.resoureId = resoureId;
        this.name = name;
    }

    public int getResoureId() {
        return resoureId;
    }

    public void setResoureId(int resoureId) {
        this.resoureId = resoureId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
