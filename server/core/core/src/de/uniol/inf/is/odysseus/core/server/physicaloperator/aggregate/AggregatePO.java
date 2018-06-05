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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.core.collection.PairMap;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IAggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IEvaluator;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IInitializer;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IMerger;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;

public class AggregatePO<M extends IMetaAttribute, R extends IStreamObject<M>, W extends IStreamObject<M>>
		extends AbstractPipe<R, W> {

	// PartialAggregate functions for different combinations of attributes and
	// aggregations functions
	private Map<FESortedClonablePair<SDFSchema, AggregateFunction>, IAggregateFunction<R, W>> aggregateFunctions = new HashMap<>();

	//
	private Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations = null;

	private SDFSchema inputSchema = null;
	private IGroupProcessor<R, W> groupProcessor;

	private final SDFSchema internalOutputSchema;

	private final List<SDFAttribute> groupingAttributes;
	private final boolean fastGrouping;

	private final Map<Object, PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, M>> groups = new HashMap<>();

	/**
	 * When combining different elements the meta data must be merged. Because
	 * the operator does not know, which meta data is used, the metadataMerge
	 * function is injection at transformation time
	 */
	final protected IMetadataMergeFunction<M> metadataMerge;

	public IGroupProcessor<R, W> getGroupProcessor() {
		return groupProcessor;
	}

	public void setGroupProcessor(IGroupProcessor<R, W> groupProcessor) {
		this.groupProcessor = groupProcessor;
	}

	public AggregatePO(SDFSchema inputSchema, SDFSchema outputSchema, List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations, boolean fastGrouping,
			IMetadataMergeFunction<M> mf) {
		this.inputSchema = inputSchema;
		this.internalOutputSchema = outputSchema;
		this.aggregations = aggregations;
		this.groupingAttributes = groupingAttributes;
		this.fastGrouping = fastGrouping;
		this.metadataMerge = mf;
	}

	public SDFSchema getInputSchema() {
		return inputSchema;
	}

	public Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> getAggregations() {
		return Collections.unmodifiableMap(aggregations);
	}

	public List<SDFAttribute> getGroupingAttribute() {
		return Collections.unmodifiableList(groupingAttributes);
	}

	public SDFSchema getInternalOutputSchema() {
		return internalOutputSchema;
	}

	public void setAggregateFunction(FESortedClonablePair<SDFSchema, AggregateFunction> p, IAggregateFunction<R, W> i) {
		aggregateFunctions.put(p, i);
	}

	public IAggregateFunction<R, W> getAggregateFunction(FESortedClonablePair<SDFSchema, AggregateFunction> p) {
		return aggregateFunctions.get(p);
	}

	protected Map<FESortedClonablePair<SDFSchema, AggregateFunction>, IAggregateFunction<R, W>> getAllAggregateFunctions() {
		return aggregateFunctions;
	}

	// Erzeugen der initialen Map f�r alle
	// Attribut/Aggregierungsfunktion-Paare,
	// abgebildet auf Partielle Aggregate
	protected PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, M> calcInit(R element) {
		PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, M> ret = new PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, M>();
		// Hier ggf. direkt die Liste der zu aggregierenden Attribute auslesen
		for (SDFSchema attrList : aggregations.keySet()) {
			if (SDFSchema.subset(attrList, inputSchema)) {
				Map<AggregateFunction, SDFAttribute> funcs = aggregations.get(attrList);
				for (Entry<AggregateFunction, SDFAttribute> e : funcs.entrySet()) {
					// Achtung! Das Eingabeattribut, nicht das Ausgabeattribut
					FESortedClonablePair<SDFSchema, AggregateFunction> toFind = new FESortedClonablePair<SDFSchema, AggregateFunction>(
							attrList, e.getKey());
					IInitializer<R> initFktn = getAggregateFunction(toFind);
					if (initFktn == null) {
						throw new RuntimeException("Aggregation runtime error ");
					}
					IPartialAggregate<R> pa = initFktn.doInit(element);
					ret.put(attrList, e.getKey(), pa);
				}
			}
		}
		return ret;
	}

	// Mergen aller Elemente in der toMerge-Map mit dem element vom Typ R (mit
	// dem passenden Merger)
	protected synchronized PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, M> calcMerge(
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, M> toMerge, R element, boolean createNew) {

		PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, M> ret;
		if (createNew) {
			ret = new PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, M>();
		} else {
			ret = toMerge;
		}

		// Jedes Element in toMerge mit element mergen
		for (Entry<FESortedClonablePair<SDFSchema, AggregateFunction>, IPartialAggregate<R>> e : toMerge.entrySet()) {
			IMerger<R> mf = getAggregateFunction(e.getKey());
			IPartialAggregate<R> pa = mf.doMerge(e.getValue(), element, createNew);
			if (createNew) {
				ret.put(e.getKey(), pa);
			}
		}

		return ret;
	}

	// Auswerten aller Elemente in der toEval map (mit dem passenden Evaluator)
	protected PairMap<SDFSchema, AggregateFunction, W, M> calcEval(
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, M> toEval, boolean clearPartialAggregate,
			Boolean dirtyFlag) {
		PairMap<SDFSchema, AggregateFunction, W, M> ret = new PairMap<SDFSchema, AggregateFunction, W, M>();
		for (Entry<FESortedClonablePair<SDFSchema, AggregateFunction>, IPartialAggregate<R>> e : toEval.entrySet()) {
			IEvaluator<R, W> eval = getAggregateFunction(e.getKey());
			W value = eval.doEvaluate(e.getValue());
			ret.put(e.getKey(), value);
			if (clearPartialAggregate) {
				e.getValue().clear();
			}
		}
		return ret;
	}

	protected PairMap<SDFSchema, AggregateFunction, W, M> calcEval(
			PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, M> toEval, boolean clearPartialAggregate) {
		return calcEval(toEval, clearPartialAggregate, null);
	}

	public IInitializer<R> getInitFunction(SDFAttribute outAttribute) {
		FESortedClonablePair<SDFSchema, AggregateFunction> pair = null;
		for (Entry<SDFSchema, Map<AggregateFunction, SDFAttribute>> vs : aggregations.entrySet()) {
			if (pair != null) {
				break;
			}
			for (Entry<AggregateFunction, SDFAttribute> v : vs.getValue().entrySet()) {
				if (v.getValue().equals(outAttribute)) {
					pair = new FESortedClonablePair<SDFSchema, AggregateFunction>(vs.getKey(), v.getKey());
					break;
				}
			}
		}

		if (pair != null) {
			return getAggregateFunction(pair);
		} else {
			return null;
		}

	}

	/**
	 * The aggregation creates always a new element. So no input data needs to
	 * be cloned.
	 */
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof AggregatePO)) {
			return false;
		}

		@SuppressWarnings("unchecked")
		AggregatePO<M, R, W> apo = (AggregatePO<M, R, W>) ipo;

		if ((this.groupProcessor == null && apo.groupProcessor != null)
				|| (this.groupProcessor != null && apo.groupProcessor == null)) {
			return false;
		}

		if (((this.groupProcessor == null && apo.groupProcessor == null)
				|| this.groupProcessor.equals(apo.groupProcessor)) && this.inputSchema.compareTo(apo.inputSchema) == 0
				&& this.internalOutputSchema.compareTo(apo.internalOutputSchema) == 0
				&& this.groupingAttributes.size() == apo.groupingAttributes.size()) {

			// Vergleich der groupingAttributes
			for (SDFAttribute a : this.groupingAttributes) {
				boolean foundmatch = false;
				for (SDFAttribute b : apo.groupingAttributes) {
					if (a.compareTo(b) == 0) {
						foundmatch = true;
					}
				}
				if (!foundmatch) {
					return false;
				}
			}

			Set<SDFSchema> s1 = this.aggregations.keySet();
			Set<SDFSchema> s2 = apo.aggregations.keySet();
			if (s1.size() != s2.size()) {
				return false;
			}

			for (SDFSchema a : s1) {
				boolean foundmatch = false;
				for (SDFSchema b : s2) {
					// Gleichen Key gefunden
					if (a.compareTo(b) == 0) {
						// Vergleichen der Werte
						Map<AggregateFunction, SDFAttribute> m1 = this.aggregations.get(a);
						Map<AggregateFunction, SDFAttribute> m2 = apo.aggregations.get(b);
						Set<AggregateFunction> k1 = m1.keySet();
						Set<AggregateFunction> k2 = m2.keySet();
						if (k1.size() != k2.size()) {
							return false;
						}
						for (AggregateFunction af1 : k1) {
							for (AggregateFunction af2 : k2) {
								if (af1.equals(af2)) {
									if (m1.get(af1).compareTo(m2.get(af2)) == 0) {
										foundmatch = true;
									}
								}
							}
						}

					}
				}
				if (!foundmatch) {
					return false;
				}
			}
			return true;
		}

		return false;
	}

	public boolean isFastGrouping() {
		return fastGrouping;
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// Nothing to do in this case
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		IGroupProcessor<R, W> g = getGroupProcessor();
		synchronized (groups) {
			g.init();
			groups.clear();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(R object, int port) {
		IGroupProcessor<R, W> g = getGroupProcessor();
		Object groupID;
		// Determine group ID from input object
		groupID = g.getGroupID(object);
		PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, M> paList = groups.get(groupID);
		if (paList == null) {
			// Create init version of partial aggregates
			paList = calcInit(object);
			if (object.getMetadata() != null) {
				paList.setMetadata((M) object.getMetadata().clone());
			}
			groups.put(groupID, paList);
		} else {
			// Merge current object with partial aggregates
			calcMerge(paList, object, false);
			if (metadataMerge != null && object.getMetadata() != null) {
				M newMeta = metadataMerge.mergeMetadata(paList.getMetadata(), object.getMetadata());
				paList.setMetadata(newMeta);
			}
		}
	}

	@Override
	protected void process_done() {
		createOutput();
	}

	private void createOutput() {
		IGroupProcessor<R, W> g = getGroupProcessor();
		for (Entry<Object, PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, M>> entry : groups.entrySet()) {
			PairMap<SDFSchema, AggregateFunction, W, M> result = calcEval(entry.getValue(), false);
			W out = g.createOutputElement(entry.getKey(), result);
			M metadata = entry.getValue().getMetadata();
			out.setMetadata(metadata);
			transfer(out);
		}
		groups.clear();
	}

	@Override
	protected void process_close() {
		createOutput();
		super.process_close();
	}
}
