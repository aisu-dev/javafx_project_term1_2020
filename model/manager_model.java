package model;

import java.util.ArrayList;

public class manager_model {

    private String name;
    private double balance;
    private ArrayList<business_model> business = new ArrayList<>();

    public manager_model(String name){
        this.name = name;
        this.balance = 50000;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
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
