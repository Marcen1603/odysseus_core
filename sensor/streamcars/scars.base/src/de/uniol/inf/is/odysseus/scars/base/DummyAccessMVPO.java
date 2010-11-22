package de.uniol.inf.is.odysseus.scars.base;

import java.net.SocketException;
import java.util.Random;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access.AbstractSensorAccessPO;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DummyAccessMVPO<M extends IProbability> extends AbstractSensorAccessPO<MVRelationalTuple<M>, M> {

	private DummyJDVEData<M> data;
	private SDFAttributeList outputSchema;
	protected static int CARCOUNT = 5;
	private MVRelationalTuple<M> buffer = null;

	private long lastTime = 0;
	
	public DummyAccessMVPO() {
	}

	public DummyAccessMVPO(DummyAccessMVPO<M> po) {
		outputSchema = po.outputSchema.clone();
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		try {
			data = new DummyJDVEData<M>(outputSchema);
		} catch (SocketException ex) {
			throw new OpenFailedException(ex);
		}
	}

	@Override
	protected void process_done() {
	}

	@Override
	public boolean hasNext() {
		
		/*
		 * Hier wird gewartet, damit die Verarbeitung der Daten besser
		 * nachvollzogen werden kann und Odysseus / Eclipse nicht �berlastet
		 * wird (siehe auch Ticket 225).
		 */
		if(!this.isDone()){
			if (System.currentTimeMillis() - lastTime > 1000) {
				this.buffer = data.getScan();
				return true;
			}
			else{
				return false;
			}
		}
		else{
			this.propagateDone();
			return false;
		}
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public AbstractSource<MVRelationalTuple<M>> clone() {
		return new DummyAccessMVPO<M>(this);
	}

	@Override
	public void transferNext() {
		transfer(this.buffer);
		this.buffer = null;
		lastTime = System.currentTimeMillis();

	}
	
	@SuppressWarnings("unchecked")
	private void assignMetadata(Class<M> clazz, Object tuple ){
		try {
			if( tuple instanceof IMetaAttributeContainer ) {
				((IMetaAttributeContainer<M>)tuple).setMetadata(clazz.newInstance());
			}
			if( tuple instanceof MVRelationalTuple<?>) {
				MVRelationalTuple<M> t = (MVRelationalTuple<M>) tuple;
				for( int i = 0; i < t.getAttributeCount(); i++ ) {
					assignMetadata(clazz, t.getAttribute(i));
				}
			}
		} catch( IllegalAccessException ex ) {
			ex.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}
	
//	private static HashSet<String> toStringSet(Class<? extends IMetaAttribute>... combinationOf) {
//		HashSet<String> typeSet = new HashSet<String>();
//		for (Class<?> c : combinationOf) {
//			typeSet.add(c.getName());
//		}
//		return typeSet;
//	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		super.setOutputSchema(outputSchema);
		this.outputSchema = outputSchema;
	}
}

class DummyJDVEData<M extends IProbability> {

	private static Random rdm = new Random();
	private SDFAttributeList attributeList;
	private long lastTimestamp = 0;

	public DummyJDVEData(SDFAttributeList list) throws SocketException {
		this.attributeList = list;
	}
	
	public long getLastTimestamp() {
		return lastTimestamp;
	}

	@SuppressWarnings("unchecked")
	public MVRelationalTuple<M> getScan() {
		Object res = parseStart(attributeList);

		if (res instanceof MVRelationalTuple<?>) {
			return (MVRelationalTuple<M>) res;
		} else {
			MVRelationalTuple<M> tuple = new MVRelationalTuple<M>(1);
			tuple.setAttribute(0, res);
			return tuple;
		}
	}

	public MVRelationalTuple<M> parseStart(SDFAttributeList schema) {
		MVRelationalTuple<M> base = new MVRelationalTuple<M>(1);
		base.setAttribute(0, parseNext(schema.get(0)));
		return base;
	}

	public MVRelationalTuple<M> parseRecord(SDFAttribute schema) {
		int count = schema.getSubattributeCount();
		MVRelationalTuple<M> recordTuple = new MVRelationalTuple<M>(count);
		for (int i = 0; i < count; i++) {
			Object obj = parseNext(schema.getSubattribute(i));
			recordTuple.setAttribute(i, obj);
		}
		return recordTuple;
	}

	public MVRelationalTuple<M> parseList(SDFAttribute schema) {
		MVRelationalTuple<M> recordTuple = new MVRelationalTuple<M>(DummyAccessMVPO.CARCOUNT);

		for (int i = 0; i < DummyAccessMVPO.CARCOUNT; i++) {
			Object obj = parseNext(schema.getSubattribute(0));
			recordTuple.setAttribute(i, obj);
		}
		return recordTuple;
	}

	public Object parseAttribute(SDFAttribute schema) {
		if ("Integer".equals(schema.getDatatype().getURIWithoutQualName())) {
			return rdm.nextInt(100);
		} else if ("Double".equals(schema.getDatatype().getURIWithoutQualName())) {
			return rdm.nextDouble();
		} else if ("String".equals(schema.getDatatype().getURIWithoutQualName())) {
			throw new RuntimeException("not implememted yet");
		} else if ("Long".equals(schema.getDatatype().getURIWithoutQualName())) {
			return (long) rdm.nextInt(100);
		} else if ("Float".equals(schema.getDatatype().getURIWithoutQualName())) {
			return rdm.nextFloat();
		} else if ("MV".equals(schema.getDatatype().getURIWithoutQualName())) {
			return rdm.nextDouble();
		} else if ("MV Float".equals(schema.getDatatype().getURIWithoutQualName())) {
			return rdm.nextFloat();
		} else if ("MV Long".equals(schema.getDatatype().getURIWithoutQualName())) {
			return (long) rdm.nextInt(100);
		} else if ("MV Integer".equals(schema.getDatatype().getURIWithoutQualName())) {
			return rdm.nextInt(100);
		} else if ("StartTimestamp".equals(schema.getDatatype().getURIWithoutQualName())) {
			lastTimestamp = System.currentTimeMillis();
			return lastTimestamp;
		} else if ("EndTimestamp".equals(schema.getDatatype().getURIWithoutQualName())) {
			return lastTimestamp + 1000; // eine Sekunde abstand
		} else {
			throw new RuntimeException("not implememted yet");
		}
	}

	public Object parseNext(SDFAttribute attr) {
		Object obj = null;
		if ("Record".equals(attr.getDatatype().getURIWithoutQualName())) {
			obj = parseRecord(attr);
		} else if ("List".equals(attr.getDatatype().getURIWithoutQualName())) {
			obj = parseList(attr);
		} else {
			obj = parseAttribute(attr);
		}
		return obj;
	}
}