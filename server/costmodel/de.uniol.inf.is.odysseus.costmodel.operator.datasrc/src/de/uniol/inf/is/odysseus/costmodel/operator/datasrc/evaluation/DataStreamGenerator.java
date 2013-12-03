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
package de.uniol.inf.is.odysseus.costmodel.operator.datasrc.evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataStreamGenerator {

	public List<Double> getEqualDistributedStream( int min, int max, int count) {
		Random rand = new Random(0);
		
		List<Double> values = new ArrayList<Double>(count);
		
		for( int i = 0; i < count; i++ )
			values.add(rand.nextDouble() * (max - min) + min);
		
		return values;
	}
	
	public List<Double> getIncreasingStream( int min, int max, int count) {
		List<Double> values = new ArrayList<Double>(count);
		
		for( int i = 0; i < count; i++ ) {
			values.add( ((double)max - min) * ((double)i / count) + min);
		}
		
		return values;
	}
	
	public List<Double> getNormalDistributedStream( int min, int max, int count ) {
		Random rand = new Random(0);
		
		List<Double> values = new ArrayList<Double>(count);
		
		for( int i = 0; i < count; i++ )
			values.add((max - min) / 2.0 + ( rand.nextGaussian() * ((max - min) / 8 ) ) );
		
		return values;
		
	}
	
	public List<Double> getJumpingStream( int start, int end, int count ) {
		Random rand = new Random(0);
		
		List<Double> values = new ArrayList<Double>(count);
		
		for( int i = 0; i < count / 2; i++ ) {
			values.add(start + ( rand.nextGaussian() * ((start - end) / 8 ) ) );
		}
		for( int i = count / 2; i < count; i++ ) {
			values.add(end + ( rand.nextGaussian() * ((start - end) / 8 ) ) );
		}
		
		return values;
		
	}
}
