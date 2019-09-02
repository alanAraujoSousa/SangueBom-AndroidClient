package com.bom.sangue.sanguebom.persistence.bean;

import java.util.Date;

/**
 * Created by alan on 25/11/15.
 */
public class Donation implements Persistent {

    private Long id;
    private Date donationDate;

    public Donation() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
    }

    public Date getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(Date donationDate) {
        this.donationDate = donationDate;
    }
}
