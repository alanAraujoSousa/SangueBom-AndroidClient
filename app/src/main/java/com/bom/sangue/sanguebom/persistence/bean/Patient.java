package com.bom.sangue.sanguebom.persistence.bean;

import com.bom.sangue.sanguebom.Utils.BloodTypeEnum;
import com.bom.sangue.sanguebom.Utils.GenderEnum;

/**
 * Created by alan on 06/12/15.
 */
public class Patient implements Persistent{

    private Long id;

    private String name;
    private BloodTypeEnum bloodType;
    private GenderEnum gender;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
