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

package de.uniol.inf.is.odysseus.generator.valuegenerator;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;

/**
 * 
 * @author Dennis Geesen
 * Created at: 27.06.2011
 */
public abstract class AbstractValueGenerator implements IValueGenerator{

	protected IErrorModel errorModel; 
	
	public AbstractValueGenerator(IErrorModel errorModel){
		this.errorModel = errorModel;
	}
	
	@Override
	public final double nextValue() {
		double newValue = generateValue(); 
		return this.errorModel.pollute(newValue);
	}
	
	public abstract double generateValue();
	public abstract void initGenerator();
	
	@Override
	public final void init() {
		errorModel.init();
		initGenerator();
	}

	
}
