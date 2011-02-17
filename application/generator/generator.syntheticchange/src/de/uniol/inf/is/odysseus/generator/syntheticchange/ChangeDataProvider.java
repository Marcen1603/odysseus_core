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
package de.uniol.inf.is.odysseus.generator.syntheticchange;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class ChangeDataProvider  extends StreamClientHandler{

	// CREATE STREAM change(timestamp STARTTIMESTAMP, value DOUBLE) CHANNEL localhost : 54321;
	
	private static final int MAX_BURST_TIME = 25;
	private static final int MAX_NON_BURST_TIME = 80;
	private static final double DIFFERENCE = 100;
	private static final double BASE = 10;
	private static final double SPREAD = 5;
	//private static final double GRADIENT = 10;
	
	private long time = 0;	
	private Random rand = new Random();
	private int currentCount = 0;
	
	@Override
	public List<DataTuple> next() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		double burst = 0.0;
		
		currentCount++;
		if(currentCount>=MAX_NON_BURST_TIME){			
			burst = DIFFERENCE;
			if(currentCount>(MAX_NON_BURST_TIME+MAX_BURST_TIME)){
				currentCount = 0;
				burst = 0;
			}
		}
		
		DataTuple tuple = new DataTuple();		
		double nextValue = BASE + (rand.nextGaussian()* SPREAD) + burst;
		tuple.addAttribute(new Long(time));
		tuple.addAttribute(new Double(nextValue));				
		time++;		
		List<DataTuple> list = new ArrayList<DataTuple>();
		list.add(tuple);
		return list;
	}

	@Override
	public void init() {		
		this.time = 0;
	}

	@Override
	public void close() {			
	}
	
}
