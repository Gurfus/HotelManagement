package ferranechaves.ioc.HolidayManagement;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Ubicacio extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacio);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // ens envia al camping amb aquestes cordenades
        LatLng camping = new LatLng(42.28927085274322, 2.364463014588096);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        float zoom = 15;
        mMap.addMarker(new MarkerOptions().position(camping).title("El teu camping"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(camping,zoom));
    }
}