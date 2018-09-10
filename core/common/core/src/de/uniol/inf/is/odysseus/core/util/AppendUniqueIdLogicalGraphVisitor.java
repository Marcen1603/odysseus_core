/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.util;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;

/**
 * @author Dennis Nowak
 *
 */
public class AppendUniqueIdLogicalGraphVisitor<T extends ILogicalOperator> implements IGraphNodeVisitor<T, List<T>> {

	private String appendix;
	private List<T> changedOperators = new ArrayList<>();

	public AppendUniqueIdLogicalGraphVisitor(String appendix) {
		this.appendix = appendix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor#nodeAction(java.lang
	 * .Object)
	 */
	@Override
	public void nodeAction(T node) {
		if (node.getUniqueIdentifier() != null && !changedOperators.contains(node)) {
			node.setUniqueIdentifier(node.getUniqueIdentifier() + this.appendix);
			this.changedOperators.add(node);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor#
	 * beforeFromSinkToSourceAction(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void beforeFromSinkToSourceAction(T sink, T source) {
		// noop
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor#
	 * afterFromSinkToSourceAction(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void afterFromSinkToSourceAction(T sink, T source) {
		// noop
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor#
	 * beforeFromSourceToSinkAction(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void beforeFromSourceToSinkAction(T source, T sink) {
		// noop

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor#
	 * afterFromSourceToSinkAction(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void afterFromSourceToSinkAction(T source, T sink) {
		// noop

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor#getResult()
	 */
	@Override
	public List<T> getResult() {
		return this.changedOperators;
	}

}
