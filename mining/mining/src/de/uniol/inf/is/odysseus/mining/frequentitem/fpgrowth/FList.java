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
package de.uniol.inf.is.odysseus.mining.frequentitem.fpgrowth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * @author Dennis Geesen
 * 
 */
public class FList<M extends IMetaAttribute> {

	//private Map<Tuple<M>, Integer> counts = new LinkedHashMap<Tuple<M>, Integer>();
	private ArrayList<Tuple<M>> pointers = new ArrayList<Tuple<M>>();
	private ArrayList<Integer> counts = new ArrayList<Integer>(); 

	public void insertTuple(Tuple<M> tuple) {
		//System.out.println("INSERT: "+tuple);
		int count = 1;
		int index = this.pointers.indexOf(tuple);
		if(index>=0){
			count = this.counts.get(index) + 1;
			this.counts.set(index, count);
		}else{
			this.pointers.add(tuple);
			this.counts.add(1);
		}		
	}

	public List<Pair<Tuple<M>, Integer>> getSortedList(int minSupport) {
		List<Pair<Tuple<M>, Integer>> fList = new ArrayList<Pair<Tuple<M>, Integer>>();
		for(int i=0;i<pointers.size();i++){
			int support = this.counts.get(i);
			if(support >= minSupport){
				fList.add(new Pair<Tuple<M>, Integer>(this.pointers.get(i), support));
			}
		}
		
		Collections.sort(fList, new CountDescendingPairComparator<Tuple<M>, Integer>());		
		return fList; 				
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "";
		String sep = "";
		for(int i=0;i<this.counts.size();i++){
			s = s+sep+this.pointers.get(i)+": "+this.counts.get(i);
			sep = " | ";
		}
		return s;
	}
	

}