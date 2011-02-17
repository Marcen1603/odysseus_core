/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.objecttracking.metadata;

import java.util.List;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;

public interface IApplicationTime extends IMetaAttribute, IClone {

	// writing
	public void addApplicationInterval(ITimeInterval interval);

	public void addAllApplicationIntervals(List<ITimeInterval> intervals);

	public void setApplicationIntervals(List<ITimeInterval> intervals);

	// reading
	public ITimeInterval getApplicationInterval(int pos);

	public List<ITimeInterval> getAllApplicationTimeIntervals();

	// removing
	public void removeApplicationInterval(ITimeInterval interval);

	public ITimeInterval removeApplicationInterval(int pos);

	public void clearApplicationIntervals();

	// other
	public boolean isApplictionTimeValid();

	@Override
	public IApplicationTime clone();
}
