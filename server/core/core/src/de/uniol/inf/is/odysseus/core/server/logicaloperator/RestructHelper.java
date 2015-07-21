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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;

public class RestructHelper {
	
	private static final Logger LOG = LoggerFactory
			.getLogger(RestructHelper.class);
	
	public static Collection<ILogicalOperator> removeOperator(BinaryLogicalOp remove, boolean reserveOutputSchema) {
        List<ILogicalOperator> ret = new ArrayList<>();
        Collection<LogicalSubscription> fathers = remove.getSubscriptions();
        LogicalSubscription left = remove.getSubscribedToSource(0);
        LogicalSubscription right = remove.getSubscribedToSource(1);

        // remove Connection between child and op
        remove.unsubscribeFromSource(left);
        remove.unsubscribeFromSource(right);

        // Subscribe Child to every father of op
        for (LogicalSubscription father : fathers) {
            remove.unsubscribeSink(father);
            left.getTarget().subscribeSink(father.getTarget(), father.getSinkInPort(), left.getSourceOutPort(), reserveOutputSchema ? remove.getOutputSchema() : left.getTarget().getOutputSchema());
            ret.add(father.getTarget());
        }
        for (LogicalSubscription father : fathers) {
            remove.unsubscribeSink(father);
            right.getTarget().subscribeSink(father.getTarget(), father.getSinkInPort(), right.getSourceOutPort(), reserveOutputSchema ? remove.getOutputSchema() : right.getTarget().getOutputSchema());
            ret.add(father.getTarget());
        }
        // prevents duplicate entry if child.getTarget=father.getTarget
        if (!ret.contains(left.getTarget())) {
            ret.add(left.getTarget());
        }
        if (!ret.contains(right.getTarget())) {
            ret.add(right.getTarget());
        }
        return ret;
    }

	public static Collection<ILogicalOperator> removeOperator(
			UnaryLogicalOp remove, boolean reserveOutputSchema) {
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		Collection<LogicalSubscription> fathers = remove.getSubscriptions();
		LogicalSubscription child = remove.getSubscribedToSource(0);
		// remove Connection between child and op
		remove.unsubscribeFromSource(child);
		// Subscribe Child to every father of op
		for (LogicalSubscription father : fathers) {
			remove.unsubscribeSink(father);
			child.getTarget().subscribeSink(
					father.getTarget(),
					father.getSinkInPort(),
					child.getSourceOutPort(),
					reserveOutputSchema ? remove.getOutputSchema() : child
							.getTarget().getOutputSchema());
			ret.add(father.getTarget());
		}
		// prevents duplicate entry if child.getTarget=father.getTarget
		if (!ret.contains(child.getTarget())) {
			ret.add(child.getTarget());
		}
		// for (LogicalSubscription a : child.getTarget().getSubscriptions()) {
		// LoggerFactory.getLogger(RestructHelper.class).debug(
		// "New subplan after remove: " + a.getTarget());
		// }
		return ret;
	}

	/**
	 * Insert an operator in the tree at some special point and update all
	 * subscriptions i.e. the new Operator gets all subscriptions currently
	 * bound to the after operator (looking from root!) and create a new
	 * subscription from toInsert to after
	 * 
	 * @param toInsert
	 *            Operator that should be inserted as child of the after
	 *            operator
	 * @param after
	 * @return
	 */
	public static Collection<ILogicalOperator> insertOperator(
			ILogicalOperator toInsert, ILogicalOperator after, int sinkInPort,
			int toInsertsinkInPort, int toInsertsourceOutPort) {
		List<ILogicalOperator> ret = new ArrayList<ILogicalOperator>();
		LogicalSubscription source = after.getSubscribedToSource(sinkInPort);
		ret.add(source.getTarget());
		after.unsubscribeFromSource(source);
		source.getTarget()
				.subscribeSink(toInsert, toInsertsinkInPort,
						source.getSourceOutPort(),
						source.getTarget().getOutputSchema());
		toInsert.subscribeSink(after, sinkInPort, toInsertsourceOutPort,
				toInsert.getOutputSchema());
		ret.add(after);
		return ret;
	}

	/**
	 * Inserts a new logical operator (toInsert) before the operator before
	 * (e.g. closer to the root!)
	 * 
	 * @param toInsert
	 * @param before
	 * @return
	 */
	public static Collection<ILogicalOperator> insertOperatorBefore(
			ILogicalOperator toInsert, ILogicalOperator before) {
		List<ILogicalOperator> ret = new ArrayList<>();
		Collection<LogicalSubscription> subs = before.getSubscriptions();
		for (LogicalSubscription sub : subs) {
			before.unsubscribeSink(sub);
			// What about the source out port
			toInsert.subscribeSink(sub.getTarget(), sub.getSinkInPort(),
					sub.getSourceOutPort(), sub.getSchema());
			ret.add(sub.getTarget());
		}
		toInsert.subscribeToSource(before, 0, 0, before.getOutputSchema());
		ret.add(before);
		ret.add(toInsert);
		return ret;

	}

