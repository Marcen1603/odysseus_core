package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.layer;


import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.ProjCoordinate;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.Buffer;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ScreenTransformation;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.LayerConfiguration;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.layer.NullConfiguration;

/**
 * Layer which displays a tile grid
 */
public class BasicLayer extends AbstractLayer<NullConfiguration>{

    private static final long serialVersionUID = -5707296757822008243L;
	@SuppressWarnings("unused")
	private static final int TILE_SIZE = 256;
	private ScreenManager screenmanager;
	private ScreenTransformation transformation;
	private Display display;
	private Canvas canvas;
	
	private Color waitBackground, waitForeground;


	public BasicLayer() {
		super(new NullConfiguration());
		this.name = "Basic";
		this.active = true;
    }
	
	public BasicLayer(NullConfiguration configuration) {
		super(configuration);
		this.active = true;
    }
	
	@Override
    public void init(ScreenManager screenManager, SDFSchema schema, SDFAttribute attribute) {
		this.screenmanager = screenManager;
		this.transformation = screenManager.getTransformation();
		
		this.display = screenManager.getDisplay();
		this.canvas = screenManager.getCanvas();
		
		waitBackground = new Color(display, 160, 160, 160);
		waitForeground = new Color(display, 200, 200, 200);	
		
		canvas.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				BasicLayer.this.widgetDisposed(e);
			}
		});
		this.active = true;
    }
	
	@Override
	public void draw(GC gc) {
		int dx = 10;
		int dy = 10;
		for (int x = -180; x < 180; x+=dx) {
			paintTile(gc, x, -85, dx, 5);
			for (int y = -80; y < 80; y+=dy) {
				paintTile(gc, x, y, dx, dy);
			}
			paintTile(gc, x, 80, dx, 5);
		}
		gc.setForeground(new Color(gc.getDevice(),255,0,0));
		if(screenmanager.getInfoText() != null){
		  gc.drawText(screenmanager.getInfoText(), 0 , 0,true); 
		}
	}
	
	private void paintTile(GC gc, int x, int y, int dx, int dy) {
		gc.setForeground(waitBackground);
		int[] pointArray = new int[8];
		int[][] xy = new int[4][];
		xy[0] = transformation.transformCoord(new Coordinate(x,y), 4326);
		xy[1] = transformation.transformCoord(new Coordinate(x+dx,y), 4326);
		xy[2] = transformation.transformCoord(new Coordinate(x+dx,y+dy), 4326);
		xy[3] = transformation.transformCoord(new Coordinate(x,y+dy), 4326);
		for (int i = 0; i < pointArray.length; i++) {
			pointArray[i]= xy[i/2][i%2];			
		}
		gc.drawPolygon(pointArray);
		gc.setForeground(waitForeground);
	}
	
	protected void widgetDisposed(DisposeEvent e) {
		waitBackground.dispose();
		waitForeground.dispose();
	}

	@Override
	public void setName(String name) {
		//This should always be called Basic
	}
	
	@Override
    public String[] getSupportedDatatypes() {
	    return null;
    }

	@Override
	public void setBuffer(Buffer puffer) {
	}
	
	@Override
    public int getTupleCount() {
	    return 0;
    }

	@Override
	public void setConfiguration(LayerConfiguration configuration) {
	}

	@Override
	public Envelope getEnvelope() {
		CoordinateTransform ct = transformation.getCoordinateTransform(this.srid, screenmanager.getSRID());
		ProjCoordinate coord0 = new ProjCoordinate();
		ProjCoordinate coord1 = new ProjCoordinate();
		ct.transform(new ProjCoordinate(-180,-85), coord0);
		ct.transform(new ProjCoordinate(180, 85), coord1);
		return new Envelope(coord0.x, coord1.x, coord0.y, coord1.y);
	}

}
