package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FeedBackActivity extends AppCompatActivity {
    EditText et,et2,et3,et4;
    Button submit;
    String id,loc,email,others,spinnerText,message;
    Spinner mySpinner;
    TextInputLayout til;
    Intent intent;
    String url = "http://utlc.tlgateway.edu.my/utlcwatch/ApiController/sendData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        et = findViewById(R.id.editText);
        et2 = findViewById(R.id.editText2);
        et3 = findViewById(R.id.editText3);
        et4 = findViewById(R.id.editText4);
        mySpinner = findViewById(R.id.spinner);
        submit = findViewById(R.id.button);
        til = findViewById(R.id.shipper_layout);

        otherEditText();
        submitForm();
    }

    private void submitForm() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitData();
                refreshPage();
            }
        });
    }

    private void submitData() {
        id = et.getText().toString();
        loc = et2.getText().toString();
        email = et3.getText().toString();
        spinnerText = mySpinner.getSelectedItem().toString();
        others = et4.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parseData(response);
                        Log.d("onResponse",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("onErrorResponse",error.toString());
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("id_no",id);
                params.put("location",loc);
                params.put("email",email);
                params.put("problem_type",spinnerText);
                params.put("other",others);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void parseData(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            message = jsonObject.getString("message");
            Toast.makeText(this,"Submitted "+message, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void otherEditText() {
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerText = mySpinner.getSelectedItem().toString();
                if(spinnerText.equals("OTHERS")){
                    et4.setVisibility(View.VISIBLE);
                    et4.requestFocus();
                    til.setHintEnabled(true);
                }else {
                    et4.setVisibility(View.INVISIBLE);
                    til.setHintEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void refreshPage(){
        intent = getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        startActivity(intent);
    }
}
