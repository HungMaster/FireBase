package com.hungvt.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Windows7 on 08/10/2017.
 */

public class RegisterActivity extends BaseActivity
        implements View.OnClickListener{
    private EditText edtUsername, edtPassword;
    private Button btnRegister;
    private String user, pass;
    private TextView txtLogin;

    @Override
    public int getContentViewId() {
        return R.layout.screen_register;
    }

    @Override
    public void initComponents() {
        edtUsername = (EditText) findViewById(R.id.edt_register_username);
        edtPassword = (EditText) findViewById(R.id.edt_register_password);
        btnRegister = (Button) findViewById(R.id.btn_register);
        txtLogin = (TextView) findViewById(R.id.txt_login);

    }

    @Override
    public void registerListener() {
        txtLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_login:
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                break;

            case R.id.btn_register:
                checkRegisterUser();
                break;
        }
    }

    private void checkRegisterUser() {
        user = edtUsername.getText().toString();
        pass = edtPassword.getText().toString();

        if(user.equals("")){
            edtUsername.setError("can't be blank");
        }
        else if(pass.equals("")){
            edtPassword.setError("can't be blank");
        }
        else if(!user.matches("[A-Za-z0-9]+")){
            edtUsername.setError("only alphabet or number allowed");
        }
        else if(user.length()<6){
            edtUsername.setError("at least 6 characters long");
        }
        else if(pass.length()<8){
            edtPassword.setError("at least 8 characters long");
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            String url = "https://fir-4a5a0.firebaseio.com/users.json";
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-4a5a0.firebaseio.com/users");
                    if (response.equals("null")){
                        reference.child(user).child("password").setValue(pass);
                        Toast.makeText(RegisterActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                    }
                    else {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (!object.has(user)){
                                reference.child(user).child("password").setValue(pass);
                                Toast.makeText(RegisterActivity.this, "registration successful", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, "username already exists", Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    progressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("" + error );
                    progressDialog.dismiss();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
            requestQueue.add(request);
        }
    }
}
