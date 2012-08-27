package de.offis.salsa.obsrec;

import java.util.ArrayList;
import java.util.List;

import com.impetus.annovention.ClasspathDiscoverer;
import com.impetus.annovention.Discoverer;
import com.impetus.annovention.listener.ClassAnnotationDiscoveryListener;

import de.offis.salsa.lms.model.Sample;
import de.offis.salsa.obsrec.annotations.ObjectRule;
import de.offis.salsa.obsrec.datasegm.ILmsDataSegmenter;
import de.offis.salsa.obsrec.datasegm.SefSegmentation;
import de.offis.salsa.obsrec.ls.DebugLaserScanner;
import de.offis.salsa.obsrec.ls.ReadingLaserScanner;
import de.offis.salsa.obsrec.ls.SavingLaserScanner;
import de.offis.salsa.obsrec.ls.SickLaserScanner;
import de.offis.salsa.obsrec.objrules.AbstractObjRule;
import de.offis.salsa.obsrec.objrules.IObjectRule;

public class Objektwelt {
//	private List<SensorMeasurement> measurements;
	private List<SickLaserScanner> scanner;
	
	private SensorMeasurement measure;
	private List<TrackedObject> boxes = new ArrayList<TrackedObject>();
	
//	private TrackedObjectsProvider boxProvider = new StandardTrackedObjects();
	
	private ILmsDataSegmenter segmenter = new SefSegmentation();
	
	public Objektwelt(){
		this.scanner = new ArrayList<>();
		registerObjRules();
	}
	
	private void registerObjectRule(IObjectRule rule){
		AbstractObjRule.registerObjRule(rule);
	}
	
	private void registerObjRules(){		
		Discoverer discoverer = new ClasspathDiscoverer();
        
        // Add class annotation listener (optional)
        discoverer.addAnnotationListener(new ClassAnnotationDiscoveryListener() {
			
			@Override
			public String[] supportedAnnotations() {
				return new String[]{
						ObjectRule.class.getName()
						};
			}
			
			@Override
			public void discovered(String clazz, String annotation) {
				try {
					Object objRule = Class.forName(clazz).newInstance();
					registerObjectRule((IObjectRule) objRule);
					System.out.println(clazz);
				} catch (InstantiationException | IllegalAccessException
						| ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
        
        // Fire it
        discoverer.discover();
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

		ArrayList<TrackedObject> tempObj = new ArrayList<>();
		for(List<Sample> segmentSamples : this.segmenter.segmentScan(measurement)){
			tempObj.add(new TrackedObject(segmentSamples));
		}
		
		this.boxes = tempObj;
		
		fireOnChangeEvent();
	}

	public List<TrackedObject> getTrackedObjects() {
		return new ArrayList<>(boxes);
	}
	
	public SensorMeasurement getMeasurement(){
		return new SensorMeasurement(measure);
	}
	
	private List<ObjektWeltListener> listener = new ArrayList<ObjektWeltListener>();
	
	private void fireOnChangeEvent(){
		for(ObjektWeltListener listener : this.listener){
			listener.onChange();
		}
	}
	
	public void registerListener(ObjektWeltListener listener){
		this.listener.add(listener);
	}
	
	public void removeListener(ObjektWeltListener listener){
		this.listener.remove(listener);
	}
}
