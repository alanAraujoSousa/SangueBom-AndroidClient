package com.bom.sangue.sanguebom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bom.sangue.sanguebom.R;
import com.bom.sangue.sanguebom.persistence.bean.Patient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alan on 08/12/15.
 */
public class PatientListAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    List<Patient> itens = new ArrayList<>();

    public PatientListAdapter(Context context, List<Patient> itens) {
        this.itens = itens;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
       return this.itens.size();
    }

    @Override
    public Object getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_patient_item, null);
        }

        Patient item = itens.get(position);

        ((TextView) convertView.findViewById(R.id.list_patient_item_name)).setText(item.getName());
        String type = item.getBloodType().getType();
        if (type.length() != 3)
            type += " ";
        ((TextView) convertView.findViewById(R.id.list_patient_item_blood)).setText(type);
        ((TextView) convertView.findViewById(R.id.list_patient_item_id)).setText("id: " + item.getId().toString());
        return convertView;
    }
}
