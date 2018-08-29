package com.rancon;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.test.mock.MockApplication;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.SendButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    RecyclerView cardRecycler;
    List<String> cardResult = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardRecycler = (RecyclerView) findViewById(R.id.cardRecycler);
        cardRecycler.setLayoutManager(new LinearLayoutManager(this));
        if (StaticMethods.isOnline(this)) {
            getAllCards();
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }




    }

    public void getAllCards() {
        final String userId = AccessToken.getCurrentAccessToken().getUserId();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("cardIds");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("TEST", dataSnapshot.toString());
                for (DataSnapshot datasnap : dataSnapshot.getChildren()) {
                    cardResult.add(datasnap.getValue(String.class));
                }
                cardRecycler.setAdapter(new cardAdapter(MainActivity.this));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public class cardAdapter extends RecyclerView.Adapter<cardHolder> {
        LayoutInflater lf;

        public cardAdapter(Context c) {
            lf = LayoutInflater.from(c);
        }

        @Override
        public cardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = lf.inflate(R.layout.card_element, parent, false);
            cardHolder cardHolder = new cardHolder(v);
            return cardHolder;
        }

        @Override
        public void onBindViewHolder(cardHolder holder, final int position) {
            holder.tv.setText(cardResult.get(position));
            MapHelper mp = new MapHelper(cardResult.get(position));
            mp.execute();
           holder.fV.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Intent a = new Intent(MainActivity.this, MapActivity.class);
                   a.putExtra("cardId",cardResult.get(position));
                   startActivity(a);
               }
           });
        }

        @Override
        public int getItemCount() {
            return cardResult.size();
        }
    }

    public class cardHolder extends RecyclerView.ViewHolder {
        TextView tv;
        FrameLayout fV;

        public cardHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.cardId);
            fV = (FrameLayout) itemView.findViewById(R.id.main_card);
        }
    }


    public void garbage() {
                /*SendButton sendButton = (SendButton)findViewById(R.id.qwerty);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        sendButton.setShareContent(content);
        sendButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

            }
        });
        Button twet = (Button) findViewById(R.id.twet);
        twet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FacebookApi fA = new FacebookApi();
                fA.getFriendsList();
            }
        });

*/

    }


}
