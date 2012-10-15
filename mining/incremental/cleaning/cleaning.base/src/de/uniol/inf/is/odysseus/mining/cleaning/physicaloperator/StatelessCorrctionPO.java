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

package de.uniol.inf.is.odysseus.mining.cleaning.physicaloperator;

import java.util.List;

import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.mining.cleaning.correction.stateless.IUnaryCorrection;
import de.uniol.inf.is.odysseus.mining.metadata.IMiningMetadata;

/**
 * 
 * @author Dennis Geesen
 * Created at: 11.07.2011
 */
public class StatelessCorrctionPO<T extends IStreamObject<? extends IMiningMetadata>> extends AbstractCorrectionPO<T, IUnaryCorrection<T>>{

	public StatelessCorrctionPO(List<IUnaryCorrection<T>> corrections) {
		super(corrections);
	}
	
	
	public StatelessCorrctionPO(StatelessCorrctionPO<T> statelessCorrctionPO) {
		super(statelessCorrctionPO.corrections);
	}


	@Override
	public StatelessCorrctionPO<T> clone() {
		return new StatelessCorrctionPO<T>(this);
	}
	
	@Override
	public void process_open() throws OpenFailedException {	
		super.process_open();
		for(IUnaryCorrection<T> c : this.corrections){
			c.init(this.getOutputSchema());
		}
	}


	@Override
	protected T getValueForCorrection(T original, IUnaryCorrection<T> c) {		
		return c.correctedValue(original);
	}


	@Override
	protected void process_next(T object, int port) {
		// TODO Auto-generated method stub
		
	}
}
