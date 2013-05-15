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

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.metadata.ILatency;

public class Latency implements ILatency{


	@SuppressWarnings("unchecked")
	public final static Class<? extends IMetaAttribute>[] classes = new Class[]{ 
		ILatency.class
	};
	
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
		return "[" + this.lstart + ", " + this.lend + "[" + (this.lend > this.lstart?  (this.lend - this.lstart):"oo");
	}
	
	@Override
	public String csvToString(char delimiter, Character textSeperator, NumberFormat floatingFormatter, NumberFormat numberFormatter, boolean withMetadata) {
		StringBuffer retBuffer = new StringBuffer();
		if (numberFormatter != null){
			retBuffer.append(numberFormatter.format(this.lstart)).append(delimiter).append(numberFormatter.format(this.lend)).append(delimiter).append(numberFormatter.format(this.lend - this.lstart));
		}else{
			retBuffer.append(this.lstart).append(delimiter).append(this.lend).append(delimiter).append(this.lend - this.lstart);
		}
		return retBuffer.toString();
	}
	
	@Override
	public String getCSVHeader(char delimiter) {
		return "lstart"+delimiter+"lend"+delimiter+"latency";
	}
	
	@Override
	public Class<? extends IMetaAttribute>[] getClasses() {
		return classes;
	}

}
