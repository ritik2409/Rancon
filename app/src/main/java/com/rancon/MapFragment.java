package com.rancon;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.rancon.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback{

    SupportMapFragment mapFragment;
    private static final String TAG = MapFragment.class.getSimpleName();
    private ArrayList<Marker> mMarkerArray = new ArrayList<>();

    List<String> nameList;
    List<String> locationList;
    List<Bitmap> pictureList;
    List<LatLng> locCoordList;
    Bitmap imageBit;
    List<Address> address;
    Geocoder gc;
    String location;
    String uD;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        Button sendCard = (Button) view.findViewById(R.id.sendButton);
        sendCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openSendCard = new Intent (getActivity(), SendCard.class);
                openSendCard.putExtra("cardId",uD);
                getActivity().startActivity(openSendCard);
            }
        });

        mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment == null)
        {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map,mapFragment).commit();
        }

        Bundle args = getArguments();
        uD = args.getString("cardID", "Delhi");



        mapFragment.getMapAsync(this);
        return view;
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        /* UserData userData = new UserData(cardId);*/

        GiveUserDataIns gUDI = GiveUserDataIns.getInstance();
        UserData userData =  gUDI.finalUserDataIns(uD);
        nameList =  userData.getNameList();
        locationList = userData.getLocationList();
        pictureList = userData.getPictureList();

      /*  Bitmap bitmap1;
        bitmap1 = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_round);
        nameList = new ArrayList<String>();
        locationList = new ArrayList<String>();
        pictureList = new ArrayList<Bitmap>();
        nameList.add("Ritik");
        nameList.add("Karthik");
        nameList.add("Kaishu");
        locationList.add("Roorkee");
        locationList.add("Ahmedabad");
        locationList.add("Chennai");
        pictureList.add(bitmap1);
        pictureList.add(bitmap2);
        pictureList.add(bitmap1);*/

        locCoordList = new ArrayList<LatLng>();


        for(int i = 0;i<locationList.size();i++){
            try {
                location = locationList.get(i);
                gc = new Geocoder(getContext());
                address = gc.getFromLocationName(location, 1);
                if(address.get(0).hasLatitude() && address.get(0).hasLongitude()){
                    locCoordList.add(new LatLng(address.get(0).getLatitude(), address.get(0).getLongitude()));
                }
            }catch (Exception e){e.printStackTrace();}

        }

        googleMap.setInfoWindowAdapter(new MapItemAdapter(getContext()));

        PolylineOptions rectOptions = new PolylineOptions();
        rectOptions.color(Color.WHITE).geodesic(true);
        Log.e("TAG",""+locCoordList.size());
        for(int i =0;i<locationList.size();i++){

            imageBit = /*pictureList.get(i)*/BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
            Marker marker = googleMap.addMarker(new MarkerOptions().position(locCoordList.get(i)).title(nameList.get(i)+"\n"+locationList.get(i)));
            marker.setTag(imageBit);
            mMarkerArray.add(marker);
            rectOptions.add(locCoordList.get(i));


        }

        Polyline polyline = googleMap.addPolyline(rectOptions);
        polyline.setJointType(JointType.ROUND);

        List<PatternItem> pattern = Arrays.<PatternItem>asList(
                new Gap(20), new Dash(30), new Gap(20));
        polyline.setPattern(pattern);
        polyline.setStartCap(new RoundCap());
        polyline.setEndCap(new RoundCap());

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(),R.raw.style_json));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : mMarkerArray){

            builder.include(marker.getPosition());

        }
        final LatLngBounds bounds = builder.build();
        final int padding = 150;

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,padding);
                googleMap.animateCamera(cameraUpdate);
            }
        });

    }


    public class MapItemAdapter implements GoogleMap.InfoWindowAdapter {

        TextView locView;
        ImageView imageView;
        View markerView;


        public  MapItemAdapter(Context context) {
            markerView = View.inflate(context,R.layout.marker_layout,null);
        }


        @Override
        public View getInfoWindow(Marker marker) {
            locView = (TextView) markerView.findViewById(R.id.location);
            locView.setText(marker.getTitle());
            imageView = (ImageView) markerView.findViewById(R.id.image);
            imageView.setImageBitmap((Bitmap)marker.getTag());
            return markerView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }


}
