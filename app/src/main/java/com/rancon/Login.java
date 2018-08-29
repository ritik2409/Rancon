package com.rancon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class Login extends AppCompatActivity {
    final String EMAIL = "email";
    final String LOCATION = "user_location";
    final String FRIENDS = "user_friends";
    LoginButton loginButton;
    CallbackManager callbackManager;
    int a = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();


        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL, LOCATION, FRIENDS));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final String userId = AccessToken.getCurrentAccessToken().getUserId();
                final DatabaseReference ref =  FirebaseDatabase.getInstance().getReference().child("users");
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChild(userId) && a == 0) {
                            DatabaseReference uu =  ref.child(userId).child("cardIds");
                            DatabaseReference u1 = uu.push();
                            DatabaseReference u2 = uu.push();
                            u1.setValue(userId + "a");
                            u2.setValue(userId + "b");
                            DatabaseReference cardsRef =  FirebaseDatabase.getInstance().getReference().child("cards");
                            DatabaseReference c1 = cardsRef.child(userId + "a");
                            DatabaseReference ca1 = c1.push();
                            ca1.setValue(userId);
                            DatabaseReference c2 = cardsRef.child(userId + "b");
                            DatabaseReference ca2 = c2.push();
                            ca2.setValue(userId);
                        }
                        a = 1;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                Toast.makeText(Login.this, "Success", Toast.LENGTH_SHORT).show();
                Log.d("test",loginResult.getAccessToken().getUserId());
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onCancel() {
                Toast.makeText(Login.this, "Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
