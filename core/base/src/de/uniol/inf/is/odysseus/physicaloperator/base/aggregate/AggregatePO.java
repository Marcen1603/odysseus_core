package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.AggregateFunction;
import de.uniol.inf.is.odysseus.base.FESortedPair;
import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.base.PairMap;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IEvaluator;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IInitializer;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IMerger;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

abstract public class AggregatePO<M extends IMetaAttribute, R extends IMetaAttributeContainer<? extends M>, A extends IClone>
        extends AbstractPipe<R, R> {

    private Map<FESortedPair<SDFAttributeList, AggregateFunction>, IInitializer<A>> init = new HashMap<FESortedPair<SDFAttributeList, AggregateFunction>, IInitializer<A>>();

    private Map<FESortedPair<SDFAttributeList, AggregateFunction>, IMerger<A>> merger = new HashMap<FESortedPair<SDFAttributeList, AggregateFunction>, IMerger<A>>();

    private Map<FESortedPair<SDFAttributeList, AggregateFunction>, IEvaluator<A>> eval = new HashMap<FESortedPair<SDFAttributeList, AggregateFunction>, IEvaluator<A>>();

    private SDFAttributeList inputSchema = null;
    private Map<SDFAttributeList, Map<AggregateFunction, SDFAttribute>> aggregations = null;

    private GroupingHelper<R> groupingHelper;

    private final SDFAttributeList outputSchema;

    private final List<SDFAttribute> groupingAttributes;

    // private AggregateAO algebraOp;

    public GroupingHelper<R> getGroupingHelper() {
        return groupingHelper;
    }

    public void setGroupingHelper(GroupingHelper<R> groupingHelper) {
        this.groupingHelper = groupingHelper;
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
        return groupingAttributes;
    }

    @Override
    public SDFAttributeList getOutputSchema() {
        return outputSchema;
    }

    public AggregatePO(AggregatePO<M, R, A> agg) {
        init = new HashMap<FESortedPair<SDFAttributeList, AggregateFunction>, IInitializer<A>>(
                agg.init);
        merger = new HashMap<FESortedPair<SDFAttributeList, AggregateFunction>, IMerger<A>>(
                agg.merger);
        eval = new HashMap<FESortedPair<SDFAttributeList, AggregateFunction>, IEvaluator<A>>(
                agg.eval);
        this.inputSchema = agg.inputSchema;
        this.outputSchema = agg.outputSchema;
        this.groupingAttributes = agg.groupingAttributes;
        this.aggregations = agg.aggregations;
        this.groupingHelper = agg.groupingHelper;
    }

    public void setAggregationFunction(
            FESortedPair<SDFAttributeList, AggregateFunction> p,
            IInitializer<A> i) {
        init.put(p, i);
    }

    public void setAggregationFunction(
            FESortedPair<SDFAttributeList, AggregateFunction> p, IMerger<A> m) {
        merger.put(p, m);
    }

    public void setAggregationFunction(
            FESortedPair<SDFAttributeList, AggregateFunction> p, IEvaluator<A> e) {
        eval.put(p, e);
    }

    protected IEvaluator<A> getEvalFunction(
            FESortedPair<SDFAttributeList, AggregateFunction> p) {
        return eval.get(p);
    }

    protected IMerger<A> getMergeFunction(
            FESortedPair<SDFAttributeList, AggregateFunction> p) {
        return merger.get(p);
    }

    protected IInitializer<A> getInitFunction(
            FESortedPair<SDFAttributeList, AggregateFunction> p) {
        return init.get(p);
    }

    // Erzeugen der initialen Map f�r alle
    // Attribut/Aggregierungsfunktion-Paare,
    // abgebildet auf Partielle Aggregate
    protected PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<A>, M> calcInit(
            A element) {
        PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<A>, M> ret = new PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<A>, M>();
        // Hier ggf. direkt die Liste der zu aggregierenden Attribute auslesen
        for (SDFAttributeList attrList : aggregations.keySet()) {
            if (SDFAttributeList.subset(attrList, inputSchema)) {
                Map<AggregateFunction, SDFAttribute> funcs = aggregations
                        .get(attrList);
                for (Entry<AggregateFunction, SDFAttribute> e : funcs
                        .entrySet()) {
                    // Achtung! Das Eingabeattribut, nicht das Ausgabeattribut
                    IPartialAggregate<A> pa = getInitFunction(
                            new FESortedPair<SDFAttributeList, AggregateFunction>(
                                    attrList, e.getKey())).init(element);
                    ret.put(attrList, e.getKey(), pa);
                }
            }
        }
        return ret;
    }

    // TODO: PairMap<...,R> gemacht. Richtig so?
    // Mergen aller Elemente in der toMerge-Map mit dem element vom Typ A (mit
    // dem passenden Merger)
    protected synchronized PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<A>, M> calcMerge(
            PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<A>, M> toMerge,
            A element) {
        PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<A>, M> ret = new PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<A>, M>();

        // Jedes Element in toMerge mit element mergen
        for (Entry<FESortedPair<SDFAttributeList, AggregateFunction>, IPartialAggregate<A>> e : toMerge
                .entrySet()) {
            IMerger<A> mf = getMergeFunction(e.getKey());
            IPartialAggregate<A> pa = mf.merge(e.getValue(), element, true);
            ret.put(e.getKey(), pa);
        }

        return ret;
    }

    // Auswerten aller Elemente in der toEval map (mit dem passenden Evaluator)
    protected PairMap<SDFAttributeList, AggregateFunction, A, M> calcEval(
            PairMap<SDFAttributeList, AggregateFunction, IPartialAggregate<A>, M> toEval) {
        PairMap<SDFAttributeList, AggregateFunction, A, M> ret = new PairMap<SDFAttributeList, AggregateFunction, A, M>();
        for (Entry<FESortedPair<SDFAttributeList, AggregateFunction>, IPartialAggregate<A>> e : toEval
                .entrySet()) {
            IEvaluator<A> eval = getEvalFunction(e.getKey());
            A value = eval.evaluate(e.getValue());
            ret.put(e.getKey(), value);
        }
        return ret;
    }
}
