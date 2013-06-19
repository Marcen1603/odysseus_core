/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package de.uniol.inf.is.odysseus.plangenerator.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOperatorOwner;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorInputSchemaLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.CollectOperatorLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.core.server.util.RemoveOwnersGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.SimplePlanPrinter;
import de.uniol.inf.is.odysseus.core.util.SetOwnerGraphVisitor;

/**
 * Contains functions for the PlanGenerator
 * 
 * @author Merlin Wasmann
 * 
 */
public class PlanGeneratorHelper {
	
	private static final Logger LOG = LoggerFactory.getLogger(PlanGeneratorHelper.class);
	
	private static Map<ILogicalOperator, ILogicalOperator> clone2original = new HashMap<ILogicalOperator, ILogicalOperator>();	
	private static Map<ILogicalOperator, SDFSchema> operatorInputSchemaMap = new HashMap<ILogicalOperator, SDFSchema>();
	
	/**
	 * Collects all input schemata for all operators and stores them into a map.
	 * 
	 * @param plan
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setupInitialInputSchemata(ILogicalOperator plan) {
		CollectOperatorInputSchemaLogicalGraphVisitor<ILogicalOperator> visitor = new CollectOperatorInputSchemaLogicalGraphVisitor<ILogicalOperator>();
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(plan, visitor);
		Set<Pair<ILogicalOperator, SDFSchema>> schemaPairs = visitor.getResult();
		for(Pair<ILogicalOperator, SDFSchema> pair : schemaPairs) {
			PlanGeneratorHelper.setInputSchemaForOperator(pair.getE1(), pair.getE2());
		}
	}
	
	/**
	 * Collects all join operators in the given plan.
	 * 
	 * @param plan
	 * @return a set of all join operators in the plan.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Set<JoinAO> getJoinOperators(ILogicalOperator plan) {
		CollectOperatorLogicalGraphVisitor<JoinAO> visitor = new CollectOperatorLogicalGraphVisitor<JoinAO>(
				JoinAO.class);
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(plan, visitor);
		return visitor.getResult();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Set<WindowAO> getWindowOperators(ILogicalOperator plan) {
		CollectOperatorLogicalGraphVisitor<WindowAO> visitor = new CollectOperatorLogicalGraphVisitor<WindowAO>(WindowAO.class);
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(plan, visitor);
		return visitor.getResult();
	}
	
	/**
	 * Collects all access operators in the given plan.
	 * 
	 * @param plan
	 * @return a set of all access operators in the plan.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Set<ILogicalOperator> getAccessOperators(ILogicalOperator plan) {
		Set<Class<? extends ILogicalOperator>> classes = new HashSet<Class<? extends ILogicalOperator>>();
		classes.add(AccessAO.class);
		classes.add(StreamAO.class);
		CollectOperatorLogicalGraphVisitor<ILogicalOperator> visitor = new CollectOperatorLogicalGraphVisitor(classes);
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(plan, visitor);
		return visitor.getResult();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setNewOwnerForPlan(ILogicalOperator plan, IOperatorOwner query) {
		SetOwnerGraphVisitor<ILogicalOperator> visitor = new SetOwnerGraphVisitor(query);
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(plan, visitor);
	}

	/**
	 * Takes a set of AccessAOs and joins it with itself to create all possible
	 * join-pairs.
	 * 
	 * @param sources
	 * @return
	 */
	public static Set<Pair<ILogicalOperator, ILogicalOperator>> joinSets(Set<ILogicalOperator> sources) {
		Set<Pair<ILogicalOperator, ILogicalOperator>> sets = new HashSet<Pair<ILogicalOperator, ILogicalOperator>>();
		ILogicalOperator[] sourcesArray = new ILogicalOperator[0];
		sourcesArray = sources.toArray(sourcesArray);
		for (int i = 0; i < sourcesArray.length - 1; i++) {
			for (int j = i + 1; j < sourcesArray.length; j++) {
				Pair<ILogicalOperator, ILogicalOperator> pair = new Pair<ILogicalOperator, ILogicalOperator>(
						sourcesArray[i].clone(), sourcesArray[j].clone());
				sets.add(pair);
				clone2original.put(pair.getE1(), sourcesArray[i]);
				clone2original.put(pair.getE2(), sourcesArray[j]);
				
			}
		}
		return sets;
	}

