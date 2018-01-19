package nl.waywayway.ahn;

import com.google.android.gms.maps.model.*;
import java.net.*;
import java.util.*;

// Factory class voor WMS tiles in Web Mercator projectie

public class WMSTileProvider extends UrlTileProvider
{
	// Web Mercator n/w corner of the map.
    private static final double[] TILE_ORIGIN = {-20037508.34789244, 20037508.34789244};
	
    //array indexes for that data
    private static final int ORIG_X = 0;
    private static final int ORIG_Y = 1;
	
    // Size of square world map in meters, using WebMerc projection.
    private static final double MAP_SIZE = 20037508.34789244 * 2;
	
    // array indexes for array to hold bounding boxes.
    protected static final int MINX = 0;
    protected static final int MAXX = 1;
    protected static final int MINY = 2;
    protected static final int MAXY = 3;
	
	private static final String URL_FORMAT =
	"http://yourApp.org/geoserver/wms" +
	"?service=WMS" +
	"&version=1.1.1" +
	"&request=GetMap" +
	"&layers=yourLayer" +
	"&bbox=%f,%f,%f,%f" +
	"&width=256" +
	"&height=256" +
	"&srs=EPSG:900913" +
	"&format=image/png" +
	"&transparent=true";

    // Construct with tile size in pixels, normally 256, see parent class
    public WMSTileProvider(int x, int y)
	{
    	super(x, y);
    }

	// return a wms tile layer
	private static TileProvider getTileProvider() {
		return new WMSTileProvider(256,256);
	}
	
	@Override
	public synchronized URL getTileUrl(int x, int y, int zoom) {
		double[] bbox = getBoundingBox(x, y, zoom);
		String s = String.format(Locale.US, URL_FORMAT, bbox[MINX],
								 bbox[MINY], bbox[MAXX], bbox[MAXY]);
		URL url = null;
		
		try {
			url = new URL(s);
		} catch (MalformedURLException e) {
			throw new AssertionError(e);
		}
		
		return url;
	}

    // Return a web Mercator bounding box given tile x/y indexes and a zoom
    // level.
    protected double[] getBoundingBox(int x, int y, int zoom)
	{
    	double tileSize = MAP_SIZE / Math.pow(2, zoom);
    	double minx = TILE_ORIGIN[ORIG_X] + x * tileSize;
    	double maxx = TILE_ORIGIN[ORIG_X] + (x + 1) * tileSize;
    	double miny = TILE_ORIGIN[ORIG_Y] - (y + 1) * tileSize;
    	double maxy = TILE_ORIGIN[ORIG_Y] - y * tileSize;

    	double[] bbox = new double[4];
    	bbox[MINX] = minx;
    	bbox[MINY] = miny;
    	bbox[MAXX] = maxx;
    	bbox[MAXY] = maxy;

    	return bbox;
    }
}
