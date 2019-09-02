package com.bom.sangue.sanguebom.Utils;

/**
 * Created by alan on 06/12/15.
 */
public enum GenderEnum {

    MALE("M"),
    FEMALE("F"),
    ;

    private String gender;

    private GenderEnum(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public static GenderEnum getGenderEnum(String gender) {
        for (GenderEnum type : values()) {
            if (type.getGender().equals(gender))
                return type;
        }
        return null;
    }
}