	/**
	 * prints the given plan using the SimplePlanPrinter.
	 * 
	 * @param plan
	 */
	public static void printPlan(ILogicalOperator plan) {
		SimplePlanPrinter<ILogicalOperator> printer = new SimplePlanPrinter<ILogicalOperator>();
		LOG.debug("Plan \n"
				+ printer.createString(plan));
	}
	public static void printPlan(String prefix, ILogicalOperator plan) {
		SimplePlanPrinter<ILogicalOperator> printer = new SimplePlanPrinter<ILogicalOperator>();
		LOG.debug(prefix + "\n" + printer.createString(plan));
	}
	public static void printErrorPlan(String prefix, ILogicalOperator plan) {
		SimplePlanPrinter<ILogicalOperator> printer = new SimplePlanPrinter<ILogicalOperator>();
		LOG.error(prefix + "\n" + printer.createString(plan));
	}

	/**
	 * Copy the given plan with a CopyLogicalGraphVisitor.
	 * @param plan
	 * @return a copy of the given plan.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ILogicalOperator copyPlan(ILogicalOperator plan) {
		RemoveOwnersGraphVisitor<ILogicalOperator> removeOwnerVisitor = new RemoveOwnersGraphVisitor<ILogicalOperator>();
		GenericGraphWalker walker = new GenericGraphWalker();
		walker.prefixWalk(plan, removeOwnerVisitor);
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>((IOperatorOwner)null);
		walker = new GenericGraphWalker();
		walker.prefixWalk(plan, copyVisitor);
		return copyVisitor.getResult();
	}
	
	public static ILogicalOperator getOriginal2Clone(ILogicalOperator clone) {
		return clone2original.get(clone);
	}
	
	public static void setOriginalForClone(ILogicalOperator clone, ILogicalOperator original) {
		clone2original.put(clone, original);
	}
	
	/**
	 * This method also preserves the inputschema for this operator.
	 * 
	 * @param operator
	 * @return
	 */
	public static ILogicalOperator cloneOperator(ILogicalOperator operator) {
		if(operator instanceof AccessAO || operator instanceof StreamAO) {
			// no input schema for sources
			return operator.clone();
		}
		SDFSchema inputSchema = getInputSchemaForOperator(operator);
		if(inputSchema == null) {
			LOG.error("No schema information found for " + operator);
			return null;
		}
		ILogicalOperator clone = operator.clone();
		setInputSchemaForOperator(clone, inputSchema);
		return clone;
	}

	public static void setInputSchemaForOperator(ILogicalOperator operator,
			SDFSchema schema) {
		operatorInputSchemaMap.put(operator, schema);
	}

	public static SDFSchema getInputSchemaForOperator(ILogicalOperator operator) {
		return operatorInputSchemaMap.get(operator);
	}

	public static boolean hasWindowBeforeJoin(ILogicalOperator source) {
		ILogicalOperator next = source.getSubscriptions().iterator().next()
				.getTarget();
		while (next != null) {
			if (next instanceof WindowAO) {
				return true;
			}
			if (next instanceof JoinAO) {
				return false;
			}
			if (!next.getSubscriptions().isEmpty()) {
				next = next.getSubscriptions().iterator().next().getTarget();
			} else {
				return false;
			}
		}
		return false;
	}

	public static boolean hasValidWindowPositions(ILogicalOperator plan) {
		Set<ILogicalOperator> sources = getAccessOperators(plan);
		for (ILogicalOperator source : sources) {
			if (!hasWindowBeforeJoin(source)) {
				return false;
			}
		}
		return true;
	}

	public static String getObjectName(Object obj) {
		return obj.getClass().getSimpleName() + " (" + obj.hashCode() + ")";
	}

}
