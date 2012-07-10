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

package de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator;

import de.uniol.inf.is.odysseus.mining.cleaning.correction.stateful.IBinaryCorrection;

/**
 * 
 * @author Dennis Geesen
 * Created at: 11.07.2011
 */
public class StatefulCorrectionAO<T> extends AbstractCorrectionAO<T, IBinaryCorrection<T>>{
	
	private static final long serialVersionUID = -6079095656526441387L;

	public StatefulCorrectionAO() {
		super();
	}
	
	public StatefulCorrectionAO(StatefulCorrectionAO<T> statefulCorrectionAO) {
		super(statefulCorrectionAO);
	}

	@Override
	public StatefulCorrectionAO<T> clone() {
		return new StatefulCorrectionAO<T>(this);
	}

}
