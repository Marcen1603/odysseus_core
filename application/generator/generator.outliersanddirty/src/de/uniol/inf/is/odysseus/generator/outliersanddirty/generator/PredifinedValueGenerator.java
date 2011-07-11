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

package de.uniol.inf.is.odysseus.generator.outliersanddirty.generator;

import de.uniol.inf.is.odysseus.generator.outliersanddirty.error.NoError;

/**
 * 
 * @author Dennis Geesen
 * Created at: 11.07.2011
 */
public class PredifinedValueGenerator  extends AbstractValueGenerator {

	private double[] values;
	private int pointer = 0;

	public PredifinedValueGenerator(double... values) {
		super(new NoError());
		this.values = values;
	}
	
		
	@Override
	public double generateValue() {
		double value = values[pointer];
		pointer = (pointer+1)%values.length;
		return value;
	}

	@Override
	public void initGenerator() {	
		this.pointer = 0;
	}

}
