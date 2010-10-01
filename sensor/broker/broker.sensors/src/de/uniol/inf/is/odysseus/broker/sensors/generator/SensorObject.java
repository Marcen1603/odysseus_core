package de.uniol.inf.is.odysseus.broker.sensors.generator;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * The SensorObject represents a concrete stream of sensor objects 
 *
 * @author Dennis Geesen
 */
public class SensorObject implements IStreamType{
	
	/** The tuple. */
	private RelationalTuple<ITimeInterval> tuple = new RelationalTuple<ITimeInterval>(getSchema().getAttributeCount());
	
	/** The current id. */
	private int currentId;
	
	/** The current run. */
	private int run;
	
	/** The current x position. */
	private double currentPosX;
	
	/** The current y position. */
	private double currentPosY;
	
	/** The type. */
	private String type;
	
	/** Waiting for 1000 ms between two tuples. */
	private long waitingMillis = 1000;
	
	/** The maximal count of items. */
	private int maxItems = Integer.MAX_VALUE;
	
	/** The punctuation enabled. */
	private boolean punctuationEnabled;	
	
	/** The number of unique objects. */
	private int numberOfObjects = 4;
	
	/**
	 * Instantiates a new sensor object.
	 *
	 * @param type the type
	 */
	public SensorObject(String type){
//		Random rand = new Random();
		this.currentId = 0;
		this.run = 0;
//		this.currentPosX = rand.nextDouble();		
//		this.currentPosX = round(this.currentPosX, 3);
//		this.currentPosY = rand.nextDouble();
//		this.currentPosY = round(this.currentPosY, 3);
		this.currentPosX = 0.012d;
		this.currentPosY = 0.036d;
		this.type = type;
	}
	
	/**
	 * Instantiates a new sensor object.
	 *
	 * @param type the type
	 * @param waitingMillis the waiting millis
	 */
	public SensorObject(String type, long waitingMillis){
		this(type);
		this.waitingMillis = waitingMillis;
		this.punctuationEnabled = false;
		this.maxItems = Integer.MAX_VALUE;
	}
	
	/**
	 * Instantiates a new sensor object.
	 * If punctuation is on, the stream will send punctuations after it has send 
	 * more than the max count of items instead of normal tuples. If it is off, 
	 * the source will be closed.
	 *
	 *
	 * @param type the type
	 * @param waitingMillis the waiting millis
	 * @param maxItems the max count of items
	 * @param punctuation enables or disables the punctuation
	 */
	public SensorObject(String type, long waitingMillis, int maxItems, boolean punctuation){
		this(type, waitingMillis);
		this.maxItems = maxItems;
		this.punctuationEnabled = punctuation;
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.broker.sensors.generator.IStreamType#getSchema()
	 */
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
		a.setDatatype(SDFDatatypeFactory.getDatatype("StartTimestamp"));
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

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.broker.sensors.generator.IStreamType#getNextTuple(long)
	 */
	@Override
	public RelationalTuple<ITimeInterval> getNextTuple(long currentTime) {	
		tuple.setAttribute(0, System.currentTimeMillis());
		tuple.setAttribute(1, currentId);
		tuple.setAttribute(2, run);
		tuple.setAttribute(3, this.type);
		tuple.setAttribute(4, currentPosX);
		tuple.setAttribute(5, currentPosY);
		tuple.setMetadata(new TimeInterval(new PointInTime(currentTime)));		
		currentId = (currentId+1)%numberOfObjects;
		currentPosX = currentPosX+1.0d;
		currentPosY = currentPosY+1.0d;
		if(currentId==0){
			run++;
		}
		return tuple;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.broker.sensors.generator.IStreamType#getWaitingMillis()
	 */
	@Override
	public long getWaitingMillis() {		
		return this.waitingMillis;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.broker.sensors.generator.IStreamType#getMaxItems()
	 */
	@Override
	public int getMaxItems() {
		return this.maxItems;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.broker.sensors.generator.IStreamType#isPunctuationEnabled()
	 */
	@Override
	public boolean isPunctuationEnabled() {
		return punctuationEnabled;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.broker.sensors.generator.IStreamType#getName()
	 */
	@Override
	public String getName() {
		return this.type;
	}	

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.broker.sensors.generator.IStreamType#getNextPunctuation(long)
	 */
	@Override
	public long getNextPunctuation(long currentTime) {		
//		long result = run;
//		run++;
//		return result;
		return currentTime;
	}	
	
}
