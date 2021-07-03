package com.example.madcamp1;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.Random;

public class MapFrag extends Fragment {
    private Random random = new Random(1984);
    private GoogleMap googleMap;
    private FragmentActivity myContext;
    private MainActivity main = (MainActivity) getActivity();
    private double lat = 36.374171;
    private double lng = 127.365293;
    private ClusterManager<MyItem> clusterManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        //Initialize view
        View rootView = inflater.inflate(R.layout.map_frag,container,false);

        //Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);

        //Async map
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // when map is loaded
                LatLng latLng = new LatLng(lat, lng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title("카이스트");
                markerOptions.snippet("N1");
                markerOptions.position(latLng);

                googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

                clusterManager = new ClusterManager<MyItem>(getContext(), googleMap);
                googleMap.setOnCameraIdleListener(clusterManager);
                googleMap.setOnMarkerClickListener(clusterManager);

                clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyItem>() {
                    @Override
                    public boolean onClusterItemClick(MyItem item) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.title(item.getTitle());
                        markerOptions.position(item.getPosition());
                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(item.getPosition()));
                        Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_LONG).show();
                        return true;
                    }
                });

                addItems();
            }
        });

        ActionBar ab = ((MainActivity)getActivity()).getSupportActionBar();
        ab.setTitle("Map");

//        return view
        return rootView;
    }

    private void addItems() {
        String[] classMates = new String[]{"정이든", "강수아", "이혜민", "송예은", "양현호", "김경하", "금나연", "김영경", "김장현", "김주형", "김찬영", "김태우", "김현수", "박종서", "송인화", "최정재", "송재현", "양현호", "이호준", "정나연", "종회", "주은", "최종윤", "최지호", "추승우"};

        for(int i = 0 ; i < 24; i++) {
            MyItem offsetItem = new MyItem(position(), classMates[i]);
            clusterManager.addItem(offsetItem);
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    //        36.374171, 127.365293
    private LatLng position() {
        return new LatLng(random(36.374171, 36.375171), random(127.365293, 127.363393));
    }

    private double random(double min, double max) {
        return random.nextDouble() * (max - min) + min;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        googleMap.setMyLocationEnabled(true);
                    }
                } else {
                    Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
