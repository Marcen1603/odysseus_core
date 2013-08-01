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
package de.uniol.inf.is.odysseus.core.server.util;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;

public class RemoveIdLogicalGraphVisitor<T extends ILogicalOperator>
		implements IGraphNodeVisitor<T, T> {

	private IDataDictionaryWritable dd;
	private ISession caller;

	public RemoveIdLogicalGraphVisitor(IDataDictionaryWritable dd, ISession caller) {
		this.dd = dd;
		this.caller = caller;
	}
	
	@Override
	public void afterFromSinkToSourceAction(T sink, T source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterFromSourceToSinkAction(T source, T sink) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeFromSinkToSourceAction(T sink, T source) {
		// TODO Auto-generated method stub

	}

	@Override
	public void beforeFromSourceToSinkAction(T source, T sink) {
		// TODO Auto-generated method stub

	}

	@Override
	public T getResult() {
		return null;
	}

	@Override
	public void nodeAction(T op) {
		if (op.getUniqueIdentifier() != null) {
			dd.removeOperator(dd.createUserUri(op.getUniqueIdentifier(), caller));
		}	
	}

}
