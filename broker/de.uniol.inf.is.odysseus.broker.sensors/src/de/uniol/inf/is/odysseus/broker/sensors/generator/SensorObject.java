package de.uniol.inf.is.odysseus.broker.sensors.generator;

import java.util.Random;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class SensorObject implements IStreamType{
	
	private RelationalTuple<ITimeInterval> tuple = new RelationalTuple<ITimeInterval>(getSchema().getAttributeCount());
	private int currentId;
	private int run;
	private double currentPosX;
	private double currentPosY;
	private String type;
	private long waitingMillis = 1000;
	private int maxItems = Integer.MAX_VALUE;
	private boolean punctuationEnabled;	
	
	public SensorObject(String type){
		Random rand = new Random();
		this.currentId = 0;
		this.run = 0;
		this.currentPosX = rand.nextDouble();
		this.currentPosY = rand.nextDouble();	
		this.type = type;
	}
	
	public SensorObject(String type, long waitingMillis){
		this(type);
		this.waitingMillis = waitingMillis;
		this.punctuationEnabled = false;
		this.maxItems = Integer.MAX_VALUE;
	}
	
	public SensorObject(String type, long waitingMillis, int maxItems, boolean punctuation){
		this(type, waitingMillis);
		this.maxItems = maxItems;
		this.punctuationEnabled = punctuation;
	}
	
	@Override
	public SDFAttributeList getSchema() {
		SDFAttributeList schema = new SDFAttributeList();
		SDFAttribute a = new SDFAttribute("timestamp");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		schema.add(a);
		a = new SDFAttribute("id");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
		schema.add(a);
		a = new SDFAttribute("interval");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
		schema.add(a);
		a = new SDFAttribute("type");
		a.setDatatype(SDFDatatypeFactory.getDatatype("String"));
		schema.add(a);
		a = new SDFAttribute("position_x");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
		schema.add(a);
		a = new SDFAttribute("position_y");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
		schema.add(a);
		return schema;
	}

	@Override
	public RelationalTuple<ITimeInterval> getNextTuple(long currentTime) {	
		tuple.setAttribute(0, run);
		tuple.setAttribute(1, currentId);
		tuple.setAttribute(2, run);
		tuple.setAttribute(3, this.type);
		tuple.setAttribute(4, currentPosX);
		tuple.setAttribute(5, currentPosY);
		tuple.setMetadata(new TimeInterval(new PointInTime(run)));		
		currentId = (currentId+1)%10;
		currentPosX = currentPosX+1.0d;
		currentPosY = currentPosY+1.0d;
		if(currentId==0){
			run++;
		}
		return tuple;
	}

	@Override
	public long getWaitingMillis() {		
		return this.waitingMillis;
	}

	@Override
	public int getMaxItems() {
		return this.maxItems;
	}

	@Override
	public boolean isPunctuationEnabled() {
		return punctuationEnabled;
	}

	@Override
	public String getName() {
		return this.type;
	}	

	@Override
	public long getNextPunctuation(long currentTime) {		
		long result = run;
		run++;
		return result;
	}
	
}
