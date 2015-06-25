package de.uniol.inf.is.odysseus.multithreaded.interoperator.transform;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BufferAO;
import de.uniol.inf.is.odysseus.core.server.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.core.server.util.GenericGraphWalker;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

public class PostOptimizationHandler {

	final private static InfoService INFO_SERVICE = InfoServiceFactory
			.getInfoService(PostOptimizationHandler.class);

	public static void doPostOptimization(ILogicalOperator logicalPlan,
			ILogicalQuery query,
			List<TransformationResult> transformationResults,
			boolean optimizationAllowed) {

		List<PostOptimizationElement> postOptimizationElements;
		postOptimizationElements = prepareOptimizations(transformationResults,
				optimizationAllowed);

		if (postOptimizationElements != null
				&& !postOptimizationElements.isEmpty()) {
			// iterate through post optimizations
			int processedOptimizationsCounter = 0;
			for (PostOptimizationElement element : postOptimizationElements) {
				boolean optimizationSuccessful = processOptimization(
						logicalPlan, query, element);
				if (optimizationSuccessful) {
					processedOptimizationsCounter++;
				}
			}
			INFO_SERVICE.info(processedOptimizationsCounter
					+ " optimizations are processed.");
		} else {
			INFO_SERVICE
					.info("Optimization not possible, because there are no fragmentations found, that can be combined.");
			return;
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static boolean processOptimization(ILogicalOperator logicalPlan,
			ILogicalQuery query, PostOptimizationElement element) {
		CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(
				query);
		GenericGraphWalker copyWalker = new GenericGraphWalker();
		copyWalker.prefixWalk(logicalPlan, copyVisitor);
		ILogicalOperator savedPlan = copyVisitor.getResult();

		try {
			ILogicalOperator unionOperator = element.getStartOperator();
			ILogicalOperator fragmentOperator = element.getEndOperator();
			CopyOnWriteArrayList<LogicalSubscription> unionSourceSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>(
					unionOperator.getSubscribedToSource());

			int iteration = 0;
			for (LogicalSubscription unionSourceSubscription : unionSourceSubscriptions) {
				ILogicalOperator unionSourceOperator = unionSourceSubscription
						.getTarget();

				ILogicalOperator currentNewOperator = null;
				ILogicalOperator lastNewOperator = unionSourceOperator;

				ILogicalOperator lastOperator = unionOperator;
				ILogicalOperator currentOperator = LogicalGraphHelper
						.getNextOperator(unionOperator);
				while (currentOperator != null) {
					if (currentOperator instanceof AbstractFragmentAO
							&& currentOperator.getUniqueIdentifier() != null) {
						if (currentOperator.getUniqueIdentifier()
								.equalsIgnoreCase(
										fragmentOperator.getUniqueIdentifier())) {
							doFinalConnection(iteration, lastNewOperator,
									currentOperator);
							break;
						} else {
							throw new Exception("");
						}
					}

					currentNewOperator = cloneOperator(iteration,
							currentOperator);

					connectNewOperators(iteration, currentNewOperator,
							lastNewOperator, lastOperator, currentOperator);

					// prepare for next iteration
					lastNewOperator = currentNewOperator;

					lastOperator = currentOperator;
					currentOperator = LogicalGraphHelper
							.getNextOperator(currentOperator);
				}
				iteration++;
			}
			// remove old subscriptions from fragment and union operator
			unionOperator.unsubscribeFromAllSources();
			fragmentOperator.unsubscribeFromAllSinks();
		} catch (Exception e) {
			// if something went wrong, revert plan
			query.setLogicalPlan(savedPlan, true);
			return false;
		}
		return true;
	}

	private static void connectNewOperators(int iteration,
			ILogicalOperator currentNewOperator,
			ILogicalOperator lastNewOperator, ILogicalOperator lastOperator,
			ILogicalOperator currentOperator) {
		// connect current new operator to last
		// created operator
		CopyOnWriteArrayList<LogicalSubscription> operatorSourceSubscriptions = new CopyOnWriteArrayList<LogicalSubscription>();
		operatorSourceSubscriptions.addAll(currentOperator
				.getSubscribedToSource());
		for (LogicalSubscription sourceSubscription : operatorSourceSubscriptions) {
			if (sourceSubscription.getTarget().equals(lastOperator)) {
				// if target of subscription is last
				// existing operator, set
				// new cloned one
				currentNewOperator.subscribeToSource(lastNewOperator,
						sourceSubscription.getSinkInPort(),
						sourceSubscription.getSourceOutPort(),
						lastNewOperator.getOutputSchema());
			} else {
				// else connect new copied operator
				// to target
				int newSourceOutPort = LogicalGraphHelper
						.calculateNewSourceOutPort(sourceSubscription,
								iteration);

				currentNewOperator.subscribeToSource(
						sourceSubscription.getTarget(),
						sourceSubscription.getSinkInPort(), newSourceOutPort,
						sourceSubscription.getTarget().getOutputSchema());
			}
		}
	}

	private static ILogicalOperator cloneOperator(int iteration,
			ILogicalOperator currentOperator) {
		ILogicalOperator currentNewOperator;
		// clone existing operator
		currentNewOperator = currentOperator.clone();
		currentNewOperator.setName(currentNewOperator.getName() + "_"
				+ iteration);
		if (currentNewOperator.getUniqueIdentifier() != null) {
			currentNewOperator.setUniqueIdentifier(currentNewOperator
					.getUniqueIdentifier() + "_" + iteration);

		}
		return currentNewOperator;
	}

	private static void doFinalConnection(int iteration,
			ILogicalOperator lastNewOperator, ILogicalOperator currentOperator) {
		// end fragment operator reached
		List<LogicalSubscription> fragmentedOperatorSubscritpions = new ArrayList<LogicalSubscription>(
				currentOperator.getSubscriptions());
		ILogicalOperator bufferOperator = fragmentedOperatorSubscritpions.get(
				iteration).getTarget();
		// we know, that a fragmentation is
		// followed by a buffer operator
		if (bufferOperator instanceof BufferAO) {
			List<LogicalSubscription> bufferSubscritpions = new ArrayList<LogicalSubscription>(
					bufferOperator.getSubscriptions());
			if (bufferSubscritpions.size() == 1) {
				ILogicalOperator destinationOperator = bufferSubscritpions.get(
						0).getTarget();

				// remove existing
				// subscriptions
				destinationOperator.unsubscribeFromAllSources();
				lastNewOperator.unsubscribeFromAllSinks();

				// connect last new Operator
				// to destination operator
				destinationOperator.subscribeToSource(lastNewOperator, 0, 0,
						lastNewOperator.getOutputSchema());
			}
		}
	}

	private static List<PostOptimizationElement> prepareOptimizations(
			List<TransformationResult> transformationResults,
			boolean optimizationAllowed) {
		List<PostOptimizationElement> postOptimizationElements = new ArrayList<PostOptimizationElement>();
		if (optimizationAllowed) {
			if (!transformationResults.isEmpty()
					&& transformationResults.size() > 1) {
				if (validateTransformationResults(transformationResults)) {
					postOptimizationElements = createPostOptimizationElements(transformationResults);
				} else {
					INFO_SERVICE
							.warning("At least one transformation result is invalid");
					return null;
				}
			} else {
				INFO_SERVICE
						.info("Optimization of plan is not needed, because no or only one strategy has been processed");
				return null;
			}
		} else {
			INFO_SERVICE.info("Optimization of plan is disabled");
			return null;
		}
		return postOptimizationElements;
	}

	private static boolean validateTransformationResults(
			List<TransformationResult> transformationResults) {
		for (TransformationResult transformationResult : transformationResults) {
			if (!transformationResult.validate()) {
				return false;
			}
		}
		return true;
	}

	private static List<PostOptimizationElement> createPostOptimizationElements(
			List<TransformationResult> transformationResults) {
		List<PostOptimizationElement> postOptimizationElements = new ArrayList<PostOptimizationElement>();
		combineTransformationResults(transformationResults,
				postOptimizationElements);

		return postOptimizationElements;
	}

	private static void combineTransformationResults(
			List<TransformationResult> transformationResults,
			List<PostOptimizationElement> postOptimizationElements) {
		for (TransformationResult currentTransformationResult : transformationResults) {
			// only first fragmentation is needed, because for every
			// strategy every fragmentation is from the same type
			AbstractFragmentAO currentFragementAO = currentTransformationResult
					.getFragmentOperators().get(0);

			for (TransformationResult otherTransformationResult : transformationResults) {
				// only first fragmentation is needed, because for every
				// strategy every fragmentation is from the same type
				AbstractFragmentAO otherFragmentAO = otherTransformationResult
						.getFragmentOperators().get(0);

				// check if it is not the same result
				if (!currentTransformationResult.getUniqueIdentifier().equals(
						otherTransformationResult.getUniqueIdentifier())) {
					// check if fragmentation type is equal
					if (currentFragementAO.getClass() == otherFragmentAO
							.getClass()) {
						// check number of fragments and attributes (hash)
						if (currentFragementAO.getNumberOfFragments() == otherFragmentAO
								.getNumberOfFragments()) {
							processEqualFragementTypes(
									postOptimizationElements,
									currentTransformationResult,
									currentFragementAO,
									otherTransformationResult);
						}
					}
				}
			}
		}
	}

	private static void processEqualFragementTypes(
			List<PostOptimizationElement> postOptimizationElements,
			TransformationResult currentTransformationResult,
			AbstractFragmentAO currentFragementAO,
			TransformationResult otherTransformationResult) {
		// check if it is a hash fragment, if so, we need to
		// check if the attributes are equal
		if (currentFragementAO instanceof HashFragmentAO) {
			// we know that both fragment have the same type
			// and we know that one is a hash fragment, so
			// both can be casted
			List<HashFragmentAO> thisFragmentations = new ArrayList<HashFragmentAO>();
			for (AbstractFragmentAO fragmentAO : currentTransformationResult
					.getFragmentOperators()) {
				if (fragmentAO instanceof HashFragmentAO) {
					thisFragmentations.add((HashFragmentAO) fragmentAO);
				}
			}

			List<HashFragmentAO> otherFragmentations = new ArrayList<HashFragmentAO>();
			for (AbstractFragmentAO fragmentAO : otherTransformationResult
					.getFragmentOperators()) {
				if (fragmentAO instanceof HashFragmentAO) {
					otherFragmentations.add((HashFragmentAO) fragmentAO);
				}
			}

			Pair<HashFragmentAO, HashFragmentAO> equalFragmentOperators = getFragmentOperatorsWithEqualAttributes(
					thisFragmentations, otherFragmentations);

			if (equalFragmentOperators == null) {
				return;
			} else {
				// remove all fragment operators that does
				// not match
				currentTransformationResult.getFragmentOperators().clear();
				currentTransformationResult
						.addFragmentOperator(equalFragmentOperators.getE1());

				otherTransformationResult.getFragmentOperators().clear();
				otherTransformationResult
						.addFragmentOperator(equalFragmentOperators.getE2());
			}

		}

		addPostOptimizationElement(postOptimizationElements,
				currentTransformationResult, otherTransformationResult);
	}

	private static void addPostOptimizationElement(
			List<PostOptimizationElement> postOptimizationElements,
			TransformationResult currentTransformationResult,
			TransformationResult otherTransformationResult) {
		if (!doPostOptimizationElementAlreadyExists(postOptimizationElements,
				currentTransformationResult, otherTransformationResult)) {
			PostOptimizationElement postOptimizationElement = createOrderedMatchingPair(
					currentTransformationResult, otherTransformationResult);
			if (postOptimizationElement != null) {
				if (postOptimizationElement.allowsModificationAfterUnion()) {
					postOptimizationElements.add(postOptimizationElement);
				}
			}
		}
	}

	private static PostOptimizationElement createOrderedMatchingPair(
			TransformationResult currentTransformationResult,
			TransformationResult otherTransformationResult) {
		PostOptimizationElement postOptimizationElement = new PostOptimizationElement();

		// check order
		ILogicalOperator result = LogicalGraphHelper
				.findDownstreamOperatorWithId(otherTransformationResult
						.getFragmentOperators().get(0).getUniqueIdentifier()
						.toString(),
						currentTransformationResult.getUnionOperator());
		if (result != null) {
			// fragment operators of otherTransformationResult are on downstream
			// side, outgoing from union operator of currentTransformationResult
			postOptimizationElement
					.setStartOperator(currentTransformationResult
							.getUnionOperator());
			postOptimizationElement
					.setAllowsModificationAfterUnion(currentTransformationResult
							.allowsModificationAfterUnion());
			postOptimizationElement.setEndOperator(otherTransformationResult
					.getFragmentOperators().get(0));
		} else {
			ILogicalOperator result2 = LogicalGraphHelper
					.findDownstreamOperatorWithId(currentTransformationResult
							.getFragmentOperators().get(0)
							.getUniqueIdentifier().toString(),
							otherTransformationResult.getUnionOperator());
			if (result2 != null) {
				// fragment operators of currentTransformationResult are on
				// downstream side, outgoing from union operator of
				// otherTransformationResult
				postOptimizationElement
						.setStartOperator(otherTransformationResult
								.getUnionOperator());
				postOptimizationElement
						.setAllowsModificationAfterUnion(otherTransformationResult
								.allowsModificationAfterUnion());
				postOptimizationElement
						.setEndOperator(currentTransformationResult
								.getFragmentOperators().get(0));
			} else {
				INFO_SERVICE
						.warning("Cannot find connection between union and fragment operators");
				return null;
			}
		}

		return postOptimizationElement;
	}

	private static boolean doPostOptimizationElementAlreadyExists(
			List<PostOptimizationElement> postOptimizationElements,
			TransformationResult currentTransformationResult,
			TransformationResult otherTransformationResult) {
		for (PostOptimizationElement element : postOptimizationElements) {
			if (element
					.getStartOperator()
					.getUniqueIdentifier()
					.equals(currentTransformationResult.getUnionOperator()
							.getUniqueIdentifier())
					&& element
							.getEndOperator()
							.getUniqueIdentifier()
							.equals(otherTransformationResult
									.getFragmentOperators().get(0)
									.getUniqueIdentifier())) {
				return true;
			} else if (element.getStartOperator().getUniqueIdentifier()
					.equals(otherTransformationResult.getUnionOperator().getUniqueIdentifier())
					&& element
							.getEndOperator()
							.getUniqueIdentifier()
							.equals(currentTransformationResult.getFragmentOperators().get(0)
									.getUniqueIdentifier())) {
				return true;
			}
		}
		return false;
	}

	private static Pair<HashFragmentAO, HashFragmentAO> getFragmentOperatorsWithEqualAttributes(
			List<HashFragmentAO> thisFragmentations,
			List<HashFragmentAO> otherFragmentations) {
		for (HashFragmentAO thisFragmentAO : thisFragmentations) {
			for (HashFragmentAO otherFragmentAO : otherFragmentations) {
				List<SDFAttribute> thisAttributes = thisFragmentAO
						.getAttributes();
				List<SDFAttribute> otherAttributes = otherFragmentAO
						.getAttributes();

				if (thisAttributes.size() != otherAttributes.size()) {
					// number of attributes is not equal
					continue;
				} else {
					int attributeFoundCounter = 0;
					for (SDFAttribute thisAttribute : thisAttributes) {
						for (SDFAttribute otherAttribute : otherAttributes) {
							if (thisAttribute.equals(otherAttribute)) {
								attributeFoundCounter++;
								break;
							}
						}
					}
					if (attributeFoundCounter == thisAttributes.size()) {
						Pair<HashFragmentAO, HashFragmentAO> hashPair = new Pair<HashFragmentAO, HashFragmentAO>();
						hashPair.setE1(thisFragmentAO);
						hashPair.setE2(otherFragmentAO);
						return hashPair;
					}
				}

			}
		}
		return null;
	}
}
