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

import java.util.HashSet;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;

/**
 * @author Merlin Wasmann
 *
 */
public class CollectOperatorPhysicalGraphVisitor<T extends IPhysicalOperator> implements IGraphNodeVisitor<T, Set<T>> {

	private final Set<Class<T>> operatorClasses;
	private final boolean addSubclasses;

	private Set<T> result;

	public CollectOperatorPhysicalGraphVisitor(Class<T> clazz, boolean addSubclasses) {
		this.result = new HashSet<T>();
		this.operatorClasses = new HashSet<Class<T>>();
		this.operatorClasses.add(clazz);
		this.addSubclasses = addSubclasses;
	}

	public CollectOperatorPhysicalGraphVisitor(Set<Class<T>> classes, boolean addSubclasses) {
		this.result = new HashSet<T>();
		this.operatorClasses = classes;
		this.addSubclasses = addSubclasses;
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
		if (this.addSubclasses) {
			for(Class<T> claz:this.operatorClasses) {
				if(claz.isAssignableFrom(node.getClass())) {
					this.result.add(node);
					return;
				}
			}
		} else {
			if (this.operatorClasses.contains(node.getClass())) {
				this.result.add(node);
			}
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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor#
	 * afterFromSinkToSourceAction(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void afterFromSinkToSourceAction(T sink, T source) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor#
	 * beforeFromSourceToSinkAction(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void beforeFromSourceToSinkAction(T source, T sink) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor#
	 * afterFromSourceToSinkAction(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void afterFromSourceToSinkAction(T source, T sink) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor#getResult()
	 */
	@Override
	public Set<T> getResult() {
		return this.result;
	}

}
