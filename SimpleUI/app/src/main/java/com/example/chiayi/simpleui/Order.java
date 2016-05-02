package com.example.chiayi.simpleui;

import io.realm.RealmObject;

/**
 * Created by chiayi on 2016/4/25.
 */

//RealmObject 可以存入的物件
public class Order extends RealmObject {
    private String note;
    private String drinkName;
    private String storeInfo;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDrinkName() {
        return drinkName;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public String getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(String storeInfo) {
        this.storeInfo = storeInfo;
    }
}
