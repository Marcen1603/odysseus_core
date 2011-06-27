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

package de.uniol.inf.is.odysseus.generator.outliersanddirty.generator.evolve;

import de.uniol.inf.is.odysseus.generator.outliersanddirty.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.outliersanddirty.generator.AbstractValueGenerator;

/**
 * 
 * @author Dennis Geesen
 * Created at: 27.06.2011
 */
public class AlternatingGenerator extends AbstractValueGenerator {

	private double currentValue;
	private double startValue;
	private double increase;
	private double max;
	private double min;
	private boolean down;

	public AlternatingGenerator(IErrorModel errorModel, double start, double increase, double min, double max){
		super(errorModel);	
		this.startValue = start;
		this.increase = increase;
		this.min = min;
		this.max = max;
	}
	
	
	@Override
	public double generateValue() {
		if(down){
			currentValue = currentValue - increase;
			if(currentValue<=min){
				down = false;
			}
		}else{
			currentValue = currentValue + increase;
			if(currentValue>=max){
				down = true;
			}
		}
		return currentValue;
	}

	@Override
	public void initGenerator() {
		currentValue = startValue;
		down = false;
	}

}
