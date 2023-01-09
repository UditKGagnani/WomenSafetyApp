package com.example.womensafetyapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    //    private static final Integer PERMISSIONS_FINE_LOCATION = 99;
    TextView name_tv, age_tv, number_tv;
    TextView latitude, longitude, tv_address;
    Switch updates, gps;
    Timer t;
    TimerTask tt;

    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

//    @Override
//    @SuppressWarnings("deprecation")
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case 99:
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                updateGPS();
//            } else {
//                Toast.makeText(getActivity(), "This app needs permission to work properly", Toast.LENGTH_SHORT).show();
//            }
//            break;
//        }
//    }



//    private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
//            new ActivityResultContracts.RequestPermission(),
//            new ActivityResultCallback<Boolean>() {
//                @Override
//                public void onActivityResult(Boolean result) {
//                    if(result){
//
//                    }
//                    else{
//
//                    }
//                }
//            });

//    private ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
//        if (isGranted) {
//            updateGPS();
//        } else {
//            Toast.makeText(getActivity().getApplicationContext(), "This app requires permissions to be granted in order to work properly", Toast.LENGTH_SHORT).show();
//        }
//    });

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        name_tv = view.findViewById(R.id.name_tv);
        age_tv = view.findViewById(R.id.age_tv);
        number_tv = view.findViewById(R.id.number_tv);

        Intent intent = getActivity().getIntent();
        String fullname = intent.getStringExtra("name");
        String age = intent.getStringExtra("age");
        String number = intent.getStringExtra("number");

        name_tv.setText(fullname);
        age_tv.setText(age);
        number_tv.setText(number);

        latitude = view.findViewById(R.id.latitude);
        longitude = view.findViewById(R.id.longitude);
        tv_address = view.findViewById(R.id.address);

        updates = view.findViewById(R.id.updates_switch);
        gps = view.findViewById(R.id.gps_switch);

        locationRequest = LocationRequest.create();

        locationRequest.setInterval(30000);

        locationRequest.setFastestInterval(5000);

        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gps.isChecked()) {
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                }
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                updateUIValues(locationResult.getLastLocation());
            }
        };

        t = new Timer();

        tt = new TimerTask() {
            @Override
            public void run() {
                updateGPS();
            }
        };

        t.schedule(tt,0,30000);

        updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updates.isChecked()) {

                    t = new Timer();

                    tt = new TimerTask() {
                        @Override
                        public void run() {
                            updateGPS();
                        }
                    };

                    t.schedule(tt,0,5000);

                } else {
                    t.cancel();
                    stopLocationUpdates();
                    gps.setChecked(false);
                }
            }
        });

        mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);

//        updateGPS();

        return view;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "This app requires permission to work properly", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            updateGPS();
        }
    }

    private void stopLocationUpdates() {
        latitude.setText("Location Tracking is OFF");
        longitude.setText("Location Tracking is OFF");
        tv_address.setText("Location Tracking is OFF");
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void updateGPS(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity().getApplicationContext());
        if(ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener((Activity) requireContext(),new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateUIValues(location);
                    Toast.makeText(getActivity(), "Location Values Updated", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            }
        }

    }

    private ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if(result) {
                        updateGPS();
                    } else {
                        Toast.makeText(getActivity(), "Permission is required to run the app properly", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void updateUIValues(Location location){
        latitude.setText(" ");
        longitude.setText(" ");
        latitude.setText(String.valueOf(location.getLatitude()));
        longitude.setText(String.valueOf(location.getLongitude()));
        Geocoder geocoder = new Geocoder(requireActivity().getApplicationContext());
        try {
            List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
//            tv_address = getView().findViewById(R.id.address);
            tv_address.setText(address.get(0).getAddressLine(0));
        }
        catch (Exception e){
            tv_address.setText("Unable to get street address");
        }
    }

}