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
package de.uniol.inf.is.odysseus.parallelization.interoperator.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractStaticFragmentAO;

/**
 * this class contains informations about transformation and is used in post
 * optimization if the result is SUCCESS
 * 
 * @author ChrisToenjesDeye
 *
 */
public class TransformationResult {

	public enum State {
		SUCCESS, FAILED;
	}

	private UUID uniqueIdentifier = UUID.randomUUID();
	private State state;
	private List<AbstractStaticFragmentAO> fragmentOperators = new ArrayList<AbstractStaticFragmentAO>();
	private UnionAO unionOperator;

	public TransformationResult(State state) {
		this.state = state;
	}

	// default is true, e.g. if partial aggregates are used, a modification
	// after union is not possible
	private boolean allowsModificationAfterUnion = true;

	/**
	 * validates if this object is valid
	 * 
	 * @return isValid
	 */
	public boolean validate() {
		if (!fragmentOperators.isEmpty() && unionOperator != null
				&& state != null) {
			return true;
		}
		return false;
	}

	public boolean allowsModificationAfterUnion() {
		return allowsModificationAfterUnion;
	}

	public void setAllowsModificationAfterUnion(
			boolean allowsModificationAfterUnion) {
		this.allowsModificationAfterUnion = allowsModificationAfterUnion;
	}

	public UnionAO getUnionOperator() {
		return unionOperator;
	}

	public void setUnionOperator(UnionAO unionOperator) {
		this.unionOperator = unionOperator;
	}

	public List<AbstractStaticFragmentAO> getFragmentOperators() {
		return fragmentOperators;
	}

	public void setFragmentOperators(
			List<AbstractStaticFragmentAO> fragmentOperators) {
		this.fragmentOperators.addAll(fragmentOperators);
	}

	public void addFragmentOperator(AbstractStaticFragmentAO fragmentOperator) {
		this.fragmentOperators.add(fragmentOperator);
	}

	public UUID getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public State getState() {
		return state;
	}
	
	public void setState(State state) {
		this.state = state;
	}
}
