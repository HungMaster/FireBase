package com.hungvt.firebase;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Windows7 on 09/10/2017.
 */

public class ChatActivity extends BaseActivity {
    private LinearLayout layoutContentChat;
    private EditText edtMessage;
    private ImageView imgSend;
    private ScrollView scrollView;
    private DatabaseReference reference1, reference2;


    @Override
    public int getContentViewId() {
        return R.layout.screen_chat;
    }

    @Override
    public void initComponents() {
        layoutContentChat = (LinearLayout) findViewById(R.id.layout_content_chat);
        edtMessage = (EditText) findViewById(R.id.edt_message);
        imgSend = (ImageView) findViewById(R.id.img_send);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);

        reference1 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-4a5a0.firebaseio.com/messages/"
                + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-4a5a0.firebaseio.com/messages/"
                + UserDetails.chatWith + "_" + UserDetails.username);
    }

    @Override
    public void registerListener() {
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contentMessage = edtMessage.getText().toString();
                if (!contentMessage.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", contentMessage);
                    map.put("user", UserDetails.username);

                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    edtMessage.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                if (map != null) {
                    String message = map.get("message").toString();
                    String username = map.get("user").toString();
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void addMessageBox(String contentMessage, int type){
        TextView txtContentMessage = new TextView(ChatActivity.this);
        txtContentMessage.setText(contentMessage);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1.0f;
        if (type==1){
            layoutParams.gravity = Gravity.START;
            txtContentMessage.setBackgroundResource(R.drawable.bubble_in);
        }
        else {
            layoutParams.gravity = Gravity.END;
            txtContentMessage.setBackgroundResource(R.drawable.bubble_out);
        }
        txtContentMessage.setLayoutParams(layoutParams);
        layoutContentChat.addView(txtContentMessage);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
