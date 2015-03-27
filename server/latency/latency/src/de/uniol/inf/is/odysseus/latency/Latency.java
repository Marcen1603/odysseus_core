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
package de.uniol.inf.is.odysseus.latency;

import java.text.NumberFormat;

import de.uniol.inf.is.odysseus.core.WriteOptions;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;

public class Latency implements ILatency{


	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[]{ 
		ILatency.class
	};
	
	private static final long serialVersionUID = -3355802503979937479L;
	private long minlstart;
	private long maxlstart;
	private long lend;
	
	public Latency(){
		this.minlstart = System.nanoTime();
		this.maxlstart = minlstart;
	}
	
//	public Latency(long start, long end){
//		this.lend = end;
//		this.lstart = start;
//	}
	
	public Latency(Latency copy){
		this.lend = copy.lend;
		this.minlstart = copy.minlstart;
		this.maxlstart = copy.maxlstart;
	}
	
	@Override
	public long getLatency() {
		return this.lend - this.minlstart;
	}
	
	@Override
	public long getMaxLatency() {
		return this.lend - this.maxlstart;
	}

	@Override
	public long getLatencyEnd() {
		return this.lend;
	}

	@Override
	public long getLatencyStart() {
		return this.minlstart;
	}
	
	@Override
	public long getMaxLatencyStart(){
		return this.maxlstart;
	}

	@Override
	public void setLatencyEnd(long timestamp) {
		this.lend = timestamp;
	}

	@Override
	public void setMinLatencyStart(long timestamp) {
		this.minlstart = timestamp;
	}
	
	@Override
	public void setMaxLatencyStart(long timestamp) {
		this.maxlstart = timestamp;
	}
	
	@Override
	public ILatency clone(){
		return new Latency(this);
	}
	
	@Override
	public String toString(){
		return "[(max=" +this.maxlstart+")"+ this.minlstart + ", " + this.lend + "[" + (this.lend > this.minlstart?  (this.lend - this.minlstart):"oo");
	}
		
	@Override	
	public String csvToString(WriteOptions options){
		NumberFormat numberFormatter = options.getNumberFormatter();
		char delimiter = options.getDelimiter();
		StringBuffer retBuffer = new StringBuffer();
		if (numberFormatter != null){
			retBuffer.append(numberFormatter.format(this.minlstart)).append(delimiter).append(numberFormatter.format(this.lend)).append(delimiter).append(numberFormatter.format(this.lend - this.minlstart));
		}else{
			retBuffer.append(this.maxlstart).append(delimiter).append(this.minlstart).append(delimiter).append(this.lend).append(delimiter).append(this.lend - this.minlstart);
		}
		return retBuffer.toString();
	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		return "maxlstart"+delimiter+"minlstart"+delimiter+"lend"+delimiter+"latency";
	}
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}

	@Override
	public String getName() {
		return "Latency";
	}
}
