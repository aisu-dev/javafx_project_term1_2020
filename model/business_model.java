package model;

import javafx.scene.input.DataFormat;

import java.io.Serializable;
import java.util.ArrayList;

public class business_model implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int level;
    private float price;
    private float interest;
    private float profit;
    private int count;
    private int period;
    private String imgLink;
    public static final DataFormat DATA_FORMAT = new DataFormat("src.model.business_model");

    public business_model(String name,float price,float interest,float profit,int period,String imgLink){
        this.name = name;
        this.level = 1;
        this.price = price;
        this.interest = interest;
        this.profit = profit;
        this.period = period;
        this.imgLink = imgLink;
    }

    public String getName() {
        return name;
    }

    public int getPeriod() {
        return period;
    }

    public float getProfit() {
        return profit;
    }
    @Override
    public String toString(){
        return this.name;
    }

    public String getImgLink() {
        return imgLink;
    }

    public float getPrice() {
        return price;
    }

    public int getLevel() {
        return level;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setProfit(float profit) {
        this.profit = profit;
    }

    public float getInterest() {
        return interest;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
