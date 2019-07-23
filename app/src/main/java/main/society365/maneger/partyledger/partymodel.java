package main.society365.maneger.partyledger;

/**
 * Created by Anas on 1/30/2019.
 */



public class partymodel {

    public String title;
    public String rating;
    public String ledgerid;

    public partymodel() {

    }

    public partymodel(String title, String rating, String ledgerid) {
        this.title = title;
        this.rating = rating;
        this.ledgerid = ledgerid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getledgerid() {
        return ledgerid;
    }

    public void setledgerid(String ledgerid) {
        this.ledgerid = ledgerid;
    }
}
