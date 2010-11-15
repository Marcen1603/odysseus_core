package de.uniol.inf.is.odysseus.scars.operator.testdata;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access.AbstractSensorAccessPO;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaData;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.StreamCarsMetaDataInitializer;
import de.uniol.inf.is.odysseus.scars.testdata.provider.Provider;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class TestdataProviderPO<M extends IProbability> extends AbstractSensorAccessPO<MVRelationalTuple<M>, M> {

	private static final int DELAY = 100; // ms
	private static final int SEND_DELAY = 15; //ms
	private static final long SEND_DELAY_NS = SEND_DELAY * 1000000; // ns

	private Provider provider;
	private long lastTime;
	private String sourceName;
	
	private int batchSize = 2000;
	
	@SuppressWarnings("unchecked")
	private MVRelationalTuple<M>[] nextElems = new MVRelationalTuple[batchSize];
	private boolean done = false;
	
	public TestdataProviderPO() {
		this.provider = new Provider();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public TestdataProviderPO(TestdataProviderPO operator) {
		this.provider = operator.provider;
		this.lastTime = operator.lastTime;
		this.batchSize = operator.batchSize;
		this.nextElems = new MVRelationalTuple[batchSize];
		for(int i = 0; i<this.nextElems.length; i++){
			this.nextElems[i] = operator.nextElems[i].clone();
		}
		this.done = operator.done;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_open() throws OpenFailedException {
		provider.setDelay(DELAY);
		provider.setNumOfCars(5);
		provider.init();
		new StreamCarsMetaDataInitializer<StreamCarsMetaData<Object>>(getOutputSchema());
		
		for(int i = 0; i<this.batchSize; i++){
			this.nextElems[i] = (MVRelationalTuple<M>)provider.nextTuple();
		}
	}

	@Override
	public AbstractSource<MVRelationalTuple<M>>  clone() {
		return new TestdataProviderPO<M>(this);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		if (this.provider != null ) {
			return this.provider.getSchema(this.sourceName);
		} else {
			return null;
		}
	}

	@Override
	protected void process_done() {
	}

	public boolean hasNext(){
		if(!this.isDone()){
			return true;
		}
		else{
			this.propagateDone();
			return false;
		}
	}
	

//	int counter = 0;
//	@SuppressWarnings("unchecked")
//	@Override
//	public boolean hasNext() {
//
//		if(counter % 100 ==0){
//			for(String curMsg : msg){
//				System.out.println(curMsg);
//			}
//			this.counter = 0;
//		}
//		
//		/*
//		 * Hier wird gewartet, damit die Verarbeitung der Daten besser
//		 * nachvollzogen werden kann und Odysseus / Eclipse nicht �berlastet
//		 * wird (siehe auch Ticket 225).
//		 */
//		if(!this.isDone()){
//			if (System.currentTimeMillis() - lastTime > SEND_DELAY) {
//				msg[counter] = "Send after : " + (System.currentTimeMillis() - lastTime);
//				counter++;
//				this.buffer = (MVRelationalTuple<M>) provider.nextTuple();
//				return true;
//			}
//			else{
//				return false;
//			}
//		}
//		else{
//			this.propagateDone();
//			return false;
//		}
//	}

	@Override
	public boolean isDone() {
		return this.done;
	}


	public void transferNext(){
		long lastTime = System.nanoTime();
		for(int i = 0; i<this.batchSize; i++){
			sleep(lastTime + SEND_DELAY_NS);
			transfer(this.nextElems[i]);
			lastTime = System.nanoTime();
		}
		this.done = true;
	}
	
	private void sleep(long until){
		while(System.nanoTime() < until){
			;
		}
	}
	
//	int counter = 0;
//	@Override
//	public void transferNext() {
//		
////		counter++;
////		System.out.println("Send Data #" + counter);
//		
//		transfer(this.buffer);
//		this.buffer = null;
////		System.out.println("Send Punctuation: " + (this.provider.getLastTimestamp()+1));
////		sendPunctuation(new PointInTime(this.provider.getLastTimestamp()+1));
//		lastTime = System.currentTimeMillis();
//
//	}

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

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		super.setOutputSchema(outputSchema);
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}



}
