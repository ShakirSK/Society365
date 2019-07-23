package main.society365.maneger.partyledger;


/**
 * Created by Anas on 1/30/2019.
 */



public class partydetailmodel {

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVoucher_type() {
        return voucher_type;
    }

    public void setVoucher_type(String voucher_type) {
        this.voucher_type = voucher_type;
    }

    public String getVoucher_number() {
        return voucher_number;
    }

    public void setVoucher_number(String voucher_number) {
        this.voucher_number = voucher_number;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public partydetailmodel() {
    }

    public String date;
    public String voucher_type;

    public partydetailmodel(String date, String voucher_type, String voucher_number, String amount,String Period) {
        this.date = date;
        this.voucher_type = voucher_type;
        this.voucher_number = voucher_number;
        this.amount = amount;
        this.Period=Period;
    }

    public String voucher_number;
    public String amount;

    public String getPrev_balance() {
        return prev_balance;
    }

    public void setPrev_balance(String prev_balance) {
        this.prev_balance = prev_balance;
    }

    public String getDr_cr() {
        return dr_cr;
    }

    public void setDr_cr(String dr_cr) {
        this.dr_cr = dr_cr;
    }

    public String prev_balance;
    public String dr_cr;

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String period) {
        Period = period;
    }

    public String Period;

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String narration;




}
