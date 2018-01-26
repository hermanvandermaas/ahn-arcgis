package nl.waywayway.ahn;

import android.content.*;
import android.os.*;
import android.support.v7.app.*;
import android.support.v7.widget.*;
import android.view.*;
import com.esri.arcgisruntime.mapping.*;
import com.esri.arcgisruntime.mapping.view.*;
import com.esri.arcgisruntime.*;

public class MainActivity extends AppCompatActivity
{
	private Context context;
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
		ArcGISMap map = new ArcGISMap(Basemap.Type.TOPOGRAPHIC, 34.056295, -117.195800, 16);
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
