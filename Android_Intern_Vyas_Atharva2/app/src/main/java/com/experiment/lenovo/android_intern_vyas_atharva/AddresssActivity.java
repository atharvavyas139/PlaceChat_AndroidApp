package com.experiment.lenovo.android_intern_vyas_atharva;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

class User {
    public static String ID;
    public static String address;
    public static String firstname;
    public static String lastname;
    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
}
class OUser{
    public String email;
    public String address;
    public String firstname;
    public String lastname;
    public OUser()
    {

    }
    public OUser(String id,String add,String f,String l)
    {
        this.email=id;
        this.address=add;
        this.firstname=f;
        this.lastname=l;
    }
}
public class AddresssActivity extends AppCompatActivity {
    private DatabaseReference rootref;
    private DatabaseReference userref;
    TextView t;
    String userId;
    private static final String[] ADDRESSES = new String[] {
            "449 Palo Verde Road, Gainesville, FL", "6731 Thompson Street, Gainesville, FL", "8771 Thomas Boulevard, Orlando, FL", "1234 Verano Place, Orlando, FL"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresss);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.addresstext);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,ADDRESSES);
        textView.setAdapter(adapter);
    }
    protected void onStart()
    {
        super.onStart();
    }
    public void addressEntered(View view){
        rootref = FirebaseDatabase.getInstance().getReference();
        userref = rootref.child("users");
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.addresstext);
        String userId = userref.push().getKey();
        User.address=((AutoCompleteTextView)(findViewById(R.id.addresstext))).getText().toString();
        userref.child(userId).setValue(new OUser(User.ID,textView.getText().toString(),User.firstname,User.lastname));
        userref.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Intent intent=new Intent(this,TopicScreen.class);
        startActivity(intent);
    }
}
