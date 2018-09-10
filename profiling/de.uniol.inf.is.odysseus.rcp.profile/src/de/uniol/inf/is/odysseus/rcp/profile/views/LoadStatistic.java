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

package de.uniol.inf.is.odysseus.rcp.profile.views;

public class LoadStatistic {

	private final static double MEMORY_IN_MB_FACTOR = 1024 * 1024;
	
	private final double actCpuLoad;
	private final double actMemLoad;
	private final long timestamp;

	public LoadStatistic(double actCpuLoad, double actMemLoad, long timestamp) {
		this.actCpuLoad = actCpuLoad;
		this.actMemLoad = actMemLoad / MEMORY_IN_MB_FACTOR;
		this.timestamp = timestamp;
	}

	public double getActCpuLoad() {
		return actCpuLoad;
	}

	public double getActMemLoad() {
		return actMemLoad;
	}

	public long getTimestamp() {
		return timestamp;
	}
}
