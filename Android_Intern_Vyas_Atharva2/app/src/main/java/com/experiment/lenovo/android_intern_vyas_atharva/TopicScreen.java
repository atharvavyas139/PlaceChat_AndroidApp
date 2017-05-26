package com.experiment.lenovo.android_intern_vyas_atharva;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

import static android.R.attr.button;

class Tconstant {
    static String topicname;
}

class Topic {
    String topicname;
    String address;

    public Topic() {

    }

    ;

    public Topic(String t, String a) {
        this.topicname = t;
        this.address = a;
    }
}

public class TopicScreen extends AppCompatActivity {
    private DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userref = rootref.child("users");
    private DatabaseReference discussionref = rootref.child("discussion");
    private DatabaseReference topicref = rootref.child("topic");
    private TextView tl;
    boolean gettopics = false;
    Vector<String> topics = new Vector<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_screen);
        getTopics();
    }

    public void getTopics() {

        Query query = topicref.orderByChild("address").equalTo(User.address);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String topicname = (String) messageSnapshot.child("topicname").getValue();
                    if (topicname != null && topicname.length() >= 1) {
                        topics.add(topicname);
                    }
                    //tl.append(topicname);
                }
                addbuttons();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }

    public void addbuttons() {
        Button my[] = new Button[topics.size()];
        String st;
        for (int i = 0; i < topics.size(); ++i) {
            my[i] = new Button(this);
            Tconstant.topicname = topics.elementAt(i);
            my[i].setText(topics.elementAt(i));
            my[i].setBackgroundColor(Color.parseColor("#E8F5E9"));
            my[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonpressed(v);
                }
            });
            LinearLayout ll = (LinearLayout) findViewById(R.id.topiclayout);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(8,8,8,8);
            ll.addView(my[i], lp);
        }
    }
    @Override
    public void onBackPressed() {
    }
    public void buttonpressed(View view) {
        Intent intent = new Intent(this, HomeScreen.class);
        Button b = (Button)view;
        String buttonText = b.getText().toString();
        Tconstant.topicname =buttonText;
        startActivity(intent);
    }

    public void addtopic(View v) {
        Button my;
        switch (v.getId()) {
            case (R.id.add_topic_button):
                String topic = (((EditText) findViewById(R.id.topicetext)).getText()).toString();
                ((EditText) (findViewById(R.id.topicetext))).setText("");
                if (topic.length() >= 1) {
                    Toast.makeText(TopicScreen.this, "topic added", Toast.LENGTH_LONG).show();
                    String userId = topicref.push().getKey();
                    topicref.child(userId).setValue(new Topic(topic, User.address));
                    discussionref.child(userId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    my = new Button(this);
                    Tconstant.topicname =topic;
                    my.setText(topic);
                    my.setBackgroundColor(Color.parseColor("#E8F5E9"));
                    my.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            buttonpressed(v);
                        }
                    });
                    LinearLayout ll = (LinearLayout) findViewById(R.id.topiclayout);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(8,8,8,8);
                    ll.addView(my, lp);
                } else {
                    Toast.makeText(TopicScreen.this, "empty topic can not be added", Toast.LENGTH_LONG).show();
                }
                break;
            /*case (R.id.minusbutton):
                Button myButton = new Button(this);
                myButton.setText("Remove Me");

                LinearLayout ll = (LinearLayout)findViewById(R.id.buttonlayout);
                LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
                ll.removeView(myButton, lp);
                break;*/
        }
    }
}
