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
package de.uniol.inf.is.odysseus.core.server.monitoring;

import java.text.NumberFormat;

import de.uniol.inf.is.odysseus.core.WriteOptions;


/**
 * @author Dennis Geesen
 *
 */
public class IncrementalDescriptiveStatistics implements IDescriptiveStatistics {

	private int count = 0;
	private long sum = 0;
	private long min = Long.MAX_VALUE;
	private long max = Long.MIN_VALUE;	
	
	@Override
	public String csvToString(WriteOptions options) {
		char delimiter = options.getDelimiter();
		NumberFormat floatingFormatter  = options.getFloatingFormatter();
		NumberFormat numberFormatter = options.getNumberFormatter();
		if (floatingFormatter != null && numberFormatter != null){
			return numberFormatter.format(min)+delimiter+numberFormatter.format(max)+delimiter+floatingFormatter.format(getMean())+delimiter+numberFormatter.format(count)+delimiter+numberFormatter.format(sum);				
		}else{
			if (floatingFormatter != null){
				return min+delimiter+max+delimiter+floatingFormatter.format(getMean())+delimiter+count+delimiter+sum;								
			}else{
				return ""+min+delimiter+max+delimiter+getMean()+delimiter+count+delimiter+sum;								
			}
		}
	}
	
	/**
	 * @return
	 */
	private double getMean() {
		return sum / (double)count;
	}

	@Override
	public String getCSVHeader(char delimiter) {
		return "Min"+delimiter+"Max"+delimiter+"Mean"+delimiter+"Count"+delimiter+"Sum";
	}

	@Override
	public synchronized void addValue(long value) {
		count++;
		sum = sum + value;
		min = Math.min(min, value);
		max = Math.max(max, value);		
	}

	@Override
	public IDescriptiveStatistics createInstance() {
		return new IncrementalDescriptiveStatistics();
	}

	

}
