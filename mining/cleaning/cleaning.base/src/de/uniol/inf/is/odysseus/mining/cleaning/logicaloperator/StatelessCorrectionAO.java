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

package de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator;

import de.uniol.inf.is.odysseus.mining.cleaning.correction.stateless.IUnaryCorrection;


/**
 * 
 * @author Dennis Geesen
 * Created at: 11.07.2011
 */
public class StatelessCorrectionAO<T> extends AbstractCorrectionAO<T, IUnaryCorrection<T>>{
	
	private static final long serialVersionUID = 7935563815355921651L;

	public StatelessCorrectionAO() {
		
	}
	
	public StatelessCorrectionAO(StatelessCorrectionAO<T> statelessCorrectionAO) {
		super(statelessCorrectionAO);
	}

	@Override
	public StatelessCorrectionAO<T> clone() {
		return new StatelessCorrectionAO<T>(this);
	}

}
