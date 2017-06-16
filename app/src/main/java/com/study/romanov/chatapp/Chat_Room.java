package com.study.romanov.chatapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Chat_Room extends AppCompatActivity {

    private Button btnSendMsg;
    private EditText inputMsg;
    private TextView chatConversation;
    private DatabaseReference root;
    private String user_name, room_name;
    private String tmp_key;
    private String chat_msg;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        btnSendMsg = (Button) findViewById(R.id.btn_send_msg);
        inputMsg = (EditText) findViewById(R.id.msg_input);
        chatConversation = (TextView) findViewById(R.id.msgr);

        room_name = getIntent().getExtras().get("room_name").toString();
        setTitle(" Room - " + room_name);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user_name = user.getDisplayName();

        root = FirebaseDatabase.getInstance().getReference().child(room_name);

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> map = new HashMap<String, Object>();
                tmp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference massage_root = root.child(tmp_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("msg", inputMsg.getText().toString());
                inputMsg.setText("");
                massage_root.updateChildren(map2);

            }
        });


        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                apped_chat_conversetion(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                apped_chat_conversetion(dataSnapshot);

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
        });}



    private void apped_chat_conversetion(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while (i.hasNext()) {
            chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            chatConversation.append(user_name + " : " + chat_msg + "\n");
        }
    }
}



