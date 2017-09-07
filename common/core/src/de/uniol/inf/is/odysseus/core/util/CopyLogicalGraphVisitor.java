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
package de.uniol.inf.is.odysseus.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.util.IGraphNodeVisitor;

public class CopyLogicalGraphVisitor<T extends ILogicalOperator> implements
		IGraphNodeVisitor<T, T> {

	/**
	 * Here the copies of each operator will stored.
	 */
	final MyIdentityHashMap<T, T> nodeCopies = new MyIdentityHashMap<T, T>();

	//IdentityHashMap<T, T> nodeCopies;
	
	/**
	 * This is the first node, this visitor has ever seen. This is used to
	 * return as result, the same root in the copy of the plan as passed before
	 * copying.
	 */
	T root;

	private IOperatorOwner owner;
	private List<IOperatorOwner> ownerList;

	public CopyLogicalGraphVisitor(IOperatorOwner owner) {
		this.owner = owner;
	}

	public CopyLogicalGraphVisitor(List<IOperatorOwner> ownerList) {
		this.ownerList = ownerList;
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
		// copy the logical plan
		for (Entry<T, T> entry : nodeCopies.entrySet()) {
			T original = entry.getKey();
			T copy = entry.getValue();

			// it should be enough, to copy only
			// the incoming subscriptions, since
			// for each incoming subscription there
			// also exists an outgoing in the preceding
			// operator.
			for (LogicalSubscription sub : original.getSubscribedToSource()) {
				// get the copy of each target
				@SuppressWarnings("unchecked")
				T targetCopy = this.nodeCopies.get((T)sub.getSource());
				// subscribe the target copy to the node copy
				// if its in the same plan, add copy
				// schema could be null, in this case getSchema would call getSource().getOutputSchema() but 
				// the source may not be connected. --> sub.isSchemaSet
				if (targetCopy != null) {
					copy.subscribeToSource(targetCopy, sub.getSinkInPort(),
							sub.getSourceOutPort(),  sub.getRealSchema());			
				}
			} 
		}

		return this.nodeCopies.get(this.root);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void nodeAction(T op) {
		// Copy Plan only for owners (if available)
		if (!op.hasOwner() || (owner != null && op.isOwnedBy(owner)) || (ownerList !=null && op.isOwnedByAll(ownerList))) {
			if (this.root == null) {
				this.root = op;
			}
			if (!this.nodeCopies.containsKey(op)) {
				T opCopy = (T) op.clone();
				opCopy.clearPhysicalSubscriptions();
				this.nodeCopies.put(op, opCopy);
			}
		}
	}

}

/**
 * Just a test to see, if some compiler problems in Java 8 occur from IdentityHashMap
 * This is not as fast, as the IdentityHashMap but plans are typically not so large
 * @author Marco Grawunder
 *
 */
class MyIdentityHashMap<KEY,VALUE>{

	class MyMapEntry<K,V> implements Entry<K,V>{
		
		K key;
		V value;
		
		
		
		public MyMapEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			// TODO Auto-generated method stub
			return value;
		}

		@Override
		public V setValue(V value) {
			this.value = value;
			return value;
		}
		
	}
	
	List<Entry<KEY,VALUE>> entries = new ArrayList<>();
	
	public List<Entry<KEY, VALUE>> entrySet() {
		return entries;
	}

	public void put(KEY key, VALUE value) {
		boolean found = false;
		for (Entry<KEY, VALUE> e:entries){
			if (e.getKey() == key){
				found = true;
				e.setValue(value);
			}
		}
		if (!found){
			entries.add(new MyMapEntry<KEY,VALUE>(key,value));
		}
	}

	public boolean containsKey(KEY key) {
		return get(key) != null;
	}

	public VALUE get(KEY key) {
		for (Entry<KEY, VALUE> e:entries){
			if (e.getKey() == key){
				return e.getValue();
			}
		}
		return null;
	}
	
}
