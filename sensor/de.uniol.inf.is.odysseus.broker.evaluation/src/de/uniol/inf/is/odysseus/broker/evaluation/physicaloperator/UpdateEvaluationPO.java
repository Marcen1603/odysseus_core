package de.uniol.inf.is.odysseus.broker.evaluation.physicaloperator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe.OutputMode;

public class UpdateEvaluationPO<T extends IMetaAttributeContainer<ITimeInterval>> extends AbstractPipe<T, T> {

	private int dataOutPort = 0;
	private int queueOutPort = 1;
	private static final int NUM_OF_RUNS = 2;
	public static final int waitingMillis = 100;
	public static final int items = 1000;
	private boolean isFirst = true;
	private int number = 0;
	private volatile int numberOfItems = 0;
	private volatile long waitedTime = 0L;
	private volatile int itemsReceived = 0;
	private volatile long currentSlot = 0L;
	private volatile long timeOverall = 0L;
	private volatile int itemsOverall = 0;
	private int runs = NUM_OF_RUNS;

	private Queue<Long> times = new LinkedList<Long>();
	
	
	private volatile List<T> liste = new ArrayList<T>();

	public UpdateEvaluationPO(int number) {
		this.number = number;
	}

	public UpdateEvaluationPO(UpdateEvaluationPO<T> updateEvaluationPO) {
		this.number = updateEvaluationPO.number;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {

	}

	private synchronized void processNew() {
		currentSlot = System.currentTimeMillis();		
		times.add(currentSlot);	
	}

	private synchronized void processAnswer() {
		itemsReceived++;
		numberOfItems++;
		if(!times.isEmpty()){
			currentSlot = times.poll();
		}
		long time = System.currentTimeMillis() - currentSlot;
		String nl = "\t";
		if(this.number==1){
			nl = "\n";
		}
		System.out.print("EvalPO No. " + this.number + ":" + (time+nl));
		waitedTime = waitedTime + time;
		if (numberOfItems > items) {
			printResults();
			timeOverall = timeOverall + waitedTime;
			itemsOverall = itemsOverall + numberOfItems;
			// reset
			numberOfItems = 0;
			waitedTime = 0;
			runs--;

			if (runs == 0) {
				System.err.println(this.number + ":");
				System.err.println("All done!");
				System.err.println("Time overall: " + timeOverall);
				System.err.println("Time per run: " + (timeOverall / NUM_OF_RUNS));
				System.err.println("Tuple overall: " + itemsOverall);
			}
		}
	}	

	private void printResults() {
		System.out.println(this.number + ". PROCESS DONE");
		System.out.println("Items: " + numberOfItems);
		System.out.println("Received: " + itemsReceived);
		System.out.println("total waited: " + waitedTime);
	}

	@Override
	protected void process_next(T object, int port) {
		
		if (isFirst) {
			transfer(object, dataOutPort);
			isFirst = false;
			return;
		}

		// DATA
		if (port == 1) {
			System.out.println("Process NewData UpdateEval No: " + this.number + " TS: " + object.getMetadata().getStart());
			processNew();
			liste.add(object);
			transfer(object, queueOutPort);

		} else {
			System.out.println("Process OldData UpdateEval No: " + this.number + " TS: " + object.getMetadata().getStart());
			// answer
			processAnswer();
			synchronized (liste) {
				if (liste.size() > 0) {
					object = liste.remove(0);
					transfer(object, dataOutPort);
				}
			}
		}
		// try {
		// Thread.sleep(waitingMillis);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public UpdateEvaluationPO<T> clone() {
		return new UpdateEvaluationPO<T>(this);
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + number;
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		UpdateEvaluationPO other = (UpdateEvaluationPO) obj;
//		if (number != other.number)
//			return false;
//		return true;
//	}

	@Override
	public String toString() {
		return super.toString() + " [" + this.number + "]";
	}

}
