package nl.waywayway.ahn;

import android.content.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.os.*;
import android.support.v4.content.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import com.esri.arcgisruntime.*;
import com.esri.arcgisruntime.geometry.*;
import com.esri.arcgisruntime.layers.*;
import com.esri.arcgisruntime.mapping.*;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.symbology.*;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.util.*;

public class MainActivity extends AppCompatActivity
{
	private Context context;
	// Coordinatensysteem EPSG:28992 (Amersfoort / RD new)
	private SpatialReference spatialReference = SpatialReference.create(28992);
	// bbox (bounding box / envelope) is minx, miny, maxx, maxy
	private double[] bbox = new double[]{646.36, 308975.28, 276050.82, 636456.31};
	private MapView mapView;
	GraphicsOverlay graphicsOverlay;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		context = this;

		makeToolbar();
		//setTransparentStatusBar();
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
		
		// Listener for taps on the map view
		mapView.setOnTouchListener(new DefaultMapViewOnTouchListener(MainActivity.this, mapView)
		{
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				android.graphics.Point screenPoint = new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY()));
				//identifyResult(screenPoint);
				//Snackbar.make(findViewById(R.id.coordinator), "tik", Snackbar.LENGTH_SHORT).show();
				placeMarker(screenPoint);
				
				return true;
			}
		});
		
		// Laag voor markers
		graphicsOverlay = new GraphicsOverlay();
        mapView.getGraphicsOverlays().add(graphicsOverlay);
	}
	
	// Plaats marker in juiste laag
	private void placeMarker(final android.graphics.Point screenPoint)
	{
		// Verwijder bestaande markers
		ListenableList graphicsList = graphicsOverlay.getGraphics();
		graphicsList.clear();
		
		BitmapDrawable markerBitmap = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.ic_marker);
		final PictureMarkerSymbol markerSymbol = new PictureMarkerSymbol(markerBitmap);
		//Optionally set the size, if not set the image will be auto sized based on its size in pixels,
		//its appearance would then differ across devices with different resolutions.
		markerSymbol.setHeight(26);
		markerSymbol.setWidth(26);
		//Optionally set the offset, to align the base of the symbol aligns with the point geometry
		markerSymbol.setOffsetY(13); //The image used for the symbol has a transparent buffer around it, so the offset is not simply height/2
		markerSymbol.loadAsync();

		markerSymbol.addDoneLoadingListener(new Runnable()
		{
			@Override
			public void run() {
				Point graphicPoint = mapView.screenToLocation(screenPoint);
				Graphic markerGraphic = new Graphic(graphicPoint, markerSymbol);
				graphicsOverlay.getGraphics().add(markerGraphic);
			}
		});
	}

	// Maak toolbar
	private void makeToolbar()
	{
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionBar = getSupportActionBar();
		//toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
	}

	private void setTransparentStatusBar()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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
