package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.aggregation.functions.IIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.functions.INonIncrementalAggregationFunction;
import de.uniol.inf.is.odysseus.aggregation.physicaloperator.AggregationPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SAOperatorDelegate;
import de.uniol.inf.is.odysseus.securitypunctuation.datatype.SecurityPunctuation;

public class SAAggregationPO<M extends ITimeInterval, T extends Tuple<M>> extends AggregationPO<M, T> {

	SAOperatorDelegate<T> saOpDel;
	List<ISecurityPunctuation> matchingSPs;

	// queryexecutor
	private ISession currentUser = UserManagementProvider.getUsermanagement(true).getSessionManagement()
			.loginSuperUser(null);

	List<String> roles;
	String tupleRangeAttribute;

	public SAAggregationPO(AggregationPO<M, T> other, String tupleRangeAttribute) {
		super(other);
		this.tupleRangeAttribute = tupleRangeAttribute;
		addRoles();
		this.saOpDel = new SAOperatorDelegate<T>();
		matchingSPs = new ArrayList<>();
	}

	public SAAggregationPO(List<INonIncrementalAggregationFunction<M, T>> nonIncrementalFunctions,
			List<IIncrementalAggregationFunction<M, T>> incrementalFunctions, boolean evaluateAtOutdatingElements,
			boolean evaluateBeforeRemovingOutdatingElements, boolean evaluateAtNewElement, boolean evaluateAtDone,
			boolean outputOnlyChanges, SDFSchema outputSchema, int[] groupingAttributesIndices,
			String tupleRangeAttribute) {
		super(nonIncrementalFunctions, incrementalFunctions, evaluateAtOutdatingElements,
				evaluateBeforeRemovingOutdatingElements, evaluateAtNewElement, evaluateAtDone, outputOnlyChanges,
				outputSchema, groupingAttributesIndices);
		this.roles = new ArrayList<String>();
		this.tupleRangeAttribute = tupleRangeAttribute;
		addRoles();
		this.saOpDel = new SAOperatorDelegate<T>();
		matchingSPs = new ArrayList<>();
	}

	private void addRoles() {
		for (IRole role : this.currentUser.getUser().getRoles()) {
			this.roles.add(role.getName());
		}

	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		if (this.saOpDel.getRecentSPs().isEmpty()) {
			sendPunctuation(new SecurityPunctuation("*", "*", "*", true, true, System.currentTimeMillis()));
		}
		if (punctuation instanceof ISecurityPunctuation) {
			this.saOpDel.override((ISecurityPunctuation) punctuation);

		}

	}

	@Override
	protected synchronized void process_next(final T object, final int port) {

		for (ISecurityPunctuation sp : saOpDel.getRecentSPs()) {
			if (sp.getDDP().match(object, this.getInputSchema(port), tupleRangeAttribute) && sp.checkRole(roles)) {
				matchingSPs.add(sp);

			}
		}
		if (!matchingSPs.isEmpty()) {
			matchingSPs.get(0).restrictObject(object, this.getInputSchema(port), matchingSPs, tupleRangeAttribute);
			super.process_next(object, port);
		}
		matchingSPs.clear();
	}

}
