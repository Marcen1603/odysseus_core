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
package de.uniol.inf.is.odysseus.parser.pql.test;

import java.util.Collections;
import java.util.Set;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter;

public class MuhBuilder extends AbstractOperatorBuilder {

	public MuhBuilder() {
		super(0, 5);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		MuhOperator op = new MuhOperator();
		return op;
	}

	@Override
	public Set<IParameter<?>> getParameters() {
		return Collections.emptySet();
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

}
