package nl.waywayway.ahn;

import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import com.esri.arcgisruntime.*;
import com.esri.arcgisruntime.geometry.*;
import com.esri.arcgisruntime.layers.*;
import com.esri.arcgisruntime.mapping.*;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.data.*;

public class MainActivity extends AppCompatActivity
{
	private Context context;
	// Coordinatensysteem EPSG:28992 (Amersfoort / RD new)
	private SpatialReference spatialReference = SpatialReference.create(28992);
	// bbox (bounding box of envelope) is minx, miny, maxx, maxy
	private double[] bbox = new double[]{646.36, 308975.28, 276050.82, 636456.31};
	private MapView mapView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		context = this;

		makeToolbar();
		setTransparentStatusBar();
		setLicense();
		makeMapView();
    }

	private void setLicense()
	{
		ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud3657265403,none,8SH93PJPXJK94P7EJ029");
	}

	private void makeMapView()
	{
		mapView = (MapView) findViewById(R.id.map);
		ArcGISMap map = new ArcGISMap(spatialReference);
		map.setInitialViewpoint(new Viewpoint(new Envelope(bbox[0], bbox[1], bbox[2], bbox[3], spatialReference)));
		
		// Basemap
		WmtsLayer wmtsLayerBase = new WmtsLayer(getResources().getString(R.string.basemap_url), getResources().getString(R.string.basemap_id));
		Basemap baseMap = new Basemap(wmtsLayerBase);
		map.setBasemap(baseMap);
		
		// Ahn2
		WmtsLayer ahn2 = new WmtsLayer(getResources().getString(R.string.operational_layer_AHN2_kleur_url), getResources().getString(R.string.operational_layer_AHN2_kleur_id));
        map.getOperationalLayers().add(ahn2);
		
		mapView.setMap(map);
	}

	// Maak toolbar
	private void makeToolbar()
	{
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
	}

	private void setTransparentStatusBar()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
	}
	
	private int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
	
	@Override 
	protected void onPause(){
		mapView.pause();
		super.onPause();
	}

	@Override 
	protected void onResume(){
		super.onResume();
		mapView.resume();
	}
}
