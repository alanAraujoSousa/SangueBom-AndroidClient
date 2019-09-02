package com.bom.sangue.sanguebom.persistence.dao;

import android.content.ContentValues;
import android.content.Context;

import com.bom.sangue.sanguebom.Utils.BloodTypeEnum;
import com.bom.sangue.sanguebom.Utils.GenderEnum;
import com.bom.sangue.sanguebom.persistence.bean.Patient;

/**
 * Created by alan on 06/12/15.
 */
public class PatientDAO extends GenericDAO<Patient> {

    private static final String TABLE = "Patient";
    // FIXME
    public static final String SCRIPT_CREATE_TABLE = "CREATE TABLE Patient ( id TEXT PRIMARY KEY, name TEXT, blood_type TEXT, gender TEXT)";
    public static final String SCRIPT_DELETE_TABLE =  "DROP TABLE IF EXISTS " + TABLE;

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String BLOOD_TYPE = "blood_type";
    private static final String GENDER = "gender";

    private static PatientDAO instance;

    public static PatientDAO getInstance(Context context) {
        if(instance == null || !instance.dataBase.isOpen())
            instance = new PatientDAO(context);
        return instance;
    }

    private PatientDAO(Context context) {
        super(context);
    }

    @Override
    public String getPrimaryKeyName() {
        return ID;
    }

    @Override
    public String getTableName() {
        return TABLE;
    }

    @Override
    public ContentValues entityToContentValues(Patient entity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, entity.getId());
        contentValues.put(NAME, entity.getName());
        contentValues.put(BLOOD_TYPE, entity.getBloodType().getType());
        contentValues.put(GENDER, entity.getGender().getGender());
        return contentValues;
    }

    @Override
    public Patient contentValuesToEntity(ContentValues contentValues) {
        Patient patient = new Patient();
        patient.setId(contentValues.getAsLong(ID));
//        patient.setName(contentValues.getAsString("first_name") + " "
//                + contentValues.getAsString("last_name"));
        patient.setName(contentValues.getAsString(NAME));
        patient.setBloodType(BloodTypeEnum.getTypeEnum(contentValues.getAsString(BLOOD_TYPE)));
        patient.setGender(GenderEnum.getGenderEnum(contentValues.getAsString(GENDER)));

        return patient;
    }
}
