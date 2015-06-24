package de.uniol.inf.is.odysseus.multithreaded.interoperator.transform;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.multithreaded.interoperator.helper.LogicalGraphHelper;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.AbstractFragmentAO;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

public class PostOptimizationHandler {

	final private static InfoService INFO_SERVICE = InfoServiceFactory
			.getInfoService(PostOptimizationHandler.class);

	@SuppressWarnings("unused")
	public static void doPostOptimization(ILogicalOperator logicalPlan,
			List<TransformationResult> transformationResults,
			boolean optimizationAllowed) {
		if (optimizationAllowed) {
			if (!transformationResults.isEmpty()
					&& transformationResults.size() > 1) {
				if (validateTransformationResults(transformationResults)) {
					List<Pair<TransformationResult, TransformationResult>> matchingTransformations = validateFragmentations(transformationResults);
					if (!matchingTransformations.isEmpty()) {
						// do it
						for (Pair<TransformationResult, TransformationResult> pair : matchingTransformations) {

						}
					} else {
						INFO_SERVICE
								.info("Optimization not possible, because there are no fragmentations found, that can be combined.");
						return;
					}
				} else {
					INFO_SERVICE
							.warning("At least one transformation result is invalid");
				}
			} else {
				INFO_SERVICE
						.info("Optimization of plan is not needed, because no or only one strategy has been processed");
				return;
			}
		} else {
			INFO_SERVICE.info("Optimization of plan is disabled");
			return;
		}
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

	/**
	 * iterates over all existing fragmentations and check if equal
	 * fragmentations exists. Only if one pair exists a post optimization is
	 * possible
	 * 
	 * @param transformationResults
	 */
	private static List<Pair<TransformationResult, TransformationResult>> validateFragmentations(
			List<TransformationResult> transformationResults) {
		List<Pair<TransformationResult, TransformationResult>> matchingTransformations = new ArrayList<Pair<TransformationResult, TransformationResult>>();
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
							// check if it is a hash fragment, if so, we need to
							// check if the attributes are equal
							if (currentFragementAO instanceof HashFragmentAO) {
								// we know that both fragment have the same type
								// and we know that one is a hash fragment, so
								// both can be casted
								if (!fragmentAttributesAreEqual(
										currentTransformationResult
												.getFragmentOperators(),
										otherTransformationResult
												.getFragmentOperators())) {
									continue;
								}
							}

							// check if pair already exists
							addMatchingPair(matchingTransformations,
									currentTransformationResult,
									otherTransformationResult);
						}
					}
				}
			}
		}

		return matchingTransformations;
	}

	private static void addMatchingPair(
			List<Pair<TransformationResult, TransformationResult>> matchingTransformations,
			TransformationResult currentTransformationResult,
			TransformationResult otherTransformationResult) {
		if (!transformationPairAlreadyExists(matchingTransformations,
				currentTransformationResult, otherTransformationResult)) {
			Pair<TransformationResult, TransformationResult> matchingPair = createOrderedMatchingPair(
					currentTransformationResult, otherTransformationResult);
			if (matchingPair != null){
				if (matchingPair.getE1().allowsModificationAfterUnion()) {
					matchingTransformations.add(matchingPair);
				}				
			}
		}
	}

	private static Pair<TransformationResult, TransformationResult> createOrderedMatchingPair(
			TransformationResult currentTransformationResult,
			TransformationResult otherTransformationResult) {
		Pair<TransformationResult, TransformationResult> matchingPair = new Pair<TransformationResult, TransformationResult>();

		// check order
		ILogicalOperator result = LogicalGraphHelper
				.findDownstreamOperatorWithId(otherTransformationResult
						.getFragmentOperators().get(0).getUniqueIdentifier()
						.toString(),
						currentTransformationResult.getUnionOperator());
		if (result != null) {
			// fragment operators of otherTransformationResult are on downstream
			// side, outgoing from union operator of currentTransformationResult
			matchingPair.setE1(currentTransformationResult);
			matchingPair.setE2(otherTransformationResult);
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
				matchingPair.setE1(otherTransformationResult);
				matchingPair.setE2(currentTransformationResult);
			} else {
				INFO_SERVICE
						.warning("Cannot find connection between union and fragment operators");
				return null;
			}
		}

		return matchingPair;
	}

	private static boolean transformationPairAlreadyExists(
			List<Pair<TransformationResult, TransformationResult>> matchingTransformations,
			TransformationResult currentTransformationResult,
			TransformationResult otherTransformationResult) {
		for (Pair<TransformationResult, TransformationResult> pair : matchingTransformations) {
			if (pair.getE1().getUniqueIdentifier()
					.equals(currentTransformationResult.getUniqueIdentifier())
					&& pair.getE2()
							.getUniqueIdentifier()
							.equals(otherTransformationResult
									.getUniqueIdentifier())) {
				return true;
			} else if (pair.getE1().getUniqueIdentifier()
					.equals(otherTransformationResult.getUniqueIdentifier())
					&& pair.getE2()
							.getUniqueIdentifier()
							.equals(currentTransformationResult
									.getUniqueIdentifier())) {
				return true;
			}
		}
		return false;
	}

	private static boolean fragmentAttributesAreEqual(
			List<AbstractFragmentAO> thisFragmentations,
			List<AbstractFragmentAO> otherFragmentations) {
		for (AbstractFragmentAO thisFragmentAO : thisFragmentations) {
			for (AbstractFragmentAO otherFragmentAO : otherFragmentations) {
				List<SDFAttribute> thisAttributes = ((HashFragmentAO) thisFragmentAO)
						.getAttributes();
				List<SDFAttribute> otherAttributes = ((HashFragmentAO) otherFragmentAO)
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
						return true;
					}
				}

			}
		}
		return false;
	}
}
