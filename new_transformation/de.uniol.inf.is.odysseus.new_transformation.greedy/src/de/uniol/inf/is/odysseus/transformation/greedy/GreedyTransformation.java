package de.uniol.inf.is.odysseus.transformation.greedy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import de.uniol.inf.is.odysseus.new_transformation.costmodel.base.TransformedPO;
import de.uniol.inf.is.odysseus.new_transformation.stream_characteristics.StreamCharacteristicCollection;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISource;
import de.uniol.inf.is.odysseus.physicaloperator.base.PhysicalSubscription;

public class GreedyTransformation implements ITransformation {
	private static Logger logger = LoggerFactory.getLogger(GreedyTransformation.class);

	private final TransformationDatabase transformationDatabase = TransformationDatabase.getInstance();
	private final Map<ILogicalOperator, StreamCharacteristicCollection> streamMetadata = new HashMap<ILogicalOperator, StreamCharacteristicCollection>();
	private ICostModel costModel;

	public void bindCostModel(ICostModel costModel) {
		this.costModel = costModel;
	}

	public void unbindCostModel(ICostModel costModel) {
		this.costModel = null;
	}

	@Override
	public IPhysicalOperator transform(ILogicalOperator op, TransformationConfiguration config)
			throws TransformationException {
		logger.info("Greedy Transformation Start");

		if (costModel == null) {
			throw new TransformationException("Missing CostModel while using GreedyTransformation");
		}
		TopAO top = new TopAO();
		op.subscribeSink(top, 0, 0, op.getOutputSchema());

		// transformiere diesen Plan bottom-up
		Map<ILogicalOperator, ILogicalOperator> transformedOperators = new IdentityHashMap<ILogicalOperator, ILogicalOperator>();
		transformOperator(op, transformedOperators, config);

		IPhysicalOperator physicalOp = top.getPhysicalInput();
		if (logger.isInfoEnabled()) {
			logger.info("transformation result: \n" + planToString(physicalOp, ""));
		}
		op.unsubscribeSink(top, 0, 0, op.getOutputSchema());

		return physicalOp;
	}

	/**
	 * Bottum-Up transformation of this Plan.
	 * 
	 * @param logicalOperator
	 *            - the current {@link ILogicalOperator} that should be
	 *            transformed
	 * @param transformedOperators
	 *            - a {@link Map} with already transformed operators
	 * @param config
	 *            - the {@link TransformationConfiguration} for this
	 *            Transformation
	 * 
	 * @throws TransformationException
	 *             - if the current operator cannot be transformed
	 */
	private void transformOperator(ILogicalOperator logicalOperator,
			Map<ILogicalOperator, ILogicalOperator> transformedOperators, TransformationConfiguration config)
			throws TransformationException {

		if (logicalOperator == null || logicalOperator instanceof TopAO) {
			return;
		}

		if (!transformedOperators.containsKey(logicalOperator)) {
			transformedOperators.put(logicalOperator, logicalOperator);

			Collection<LogicalSubscription> children = logicalOperator.getSubscribedToSource();
			for (LogicalSubscription logicalChild : children) {
				transformOperator(logicalChild.getTarget(), transformedOperators, config);
			}

			transformLogicalToPhysical(logicalOperator, config);

			Collection<LogicalSubscription> parents = logicalOperator.getSubscriptions();
			for (LogicalSubscription logicalParent : parents) {
				transformOperator(logicalParent.getTarget(), transformedOperators, config);
			}
		}
	}

	/**
	 * Transformiert den {@link ILogicalOperator} zu einem
	 * {@link IPhysicalOperator}, der nach dem benutzten Kostenmodell der beste
	 * ist.
	 * 
	 * @param logicalOperator
	 *            - der zu transformierende {@link ILogicalOperator}
	 * @param config
	 * @throws TransformationException
	 */
	private void transformLogicalToPhysical(ILogicalOperator logicalOperator, TransformationConfiguration config)
			throws TransformationException {
		logger.info("transform: " + logicalOperator.getName());

		IPOTransformator<ILogicalOperator> transformator = findBestTransformator(logicalOperator, config);

		// transformiere den Operator und platziere den erstellten physischen
		// Operator in dem physischen Baum anstelle des logischen Operators
		TransformedPO transformedPO = transformator.transform(logicalOperator, config, this);

		// Wenn ein Transformator keinen PO erzeugt (null), loesche den AO im
		// Anfrageplan. Dies ist z.B. im Fall des unbounded Windows so.
		// Wenn ein PO erzeugt wurde, setze in in den Plan ein.
		if (transformedPO == null) {
			replace(logicalOperator, logicalOperator.getPhysInputPOs().iterator().next());
		} else {
			replace(logicalOperator, transformedPO.getSink());
			replace(logicalOperator, transformedPO.getSource());
		}

		// update die Metadaten vom Strom, indem die Metadaten der Kinder
		// gemerged werden
		List<StreamCharacteristicCollection> incomingStreamCharacteristics = getIncomingStreamCharacteristics(logicalOperator);
		StreamCharacteristicCollection outgoingStreamCharacteristics = costModel.mergeStreamMetadata(transformator,
				logicalOperator,incomingStreamCharacteristics);
		streamMetadata.put(logicalOperator, outgoingStreamCharacteristics);
	}

