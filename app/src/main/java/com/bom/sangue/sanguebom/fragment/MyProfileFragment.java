package com.bom.sangue.sanguebom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bom.sangue.sanguebom.R;
import com.bom.sangue.sanguebom.Utils.Constants;
import com.bom.sangue.sanguebom.Utils.GenderEnum;
import com.bom.sangue.sanguebom.Utils.HttpManager;
import com.bom.sangue.sanguebom.persistence.bean.User;
import com.bom.sangue.sanguebom.persistence.dao.UserDAO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by alan on 09/11/15.
 */
public class MyProfileFragment extends Fragment{

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (hasToken()) {
            rootView = inflater.inflate(R.layout.my_profile_layout, container, false);
            refreshLastDonation();
            String lastDonation = getUserDB().getLastDonation();
            String percent = convertToPercent(lastDonation);
            lastDonation = convertDateFormat(lastDonation);
            ((TextView) rootView.findViewById(R.id.last_donation)).setText(lastDonation);
            ((TextView) rootView.findViewById(R.id.percent_to_donate)).setText(percent);
        } else {
            rootView = inflater.inflate(R.layout.login, container, false);
            ImageButton signButton = (ImageButton) rootView.findViewById(R.id.sign_btn);
            signButton.setOnClickListener(mSignUserListener);
            ImageButton signupButton = (ImageButton) rootView.findViewById(R.id.sign_btn_signup);
            signupButton.setOnClickListener(mRedirectToSignup);
        }
        return rootView;
    }

    private void refreshLastDonation() {
        String urlLastDonation = Constants.URL_LAST_DONATION;
        User user = getUserDB();
        final String userToken = user.getToken();
        String url = urlLastDonation.replace("{id}", user.getLogin());

        try {
            JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String donation = null;
                            try {
                                donation = response.getString("donation_date");
                                String percent = convertToPercent(donation);
                                updateLastDonation(donation);
                                donation = convertDateFormat(donation);
                                ((TextView) rootView.findViewById(R.id.last_donation)).setText(donation);
                                ((TextView) rootView.findViewById(R.id.percent_to_donate)).setText(percent);
                            } catch (Exception e) {
                                Log.e("SANGUE_BOM donation", e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            try {
                                byte[] data  = error.networkResponse.data;
                                Log.e("SANGUE_BOM REQUEST", "onErrorResponse " + new String(data, "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Token " + userToken);
                    return headers;
                }
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
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

    private View.OnClickListener mRedirectToSignup = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container, new SignupFragment());
            ft.commit();
        }
    };

    private View.OnClickListener mSignUserListener = new View.OnClickListener() {
        public void onClick(View v) {
            EditText loginBox = (EditText) rootView.findViewById(R.id.sign_login);
            EditText passwordBox = (EditText) rootView.findViewById(R.id.sign_password);

            final String login = loginBox.getText().toString();
            final String password = passwordBox.getText().toString();

            // FIXME this is for DEV purposes
            if (login.equals("ivan")) {
                registerUser(new User("ivan", Constants.ROOT_TOKEN));
                refreshScreen();
                return;
            }

            try {
                final JSONObject j = new JSONObject();
                j.put("username", login);
                j.put("password", password);

                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL_SIGN, j,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String token = response.getString("token");
                                    User user = new User(login, token);
                                    registerUser(user);
                                    refreshScreen();
                                } catch (Exception e) {
                                    Log.e("SANGUE_BOM Login", e.getMessage());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "Ops, servidor indisponível!",
                                        Toast.LENGTH_SHORT).show();
                                try {
                                    error.printStackTrace();
                                    byte[] data  = error.networkResponse.data;
                                    Log.e("SANGUE_BOM REQUEST", "onErrorResponse " + new String(data, "UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                HttpManager.getInstance(getContext()).addToRequestQueue(stringRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private String convertToPercent(String lastDon) {
        String total = "0";
        try {
            Date now = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dateLastDon = sdf.parse(lastDon);

            Long diff = now.getTime() - dateLastDon.getTime();
            diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            GenderEnum gender = getUserDB().getGender();
            Double restDays = 60d;
            if (gender != null && gender.equals(GenderEnum.FEMALE))
                restDays = 90d;

            if (diff <= restDays) {
                Double delta = diff / restDays;
                delta *= 100;
                diff = delta.longValue();
            } else
                diff = 100l;

            total = diff.toString();
        } catch (Exception e) {
            total = "0'";
        }
        return total + "%";
    }

    private String convertDateFormat(String donation) {
        try {
            final String OLD_FORMAT = "yyyy-MM-dd";
            final String NEW_FORMAT = "dd/MM/yyyy";

            SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
            Date d = sdf.parse(donation);
            sdf.applyPattern(NEW_FORMAT);
            donation = sdf.format(d);
            return donation;
        } catch (Exception e) {
            return "Nunca doou ):";
        }
    }

    private void refreshScreen() {
        Fragment frg = getActivity().getSupportFragmentManager().findFragmentByTag("MyProfileFragment");
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }

    private void updateLastDonation(String lastDon) {
        UserDAO userDAO = UserDAO.getInstance(getActivity().getApplicationContext());
        User userDB = userDAO.findById(1); // Always save user on ID 1.
        userDB.setLastDonation(lastDon);
        userDAO.update(userDB);
        userDAO.closeConnection();
    }

    private void registerUser(User user) {
        user.setId(1l);
        UserDAO userDAO = UserDAO.getInstance(getActivity().getApplicationContext());
        User userDB = userDAO.findById(1); // Always save user on ID 1.
        if (userDB != null)
            userDAO.update(user);
        else
            userDAO.save(user);
        userDAO.closeConnection();
    }

    private boolean isUserRegistered() {
        UserDAO userDAO = UserDAO.getInstance(getActivity().getApplicationContext());
        User user = userDAO.findById(1); // Always save user on ID 1.
        userDAO.closeConnection();
        if (user != null)
            return true;
        return false;
    }

    private boolean hasToken() {
        UserDAO userDAO = UserDAO.getInstance(getActivity().getApplicationContext());
        User user = userDAO.findById(1); // Always save user on ID 1.
        userDAO.closeConnection();
        if (user != null && user.getToken() != null && !user.getToken().isEmpty())
            return true;
        return false;
    }

    private User getUserDB() {
        UserDAO userDAO = UserDAO.getInstance(getActivity().getApplicationContext());
        User user = userDAO.findById(1); // Always save user on ID 1.
        userDAO.closeConnection();
        return user;
    }
}
