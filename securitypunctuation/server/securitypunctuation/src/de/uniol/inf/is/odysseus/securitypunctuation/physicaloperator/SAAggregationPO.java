package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.aggregation.functions.IIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.INonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.physicaloperator.AggregationPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SAOperatorDelegate;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SecurityPunctuation;

public class SAAggregationPO<M extends ITimeInterval, T extends Tuple<M>> extends AggregationPO<M, T> {

	SAOperatorDelegate<T> saOpDel;
	List<ISecurityPunctuation> matchingSPs;

	List<String> roles;
	String tupleRangeAttribute;

	public SAAggregationPO(AggregationPO<M, T> other, String tupleRangeAttribute, List<? extends IRole> roles) {
		super(other);
		this.tupleRangeAttribute = tupleRangeAttribute;
		addRoles(roles);
		this.saOpDel = new SAOperatorDelegate<T>();
		matchingSPs = new ArrayList<>();
	}

	public SAAggregationPO(List<INonIncrementalAggregationFunction<M, T>> nonIncrementalFunctions,
			List<IIncrementalAggregationFunction<M, T>> incrementalFunctions, boolean evaluateAtOutdatingElements,
			boolean evaluateBeforeRemovingOutdatingElements, boolean evaluateAtNewElement, boolean evaluateAtDone,
			boolean outputOnlyChanges, SDFSchema outputSchema, int[] groupingAttributesIndices,
			int[] groupingAttributeIndicesOutputSchema, String tupleRangeAttribute, List<? extends IRole> roles,
			final boolean createOutputOnPunctuation,
			final IMetadataMergeFunction<M> mmf, boolean alwaysUseSweepArea) {
		super(nonIncrementalFunctions, incrementalFunctions, evaluateAtOutdatingElements,
				evaluateBeforeRemovingOutdatingElements, evaluateAtNewElement, evaluateAtDone, outputOnlyChanges,
				outputSchema, groupingAttributesIndices, groupingAttributeIndicesOutputSchema, outputOnlyChanges, createOutputOnPunctuation,
				mmf, alwaysUseSweepArea);
		this.roles = new ArrayList<String>();
		this.tupleRangeAttribute = tupleRangeAttribute;
		addRoles(roles);
		this.saOpDel = new SAOperatorDelegate<T>();
		matchingSPs = new ArrayList<>();
	}

	// adds the name of the queryexecutors roles to a list
	private void addRoles(List<? extends IRole> roles) {
		for (IRole role : roles) {
			this.roles.add(role.getName());
		}

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (this.saOpDel.getRecentSPs().isEmpty()) {
			// sends a security punctuation that allows access to all following
			// tuples
			sendPunctuation(new SecurityPunctuation("*", "*", "*", true, true, System.currentTimeMillis()));
		}
		if (punctuation instanceof ISecurityPunctuation) {
			this.saOpDel.override((ISecurityPunctuation) punctuation);

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	protected synchronized void process_next(final T object, final int port) {
		// object has to be cloned, before it gets restricted
		T clone = (T) object.clone();
		// adds the sps that refer to the object and to the queryexecutor to a
		// list
		for (ISecurityPunctuation sp : saOpDel.getRecentSPs()) {
			if (sp.getDDP().match(object, this.getInputSchema(port), tupleRangeAttribute) && sp.checkRole(roles)) {
				matchingSPs.add(sp);

			}
		}
		if (!matchingSPs.isEmpty()) {
			// removes the attributes, the queryexecutor has no access to
			matchingSPs.get(0).restrictObject(clone, this.getInputSchema(port), matchingSPs, tupleRangeAttribute);
			super.process_next(clone, port);
		}
		matchingSPs.clear();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof SAAggregationPO)) {
			return false;
		} else if (!((SAAggregationPO) ipo).roles.containsAll(this.roles)
				|| !((SAAggregationPO) ipo).tupleRangeAttribute.equals(this.tupleRangeAttribute)) {
			return false;
		}
		return super.process_isSemanticallyEqual(ipo);
	}

}
