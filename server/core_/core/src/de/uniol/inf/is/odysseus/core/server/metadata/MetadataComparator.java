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

import java.io.Serializable;
import java.util.Comparator;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 * @author Jonas Jacobi
 */
public class MetadataComparator<T extends IClone> implements Comparator<IStreamObject<? extends T>>, Serializable {

	private static final long serialVersionUID = -1674438499933717924L;

	@Override
	@SuppressWarnings("unchecked")
	public int compare(IStreamObject<? extends T> o1, IStreamObject<? extends T> o2) {
		return ((Comparable<T>)o1.getMetadata()).compareTo(o2.getMetadata());
	}
      
}
