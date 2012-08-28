package de.offis.salsa.obsrec.ui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.TrackedObject;
import de.offis.salsa.obsrec.ObjWorldListener;
import de.offis.salsa.obsrec.Objectworld;
import de.offis.salsa.obsrec.SensorMeasurement;
import de.offis.salsa.obsrec.ui.paint.CanvasElement;
import de.offis.salsa.obsrec.ui.paint.CoordMarker;
import de.offis.salsa.obsrec.ui.paint.DrawingContext;
import de.offis.salsa.obsrec.ui.paint.LaserAreaFreePolygon;
import de.offis.salsa.obsrec.ui.paint.MouseDragZoomListener;
import de.offis.salsa.obsrec.ui.paint.SampleIndicator;
import de.offis.salsa.obsrec.ui.paint.TrackedObjectElement;


@SuppressWarnings("serial")
public class BoxWorldPanel extends JPanel implements ObjWorldListener {


	private MouseDragZoomListener mouseListener = new MouseDragZoomListener(this);
	private DrawingContext drawCtx = new DrawingContext();
	
	private Objectworld objWelt;	
	private SensorMeasurement measure = null;
	
	
	private List<CanvasElement> samplePoints = new ArrayList<CanvasElement>();
	private List<CanvasElement> area = new ArrayList<CanvasElement>();
	private List<CanvasElement> boxes = new ArrayList<CanvasElement>();
	private List<CanvasElement> marker = new ArrayList<CanvasElement>();
	
	public BoxWorldPanel(Objectworld objWelt) {
		this.objWelt = objWelt;
		this.objWelt.addObjectWorldListener(this);
		this.setDoubleBuffered(true);
		this.setVisible(true);
		
		addMouseListener(mouseListener);
		addMouseWheelListener(mouseListener);
		addMouseMotionListener(mouseListener);		
		
		marker.add(new CoordMarker(0, 0));
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		for(CanvasElement o : marker){
			o.paint(g, drawCtx);
		}
		
		for(CanvasElement o : area){
			o.paint(g, drawCtx);
		}
		
		for(CanvasElement o : samplePoints){
			o.paint(g, drawCtx);
		}
		
		for(CanvasElement o : boxes){
			o.paint(g, drawCtx);
		}
		
		if(sensorMeasurementTemp != null){
			setNewMeasurement(sensorMeasurementTemp);
		}
		
		if(boxesTemp != null){
			setNewBoxes(boxesTemp);
		}
	}
	
	SensorMeasurement sensorMeasurementTemp;
	List<TrackedObject> boxesTemp;
	

	private TrackedObjectElement createTrackedObjectGfx(TrackedObject trackedObject){
		return new TrackedObjectElement(trackedObject);
	}
	
	private SampleIndicator createSampleIndicatorGfx(Sample sample){
		return new SampleIndicator(sample);
	}
	
	@Override
	public void onChange() {		
		sensorMeasurementTemp = objWelt.getMeasurement();
		boxesTemp = objWelt.getTrackedObjects();
	}
	
	private void setNewBoxes(List<TrackedObject> boxes){
		this.boxes.clear();
		
		for(TrackedObject b : boxes){
			this.boxes.add(createTrackedObjectGfx(b));
		}
	}
	
	private void setNewMeasurement(SensorMeasurement sensorMeasurement){
		this.measure = new SensorMeasurement(sensorMeasurement);
		
		this.samplePoints.clear();
		this.area.clear();
		
		LaserAreaFreePolygon p = new LaserAreaFreePolygon();
	
		
		for(Sample s : this.measure.getData()){
			this.samplePoints.add(createSampleIndicatorGfx(s));
			p.addPoint((int)s.getX(), (int)s.getY());
		}
		
		this.area.add(p);
		
	}
	
	public void setOffset(double x, double y){
		drawCtx.dragOffsetX = x;
		drawCtx.dragOffsetY = y;
	}
	
	public Double[] getOffset(){
		return new Double[]{drawCtx.dragOffsetX, drawCtx.dragOffsetY};
	}
	
	
	public void zoomIn(double left, double top){
		if (drawCtx.zoomRatio + drawCtx.zoomInterval > drawCtx.zoomMax) {
			return;
		}

		drawCtx.zoomRatio += drawCtx.zoomInterval;

		updateZoom(left, top);
	}
	
	public void zoomOut(double left, double top){
		if (drawCtx.zoomRatio - drawCtx.zoomInterval  < drawCtx.zoomMin) {
            return;
        }

		drawCtx.zoomRatio -= drawCtx.zoomInterval;

        updateZoom(left, top);
	}
	
	private void updateZoom(double left, double top) {
//        double newWidth = getOffsetWidth() * zoomRatio;
//        double newHeight = getOffsetHeight() * zoomRatio;
//
//        double deltaWidth = area.getViewBox()[2] - newWidth;
//        double deltaHeight = area.getViewBox()[3] - newHeight;
//
//        double leftPct = left / getOffsetWidth();
//        double topPct = top / getOffsetHeight();
//
//        double newX = area.getViewBox()[0] + (deltaWidth * leftPct);
//        double newY = area.getViewBox()[1] + (deltaHeight * topPct);
//
//        area.setViewBox(newX, newY, newWidth, newHeight);
    }
}
