package de.uniol.inf.is.odysseus.benchmarker.impl;

import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

/**
 * @author Marco Grawunder, Jonas Jacobi
 */
public class BenchmarkPO<R extends IMetaAttributeContainer<?>> extends AbstractPipe<R, R> {

	final double selectivity;
	double oldVal = 0;
	int processingTime = 300;

	/**
	 * 
	 * @param processingTime
	 * @param selectivity
	 *            Nur wenn Selektivitaeten sich zu maximal 1 adieren (z.B. 1/3)
	 *            wird in jedem Durchlauf maximal ein Element geschrieben,
	 *            ansonsten koennen auch mal mehrere geschrieben werden
	 */
	public BenchmarkPO(int processingTime, double selectivity) {
		super();
		this.processingTime = processingTime;
		this.selectivity = selectivity;
	};

	@SuppressWarnings("unchecked")
	protected final void process_next(R object, int port) {
		long end = System.nanoTime() + this.processingTime;

		if (selectivity == 1) {
			waitProcessingTime(end);
			transfer(object);
		} else {

			// Bestimmen, wie viele Objekte produziert werden sollen
			// alle ganzzahligen Vielfachen der Selektitivitaet
			// oldVal speichert die Reste
			double tmpOldVal = getOldVal(object) + selectivity;
			long toProduce = Math.round(Math.floor(tmpOldVal));
			setOldVal(object, tmpOldVal - toProduce);
			waitProcessingTime(end);
			while (toProduce-- > 0) {
				if (toProduce > 1) {
					object = (R) object.clone();
				}
				transfer(object);
			}
		}
	}

	private void waitProcessingTime(long end) {
		while (System.nanoTime() < end)
			;
	}

	protected void setOldVal(R object, double d) {
		this.oldVal = d;
	}

	protected double getOldVal(R element) {
		return oldVal;
	}

	public long getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(int processingTime) {
		this.processingTime = processingTime;
	}

	@Override
	public String toString() {
		return super.toString() + " " + getName() + " {Sel " + selectivity
				+ " PTime " + processingTime + "}";
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

}
