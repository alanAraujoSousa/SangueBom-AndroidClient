package com.bom.sangue.sanguebom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bom.sangue.sanguebom.R;
import com.bom.sangue.sanguebom.Utils.BloodTypeEnum;
import com.bom.sangue.sanguebom.Utils.Constants;
import com.bom.sangue.sanguebom.Utils.GenderEnum;
import com.bom.sangue.sanguebom.Utils.HttpManager;
import com.bom.sangue.sanguebom.adapter.PatientListAdapter;
import com.bom.sangue.sanguebom.persistence.bean.Patient;
import com.bom.sangue.sanguebom.persistence.dao.PatientDAO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alan on 05/12/15.
 */
public class PatientFragment extends Fragment {

    View rootView;

    PatientListAdapter patientListAdpt;
    List<Patient> patients;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.list_emergency, container, false);

        patients = new ArrayList<Patient>();
        patients.addAll(getPreviousPatientList());

        ListView patientList = (ListView) rootView.findViewById(R.id.patient_list);
        patientListAdpt = new PatientListAdapter(getActivity(), patients);
        patientList.setAdapter(patientListAdpt);

        fillPatientList();
        return rootView;
    }

    private void fillPatientList() {
        try {
            JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, Constants.URL_LIST_PATIENT,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                patients.clear();
                                deleteAllPatients(); // clean database.
                                for (int i=0; i < response.length(); i++) {
                                    JSONObject item = response.getJSONObject(i);
                                    Long id = item.getLong("id");
                                    String firstName = item.getString("first_name");
                                    String lastName = item.getString("last_name");
                                    String name = firstName + " " + lastName;
                                    BloodTypeEnum bloodType = BloodTypeEnum.getTypeEnum(item.getString("blood_type"));
                                    GenderEnum gender = GenderEnum.getGenderEnum(item.getString("gender"));

                                    Patient patient = new Patient();
                                    patient.setId(id);
                                    patient.setName(name);
                                    patient.setBloodType(bloodType);
                                    patient.setGender(gender);

                                    registerPatient(patient);
                                    patients.add(patient); // fill screen
                                }

                                patients = alphabeticalOrdering(patients);
                                patientListAdpt.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String message = error.getMessage();
                            Log.e("SANGUE_BOM REQUEST", "onErrorResponse " + message);
                            try {
                                byte[] data  = error.networkResponse.data;
                                Log.e("SANGUE_BOM REQUEST", "onErrorResponse " + new String(data, "UTF-8"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }) {
                // FIXME SECURITY FAILURE, this route has a admin token to catch a list of patient remove this dependence.
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Token " + Constants.ROOT_TOKEN);
                    return headers;
                }
                @Override
                protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                    try {
                        if (response.data.length == 0) {
                            byte[] responseData = "{}".getBytes("UTF8");
                            response = new NetworkResponse(response.statusCode, responseData, response.headers, response.notModified);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    return super.parseNetworkResponse(response);
                }
            };

            HttpManager.getInstance(getContext()).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<Patient> getPreviousPatientList() {
        List<String> names = new ArrayList<>();

        PatientDAO patientDAO = PatientDAO.getInstance(getActivity().getApplicationContext());
        List<Patient> patients = patientDAO.listAll();
        patientDAO.closeConnection();
        patients = alphabeticalOrdering(patients);
        return patients;
    }

    private List<Patient> alphabeticalOrdering(List<Patient> patients) {
        // Ordering names.
        Collections.sort(patients, new Comparator<Patient>() {
            public int compare(Patient p1, Patient p2) {
                String n1 = p1.getName();
                String n2 = p2.getName();
                int res = String.CASE_INSENSITIVE_ORDER.compare(n1, n2);
                if (res == 0) {
                    res = n1.compareTo(n2);
                }
                return res;
            }
        });

        return patients;
    }

    private void deleteAllPatients() {
        PatientDAO patientDAO = PatientDAO.getInstance(getActivity().getApplicationContext());
        patientDAO.deleteAll();
        patientDAO.closeConnection();
    }

    private void registerPatient(Patient patient) {
        PatientDAO patientDAO = PatientDAO.getInstance(getActivity().getApplicationContext());
        patientDAO.save(patient);
        patientDAO.closeConnection();
    }
}
