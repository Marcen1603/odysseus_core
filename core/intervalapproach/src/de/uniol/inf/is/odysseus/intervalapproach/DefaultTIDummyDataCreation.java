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
package de.uniol.inf.is.odysseus.intervalapproach;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;

public class DefaultTIDummyDataCreation implements IDummyDataCreationFunction<ITimeInterval, IMetaAttributeContainer<ITimeInterval>>{

	public DefaultTIDummyDataCreation(
			DefaultTIDummyDataCreation defaultTIDummyDataCreation) {
		
	}

	public DefaultTIDummyDataCreation(){}
	
	@SuppressWarnings("unchecked")
	@Override
	public IMetaAttributeContainer<ITimeInterval> createMetadata(
			IMetaAttributeContainer<ITimeInterval> source) {
		return (IMetaAttributeContainer<ITimeInterval>) source.clone();
	}

	@Override
	public boolean hasMetadata(IMetaAttributeContainer<ITimeInterval> source) {
		return true;
	}

	@Override
	public DefaultTIDummyDataCreation clone() {
		return new DefaultTIDummyDataCreation(this);
	}

}
