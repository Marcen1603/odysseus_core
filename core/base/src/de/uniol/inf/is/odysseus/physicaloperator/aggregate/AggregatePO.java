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
package de.uniol.inf.is.odysseus.physicaloperator.aggregate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.collection.FESortedClonablePair;
import de.uniol.inf.is.odysseus.collection.PairMap;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.AggregateFunction;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IEvaluator;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IInitializer;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IMerger;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

abstract public class AggregatePO<M extends IMetaAttribute, R extends IMetaAttributeContainer<? extends M> & IClone, W extends IClone>
        extends AbstractPipe<R, W> {

	// PartialAggregate functions for different combinations of attributes and aggregations functions 
    private Map<FESortedClonablePair<SDFAttributeList, AggregateFunction>, IInitializer<R>> init = new HashMap<FESortedClonablePair<SDFAttributeList, AggregateFunction>, IInitializer<R>>();
    private Map<FESortedClonablePair<SDFAttributeList, AggregateFunction>, IMerger<R>> merger = new HashMap<FESortedClonablePair<SDFAttributeList, AggregateFunction>, IMerger<R>>();
    private Map<FESortedClonablePair<SDFAttributeList, AggregateFunction>, IEvaluator<R, W>> eval = new HashMap<FESortedClonablePair<SDFAttributeList, AggregateFunction>, IEvaluator<R, W>>();

    // 
    private Map<SDFAttributeList, Map<AggregateFunction, SDFAttribute>> aggregations = null;

    private SDFAttributeList inputSchema = null;
    private IGroupProcessor<R, W> groupProcessor;

    private final SDFAttributeList outputSchema;

    private final List<SDFAttribute> groupingAttributes;

    // private AggregateAO algebraOp;

    public IGroupProcessor<R, W> getGroupProcessor() {
        return groupProcessor;
    }

    public void setGroupProcessor(IGroupProcessor<R, W> groupProcessor) {
        this.groupProcessor = groupProcessor;
    }

    public AggregatePO(
            SDFAttributeList inputSchema,
            SDFAttributeList outputSchema,
            List<SDFAttribute> groupingAttributes,
            Map<SDFAttributeList, Map<AggregateFunction, SDFAttribute>> aggregations) {
        this.inputSchema = inputSchema;
        this.outputSchema = outputSchema;
        this.aggregations = aggregations;
        this.groupingAttributes = groupingAttributes;
    }

    public SDFAttributeList getInputSchema() {
        return inputSchema;
    }

    public Map<SDFAttributeList, Map<AggregateFunction, SDFAttribute>> getAggregations() {
        return Collections.unmodifiableMap(aggregations);
    }

    public List<SDFAttribute> getGroupingAttribute() {
        return Collections.unmodifiableList(groupingAttributes);
    }

    @Override
    public SDFAttributeList getOutputSchema() {
        return outputSchema;
    }

    public AggregatePO(AggregatePO<M, R, W> agg) {
        init = new HashMap<FESortedClonablePair<SDFAttributeList, AggregateFunction>, IInitializer<R>>(
                agg.init);
        merger = new HashMap<FESortedClonablePair<SDFAttributeList, AggregateFunction>, IMerger<R>>(
                agg.merger);
        eval = new HashMap<FESortedClonablePair<SDFAttributeList, AggregateFunction>, IEvaluator<R, W>>(
                agg.eval);
        this.inputSchema = agg.inputSchema;
        this.outputSchema = agg.outputSchema;
        this.groupingAttributes = agg.groupingAttributes;
        this.aggregations = agg.aggregations;
        this.groupProcessor = agg.groupProcessor;
    }

    public void setInitFunction(
            FESortedClonablePair<SDFAttributeList, AggregateFunction> p,
            IInitializer<R> i) {
        init.put(p, i);
    }

    public void setMergeFunction(
            FESortedClonablePair<SDFAttributeList, AggregateFunction> p, IMerger<R> m) {
        merger.put(p, m);
    }

    public void setEvalFunction(
            FESortedClonablePair<SDFAttributeList, AggregateFunction> p, IEvaluator<R,W> e) {
        eval.put(p, e);
    }

    protected IEvaluator<R, W> getEvalFunction(
            FESortedClonablePair<SDFAttributeList, AggregateFunction> p) {
        return eval.get(p);
    }

    protected IMerger<R> getMergeFunction(
            FESortedClonablePair<SDFAttributeList, AggregateFunction> p) {
        return merger.get(p);
    }

    protected IInitializer<R> getInitFunction(
            FESortedClonablePair<SDFAttributeList, AggregateFunction> p) {
        return init.get(p);
    }

    // Erzeugen der initialen Map fï¿½r alle
    // Attribut/Aggregierungsfunktion-Paare,
    // abgebildet auf Partielle Aggregate
    protected PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, M> calcInit(
            R element) {
        PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, M> ret = new PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, M>();
        // Hier ggf. direkt die Liste der zu aggregierenden Attribute auslesen
        for (SDFAttributeList attrList : aggregations.keySet()) {
            if (SDFAttributeList.subset(attrList, inputSchema)) {
                Map<AggregateFunction, SDFAttribute> funcs = aggregations
                        .get(attrList);
                for (Entry<AggregateFunction, SDFAttribute> e : funcs
                        .entrySet()) {
                    // Achtung! Das Eingabeattribut, nicht das Ausgabeattribut
                    IPartialAggregate<R> pa = getInitFunction(
                            new FESortedClonablePair<SDFAttributeList, AggregateFunction>(
                                    attrList, e.getKey())).init(element);
                    ret.put(attrList, e.getKey(), pa);
                }
            }
        }
        return ret;
    }

    // Mergen aller Elemente in der toMerge-Map mit dem element vom Typ R (mit
    // dem passenden Merger)
    protected synchronized PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, M> calcMerge(
            PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, M> toMerge,
            R element) {
        PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, M> ret = new PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, M>();

        // Jedes Element in toMerge mit element mergen
        for (Entry<FESortedClonablePair<SDFAttributeList, AggregateFunction>, IPartialAggregate<R>> e : toMerge
                .entrySet()) {
            IMerger<R> mf = getMergeFunction(e.getKey());
            IPartialAggregate<R> pa = mf.merge(e.getValue(), element, true);
            ret.put(e.getKey(), pa);
        }

        return ret;
    }

    // Auswerten aller Elemente in der toEval map (mit dem passenden Evaluator)
    protected PairMap<SDFAttributeList, AggregateFunction, W, M> calcEval(
            PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<R>, M> toEval) {
        PairMap<SDFAttributeList, AggregateFunction, W, M> ret = new PairMap<SDFAttributeList, AggregateFunction, W, M>();
        for (Entry<FESortedClonablePair<SDFAttributeList, AggregateFunction>, IPartialAggregate<R>> e : toEval
                .entrySet()) {
            IEvaluator<R, W> eval = getEvalFunction(e.getKey());
            W value = eval.evaluate(e.getValue());
            ret.put(e.getKey(), value);
        }
        return ret;
    }
    
    @Override
    public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
    	if(!(ipo instanceof AggregatePO)) {
    		return false;
    	}
		
		@SuppressWarnings("unchecked")
		AggregatePO<M,R, W> apo = (AggregatePO<M,R, W>) ipo;
		
		// Falls die Operatoren verschiedene Quellen haben, wird false zurück gegeben
		if(!this.hasSameSources(ipo)) {
			return false;
		}
		if(this.groupProcessor.getClass().toString().equals(apo.groupProcessor.getClass().toString())
				&& this.inputSchema.compareTo(apo.inputSchema) == 0
				&& this.outputSchema.compareTo(apo.outputSchema) == 0
				&& this.groupingAttributes.size() == apo.groupingAttributes.size()) {
			
			// Vergleich der groupingAttributes
			for(SDFAttribute a : this.groupingAttributes) {
				boolean foundmatch = false;
				for(SDFAttribute b : apo.groupingAttributes) {
					if(a.compareTo(b) == 0) {
						foundmatch = true;
					}
				}
				if(!foundmatch) {
					return false;
				}
			}
			
			
			Set<SDFAttributeList> s1 = this.aggregations.keySet();
			Set<SDFAttributeList> s2 = apo.aggregations.keySet();
			if(s1.size() != s2.size()) {
				return false;
			}
			
			for(SDFAttributeList a : s1) {
				boolean foundmatch = false;
				for(SDFAttributeList b : s2) {
					// Gleichen Key gefunden
					if(a.compareTo(b) == 0) {
						// Vergleichen der Werte
						Map<AggregateFunction, SDFAttribute> m1 = this.aggregations.get(a);
						Map<AggregateFunction, SDFAttribute> m2 = apo.aggregations.get(b);
						Set<AggregateFunction> k1 = m1.keySet();
						Set<AggregateFunction> k2 = m2.keySet();
						if(k1.size() != k2.size()) {
							return false;
						}
						for(AggregateFunction af1 : k1) {
							for(AggregateFunction af2 : k2) {
								if(af1.compareTo(af2) == 0) {
									if(m1.get(af1).compareTo(m2.get(af2)) == 0) {
										foundmatch = true;
									}
								}
							}
						}
						
					}
				}
				if(!foundmatch) {
					return false;
				}
			}
			return true;
		}
    	
    	return false;
    }
}
