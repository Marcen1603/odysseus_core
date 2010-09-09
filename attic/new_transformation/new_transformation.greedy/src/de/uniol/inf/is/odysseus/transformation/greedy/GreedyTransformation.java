package de.uniol.inf.is.odysseus.transformation.greedy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.ITransformation;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.Subscription;
import de.uniol.inf.is.odysseus.base.TransformationConfiguration;
import de.uniol.inf.is.odysseus.base.TransformationException;
import de.uniol.inf.is.odysseus.logicaloperator.base.TopAO;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.ICost;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.ICostModel;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.IPOTransformator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TempTransformationOperator;
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.new_transformation.stream_characteristics.StreamCharacteristicCollection;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.FindQueryRootsVisitor;
import de.uniol.inf.is.odysseus.util.PrintGraphVisitor;

/**
 * An {@link ITransformation} which evaluates the cost of multiple
 * transformation choices.
 * 
 */
public class GreedyTransformation implements ITransformation {
	private static Logger logger = LoggerFactory.getLogger(GreedyTransformation.class);

	/**
	 * The used {@link TransformationDatabase} which stores every
	 * transformation-"rule"
	 */
	private final TransformationDatabase transformationDatabase = TransformationDatabase.getInstance();

	private ICostModel costModel;

	public void bindCostModel(ICostModel costModel) {
		this.costModel = costModel;
	}

	public void unbindCostModel(ICostModel costModel) {
		this.costModel = null;
	}

	@Override
	public ArrayList<IPhysicalOperator> transform(ILogicalOperator op, TransformationConfiguration config)
			throws TransformationException {
		logger.info("Greedy Transformation Start");

		if (costModel == null) {
			throw new TransformationException("CostModel is missing while using GreedyTransformation");
		}

		TopAO top = new TopAO();
		op.subscribeSink(top, 0, 0, op.getOutputSchema());

		// transformiere diesen Plan bottom-up
		Map<ILogicalOperator, TempTransformationOperator> transformationMap = new IdentityHashMap<ILogicalOperator, TempTransformationOperator>();
		transformBottomUp(op, transformationMap, config);

		IPhysicalOperator physicalOp = top.getPhysicalInput();
		
		// The physical plan can have more than one
		// root. So find all roots in the physical plan
		// that have no owner. These roots belong to the
		// current query.
		FindQueryRootsVisitor visitor = new FindQueryRootsVisitor<IPhysicalOperator>();
		AbstractGraphWalker walker = new AbstractGraphWalker();
		walker.prefixWalkPhysical(physicalOp, visitor);
		ArrayList<IPhysicalOperator> queryRoots = visitor.getResult(); 
		
		if (logger.isInfoEnabled()) {
			PrintGraphVisitor<ILogicalOperator> pv = new PrintGraphVisitor<ILogicalOperator>();
			new AbstractGraphWalker().prefixWalk(top, pv);
			logger.info("Transformation result: \n" + pv.getResult());
		}
		
		op.unsubscribeSink(top, 0, 0, op.getOutputSchema());

		
		
		return queryRoots;
	}

	/**
	 * Bottum-Up Transformation of the logical Plan.
	 * 
	 * @param op
	 *            - the {@link ILogicalOperator} which should be transformed
	 * @param transformationMap
	 *            - a {@link Map} to save, which {@link ILogicalOperator}s were
	 *            already transformed
	 * @param config
	 *            - the {@link TransformationConfiguration} of this
	 *            transformation
	 * @throws TransformationException
	 */
	private void transformBottomUp(ILogicalOperator op,
			Map<ILogicalOperator, TempTransformationOperator> transformationMap, TransformationConfiguration config)
			throws TransformationException {

		if (op == null || op instanceof TopAO || transformationMap.containsKey(op)) {
			return;
		}

		// erst kinder
		Collection<LogicalSubscription> children = op.getSubscribedToSource();
		for (LogicalSubscription child : children) {
			transformBottomUp(child.getTarget(), transformationMap, config);
		}

		// dann selbst
		TempTransformationOperator to = getTransformOperator(op, transformationMap, config);
		IPOTransformator<ILogicalOperator> transformator = to.getProperty("associatedTransformator");
		applyTransformation(op, transformator, config);
		transformationMap.put(op, to);

		// dann eltern
		Collection<LogicalSubscription> parents = op.getSubscriptions();
		for (LogicalSubscription parent : parents) {
			transformBottomUp(parent.getTarget(), transformationMap, config);
		}
	}

