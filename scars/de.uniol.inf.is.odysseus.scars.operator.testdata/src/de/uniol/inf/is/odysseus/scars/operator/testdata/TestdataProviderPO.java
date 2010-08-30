package de.uniol.inf.is.odysseus.scars.operator.testdata;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access.AbstractSensorAccessPO;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import de.uniol.inf.is.odysseus.scars.testdata.provider.Provider;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class TestdataProviderPO<M extends IProbability> extends AbstractSensorAccessPO<MVRelationalTuple<M>, M> {
	
	private Provider provider;
	private SDFAttributeList outputSchema;
	private long lastTime;
	private MVRelationalTuple<M> buffer = null;
	private int counter = 0;
	
	public TestdataProviderPO() {
		
	}
	
	@SuppressWarnings("rawtypes")
	public TestdataProviderPO(TestdataProviderPO operator) {
		this.provider = operator.provider;
		this.outputSchema = operator.getOutputSchema().clone();
		this.lastTime = operator.lastTime;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		this.provider = new Provider();
		provider.setDelay(50);
		provider.setNumOfCars(5);
		provider.init();
		for (int i = 0; i < 200; i++) {
			//provider.nextTuple();
			System.out.println(provider.nextTuple());
		}
	}

	@Override
	public AbstractSource<MVRelationalTuple<M>>  clone() {
		return new TestdataProviderPO<M>(this);
	}
	
	@Override
	public SDFAttributeList getOutputSchema() {
		return outputSchema;
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
			if (System.currentTimeMillis() - lastTime > 20) {
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
		transfer(this.buffer);
		this.buffer = null;
		if(this.counter % 5 == 0){
			sendPunctuation(new PointInTime(this.provider.getLastTimestamp()));
		}
		lastTime = System.currentTimeMillis();

	}

	@Override
	public void setOutputSchema(SDFAttributeList outputSchema) {
		super.setOutputSchema(outputSchema);
		this.outputSchema = outputSchema;
	}

}
