package com.bom.sangue.sanguebom.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bom.sangue.sanguebom.R;
import com.bom.sangue.sanguebom.Utils.Constants;
import com.bom.sangue.sanguebom.Utils.HttpManager;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by alan on 29/11/15.
 */
public class SignupFragment extends Fragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.signup, container, false);
        ArrayAdapter<CharSequence> bloodTypeAdpt =
                ArrayAdapter.createFromResource(getActivity()
                        , R.array.categoria_sangue,
                        android.R.layout.simple_spinner_item);

        ArrayAdapter<CharSequence> genderAdpt =
                ArrayAdapter.createFromResource(getActivity()
                        , R.array.sexo,
                        android.R.layout.simple_spinner_item);

        ((Spinner) rootView.findViewById(R.id.signup_blood_type)).setAdapter(bloodTypeAdpt);
        ((Spinner) rootView.findViewById(R.id.signup_gender)).setAdapter(genderAdpt);
        ImageButton btn = (ImageButton) rootView.findViewById(R.id.signup_btn);
        btn.setOnClickListener(mSignUpUserListener);

        return rootView;
    }

    private View.OnClickListener mSignUpUserListener = new View.OnClickListener() {
        public void onClick(View v) {
            final String login = ((EditText) rootView.findViewById(R.id.signup_login)).getText().toString();
            final String password = ((EditText) rootView.findViewById(R.id.signup_password)).getText().toString();
            final String email = ((EditText) rootView.findViewById(R.id.signup_email)).getText().toString();

            // TODO Validade data.
            // TODO create a user to send.

           /* DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.signup_birth_date);
            int day = datePicker.getDayOfMonth();
            int month = datePicker.getMonth();
            int year = datePicker.getYear();

            // TODO Validate the birthDate only +18 !

            Date birthDate = new Date();
            Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            cal.set(year, month, day, 0, 0, 0);
            String date = cal.getTime().getTime()
*/

            String bloodType = ((Spinner) rootView.findViewById(R.id.signup_blood_type)).getSelectedItem().toString();
            String gender = ((Spinner) rootView.findViewById(R.id.signup_gender)).getSelectedItem().toString();

            try {
                final JSONObject user = new JSONObject();
                JSONObject profile = new JSONObject();
                user.put("username", login);
                user.put("password", password);
                user.put("email", email);
                user.put("userProfile", profile);
                profile.put("birth_date", "1990-12-20");
                profile.put("blood_type", bloodType);
                profile.put("gender", gender);

                JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL_SIGNUP, user,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                redirectToLogin();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                byte[] data  = error.networkResponse.data;
                                try {
                                    Log.e("SANGUE_BOM REQUEST", "onErrorResponse " + new String(data, "UTF-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }) {
                    // FIXME SECURITY FAILURE, this route has a admin token to register new users remove this dependence.
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", "Token " + Constants.ROOT_TOKEN);
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

                HttpManager.getInstance(getActivity().getApplicationContext()).addToRequestQueue(stringRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void redirectToLogin() {
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, new MyProfileFragment());
        ft.commit();
    }
}
