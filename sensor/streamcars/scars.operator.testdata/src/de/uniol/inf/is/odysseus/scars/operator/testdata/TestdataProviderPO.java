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

	private static final int DELAY = 100;
	private static final int SEND_DELAY = 300;

	private Provider provider;
	private long lastTime;
	private MVRelationalTuple<M> buffer = null;
	private String sourceName;
	public TestdataProviderPO() {
		this.provider = new Provider();
	}

	@SuppressWarnings("rawtypes")
	public TestdataProviderPO(TestdataProviderPO operator) {
		this.provider = operator.provider;
		this.lastTime = operator.lastTime;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		provider.setDelay(DELAY);
		provider.setNumOfCars(5);
		provider.init();
		new StreamCarsMetaDataInitializer<StreamCarsMetaData<Object>>(getOutputSchema());
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean hasNext() {

		/*
		 * Hier wird gewartet, damit die Verarbeitung der Daten besser
		 * nachvollzogen werden kann und Odysseus / Eclipse nicht �berlastet
		 * wird (siehe auch Ticket 225).
		 */
		if(!this.isDone()){
			if (System.currentTimeMillis() - lastTime > SEND_DELAY) {
				this.buffer = (MVRelationalTuple<M>) provider.nextTuple();
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
	public void transferNext() {
		System.out.println("Send Data: " + this.buffer);

		transfer(this.buffer);
		this.buffer = null;
//		System.out.println("Send Punctuation: " + (this.provider.getLastTimestamp()+1));
//		sendPunctuation(new PointInTime(this.provider.getLastTimestamp()+1));
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
