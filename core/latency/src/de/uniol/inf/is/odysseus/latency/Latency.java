/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.latency;

import de.uniol.inf.is.odysseus.metadata.ILatency;

public class Latency implements ILatency{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3355802503979937479L;
	private long lstart;
	private long lend;
	
	public Latency(){
		this.lstart = System.nanoTime();
	}
	
	public Latency(long start, long end){
		this.lend = end;
		this.lstart = start;
	}
	
	private Latency(Latency copy){
		this.lend = copy.lend;
		this.lstart = copy.lstart;
	}
	
	@Override
	public long getLatency() {
		return this.lend - this.lstart;
	}

	@Override
	public long getLatencyEnd() {
		return this.lend;
	}

	@Override
	public long getLatencyStart() {
		return this.lstart;
	}

	@Override
	public void setLatencyEnd(long timestamp) {
		this.lend = timestamp;
	}

	@Override
	public void setLatencyStart(long timestamp) {
		this.lstart = timestamp;
		
	}
	
	@Override
	public ILatency clone(){
		return new Latency(this);
	}
	
	@Override
	public String toString(){
		return this.lend > this.lstart? "" + (this.lend - this.lstart):"\u221E";
	}
	
	@Override
	public String csvToString() {
		return ""+ this.lstart+";"+this.lend+";"+(this.lend - this.lstart);
	}
	
	@Override
	public String getCSVHeader() {
		return "lstart;lend;latency";
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ICSVToString#csvToString(boolean)
	 */
	@Override
	public String csvToString(boolean withMetada) {
		return this.csvToString();
	}

}
