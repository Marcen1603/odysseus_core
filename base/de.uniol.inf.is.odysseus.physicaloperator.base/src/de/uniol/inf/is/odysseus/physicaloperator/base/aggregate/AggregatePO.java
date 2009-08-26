package de.uniol.inf.is.odysseus.physicaloperator.base.aggregate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.base.AggregateFunction;
import de.uniol.inf.is.odysseus.base.FESortedPair;
import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.logicaloperator.base.AggregateAO;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.base.PairMap;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.Evaluator;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.Initializer;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.Merger;
import de.uniol.inf.is.odysseus.physicaloperator.base.aggregate.basefunctions.PartialAggregate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

abstract public class AggregatePO<M extends IClone, R extends IMetaAttribute<? extends M>, A extends IClone>
		extends AbstractPipe<R, R> {

	private Map<FESortedPair<SDFAttribute, AggregateFunction>, Initializer<A>> init = new HashMap<FESortedPair<SDFAttribute, AggregateFunction>, Initializer<A>>();

	private Map<FESortedPair<SDFAttribute, AggregateFunction>, Merger<A>> merger = new HashMap<FESortedPair<SDFAttribute, AggregateFunction>, Merger<A>>();

	private Map<FESortedPair<SDFAttribute, AggregateFunction>, Evaluator<A>> eval = new HashMap<FESortedPair<SDFAttribute, AggregateFunction>, Evaluator<A>>();

	SDFAttributeList inputSchema = null;
	Map<SDFAttribute, Map<AggregateFunction, SDFAttribute>> aggregations = null;
	
	private GroupingHelper<R> groupingHelper;

	private AggregateAO algebraOp;
	
	
	public GroupingHelper<R> getGroupingHelper() {
		return groupingHelper;
	}

	public void setGroupingHelper(GroupingHelper<R> groupingHelper) {
		this.groupingHelper = groupingHelper;
	}
	
	public AggregatePO(AggregateAO algebraOP){
		this.inputSchema = algebraOP.getInputSchema();
		this.aggregations = algebraOP.getAggregations();
		this.algebraOp = algebraOP;
	}

	public AggregateAO getAlgebraOp() {
		return algebraOp;
	}

	public AggregatePO(AggregatePO<M, R, A> agg) {
		init = new HashMap<FESortedPair<SDFAttribute, AggregateFunction>, Initializer<A>>(
				agg.init);
		merger = new HashMap<FESortedPair<SDFAttribute, AggregateFunction>, Merger<A>>(
				agg.merger);
		eval = new HashMap<FESortedPair<SDFAttribute, AggregateFunction>, Evaluator<A>>(
				agg.eval);
		this.inputSchema = agg.inputSchema;
		this.aggregations = agg.aggregations;
		this.groupingHelper = agg.groupingHelper;
	}

	public void setAggregationFunction(
			FESortedPair<SDFAttribute, AggregateFunction> p, Initializer<A> i) {
		init.put(p, i);
	}

	public void setAggregationFunction(
			FESortedPair<SDFAttribute, AggregateFunction> p, Merger<A> m) {
		merger.put(p, m);
	}

	public void setAggregationFunction(
			FESortedPair<SDFAttribute, AggregateFunction> p, Evaluator<A> e) {
		eval.put(p, e);
	}

	protected Evaluator<A> getEvalFunction(
			FESortedPair<SDFAttribute, AggregateFunction> p) {
		return eval.get(p);
	}

	protected Merger<A> getMergeFunction(
			FESortedPair<SDFAttribute, AggregateFunction> p) {
		return merger.get(p);
	}

	protected Initializer<A> getInitFunction(
			FESortedPair<SDFAttribute, AggregateFunction> p) {
		return init.get(p);
	}

	// Erzeugen der initialen Map f�r alle
	// Attribut/Aggregierungsfunktion-Paare,
	// abgebildet auf Partielle Aggregate
	protected PairMap<SDFAttribute, AggregateFunction, PartialAggregate<A>, M> calcInit(
			A element) {
		PairMap<SDFAttribute, AggregateFunction, PartialAggregate<A>, M> ret = new PairMap<SDFAttribute, AggregateFunction, PartialAggregate<A>, M>();
		// Hier ggf. direkt die Liste der zu aggregierenden Attribute auslesen
		for (SDFAttribute a : inputSchema) {
			Map<AggregateFunction, SDFAttribute> funcs = aggregations.get(a);
			if (funcs != null) {
				for (Entry<AggregateFunction, SDFAttribute> e : funcs
						.entrySet()) {
					// Achtung! Das Eingabeattribut, nicht das Ausgabeattribut
					PartialAggregate<A> pa = getInitFunction(
							new FESortedPair<SDFAttribute, AggregateFunction>(
									a, e.getKey())).init(element);
					ret.put(a, e.getKey(), pa);
				}
			}
		}
		return ret;
	}

	// TODO: PairMap<...,R> gemacht. Richtig so?
	// Mergen aller Elemente in der toMerge-Map mit dem element vom Typ A (mit
	// dem passenden Merger)
	protected synchronized PairMap<SDFAttribute, AggregateFunction, PartialAggregate<A>, M> calcMerge(
			PairMap<SDFAttribute, AggregateFunction, PartialAggregate<A>, M> toMerge,
			A element) {
		PairMap<SDFAttribute, AggregateFunction, PartialAggregate<A>, M> ret = new PairMap<SDFAttribute, AggregateFunction, PartialAggregate<A>, M>();

		// Jedes Element in toMerge mit element mergen
		for (Entry<FESortedPair<SDFAttribute, AggregateFunction>, PartialAggregate<A>> e : toMerge
				.entrySet()) {
			Merger<A> mf = getMergeFunction(e.getKey());
			PartialAggregate<A> pa = mf.merge(e.getValue(), element, true);
			ret.put(e.getKey(), pa);
		}

		return ret;
	}

	// Auswerten aller Elemente in der toEval map (mit dem passenden Evaluator)
	protected PairMap<SDFAttribute, AggregateFunction, A, M> calcEval(
			PairMap<SDFAttribute, AggregateFunction, PartialAggregate<A>, M> toEval) {
		PairMap<SDFAttribute, AggregateFunction, A, M> ret = new PairMap<SDFAttribute, AggregateFunction, A, M>();
		for (Entry<FESortedPair<SDFAttribute, AggregateFunction>, PartialAggregate<A>> e : toEval
				.entrySet()) {
			Evaluator<A> eval = getEvalFunction(e.getKey());
			A value = eval.evaluate(e.getValue());
			ret.put(e.getKey(), value);
		}
		return ret;
	}
}