	public static Collection<ILogicalOperator> simpleOperatorSwitch(
			UnaryLogicalOp father, UnaryLogicalOp son) {
		// TODO: Can there be more than one father??
		if (son.getSubscriptions().size() != 1) {
			LOG.error("MAY NOT HAPPEN IN SIMPLE SWITCH!!!");
			LOG.error("FATHER: " + father);
			LOG.error("SON HAS MORE THAN ONE FATHER!!!!");
			LOG.error("SON: " + son);
			for (LogicalSubscription sub : son.getSubscriptions()) {
				System.out.println(sub);
			}
		}
		son.unsubscribeSink(son.getSubscriptions().iterator().next());

		LogicalSubscription toDown = son.getSubscribedToSource(0);
		son.unsubscribeFromSource(toDown);

		// TODO: Can there be more than one father??
		if (father.getSubscriptions().size() != 1) {
			LOG.error("THIS MAY NOT HAPPEN");
			for (LogicalSubscription sub : son.getSubscriptions()) {
				System.out.println(sub);
			}
		}
		LogicalSubscription toUp = father.getSubscriptions().iterator().next();
		father.unsubscribeSink(toUp);

		father.subscribeToSource(toDown.getTarget(), 0,
				toDown.getSourceOutPort(), toDown.getSchema());
		father.subscribeSink(son, 0, 0, father.getOutputSchema());

		son.subscribeSink(toUp.getTarget(), toUp.getSinkInPort(), 0,
				son.getOutputSchema());

		Collection<ILogicalOperator> toUpdate = new ArrayList<ILogicalOperator>(
				2);
		toUpdate.add(toDown.getTarget());
		toUpdate.add(toUp.getTarget());
		return toUpdate;
	}

	public static Collection<ILogicalOperator> replace(ILogicalOperator oldOP,
			ILogicalOperator newOp) {
		Collection<ILogicalOperator> touched = new LinkedList<>();

		Collection<LogicalSubscription> subscriptions = oldOP
				.getSubscriptions();

		if (newOp.getSubscriptions().size() > 0
				|| newOp.getSubscribedToSource().size() > 0) {
			throw new IllegalArgumentException(
					"Replacement operator is not allowed to be connected!");
		}

		// Replace subscriptions to sinks
		for (LogicalSubscription s : subscriptions) {
			oldOP.unsubscribeSink(s);
			newOp.subscribeSink(s.getTarget(), s.getSinkInPort(),
					s.getSourceOutPort(), s.getSchema());
			touched.add(s.getTarget());
		}

		// Replace subscriptions to source
		subscriptions = new ArrayList<>(oldOP.getSubscribedToSource());
		for (LogicalSubscription s : subscriptions) {
			oldOP.unsubscribeFromSource(s);
			newOp.subscribeToSource(s.getTarget(), s.getSinkInPort(),
					s.getSourceOutPort(), s.getSchema());
			touched.add(s.getTarget());
		}

		return touched;
	}

	/**
	 * Replaces a logical leaf operator by a subplan.
	 * 
	 * @param leafOp
	 *            The logical operator to be replaced.
	 * @param newOp
	 *            The subplan to be inserted instead.
	 */
	public static void replaceWithSubplan(ILogicalOperator leafOp,
			ILogicalOperator newOp) {

		if (leafOp.getSubscribedToSource().size() > 0) {
			throw new IllegalArgumentException(
					"Method can only be called for a leaf");
		}

		// change all subscriptions, which were from oldOp to its targets
		for (LogicalSubscription subToSink : leafOp.getSubscriptions()) {

			ILogicalOperator target = subToSink.getTarget();

			target.unsubscribeFromSource(leafOp, subToSink.getSinkInPort(),
					subToSink.getSourceOutPort(), subToSink.getSchema());
			target.subscribeToSource(newOp, subToSink.getSinkInPort(),
					subToSink.getSourceOutPort(), subToSink.getSchema());

		}

	}

	/**
	 * Creates a new {@link TopAO} on top of the sinks.
	 * 
	 * @param sinks
	 *            The {@link ILogicalOperator}s which shall be subscribed to new
	 *            {@link TopAO}.
	 * @return The new {@link TopAO}.
	 */
	public static TopAO generateTopAO(final Collection<ILogicalOperator> sinks) {

		final TopAO topAO = new TopAO();
		int inputPort = 0;
		for (ILogicalOperator sink : sinks)
			topAO.subscribeToSource(sink, inputPort++, 0,
					sink.getOutputSchema());

		return topAO;

	}

