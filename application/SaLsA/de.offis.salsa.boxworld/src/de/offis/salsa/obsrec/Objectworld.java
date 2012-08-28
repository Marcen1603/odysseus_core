package de.offis.salsa.obsrec;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.ls.DebugLaserScanner;
import de.offis.salsa.obsrec.ls.ReadingLaserScanner;
import de.offis.salsa.obsrec.ls.SavingLaserScanner;
import de.offis.salsa.obsrec.ls.SickLaserScanner;
import de.offis.salsa.obsrec.objrules.IObjectRule;
import de.offis.salsa.obsrec.scansegm.IScanSegmentation;

public class Objectworld {
	private Logger log = Logger.getLogger("Objektwelt");
	
	private List<SickLaserScanner> scanner;
	
	private SensorMeasurement measure;
	private List<TrackedObject> boxes = new ArrayList<TrackedObject>();
	
//	private IScanSegmentation segmenter = new SefSegmentation();
	private String activeSegmenter = "SefSegmentation";
	private HashMap<String, IScanSegmentation> scanSegmenter = new HashMap<String, IScanSegmentation>();
	
//	private HashMap<Type, IObjectRule> objRulesActivated = new HashMap<Type, IObjectRule>();
	private HashMap<String, IObjectRule> objRules = new HashMap<String, IObjectRule>();
	
	public Objectworld(){
		log.info("Initiating Objektwelt ...");
		
		this.scanner = new ArrayList<SickLaserScanner>();
		
		doAnnotationProcessing();
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
	
	public void receiveMeasure(SensorMeasurement measurement){
		// TODO insert into objektwelt
		// bei mehreren scanners probleme mit der synchronisation! (vielleicht)
		
		this.measure = new SensorMeasurement(measurement);

		ArrayList<TrackedObject> tempObj = new ArrayList<TrackedObject>();
		for(List<Sample> segmentSamples : getActiveSegmenter().segmentScan(measurement)){
			tempObj.add(createTrackedObject(segmentSamples));
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
	
	private TrackedObject createTrackedObject(List<Sample> samplesObject){
//		GeometryFactory fact = new GeometryFactory();
//		LinearRing linear = new GeometryFactory().createLinearRing(coordinates);
//		Polygon poly = new Polygon(linear, null, fact);
		 
		Polygon p = new Polygon();
		
		for(Sample s : samplesObject){
			p.addPoint((int)s.getX(), (int)s.getY());
		}
		
		Rectangle b = p.getBounds();
		
		
		Map<String, Double> affs = new HashMap<String, Double>();
		Map<String, Polygon> polygons = new HashMap<String, Polygon>();
		for(Entry<String, IObjectRule> objRule : objRules.entrySet()){
			// put affinity info
			affs.put(objRule.getKey(), objRule.getValue().getTypeAffinity(samplesObject));

			// put polygon			
			Polygon poly = new Polygon();
			poly = objRule.getValue().getPredictedPolygon(samplesObject);
			polygons.put(objRule.getKey(), poly);				
		}
		
		return new TrackedObject(b.x, b.y, b.width, b.height, affs, polygons);
	}
	
	public List<TrackedObject> getTrackedObjects() {
		return new ArrayList<TrackedObject>(boxes);
	}
	
	public SensorMeasurement getMeasurement(){
		return new SensorMeasurement(measure);
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
