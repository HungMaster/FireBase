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

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private TextView txtRegister;
    private String user;
    private String pass;

    @Override
    public int getContentViewId() {
        return R.layout.screen_login;
    }

    @Override
    public void initComponents() {
        edtUsername = (EditText) findViewById(R.id.edt_username);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        txtRegister = (TextView) findViewById(R.id.txt_register);

    }

    @Override
    public void registerListener() {
        txtRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_register:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            case R.id.btn_login:
                checkLoginUser();
                break;
        }
    }

    private void checkLoginUser() {
        user = edtUsername.getText().toString();
        pass = edtPassword.getText().toString();

        if (user.equals("")) {
            edtUsername.setError("can't be blank");
        } else if (pass.equals("")) {
            edtPassword.setError("can't be blank");
        } else {
            String url = "https://fir-4a5a0.firebaseio.com/users";
            final ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Loading...");
            dialog.show();
            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("null")) {
                        Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (!object.has(user)) {
                                Toast.makeText(LoginActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                            } else if (object.getJSONObject(user).getString("password").equals(pass)) {
                                UserDetails.username = user;
                                UserDetails.password = pass;
                                startActivity(new Intent(LoginActivity.this, UsersActivity.class));
                            } else {
                                Toast.makeText(LoginActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    dialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(""+error);
                    dialog.dismiss();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            requestQueue.add(request);
        }
    }
}
