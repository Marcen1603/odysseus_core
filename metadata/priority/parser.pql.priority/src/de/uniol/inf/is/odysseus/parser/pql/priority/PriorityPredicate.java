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
package de.uniol.inf.is.odysseus.parser.pql.priority;

import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.predicate.AbstractPredicate;
import de.uniol.inf.is.odysseus.priority.IPriority;

public class PriorityPredicate extends
		AbstractPredicate<IMetaAttributeContainer<? extends IPriority>> {

	private static final long serialVersionUID = -8530604545098107300L;

	@Override
	public PriorityPredicate clone() {
		return this;
	}

	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends IPriority> input) {
		return input.getMetadata().getPriority() > 0;
	}
	

	@Override
	public boolean evaluate(IMetaAttributeContainer<? extends IPriority> left,
			IMetaAttributeContainer<? extends IPriority> right) {
		throw new UnsupportedOperationException();
	}

}