	/**
	 * Replace the logical operator with a physical one
	 * 
	 * @param logical
	 *            - the {@link ILogicalOperator} to replace
	 * @param physical
	 *            - the target {@link ISource}
	 */
	private void replace(ILogicalOperator logical, ISource<?> physical) {
		if (physical == null)
			return;
		for (LogicalSubscription l : logical.getSubscriptions()) {
			l.getTarget().setPhysSubscriptionTo(physical, l.getSinkInPort(), l.getSourceOutPort(), l.getSchema());
		}
	}

	/**
	 * Replace the logical operator with a physical one
	 * 
	 * @param logical
	 *            - the {@link ILogicalOperator} to replace
	 * @param physical
	 *            - the target {@link ISink}
	 */
	private void replace(ILogicalOperator logical, ISink<?> physical) {
		if (physical == null)
			return;
		for (Subscription<ISource<?>> psub : logical.getPhysSubscriptionsTo()) {
			physical.subscribeToSource((ISource) psub.getTarget(), psub.getSinkInPort(), psub.getSourceOutPort(), psub
					.getSchema());
		}
	}

	/**
	 * Uses an {@link IPOTransformator} to replace a logical operator.
	 * 
	 * @param op
	 *            - the {@link ILogicalOperator} to replace
	 * @param transformator
	 *            - the {@link IPOTransformator} to use
	 * @param config
	 *            - the used {@link TransformationConfiguration}
	 * @throws TransformationException
	 *             if the transformator cannot transform the logical operator
	 */
	private void applyTransformation(ILogicalOperator op, IPOTransformator<ILogicalOperator> transformator,
			TransformationConfiguration config) throws TransformationException {
		// transformiere den Operator und platziere den erstellten physischen
		// Operator in dem physischen Baum anstelle des logischen Operators
		TransformedPO transformedPO = transformator.transform(op, config, this);

		// Wenn ein Transformator keinen PO erzeugt (null), loesche den AO im
		// Anfrageplan. Dies ist z.B. im Fall des unbounded Windows so.
		// Wenn ein PO erzeugt wurde, setze in in den Plan ein.
		if (transformedPO == null) {
			replace(op, op.getPhysInputPOs().iterator().next());
		} else {
			replace(op, transformedPO.getSink());
			replace(op, transformedPO.getSource());
		}
	}

	/**
	 * Returns the "best" transformator (with a simple greedy search) for a
	 * given {@link ILogicalOperator} and merges the incoming
	 * StreamCharacteristics.
	 * 
	 * @param op
	 *            - the {@link ILogicalOperator} to which a transformator should
	 *            be found
	 * @param transformationMap
	 *            - a {@link Map}, used to find the incoming
	 *            StreamCharacteristics
	 * @param config
	 *            - the {@link TransformationConfiguration} of this
	 *            transformation
	 * @return a {@link TempTransformationOperator}
	 * @throws TransformationException
	 *             when no transformator can be found
	 */
	private TempTransformationOperator getTransformOperator(ILogicalOperator op,
			Map<ILogicalOperator, TempTransformationOperator> transformationMap, TransformationConfiguration config)
			throws TransformationException {
		// find operator
		List<StreamCharacteristicCollection> incomingStreamCharacteristics = getIncomingStreamCharacteristics(op,
				transformationMap);
		TempTransformationOperator operator = findBestOperator(op, incomingStreamCharacteristics, config);

		// merge streamCharacteristics
		StreamCharacteristicCollection mergedStreamMetadata = costModel.mergeStreamMetadata(operator, op,
				incomingStreamCharacteristics);
		operator.setStreamCharacteristics(mergedStreamMetadata);

		return operator;
	}

