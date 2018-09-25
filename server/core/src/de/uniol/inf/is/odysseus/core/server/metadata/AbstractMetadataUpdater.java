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
package de.uniol.inf.is.odysseus.core.server.metadata;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

abstract public class AbstractMetadataUpdater<M extends IClone, T extends IStreamObject<? extends M>> implements IMetadataUpdater<M, T> {

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}
	
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractMetadataUpdater)){
			return false;
		}
		return ((AbstractMetadataUpdater<?,?>)obj).getName().equals(getName());
	}

}
