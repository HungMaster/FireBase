package com.hungvt.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Windows7 on 09/10/2017.
 */

public class UsersActivity extends BaseActivity {

    private TextView txtNoUser;
    private ListView lvUsers;
    private ArrayList<String> arrUsers;
    private int totalUser = 0;
    private ProgressDialog dialog;

    @Override
    public int getContentViewId() {
        return R.layout.screen_list_users;
    }

    @Override
    public void initComponents() {
        arrUsers = new ArrayList<>();
        txtNoUser = (TextView) findViewById(R.id.txt_no_users);
        lvUsers = (ListView) findViewById(R.id.lv_list_user);
        dialog = new ProgressDialog(UsersActivity.this);
        dialog.setMessage("Loading...");
        dialog.show();

        String url = "https://fir-4a5a0.firebaseio.com/users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    Iterator i = object.keys();
                    String key = "";
                    while (i.hasNext()){
                        key = i.next().toString();
                        if (!key.equals(UserDetails.username)){
                            arrUsers.add(key);
                        }
                        totalUser++;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (totalUser<=1){
                    txtNoUser.setVisibility(View.VISIBLE);
                    lvUsers.setVisibility(View.INVISIBLE);
                }
                else {
                    txtNoUser.setVisibility(View.INVISIBLE);
                    lvUsers.setVisibility(View.VISIBLE);
                    lvUsers.setAdapter(new ArrayAdapter<String>(UsersActivity.this,android.R.layout.simple_list_item_1,arrUsers));
                }
                dialog.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("" + error);
            }
        });

        RequestQueue queue = Volley.newRequestQueue(UsersActivity.this);
        queue.add(request);


    }

    @Override
    public void registerListener() {
        lvUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = arrUsers.get(position);
                startActivity(new Intent(UsersActivity.this,ChatActivity.class));
            }
        });
    }

}
