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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.collection.FESortedPair;
import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class RelationalGroupProcessor<T extends IMetaAttribute> implements IGroupProcessor<Tuple<T>, Tuple<T>>, IClone {

	Map<Object, Tuple<T>> tupleMap = null;
	List<FESortedPair<Tuple<?>, Object>> groupList = new LinkedList<>();
	Long maxId = 0L;
	int[] gRestrict = null;
	private final List<SDFAttribute> grAttribs;
	private final Map<FESortedClonablePair<SDFSchema, AggregateFunction>, Integer> aggrOutputPos = new HashMap<FESortedClonablePair<SDFSchema, AggregateFunction>, Integer>();
	private final Map<SDFAttribute, Integer> groupOutputPos = new HashMap<SDFAttribute, Integer>();
	final private SDFSchema inputSchema;
	final private SDFSchema outputSchema;
	final private Map<SDFAttribute, Map<AggregateFunction, SDFAttribute>> aggregations;
	final private boolean fast;

	public RelationalGroupProcessor(SDFSchema inputSchema, SDFSchema outputSchema,
			List<SDFAttribute> groupingAttributes, Map<SDFAttribute, Map<AggregateFunction, SDFAttribute>> aggregations,
			boolean fast) {
		super();
		this.grAttribs = groupingAttributes;
		this.inputSchema = inputSchema;
		this.outputSchema = outputSchema;
		this.aggregations = aggregations;
		this.fast = fast;
	}

	public RelationalGroupProcessor(RelationalGroupProcessor<T> other) {
		if (other.tupleMap != null) {
			this.tupleMap = new HashMap<>(other.tupleMap);
		}
		if (other.groupList != null) {
			this.groupList = new ArrayList<>(other.groupList);
		}
		this.maxId = other.maxId;
		if (other.gRestrict != null) {
			this.gRestrict = new int[other.gRestrict.length];
			System.arraycopy(other.gRestrict, 0, this.gRestrict, 0, other.gRestrict.length);
		}
		this.grAttribs = new ArrayList<>(other.grAttribs);
		this.aggrOutputPos.putAll(other.aggrOutputPos);
		this.groupOutputPos.putAll(other.groupOutputPos);
		this.inputSchema = other.inputSchema;
		this.outputSchema = other.outputSchema;
		this.aggregations = new HashMap<>(other.aggregations);
		this.fast = other.fast;
	}

	@Override
	public Long getAscendingGroupID(Tuple<T> elem) {
		if (gRestrict == null || gRestrict.length == 0)
			return Long.valueOf(0);

		Long hash;

		Tuple<T> gTuple = getGroupingPart(elem);
		FESortedPair<Tuple<?>, Object> p = new FESortedPair<Tuple<?>, Object>(gTuple, maxId);
		// Add new value sorted
		int pos = Collections.binarySearch(groupList, p);
		// System.err.println(pos + " for " + p + " in List " +
		// groupList);
		if (pos < 0) { // Element not found in list
			int insert = (-1) * pos - 1;
			groupList.add(insert, p);
			hash = maxId;
			// System.err.println("Created a new group "+hash+" for
			// "+gTuple);
			// if (hash >= 1955){
			// System.err.println("List "+groupList);
			// }
			maxId++;
		} else {
			hash = (Long) groupList.get(pos).getE2();
		}
		tupleMap.put(hash, gTuple);
		return hash;
	}

	@Override
	public Object getGroupID(Tuple<T> elem) {
		// if there are no group attributes, value is 0
		if (gRestrict == null || gRestrict.length == 0)
			return Long.valueOf(0);

		Object groupKey;
		Tuple<T> restrict = null;
		
		if (gRestrict.length == 1) {
			groupKey = elem.getAttribute(gRestrict[0]);
		}else{
			 restrict = elem.restrict(gRestrict, true);
			groupKey = Arrays.asList(restrict.getAttributes());
		}
		if (!tupleMap.containsKey(groupKey)){
			// In case of single attribute, this var could not be initialized
			if (restrict == null){
				restrict = elem.restrict(gRestrict, true);
			}
			tupleMap.put(groupKey, restrict);
		}
		return groupKey;

		// // Fast version uses hash code of tuple. Warning: This may not always
		// be
		// // correct!
		// if (fast) {
		// hash = elem.restrictedHashCode(gRestrict);
		// if (!tupleMap.containsKey(hash)) {
		// Tuple<T> gTuple = getGroupingPart(elem);
		// tupleMap.put(hash, gTuple);
		// // System.err.println("Created a new group "+hash+" for
		// // "+gTuple+" "+tupleMap.size());
		// // if (tupleMap.size() >= 1955){
		// // System.err.println("List "+tupleMap);
		// // }
		// }
		// } else {
		// Tuple<T> gTuple = getGroupingPart(elem);
		// FESortedPair<Tuple<?>, Long> p = new FESortedPair<Tuple<?>,
		// Long>(gTuple, maxId);
		// // Add new value sorted
		// int pos = Collections.binarySearch(groupList, p);
		// // System.err.println(pos + " for " + p + " in List " +
		// // groupList);
		// if (pos < 0) { // Element not found in list
		// int insert = (-1) * pos - 1;
		// groupList.add(insert, p);
		// hash = maxId;
		// // System.err.println("Created a new group "+hash+" for
		// // "+gTuple);
		// // if (hash >= 1955){
		// // System.err.println("List "+groupList);
		// // }
		// maxId++;
		// } else if (pos >= 0) {
		// hash = groupList.get(pos).getE2();
		// }
		// tupleMap.put(hash, gTuple);
		//
		// }
		// return hash;
	}

	@Override
	public void setGroup(Object id, Tuple<T> elem) {
		if (!tupleMap.containsKey(id)) {
			Tuple<T> gTuple = getGroupingPart(elem);
			tupleMap.put(id, gTuple);
		}
	}

	@Override
	public Tuple<T> getGroupingPart(Tuple<T> elem) {
		return elem.restrict(gRestrict, true);
	}

	@Override
	public void init() {
		if (grAttribs != null && grAttribs.size() > 0) {
			gRestrict = new int[grAttribs.size()];
			for (int i = 0; i < grAttribs.size(); i++) {
				gRestrict[i] = inputSchema.indexOf(grAttribs.get(i));
			}
		}
		maxId = 0L;
		tupleMap = new HashMap<>();
	}

	protected int getOutputPos(FESortedClonablePair<SDFSchema, AggregateFunction> p) {
		Integer pos = aggrOutputPos.get(p);
		if (pos == null) {
			Map<AggregateFunction, SDFAttribute> funcs = aggregations.get(p.getE1());
			SDFAttribute outAttr = funcs.get(p.getE2());
			pos = getOutputSchema().indexOf(outAttr);
			aggrOutputPos.put(p, pos);
		}
		return pos;
	}

	private int getOutputPos(SDFAttribute attribute) {
		Integer pos = groupOutputPos.get(attribute);
		if (pos == null) {
			pos = getOutputSchema().indexOf(attribute);
			groupOutputPos.put(attribute, pos);
		}
		return pos;
	}

	@Override
	public Tuple<T> createOutputElement(Object groupID, PairMap<SDFSchema, AggregateFunction, Tuple<T>, ?> r) {
		Tuple<T> returnTuple = new Tuple<T>(getOutputSchema().size(), false);

		// in r stecken alle Aggregate drin
		// notwendig: Finde die Ziel-Position in dem returnTuple
		// ermittelt sich aus dem Attribute und der Aggregatfunktio
		for (Entry<FESortedClonablePair<SDFSchema, AggregateFunction>, Tuple<T>> e : r.entrySet()) {
			int pos = getOutputPos(e.getKey());
			returnTuple.setAttribute(pos, e.getValue().getAttribute(0));

		}

		// Jetzt die Gruppierungsattribute
		addGroupingAttributes(groupID, returnTuple);
		return returnTuple;
	}

	@Override
	public Tuple<T> createOutputElement2(Object groupID,
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<Tuple<T>>, ?> r) {
		Tuple<T> returnTuple = new Tuple<T>(getOutputSchema().size(), false);

		// in r stecken alle Aggregate drin
		// notwendig: Finde die Ziel-Position in dem returnTuple
		// ermittelt sich aus dem Attribute und der Aggregatfunktio
		for (Entry<FESortedClonablePair<SDFSchema, AggregateFunction>, IPartialAggregate<Tuple<T>>> e : r.entrySet()) {
			int pos = getOutputPos(e.getKey());
			returnTuple.setAttribute(pos, e.getValue());
		}

		addGroupingAttributes(groupID, returnTuple);
		return returnTuple;
	}

	protected void addGroupingAttributes(Object groupID, Tuple<T> returnTuple) {
		if (grAttribs.size() > 0) {
			Tuple<T> gruppAttr = tupleMap.get(groupID);
			int groupTupPos = 0;
			for (SDFAttribute ga : grAttribs) {
				int pos = getOutputPos(ga);
				returnTuple.setAttribute(pos, gruppAttr.getAttribute(groupTupPos++));
			}
		}
	}

	@Override
	public String toGroupString(Tuple<T> elem) {
		return elem.toString(gRestrict);
	}

	/**
	 * @return the outputSchema
	 */
	protected SDFSchema getOutputSchema() {
		return outputSchema;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aggrOutputPos == null) ? 0 : aggrOutputPos.hashCode());
		result = prime * result + ((aggregations == null) ? 0 : aggregations.hashCode());
		result = prime * result + (fast ? 1231 : 1237);
		result = prime * result + ((grAttribs == null) ? 0 : grAttribs.hashCode());
		result = prime * result + ((groupOutputPos == null) ? 0 : groupOutputPos.hashCode());
		result = prime * result + ((inputSchema == null) ? 0 : inputSchema.hashCode());
		result = prime * result + ((getOutputSchema() == null) ? 0 : getOutputSchema().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		RelationalGroupProcessor other = (RelationalGroupProcessor) obj;
		if (aggrOutputPos == null) {
			if (other.aggrOutputPos != null)
				return false;
		} else if (!aggrOutputPos.equals(other.aggrOutputPos))
			return false;
		if (aggregations == null) {
			if (other.aggregations != null)
				return false;
		} else if (!aggregations.equals(other.aggregations))
			return false;
		if (fast != other.fast)
			return false;
		if (grAttribs == null) {
			if (other.grAttribs != null)
				return false;
		} else if (!grAttribs.equals(other.grAttribs))
			return false;
		if (groupOutputPos == null) {
			if (other.groupOutputPos != null)
				return false;
		} else if (!groupOutputPos.equals(other.groupOutputPos))
			return false;
		if (inputSchema == null) {
			if (other.inputSchema != null)
				return false;
		} else if (!inputSchema.equals(other.inputSchema))
			return false;
		if (getOutputSchema() == null) {
			if (other.getOutputSchema() != null)
				return false;
		} else if (!getOutputSchema().equals(other.getOutputSchema()))
			return false;
		return true;
	}

	@Override
	public IGroupProcessor<Tuple<T>, Tuple<T>> clone() {
		return new RelationalGroupProcessor<T>(this);
	}

}
