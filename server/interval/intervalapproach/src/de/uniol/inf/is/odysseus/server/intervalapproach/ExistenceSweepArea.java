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
package de.uniol.inf.is.odysseus.server.intervalapproach;

import java.util.Collections;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.MetadataComparator;

public class ExistenceSweepArea<T extends IStreamObject<? extends ITimeInterval>> extends JoinTISweepArea<T>{

	private static final long serialVersionUID = -9093153099994338083L;

	@Override
	public void insert(T object){
		synchronized(this.getElements()){
			this.getElements().add(object);
			MetadataComparator<ITimeInterval> comp = new MetadataComparator<ITimeInterval>();
			Collections.sort(this.getElements(), comp);
		}
	}
}
