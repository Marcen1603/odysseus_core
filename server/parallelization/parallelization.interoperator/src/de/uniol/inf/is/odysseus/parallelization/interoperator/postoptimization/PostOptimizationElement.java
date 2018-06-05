/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.parallelization.interoperator.postoptimization;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;

/**
 * this element stores information needed in post optimization
 * 
 * @author ChrisToenjesDeye
 *
 */
public class PostOptimizationElement {
	private int degreeOfParallelization;
	private UnionAO startOperator;
	private AbstractStaticFragmentAO endOperator;
	private boolean allowsModificationAfterUnion;

	public AbstractStaticFragmentAO getEndOperator() {
		return endOperator;
	}

	public void setEndOperator(AbstractStaticFragmentAO endOperator) {
		this.endOperator = endOperator;
	}

	public UnionAO getStartOperator() {
		return startOperator;
	}

	public void setStartOperator(UnionAO startOperator) {
		this.startOperator = startOperator;
	}

	public int getDegreeOfParallelization() {
		return degreeOfParallelization;
	}

	public void setDegreeOfParallelization(int degreeOfParallelization) {
		this.degreeOfParallelization = degreeOfParallelization;
	}

	public void setAllowsModificationAfterUnion(
			boolean allowsModificationAfterUnion) {
		this.allowsModificationAfterUnion = allowsModificationAfterUnion;

	}

	public boolean allowsModificationAfterUnion() {
		return this.allowsModificationAfterUnion;
	}
}
