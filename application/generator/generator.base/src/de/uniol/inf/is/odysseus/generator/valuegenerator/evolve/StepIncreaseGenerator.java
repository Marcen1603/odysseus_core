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

package de.uniol.inf.is.odysseus.generator.valuegenerator.evolve;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractSingleValueGenerator;

/**
 * 
 * @author Dennis Geesen, Marco Grawunder
 * Created at: 12.02.2014
 */
public class StepIncreaseGenerator extends AbstractSingleValueGenerator {
		
	private double currentValue;
	private int send = 0;
	final private double startValue;
	final private double increase;
	final private double count;
	

	public StepIncreaseGenerator(IErrorModel errorModel, double start, double count, double increase){
		super(errorModel);	
		this.startValue = start;
		this.count = count;
		this.increase = increase;
	}
	
	
	@Override
	public double generateValue() {
		if (send < count){
			send++;
		}else{
			currentValue = currentValue + increase;		
			send = 0;
		}
		return currentValue;
	}

	@Override
	public void initGenerator() {
		currentValue = startValue;		
	}

}
