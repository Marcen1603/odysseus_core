/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.generator.outliersanddirty.generator.distribution;

import java.util.Random;

import de.uniol.inf.is.odysseus.generator.outliersanddirty.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.generator.AbstractValueGenerator;

/**
 * 
 * @author Dennis Geesen
 * Created at: 27.06.2011
 */
public class UniformDistributionGenerator extends AbstractValueGenerator{

	private double min;
	private double max;
	private Random random;

	public UniformDistributionGenerator(IErrorModel errorModel, double min, double max) {
		super(errorModel);
		this.min = min;
		this.max = max;
		
		if(max<min){
			throw new IllegalArgumentException("Max must be higher than min");	
		}
	}

	@Override
	public double generateValue() {
		double val = random.nextDouble();
		double range = max - min;
		val = val * range + min;
		return val;
	}

	@Override
	public void initGenerator() {	
		this.random = new Random();
	}
	

}
