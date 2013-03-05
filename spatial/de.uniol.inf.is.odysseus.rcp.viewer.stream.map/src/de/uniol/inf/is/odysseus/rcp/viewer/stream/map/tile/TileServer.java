package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Point;
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.CoordinateTransformFactory;
import org.osgeo.proj4j.ProjCoordinate;

import com.vividsolutions.jts.geom.Envelope;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer.RasterLayer;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.model.layer.RasterLayerConfiguration;

public final class TileServer {
	
    private final String url;
    private final String format;    
    private final int minZoom;
    private int zoom;
    private final int maxZoom;
    private boolean broken;
    private TileCache cache;
    private int tileWidth;
    private int tileHeight;
    private Envelope coverage;
    private ScreenManager manager;
    private int srid;
	private CoordinateReferenceSystem crs;
	private RasterLayer layer;
    
    public TileServer(RasterLayer layer, ScreenManager manager) {
    	this.layer = layer;
    	RasterLayerConfiguration config = layer.getConfiguration();
        this.url = config.getUrl();
        this.format = config.getFormat();
        this.minZoom = config.getMinZoom();
    	this.zoom = minZoom;
        this.maxZoom = config.getMaxZoom();
        this.cache = new TileCache();
        this.manager = manager;
        this.tileWidth = config.getTileSizeX();
        this.tileHeight = config.getTileSizeY();
        this.setSRID(config.getSrid());
        if (config.isCoverageProjected())
        	this.coverage = new Envelope(config.getCoverage());
        else{
        	Envelope geoEnv = config.getCoverage();
			ProjCoordinate src1 = new ProjCoordinate(geoEnv.getMinX(),  geoEnv.getMinY());
			ProjCoordinate src2 = new ProjCoordinate(geoEnv.getMaxX(), geoEnv.getMaxY());
			ProjCoordinate dst1 = new ProjCoordinate();
			ProjCoordinate dst2 = new ProjCoordinate();
			this.crs.getProjection().project(src1, dst1);
			this.crs.getProjection().project(src2, dst2);
			this.coverage = new Envelope(dst1.x, dst2.x, dst1.y, dst2.y);
        }
    }
    
    public void init(int tileSize, int maxZoom, int minZoom, String format, int tilesize, int srid, Envelope coverage){
    	
    }
    
    public String toString() {
        return url;
    }


    public int getMinZoom() {
        return minZoom;
    }

    public int getMaxZoom() {
        return maxZoom;
    }
    
    public RasterLayer getLayer() {
		return layer;
	}
    
    public String getURL() {
        return url;
    }

	public void setSRID(int srid){
		if (this.srid != srid){
			this.srid = srid;
			CRSFactory csFactory = new CRSFactory();
			this.crs = csFactory.createFromName("EPSG:" + this.srid);
			this.crs.getProjection().initialize();
		}
	}
    
    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean broken) {
        this.broken = broken;
    }

	public TileCache getCache() {
		return cache;
	}


	public int getXTileCount(int z) {
		return (1 << z);
	}

	public int getYTileCount(int z) {
		return (1 << z);
	}

	public int getXMax(int z) {
		return tileWidth * getXTileCount(z);
	}

	public int getYMax(int z) { 
		return tileHeight * getYTileCount(z);
	}

	public int getCurrentZoom(){
		return zoom;
	}
	public List<AsyncImage> getTiles(Envelope coverage, int srid, Point screenSize) {
		Envelope world = coverage; 
		if (this.srid != srid){
			CRSFactory csFactory = new CRSFactory();
			CoordinateReferenceSystem targetCRS = csFactory.createFromName("EPSG:" + srid);
			CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
			CoordinateTransform ct = ctFactory.createTransform(this.crs, targetCRS);
			ProjCoordinate src1 = new ProjCoordinate( world.getMaxX(),  world.getMaxY());
			ProjCoordinate src2 = new ProjCoordinate( world.getMinX(),  world.getMinY());
			ProjCoordinate dst1 = new ProjCoordinate();
			ProjCoordinate dst2 = new ProjCoordinate();
			ct.transform(src1, dst1);
			ct.transform(src2, dst2);
			world = new Envelope(dst1.x, dst2.x, dst1.y, dst2.y);
		}			
		ArrayList<AsyncImage> images = new ArrayList<AsyncImage>();
		zoom = minZoom;
		for (int z1 = 0; z1 <= maxZoom; z1++){
			double tileSizeWorld = Math.abs(tile2X(0, z1) - tile2X(1, z1));
			double maxTilePerScreenWidth = screenSize.x / tileWidth;
			double erg =  world.getWidth() / tileSizeWorld;
			if (erg > maxTilePerScreenWidth ){
				zoom = z1;
				z1 = maxZoom+1;
			}
		}
		
		//z = 2;
		for (int i = 0; i < getXTileCount(zoom); i++){
			for (int j = 0; j < getYTileCount(zoom); j++){
				Envelope env = new Envelope(tile2X(i, zoom), tile2X(i+1, zoom), tile2Y(j, zoom), tile2Y(j+1, zoom));
				if (world.intersects(env)){
					AsyncImage image = cache.get(this, i, j, zoom);
					if (image == null){
						image = new AsyncImage(manager, this, i, j, zoom, env);
						cache.put(this, i, j, zoom, image);
					}
					images.add(image);
				}
			}
		}
		return images;
	}

//    public static double tile2lon(int x, int z) {
//        return x / Math.pow(2.0, z) * 360.0 - 180;
//    }
    
    public double tile2X(int i, int z) {
    	return this.coverage.getMinX() + i / Math.pow(2.0, z) * this.coverage.getWidth();
    }
    
    public double tile2Y(int j, int z) {
    	return this.coverage.getMaxY() - j / Math.pow(2.0, z) * this.coverage.getHeight();
    }
    
    public int getTileWitdh(){
    	return this.tileWidth;
    }

    public int getTileHeight(){
    	return this.tileWidth;
    }
//    public static double tile2lat(int y, int z) {
//        return Math.toDegrees(Math.atan(Math.sinh(Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z))));
//    }
//	public Point getTile(Point position) {
//		return new Point((int) Math.floor(((double) position.x)
//				/ ProjectionUtil.TILE_SIZE),
//				(int) Math.floor(((double) position.y)
//						/ ProjectionUtil.TILE_SIZE));
//	}

    public String getTileString(int xtile, int ytile, int zoom) {
        String number = ("" + zoom + "/" + xtile + "/" + ytile);
        String url = getURL() + number + "." + this.format;
        return url;
    }
    
	public int getSRID() {
		return this.srid;
	}

	
}