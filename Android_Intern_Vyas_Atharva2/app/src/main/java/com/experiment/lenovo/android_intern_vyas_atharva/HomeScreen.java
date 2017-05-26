package com.experiment.lenovo.android_intern_vyas_atharva;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.R.attr.name;
class Discussion
{
    String topic;
    String address;
    String chat;
    String email;
    String topic_address;
    String firstname;
    public Discussion(){

    }
    public Discussion(String t,String a,String c,String e,String ta,String f)
    {
        this.topic=t;
        this.address=a;
        this.chat=c;
        this.email=e;
        this.topic_address=ta;
        this.firstname=f;
    }
}
public class HomeScreen extends AppCompatActivity {
    TextView t,ct;
    Button b1;
    Button b2;
    private DatabaseReference rootref= FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userref=rootref.child("users");
    private DatabaseReference discussionref=rootref.child("discussion");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ((TextView)(findViewById(R.id.topictitle))).append(Tconstant.topicname);
    }
    public void adddiscussion(View view)
    {
        ct=(TextView)findViewById(R.id.chatcollection);
        Toast.makeText(HomeScreen.this, "chat added", Toast.LENGTH_LONG).show();
        EditText textView = (EditText) findViewById(R.id.discussion);
        String chat=textView.getText().toString();
        ct.append(chat);
        ((EditText)(findViewById(R.id.discussion))).setText("");
        String userId = discussionref.push().getKey();
        discussionref.child(userId).setValue(new Discussion(Tconstant.topicname,User.address,chat,User.ID,Tconstant.topicname+"_"+User.address,User.firstname));
        discussionref.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void getchat(View view)
    {
        ct=(TextView)findViewById(R.id.chatcollection);
        ct.setText("");
        ct.append("chats for this place and about the chosen topic:\n");
        Query query = discussionref.orderByChild("topic_address").equalTo(Tconstant.topicname+"_"+User.address);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    String name = (String) messageSnapshot.child("firstname").getValue();
                    String chat = (String) messageSnapshot.child("chat").getValue();
                    //Toast.makeText(HomeScreen.this, "found chat " + chat+" name: "+name, Toast.LENGTH_LONG).show();
                    String ss=chat+":"+name+"\n";
                    ct.append(ss);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
    @Override
    protected void onStart()
    {
        super.onStart();
    }
}
