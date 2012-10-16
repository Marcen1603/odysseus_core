/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.benchmark.physical;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;

/**
 * @author Marco Grawunder, Jonas Jacobi
 */
public class BenchmarkPO<R extends IStreamObject<?>> extends
		AbstractPipe<R, R> {

	protected final double selectivity;
	double oldVal = 0;
	int processingTime = 300;
	private long memUsage;
	private byte[] usedMem;

	/**
	 * 
	 * @param processingTime
	 * @param selectivity
	 *            Nur wenn Selektivitaeten sich zu maximal 1 adieren (z.B. 1/3)
	 *            wird in jedem Durchlauf maximal ein Element geschrieben,
	 *            ansonsten koennen auch mal mehrere geschrieben werden
	 */
	public BenchmarkPO(int processingTime, double selectivity, long memUsage) {
		super();
		this.processingTime = processingTime;
		this.selectivity = selectivity;
		this.memUsage = memUsage;
		
		usedMem = new byte[(int)memUsage];
		for( int i = 0; i < memUsage; i++ ) {
			usedMem[i] = 0;
		}
	}

	public BenchmarkPO(BenchmarkPO<R> benchmarkPO) {
		super(benchmarkPO);
		this.processingTime = benchmarkPO.processingTime;
		this.selectivity = benchmarkPO.selectivity;
		this.memUsage = benchmarkPO.memUsage;
		this.usedMem = new byte[(int)memUsage];
		System.arraycopy(benchmarkPO.usedMem, 0, usedMem, 0, benchmarkPO.usedMem.length);
	}

	@Override
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
	
	private static void waitProcessingTime(long end) {
		while( System.nanoTime() < end );
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
	
	public long getMemoryUsage() {
		return memUsage;
	}

	@Override
	public String toString() {
		return getName() + " {Sel " + selectivity
				+ " PTime " + processingTime + "}";
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public BenchmarkPO<R> clone() {
		return new BenchmarkPO<R>(this);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}
	
	public double getSelectivity() {
		return selectivity;
	}
}
