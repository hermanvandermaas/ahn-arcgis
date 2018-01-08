package nl.waywayway.ahn;

import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

public class MainActivity extends AppCompatActivity
implements OnMapReadyCallback
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		makeToolbar();
		
		MapFragment mapFragment = (MapFragment) getFragmentManager()
            .findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
    }
	
	// Maak toolbar
	private void makeToolbar()
	{
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
	}
	
	public void onMapReady(GoogleMap googleMap)
	{
        LatLng sydney = new LatLng(-33.852, 151.211);
        googleMap.addMarker(new MarkerOptions().position(sydney)
							.title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
