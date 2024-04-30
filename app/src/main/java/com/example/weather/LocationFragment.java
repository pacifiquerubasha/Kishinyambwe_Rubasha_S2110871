package com.example.weather;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.widget.EditText;
import android.widget.Toast;


import android.location.Address;
import android.location.Geocoder;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;



/**
 * Student Name: Pacifique Kishinyambwe RUBASHA
 * Student ID: S2110871
 */
public class LocationFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GoogleMap googleMap;

    SearchView searchView;
    private Boolean isConnected = true;

    public LocationFragment() {
        // Required empty public constructor
    }

    public LocationFragment(Boolean isConnected) {
        this.isConnected = isConnected;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationFragment newInstance(String param1, String param2) {
        LocationFragment fragment = new LocationFragment();
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
        View view = inflater.inflate(R.layout.fragment_location, container, false);

        if(!isConnected){
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
        }

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                // Show dialog to resolve the error
                googleApiAvailability.getErrorDialog(getActivity(), status, 2404).show();
                Log.d("GOOGLE PLAY SERVICES", "RESOLVABLE");
            } else {
                // Google Play Services is not supported
                Log.d("GOOGLE PLAY SERVICES", "NOT SUPPORTED");
            }
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.id_map);
            if (mapFragment != null) {
                Log.d("MAP READY", "YES");
                mapFragment.getMapAsync(this);
            } else {
                Log.d("MAP READY", "NO");
            }
        }

        // Example of a search box
        searchView = view.findViewById(R.id.search_box);


        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        addMarkers(googleMap);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("PERMISSION", "GRANTED");

        } else {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                LatLng destination = marker.getPosition();
                Log.d("MARKER", "CLICKED");
                return false;
            }

        });

        initSearch();

    }

    public void addMarkers(GoogleMap googleMap) {
        Bitmap customMarkerBitmap = getMarkerBitmapFromView(R.layout.custom_marker, 64, 64);

        LatLng glasgow = new LatLng(55.8642, -4.2518);
        googleMap.addMarker(
                new MarkerOptions().position(glasgow).title("Glasgow")
                        .icon(BitmapDescriptorFactory.fromBitmap(customMarkerBitmap)));

        LatLng london = new LatLng(51.5074, -0.1278);
        googleMap.addMarker(new MarkerOptions().position(london).title("London")
                .icon(BitmapDescriptorFactory.fromBitmap(customMarkerBitmap)));

        LatLng newYork = new LatLng(40.7128, -74.0060);
        googleMap.addMarker(new MarkerOptions().position(newYork).title("New York")
                .icon(BitmapDescriptorFactory.fromBitmap(customMarkerBitmap)));

        LatLng oman = new LatLng(23.5888, 58.3842);
        googleMap.addMarker(new MarkerOptions().position(oman).title("Oman")
                .icon(BitmapDescriptorFactory.fromBitmap(customMarkerBitmap)));

        LatLng mauritius = new LatLng(-20.2855, 57.4783);
        googleMap.addMarker(new MarkerOptions().position(mauritius).title("Mauritius")
                .icon(BitmapDescriptorFactory.fromBitmap(customMarkerBitmap)));

        LatLng bangladesh = new LatLng(23.684997, 90.356331);
        googleMap.addMarker(new MarkerOptions().position(bangladesh).title("Bangladesh")
                .icon(BitmapDescriptorFactory.fromBitmap(customMarkerBitmap)));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(glasgow, 10));
    }

    //Function to search
    public void initSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on below line we are getting the
                // location name from search view.
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || location.equals("")) {
                    Geocoder geocoder = new Geocoder(requireContext());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);

                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    googleMap.addMarker(new MarkerOptions().position(latLng).title(location));

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private Bitmap getMarkerBitmapFromView(int layoutResId, int width, int height) {
        View view = getLayoutInflater().inflate(layoutResId, null);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.draw(canvas);

        // Scale the bitmap to the desired size
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

        return scaledBitmap;
    }
}