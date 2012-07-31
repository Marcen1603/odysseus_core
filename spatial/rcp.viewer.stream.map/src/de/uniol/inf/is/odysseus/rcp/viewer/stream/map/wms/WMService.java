package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.wms;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.util.projection.Mercator;

public class WMService {
    private String baseUrl;
    private String layer;
    /** Creates a new instance of WMSService */
    public WMService() {
        // by default use a known osm server
        setLayer("ors-osm");
        //setBaseUrl("http://wms.jpl.nasa.gov/wms.cgi?");
        setBaseUrl("http://129.206.228.72/cached/osm?");
    }
    public WMService(String baseUrl, String layer) {
        this.baseUrl = baseUrl;
        this.layer = layer;
    }
    
    public String toWMSURL(int x, int y, int zoom, int tileSize) {
        String format = "image/jpeg";
        String styles = "";
        String srs = "EPSG:4326";
        int ts = tileSize;
        int circumference = widthOfWorldInPixels(zoom, tileSize);
        double radius = circumference / (2* Math.PI);
        double ulx = Mercator.xToLong(x*ts, radius);
        double uly = Mercator.yToLat(y*ts, radius);
        double lrx = Mercator.xToLong((x+1)*ts, radius);
        double lry = Mercator.yToLat((y+1)*ts, radius);
        String bbox = ulx + "," + uly+","+lrx+","+lry;
        String url = getBaseUrl() + 
                "version=1.1.1&request="+
                "GetMap&Layers="+layer+
                "&format="+format+
                "&BBOX="+bbox+
                "&width="+ts+"&height="+ts+
                "&SRS="+srs+
                "&Styles="+styles+
                //"&transparent=TRUE"+
                "";
        return url;
    }
    
    
    private int widthOfWorldInPixels(int zoom, int TILE_SIZE) {
//        int TILE_SIZE = 256;
        int tiles = (int)Math.pow(2 , zoom);
        int circumference = TILE_SIZE * tiles;
        return circumference;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
   
}

