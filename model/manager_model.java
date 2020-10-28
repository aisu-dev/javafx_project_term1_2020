package model;

import java.util.ArrayList;

public class manager_model {

    private String name;
    private float balance;
    private ArrayList<business_model> business = new ArrayList<>();

    public manager_model(String name){
        this.name = name;
        this.balance = 10000000;
    }

    public String getName() {
        return name;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public ArrayList<business_model> getBusiness() {
        return business;
    }

    public void setBusiness(ArrayList<business_model> business) {
        this.business = business;
    }

    public void addBusiness(business_model business) {
        this.business.add(business);
    }

}
