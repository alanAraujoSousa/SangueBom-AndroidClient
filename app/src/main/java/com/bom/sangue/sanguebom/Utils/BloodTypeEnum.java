package com.bom.sangue.sanguebom.Utils;

/**
 * Created by alan on 06/12/15.
 */
public enum BloodTypeEnum {

    OP("O+"),
    ON("O-"),
    AP("A+"),
    AN("A-"),
    BP("B+"),
    BN("B+"),
    ABP("AB+"),
    ABN("AB-"),
    ;

    private String type;

    private BloodTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static BloodTypeEnum getTypeEnum(String bloodType) {
        for (BloodTypeEnum type : values()) {
            if (type.getType().equals(bloodType))
                return type;
        }
        return null;
    }
}
