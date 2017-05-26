package com.experiment.lenovo.android_intern_vyas_atharva;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.R.attr.name;
import static com.google.android.gms.common.api.Status.st;
class SuccessS
{
    public static boolean success;
}
public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,View.OnClickListener{
    private static int RC_SIGN_IN=0;
    private static String TAG="MAIN_ACTIVITY";
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference rootref= FirebaseDatabase.getInstance().getReference();
    private DatabaseReference userref=rootref.child("users");
    private DatabaseReference discussionref=rootref.child("discussion");
    private Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        i=new Intent(this,TopicScreen.class);
        SuccessS.success=false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null)
        {
            User.ID=user.getEmail();
            Query query = userref.orderByChild("email").equalTo(User.ID);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                        User.firstname= (String) messageSnapshot.child("firstname").getValue();
                        User.lastname = (String) messageSnapshot.child("lastname").getValue();
                        User.address = (String) messageSnapshot.child("address").getValue();
                        startActivity(i);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    Toast.makeText(MainActivity.this,"loading please wait", Toast.LENGTH_LONG).show();
                    User.ID=user.getEmail();
                    Log.d("AFTE","user logge in:"+user.getEmail());
                }
                else
                {
                    Log.d("AFTE","user logged out");
                }
            }
        };
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        mGoogleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        if(mAuthListener!=null)
        {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void signin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void signout(){
        FirebaseAuth.getInstance().signOut();
    }
    public void onClick(View view)
    {
        Intent intent;
        switch (view.getId()){
            case R.id.sign_in_button:
                if(SuccessS.success==true) {
                    intent = new Intent(this, AddresssActivity.class);
                    startActivity(intent);
                }
                else {
                    User.firstname = ((EditText) findViewById(R.id.firstname)).getText().toString();
                    User.lastname = ((EditText) findViewById(R.id.lastname)).getText().toString();
                    signin();
                }
                break;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {

                Log.d(TAG,"Connection filed");
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        User.ID=acct.getEmail();
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("AUTH","signINWithCredentialsComplete:"+task.isSuccessful());
                        //intent.putExtra("fname", etFName.getText().toString());
                        //intent.putExtra("lname", etLName.getText().toString());

                        // ...
                        SuccessS.success=true;
                    }
                });

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionresult)
    {
        Toast.makeText(MainActivity.this, "No connection", Toast.LENGTH_LONG).show();
    }
}
