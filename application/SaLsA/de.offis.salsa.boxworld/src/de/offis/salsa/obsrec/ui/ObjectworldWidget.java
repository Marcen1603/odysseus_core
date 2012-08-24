package de.offis.salsa.obsrec.ui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.TrackedObject;
import de.offis.salsa.obsrec.ObjektWeltListener;
import de.offis.salsa.obsrec.Objektwelt;
import de.offis.salsa.obsrec.SensorMeasurement;


public class ObjectworldWidget extends JPanel implements ObjektWeltListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3515945442158600662L;

	private Objektwelt objWelt;	
	private SensorMeasurement measure = null;
	
	private MouseDragZoomListener mouseListener = new MouseDragZoomListener(this);
	
	private List<CanvasElement> samplePoints = new ArrayList<CanvasElement>();
	private List<CanvasElement> area = new ArrayList<CanvasElement>();
	private List<CanvasElement> boxes = new ArrayList<CanvasElement>();
	private List<CanvasElement> marker = new ArrayList<CanvasElement>();
	
	public ObjectworldWidget(Objektwelt objWelt) {
		this.objWelt = objWelt;
		this.objWelt.registerListener(this);
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
			o.paint(g);
		}
		
		for(CanvasElement o : area){
			o.paint(g);
		}
		
		for(CanvasElement o : samplePoints){
			o.paint(g);
		}
		
		for(CanvasElement o : boxes){
			o.paint(g);
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
	

	@Override
	public void onChange() {		
		sensorMeasurementTemp = objWelt.getMeasurement();
		boxesTemp = objWelt.getTrackedObjects();
		repaint();
	}
	
	private void setNewBoxes(List<TrackedObject> boxes){
		this.boxes.clear();
		
		for(TrackedObject b : boxes){
			this.boxes.add(new TrackedObjectElement(b));
		}
	}
	
	private void setNewMeasurement(SensorMeasurement sensorMeasurement){
		this.measure = new SensorMeasurement(sensorMeasurement);
		
		this.samplePoints.clear();
		this.area.clear();
		
		LaserAreaFreePolygon p = new LaserAreaFreePolygon();
	
		
		for(Sample s : this.measure.getData()){
			this.samplePoints.add(new SampleIndicator(s));
			p.addPoint((int)s.getX(), (int)s.getY());
		}
		
		this.area.add(p);
		
	}
}
