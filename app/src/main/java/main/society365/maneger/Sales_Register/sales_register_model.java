package main.society365.maneger.Sales_Register;

/**
 * Created by Anas on 2/7/2019.
 */

public class sales_register_model {

    public sales_register_model() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    String type;
    String name;
    String amount;
    String date1;
    String date2;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVn() {
        return vn;
    }

    public void setVn(String vn) {
        this.vn = vn;
    }

    String date;
    String vn;
    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    String test;

    public String getFlatno() {
        return flatno;
    }

    public void setFlatno(String flatno) {
        this.flatno = flatno;
    }

    String flatno;

    public sales_register_model(String name, String amount, String date1, String date2) {
        this.name = name;
        this.amount = amount;
        this.date1 = date1;
        this.date2 = date2;
    }
}
