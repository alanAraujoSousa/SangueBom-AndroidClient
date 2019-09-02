package com.bom.sangue.sanguebom.persistence.bean;

import com.bom.sangue.sanguebom.Utils.BloodTypeEnum;
import com.bom.sangue.sanguebom.Utils.GenderEnum;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by alan on 25/11/15.
 */
public class User implements Persistent{

    private Long id;
    private String login;
    private Date lastDonation;
    private String email;
    private BloodTypeEnum bloodType;
    private GenderEnum gender;
    private String token;

    public User(String login, String email, String token) {
        this.login = login;
        this.email = email;
        this.token = token;
    }

    public User(String login, String token) {
        this.login = login;
        this.token = token;
    }

    public User(String token) {
        this.token = token;
    }

    public User() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BloodTypeEnum getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodTypeEnum bloodType) {
        this.bloodType = bloodType;
    }

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getLastDonation() {
        return lastDonation == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(lastDonation);
    }

    public void setLastDonation(Date lastDonation) {
        this.lastDonation = lastDonation;
    }

    public void setLastDonation(String lastDonation) {
        if(lastDonation != null)
            try {
                this.lastDonation = new SimpleDateFormat("yyyy-MM-dd").parse(lastDonation);
            } catch (ParseException e) {
                e.printStackTrace();
            }
    }
}
