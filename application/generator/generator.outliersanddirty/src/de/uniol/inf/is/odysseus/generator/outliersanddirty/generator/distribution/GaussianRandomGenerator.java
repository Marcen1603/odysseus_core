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
public class GaussianRandomGenerator extends AbstractValueGenerator {
	
	private Random random;
	private double deviation;
	private double mean;

	public GaussianRandomGenerator(IErrorModel errorModel, double mean, double deviation){
		super(errorModel);
		this.mean = mean;
		this.deviation = deviation;
	}
	

	@Override
	public double generateValue() {
		double val = random.nextGaussian();		
		val = (val * deviation) + mean;
		return val;
	}

	@Override
	public void initGenerator() {	
		this.random = new Random();
	}

}