	/**
	 * Collects all incoming {@link StreamCharacteristicCollection}s for this
	 * {@link ILogicalOperator}.
	 * 
	 * @param logicalOperator
	 * @return a {@link List} of the incoming
	 *         {@link StreamCharacteristicCollection}s
	 */
	private List<StreamCharacteristicCollection> getIncomingStreamCharacteristics(ILogicalOperator logicalOperator) {
		Collection<LogicalSubscription> childSubscriptions = logicalOperator.getSubscribedToSource();
		List<StreamCharacteristicCollection> incomingStreamMetadatas = new ArrayList<StreamCharacteristicCollection>();
		for (LogicalSubscription childSubscription : childSubscriptions) {
			ILogicalOperator child = childSubscription.getTarget();
			incomingStreamMetadatas.add(streamMetadata.get(child));
		}

		return incomingStreamMetadatas;
	}

	private void replace(ILogicalOperator logical, ISource<?> physical) {
		if (physical == null)
			return;
		for (LogicalSubscription l : logical.getSubscriptions()) {
			l.getTarget().setPhysSubscriptionTo(physical, l.getSinkInPort(), l.getSourceOutPort(), l.getSchema());
		}
	}

	private void replace(ILogicalOperator logical, ISink<?> physical) {
		if (physical == null)
			return;
		for (Subscription<ISource<?>> psub : logical.getPhysSubscriptionsTo()) {
			physical.subscribeToSource((ISource) psub.getTarget(), psub.getSinkInPort(), psub.getSourceOutPort(), psub
					.getSchema());
		}
	}

	/**
	 * Find the best {@link IPOTransformator} for this {@link ILogicalOperator}
	 * 
	 * @param logicalOperator
	 *            - the {@link ILogicalOperator} that should be transformed
	 * @param config
	 *            - the {@link TransformationConfiguration} of the current
	 *            Transformation Process
	 * @return the found {@link IPOTransformator}
	 * @throws TransformationException
	 *             if no Transformator was found
	 */
	private IPOTransformator<ILogicalOperator> findBestTransformator(ILogicalOperator logicalOperator,
			TransformationConfiguration config) throws TransformationException {

		// hole alle Implementierungen des logischen Operators (min. 1)
		SortedLinkedList operatorTransformations = transformationDatabase.getOperatorTransformations(logicalOperator,
				config);
		if (operatorTransformations == null || operatorTransformations.isEmpty()) {
			throw new TransformationException("No Transformation Found for: " + logicalOperator);
		}

		Iterator<IPOTransformator<ILogicalOperator>> iterator = operatorTransformations.iterator();
		IPOTransformator<ILogicalOperator> bestTransformator = null;
		ICost bestCost = null;

		// starte mit dem ersten Element, dass ausgefuert werden kann als
		// "bestes" Element
		while (iterator.hasNext()) {
			IPOTransformator<ILogicalOperator> transformator = iterator.next();
			if (transformator.canExecute(logicalOperator, config)) {
				bestTransformator = transformator;
				List<StreamCharacteristicCollection> incomingStreamCharacteristics = getIncomingStreamCharacteristics(logicalOperator);
				bestCost = costModel.calculateOperatorCost(bestTransformator, incomingStreamCharacteristics);
				break;
			}
		}

		// es gibt nichtmal einen, der ausgefuehrt werden kann
		if (bestTransformator == null) {
			logger.error("No Transformation Found for: " + logicalOperator);
			throw new TransformationException("No Transformation Found for: " + logicalOperator);
		}

		// suche nach einem besseren
		while (iterator.hasNext()) {
			IPOTransformator<ILogicalOperator> transformator = iterator.next();
			if (bestTransformator.getPriority() > transformator.getPriority())
				break;

			if (!transformator.canExecute(logicalOperator, config))
				continue;

			List<StreamCharacteristicCollection> incomingStreamCharacteristics = getIncomingStreamCharacteristics(logicalOperator);
			ICost newCost = costModel.calculateOperatorCost(transformator, incomingStreamCharacteristics);
			if (newCost.isBetterThan(bestCost)) {
				bestCost = newCost;
				bestTransformator = transformator;
			}
		}
		return bestTransformator;
	}

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
