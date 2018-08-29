package com.rancon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaishu on 5/2/18.
 */

public class MapHelper extends AsyncTask {
    String id;
    UserData tempUser;
    List<String> recipientUidList = new ArrayList<>();
    List<String> nameList = new ArrayList<>();
    List<String> locationList = new ArrayList<>();
    List<Bitmap> pictureList = new ArrayList<>();

    public MapHelper(String id) {
        super();
        this.id = id;
        tempUser = new UserData(id);
        GiveUserDataIns gUID = GiveUserDataIns.getInstance();
        gUID.userDataList.add(tempUser);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("cards").child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("TEST", dataSnapshot.toString());
                for (DataSnapshot datasnap : dataSnapshot.getChildren()) {
                    recipientUidList.add(datasnap.getValue(String.class));

                }
                for (final String recipient : recipientUidList) {


                    //DownloadNameFromServer
                    new GraphRequest(
                            AccessToken.getCurrentAccessToken(), "/" + recipient+"?fields=name,location", null, HttpMethod.GET, new GraphRequest.Callback() {
                                @Override
                                public void onCompleted(GraphResponse response) {
                            Log.e("TEST",response.getJSONObject().toString());
                            try {
                                JSONObject main = response.getJSONObject();
                                String name = main.getString("name");
                                nameList.add(name);
                                JSONObject location = main.getJSONObject("location");
                                String locat = location.getString("name");
                                locationList.add(location.getString("name"));
                                Log.e("TEST987654321",name+"   "+ locat );
                            } catch (Exception e) {
                                locationList.add("Delhi");
                                e.printStackTrace();
                            }
                        }
                    }
                    ).executeAsync();

                    //DownloadProfilePictureFromServer
                    try {
                        URL imgEnc = new URL("http://graph.facebook.com/" + id + "/picture?type=small");
                         Bitmap picture = BitmapFactory.decodeStream(imgEnc.openConnection().getInputStream());
                         pictureList.add(picture);
                    } catch (Exception e) {

                    }
                    Log.e("TEST",AccessToken.getCurrentAccessToken().getToken());
                }
                tempUser.setNameList(nameList);
                tempUser.setLocationList(locationList);
                tempUser.setPictureList(pictureList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

    }



}
