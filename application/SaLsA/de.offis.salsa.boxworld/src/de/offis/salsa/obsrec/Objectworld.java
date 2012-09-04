package de.offis.salsa.obsrec;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;

import de.offis.salsa.lms.model.Measurement;
import de.offis.salsa.obsrec.ls.DebugLaserScanner;
import de.offis.salsa.obsrec.ls.ReadingLaserScanner;
import de.offis.salsa.obsrec.ls.SavingLaserScanner;
import de.offis.salsa.obsrec.ls.SickLaserScanner;
import de.offis.salsa.obsrec.models.TrackedObject;
import de.offis.salsa.obsrec.objrules.IObjectRule;
import de.offis.salsa.obsrec.scansegm.IScanSegmentation;

public class Objectworld {
	private Logger log = Logger.getLogger("Objektwelt");
	
	private List<SickLaserScanner> scanner;
	
	private Measurement measure;
	private List<TrackedObject> boxes = new ArrayList<TrackedObject>();
	
	private String activeSegmenter = "SefSegmentation";
	private Map<String, IScanSegmentation> scanSegmenter = new HashMap<String, IScanSegmentation>();
	
	private ArrayList<String> activeObjRules = new ArrayList<String>();
	private Map<String, IObjectRule> objRules = new HashMap<String, IObjectRule>();
	
	public Objectworld(){
		log.info("Initiating Objektwelt ...");
		
		this.scanner = new ArrayList<SickLaserScanner>();
		
		doAnnotationProcessing();
	}
	
	public void setActiveObjectRules(ArrayList<String> arrayList){
		activeObjRules.clear();
		
		for(String s : arrayList){
			activeObjRules.add(s);
		}
	}
	
	public ArrayList<String> getRegisteredObjectRules(){
		return new ArrayList<String>(objRules.keySet());
	}
	
	private void doAnnotationProcessing(){
		log.info("Starting Annotation Processing ...");
		
		// we will look in classpath for classes with annotation ...
		Discoverer discoverer = new ClasspathDiscoverer();
		
		// register all scansegmentation and objectrule classes  ...
        discoverer.addAnnotationListener(new AnnotationListener(this));
        
        // Fire it
        discoverer.discover();
        log.info("Finished Annotation Processing ...");
	}
	
	
	public void registerScanSegmentation(String name, IScanSegmentation segmenter){
		this.scanSegmenter.put(name, segmenter);
		log.info("Registered ScanSegmentation: " + name);
	}
	
	public void registerObjectRule(String name, IObjectRule rule){
		this.objRules.put(name, rule);
		log.info("Registered ObjectRule: " + rule.toString());
	}
	

	public SickLaserScanner addSickLaserScanner(String host, int port){
		SickLaserScanner ls = new SickLaserScanner(this, host, port);
		this.scanner.add(ls);
		return ls;
	}
	
	public ReadingLaserScanner addReadingLaserScanner(String filename){
		ReadingLaserScanner ls = new ReadingLaserScanner(this, filename);
		this.scanner.add(ls);
		return ls;
	}
	
	public SavingLaserScanner addSavingLaserScanner(String host, int port, String filename){
		SavingLaserScanner ls = new SavingLaserScanner(this, host, port, filename);
		this.scanner.add(ls);
		return ls;
	}

	public DebugLaserScanner addDebugLaserScanner() {
		DebugLaserScanner ls = new DebugLaserScanner(this);
		this.scanner.add(ls);
		return ls;
	}
	
	public void receiveMeasure(Measurement measurement){
		// TODO insert into objektwelt
		// bei mehreren scanners probleme mit der synchronisation! (vielleicht)
		
		this.measure = measurement;

		ArrayList<TrackedObject> tempObj = new ArrayList<TrackedObject>();
		MultiLineString segments = getActiveSegmenter().segmentScan(measurement);
		for(int i = 0 ; i < segments.getNumGeometries() ; i++){
			tempObj.add(createTrackedObject((LineString) segments.getGeometryN(i)));
		}
		
		this.boxes = tempObj;
		
		fireOnChangeEvent();
	}
	
	private IScanSegmentation getActiveSegmenter(){		
		return scanSegmenter.get(activeSegmenter);		
	}
	
	public void setActiveSegmenter(String segmenter){
		if(scanSegmenter.containsKey(segmenter)){
			this.activeSegmenter = segmenter;
		} else {
			throw new RuntimeException("ScanSegmenter not Found! " + segmenter);
		}
	}
	
	
	
	public String[] getRegisteredSegmenter(){
		return scanSegmenter.keySet().toArray(new String[0]);
	}
	
	private TrackedObject createTrackedObject(LineString samplesObject){
		Coordinate[] coords = samplesObject.getCoordinates();
		
		java.awt.Polygon p = new java.awt.Polygon();		
		for(Coordinate s : coords){
			p.addPoint((int)s.x, (int)s.y);
		}		
		Rectangle b = p.getBounds();
		
		
		Map<String, Double> affs = new HashMap<String, Double>();
		Map<String, Polygon> polygons = new HashMap<String, Polygon>();
		for(String objRuleName : new ArrayList<String>(activeObjRules)){
			IObjectRule objRule = objRules.get(objRuleName);			
			
			if(objRule == null){
				String s = "";
			}
			
			double affinity = objRule.getTypeAffinity(samplesObject);
			
			if(affinity > 0){
				// put affinity info
				affs.put(objRuleName, affinity);

				// put polygon
				polygons.put(objRuleName, objRule.getPredictedPolygon(samplesObject));
			}							
		}
		
		return new TrackedObject(b.x, b.y, b.width, b.height, affs, polygons);
	}
	
	public List<TrackedObject> getTrackedObjects() {
		return new ArrayList<TrackedObject>(boxes);
	}
	
	public Measurement getMeasurement(){
		return measure;
	}
	
	private List<ObjWorldListener> listener = new ArrayList<ObjWorldListener>();
		
	private void fireOnChangeEvent(){
		for(ObjWorldListener listener : this.listener){
			listener.onChange();
		}
	}
	
	public void addObjectWorldListener(ObjWorldListener listener){
		this.listener.add(listener);
	}
	
	public void removeListener(ObjWorldListener listener){
		this.listener.remove(listener);
	}
}
