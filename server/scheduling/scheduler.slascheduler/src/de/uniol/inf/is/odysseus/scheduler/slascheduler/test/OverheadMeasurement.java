/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.scheduler.slascheduler.test;

/**
 * Class for measuring overhead of scheduling
 * @author Thomas Vogelgesang
 *
 */
public class OverheadMeasurement {
	
	private long startTS;
	@SuppressWarnings("unused")
	private long totalTime;
	private long outputTS;
	@SuppressWarnings("unused")
	private int calls;
	
	private final long waitingTimeForOutput = 1000000000;

	public OverheadMeasurement() {
		this.outputTS = System.nanoTime(); 
	}
	
	public void start() {
		this.startTS = System.nanoTime();
		this.calls++;
	}
	
	public void stop() {
		this.totalTime += System.nanoTime() - this.startTS;
		if (System.nanoTime() > (outputTS + waitingTimeForOutput)) {
//			System.out.format("%-11.9f%n", (this.totalTime  / 1000000000.0) / (double)this.calls);
//			System.out.format("%-11.9f%n", (this.totalTime  / 1000000000.0));
//			System.err.println(this.calls);
			this.totalTime = 0L;
			this.calls = 0;
			this.outputTS = System.nanoTime();
		}
	}
	
}