	/**
	 * Removes all {@link TopAO} logical operators from a list of
	 * {@link ILogicalOperator}s representing an {@link ILogicalQuery}.
	 * 
	 * @param operators
	 *            The list of {@link ILogicalOperator}s representing an
	 *            {@link ILogicalQuery}.
	 */
	public static void removeTopAOs(List<ILogicalOperator> operators) {

		final List<ILogicalOperator> operatorsToRemove = Lists.newArrayList();

		for (final ILogicalOperator operator : operators) {

			if (operator instanceof TopAO) {

				operator.unsubscribeFromAllSources();
				operatorsToRemove.add(operator);

			}

		}

		for (final ILogicalOperator operatorToRemove : operatorsToRemove)
			operators.remove(operatorToRemove);

	}

	/**
	 * Searches for a {@link LogicalSubscription} between two
	 * {@link ILogicalOperator}s.
	 * 
	 * @param source
	 *            The relative source for the {@link LogicalSubscription}.
	 * @param sink
	 *            The relative sink for the {@link LogicalSubscription}.
	 * @return The {@link LogicalSubscription} between <code>source</code> and
	 *         <code>sink</code> or null, if there is no such subscription.
	 */
	public static LogicalSubscription determineSubscription(
			ILogicalOperator source, ILogicalOperator sink) {

		for (final LogicalSubscription subscription : source.getSubscriptions()) {

			if (subscription.getTarget().equals(sink))
				return subscription;

		}

		return null;

	}

//	/**
//	 * Generates a new {@link SDFSchema} with a given base name and adopting
//	 * {@link SDFAttribute}s of an existing {@link SDFSchema}.
//	 * 
//	 * @param basename
//	 *            The base name for the new {@link SDFSchema}.
//	 * @param outputSchema
//	 *            The {@link SDFSchema} whose {@link SDFAttribute}s shall be
//	 *            adopted.
//	 * @return The new {@link SDFSchema}.
//	 */
//	public static SDFSchema generateOutputSchema(String basename,
//			SDFSchema outputSchema) {
//
//		List<SDFAttribute> attributes = Lists.newArrayList();
//
//		for (SDFAttribute attribute : outputSchema)
//			attributes.add(new SDFAttribute(basename, attribute
//					.getAttributeName(), attribute));
//
//		return new SDFSchema(basename, outputSchema, attributes);
//
//	}

	/**
	 * Collects recursive all {@link ILogicalOperator}s representing an
	 * <code>ILogicalQuery</code>. <br />
	 * This method should be called with {@link ILogicalQuery#getLogicalPlan()}
	 * as <code>currentOperator</code>.
	 * 
	 * @param currentOperator
	 *            The <code>IlogicalOperator</code> to collect next.
	 * @param list
	 *            The list of all <code>ILogicalOperators</code> collected so
	 *            far.
	 */
	public static void collectOperators(ILogicalOperator currentOperator,
			Collection<ILogicalOperator> list) {

		if (!list.contains(currentOperator)) {

			list.add(currentOperator);

			for (final LogicalSubscription subscription : currentOperator
					.getSubscriptions())
				RestructHelper.collectOperators(subscription.getTarget(), list);

			for (final LogicalSubscription subscription : currentOperator
					.getSubscribedToSource())
				RestructHelper.collectOperators(subscription.getTarget(), list);

		}

	}
	
	/**
	 * Assigns a plane to every operator within a logical plan.
	 * @param root The operator, which acts as the root of the logical plan.
	 * @return A mapping of planes to the operators. <br />
	 * The plane '0' will be assigned to <code>root</code>.
	 */
	public static Map<ILogicalOperator, Integer> assignOperatorPlanes(ILogicalOperator root) {
		
		Preconditions.checkNotNull(root);
		
		// The return value
		Map<ILogicalOperator, Integer> planeToOperatorMap = Maps.newHashMap();
		
		assignOperatorPlanes(root, 0, planeToOperatorMap);
		
		return planeToOperatorMap;
		
	}
	
	/**
	 * Assigns a plane to every operator within a logical plan.
	 * @param currentOperator The current operator to be assigned.
	 * @param currentPlane The plane of <code>currrentOperator</code>.
	 * @param planeToOperatorMap A mapping of planes to the operators.
	 */
	private static void assignOperatorPlanes(ILogicalOperator currentOperator, int currentPlane, 
			Map<ILogicalOperator, Integer> planeToOperatorMap) {
		
		Preconditions.checkNotNull(currentOperator);
		Preconditions.checkNotNull(planeToOperatorMap);
		
		if(!planeToOperatorMap.containsKey(currentOperator)) {
			
			planeToOperatorMap.put(currentOperator, currentPlane);
			
			for(final LogicalSubscription subscription : currentOperator.getSubscriptions())
				assignOperatorPlanes(subscription.getTarget(), currentPlane + 1, planeToOperatorMap);

			for (final LogicalSubscription subscription : currentOperator.getSubscribedToSource())
				assignOperatorPlanes(subscription.getTarget(), currentPlane - 1, planeToOperatorMap);

		}
		
	}

}