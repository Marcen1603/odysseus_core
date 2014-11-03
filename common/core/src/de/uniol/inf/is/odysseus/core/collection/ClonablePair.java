/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.collection;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.IClone;

public class ClonablePair<E1 extends IClone, E2 extends IClone> extends CloneableIdPair<E1, E2> implements Serializable{

	private static final long serialVersionUID = -3270815529098454372L;

	public ClonablePair(E1 e1, E2 e2) {
		super(e1,e2);
	}
	
	public ClonablePair(ClonablePair<E1, E2> pair, boolean deepClone) {
		super(pair,deepClone);
	}

	
}