	/**
	 * Returns the "best" transformator (with a simple greedy search) for a
	 * given {@link ILogicalOperator}. Searches all transformations, saved in
	 * the {@link TransformationDatabase}. The operator with the highest
	 * priority and lowest cost is chosen.
	 * 
	 * @param logicalOperator
	 *            - the {@link ILogicalOperator} to which a transformator should
	 *            be found
	 * @param incomingStreamCharacteristics
	 *            - the incoming StreamCharacteristics of this operator
	 * @param config
	 *            - the {@link TransformationConfiguration} of this
	 *            transformation
	 * @return the "best" TempTransformationOperator
	 * @throws TransformationException
	 *             when no transformator can be found
	 */
	private TempTransformationOperator findBestOperator(ILogicalOperator logicalOperator,
			List<StreamCharacteristicCollection> incomingStreamCharacteristics, TransformationConfiguration config)
			throws TransformationException {

		// hole alle Implementierungen des logischen Operators (min. 1)
		SortedLinkedList operatorTransformations = transformationDatabase.getOperatorTransformations(logicalOperator,
				config);
		if (operatorTransformations == null || operatorTransformations.isEmpty()) {
			throw new TransformationException("No Transformation Found for: " + logicalOperator);
		}

		Iterator<IPOTransformator<ILogicalOperator>> iterator = operatorTransformations.iterator();
		IPOTransformator<ILogicalOperator> bestTransformator = null;
		TempTransformationOperator bestOperator = null;
		ICost bestCost = null;

		// starte mit dem ersten Element, dass ausgefuert werden kann als
		// "bestes" Element
		while (iterator.hasNext()) {
			IPOTransformator<ILogicalOperator> transformator = iterator.next();
			if (transformator.canExecute(logicalOperator, config)) {
				bestTransformator = transformator;

				bestOperator = bestTransformator.createTempOperator();
				bestOperator.setProperty("incomingStreamCharacteristics", incomingStreamCharacteristics);
				bestOperator.setProperty("associatedTransformator", transformator);

				bestCost = costModel.calculateCost(bestOperator);

				break;
			}
		}

		// es gibt nichtmal einen, der ausgefuehrt werden kann -> breche ab
		if (bestTransformator == null) {
			logger.error("No Transformation Found for: " + logicalOperator);
			throw new TransformationException("No Transformation Found for: " + logicalOperator);
		}

		// suche nach einem besseren Operator mit mindestens gleich hoher Priorität
		while (iterator.hasNext()) {
			IPOTransformator<ILogicalOperator> transformator = iterator.next();
			if (bestTransformator.getPriority() > transformator.getPriority())
				break;

			if (!transformator.canExecute(logicalOperator, config))
				continue;

			TempTransformationOperator to = transformator.createTempOperator();
			to.setProperty("incomingStreamCharacteristics", incomingStreamCharacteristics);
			ICost newCost = costModel.calculateCost(to);
			if (newCost.isBetterThan(bestCost)) {
				bestCost = newCost;
				bestTransformator = transformator;
				bestOperator = to;
				bestOperator.setProperty("associatedTransformator", transformator);
			}
		}
		
		return bestOperator;
	}

	/**
	 * Retrieves the incoming StreamCharacteristics of a
	 * {@link ILogicalOperator} as a {@link List}. This list is empty if the
	 * operator has no children (AccessAOs).
	 * 
	 * @param op
	 *            - the {@link ILogicalOperator}
	 * @param transformationMap
	 *            - the {@link Map} to search for the already transformed
	 *            {@link TempTransformationOperator}
	 * @return a {@link List} of {@link StreamCharacteristicCollection}s
	 */
	private List<StreamCharacteristicCollection> getIncomingStreamCharacteristics(ILogicalOperator op,
			Map<ILogicalOperator, TempTransformationOperator> transformationMap) {
		Collection<LogicalSubscription> children = op.getSubscribedToSource();
		TempTransformationOperator to;

		List<StreamCharacteristicCollection> incomingStreamCharacteristics = new ArrayList<StreamCharacteristicCollection>();
		for (LogicalSubscription child : children) {
			to = transformationMap.get(child.getTarget());
			incomingStreamCharacteristics.add(to.getStreamCharacteristics());
		}

		return incomingStreamCharacteristics;
	}

	/**
	 * Print a plan top-down, beginning at physicalPO
	 * 
	 * @param physicalPO
	 * @param indent
	 * @return
	 */
	private String planToString(IPhysicalOperator physicalPO, String indent) {
		StringBuilder builder = new StringBuilder();
		builder.append(indent);
		builder.append(physicalPO);
		builder.append('\n');
		if (physicalPO.isSink()) {
			for (PhysicalSubscription<?> sub : ((ISink<?>) physicalPO).getSubscribedToSource()) {
				builder.append(planToString((IPhysicalOperator) sub.getTarget(), "  " + indent));
			}
		}
		return builder.toString();
	}
}
