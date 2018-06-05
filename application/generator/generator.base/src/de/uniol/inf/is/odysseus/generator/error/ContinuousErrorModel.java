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

package de.uniol.inf.is.odysseus.generator.error;

import de.uniol.inf.is.odysseus.generator.noise.INoise;

/**
 * 
 * @author Dennis Geesen
 * Created at: 27.06.2011
 */
public class ContinuousErrorModel extends AbstractErrorModel {

	private int counter;
	private int errorAtStep;

	
	public ContinuousErrorModel(INoise noise, int errorAtStep){
		super(noise);
		this.errorAtStep = errorAtStep;		
	}	
	
	@Override
	public void init() {
		this.counter = 0;
	}	

	@Override
	public double pollute(double value) {
		this.counter++;
		if((this.counter%errorAtStep)==0){
			this.counter = 0;
			return noise.addNoise(value);
		}		
		return value;
	}

}
