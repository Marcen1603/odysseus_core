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

import java.util.Comparator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

/**
 * This comparator compares two {@link ITimeInterval} objects.
 * The comparison is only based on the start point of the {@link ITimeInterval}s
 * 
 *  @author Dennis Geesen
 *
 * @param <T> the generic type
 */
public class TimeIntervalComparator<T extends IStreamObject<? extends ITimeInterval>> implements Comparator<T> {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(T one, T two) {
		if(TimeInterval.startsBefore(one.getMetadata(), two.getMetadata())){
			return -1;
		}
        if(one.getMetadata().getStart().equals(two.getMetadata().getStart())){
        	return 0;
        }
        return 1;
	}
}
