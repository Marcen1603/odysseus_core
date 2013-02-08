package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.math3.linear.RealMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.metadata.CombinedMergeFunction;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.interval.transform.join.JoinTransformationHelper;
import de.uniol.inf.is.odysseus.intervalapproach.DefaultTIDummyDataCreation;
import de.uniol.inf.is.odysseus.intervalapproach.JoinTIPO;
import de.uniol.inf.is.odysseus.intervalapproach.TITransferArea;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.TransformUtil;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ITimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.ProbabilisticMergeFunction;
import de.uniol.inf.is.odysseus.probabilistic.metadata.TimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 * @param <K>
 * @param <T>
 */
public class ContinuousProbabilisticEquiJoinPO<K extends ITimeInterval, T extends ProbabilisticTuple<K>>
		extends JoinTIPO<K, T> {

	private final RealMatrix[][] sigmas;
	private final RealMatrix[][] betas;
	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory
			.getLogger(ContinuousProbabilisticEquiJoinPO.class);

	public ContinuousProbabilisticEquiJoinPO() {
		super();
		this.sigmas = new RealMatrix[2][];
		this.betas = new RealMatrix[2][];
	}

	public RealMatrix[] getBetas(int port) {
		return this.betas[port];
	}

	public void setBetas(RealMatrix[] betas, int port) {
		this.betas[port] = betas;
	}

	public RealMatrix[] getSigmas(int port) {
		return this.sigmas[port];
	}

	public void setSigmas(RealMatrix[] sigmas, int port) {
		this.sigmas[port] = sigmas;
	}

	@Override
	protected void process_next(T object, int port) {
		// transferFunction.newElement(object, port);
		System.out.println("New Element: " + object + " on port " + port);
		// if (isDone()) {
		// return;
		// }

		int otherport = port ^ 1;
		Order order = Order.fromOrdinal(port);

		if (inOrder) {
			synchronized (this.areas[otherport]) {
				areas[otherport].purgeElements(object, order);
			}
		}

		// synchronized (this) {
		// if (isDone()) {
		// propagateDone();
		// return;
		// }
		// }
		Iterator<T> qualifies;
		synchronized (this.areas) {
			synchronized (this.areas[otherport]) {
				qualifies = areas[otherport].queryCopy(object, order);
			}
			synchronized (areas[port]) {
				areas[port].insert(object);
			}
		}

		while (qualifies.hasNext()) {
			T next = qualifies.next();
			T newElement = dataMerge.merge(object, next, metadataMerge, order);

			transferFunction.transfer(newElement);

			System.out.println("Transfer: " + object + " on port " + port);
		}
	}

	/**
	 * @param args
	 */
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	public static void main(String[] args) {

		Collection<SDFAttribute> leftAttr = new ArrayList<SDFAttribute>();
		leftAttr.add(new SDFAttribute("", "a", SDFDatatype.DOUBLE));
		leftAttr.add(new SDFAttribute("", "b", SDFDatatype.DOUBLE));
		leftAttr.add(new SDFAttribute("", "c", SDFDatatype.DOUBLE));

		Collection<SDFAttribute> rightAttr = new ArrayList<SDFAttribute>();
		rightAttr.add(new SDFAttribute("", "x", SDFDatatype.DOUBLE));
		rightAttr.add(new SDFAttribute("", "y", SDFDatatype.DOUBLE));
		rightAttr.add(new SDFAttribute("", "z", SDFDatatype.DOUBLE));

		SDFSchema[] schemas = new SDFSchema[] { new SDFSchema("", leftAttr),
				new SDFSchema("", rightAttr) };

		ContinuousProbabilisticEquiJoinPO joinPO = new ContinuousProbabilisticEquiJoinPO<>();

		joinPO.setJoinPredicate(new RelationalPredicate(new SDFExpression(
				"a=x", MEP.getInstance())));
		joinPO.setTransferFunction(new TITransferArea());
		joinPO.setMetadataMerge(new CombinedMergeFunction());
		((CombinedMergeFunction) joinPO.getMetadataMerge())
				.add(new ProbabilisticMergeFunction());
		joinPO.setCreationFunction(new DefaultTIDummyDataCreation());

		ContinuousProbabilisticEquiJoinTISweepArea[] areas = new ContinuousProbabilisticEquiJoinTISweepArea[2];

		for (int port = 0; port < 2; port++) {
			int otherPort = port ^ 1;

			Set<Pair<SDFAttribute, SDFAttribute>> neededAttrs = new TreeSet<Pair<SDFAttribute, SDFAttribute>>();

			if (JoinTransformationHelper.checkPredicate(joinPO.getPredicate(),
					neededAttrs, schemas[port], schemas[otherPort])) {
				SDFSchema schema = schemas[port];

				List<SDFAttribute> joinAttributes = new ArrayList<SDFAttribute>();

				for (Pair<SDFAttribute, SDFAttribute> pair : neededAttrs) {
					if (TransformUtil.isContinuousProbabilisticAttribute(pair
							.getE2())) {
						joinAttributes.add(pair.getE1());
					}
				}
				int[] joinPos = TransformUtil.getAttributePos(schema,
						joinAttributes);

				List<SDFAttribute> viewAttributes = new ArrayList<SDFAttribute>(
						schema.getAttributes());
				viewAttributes.removeAll(joinAttributes);
				int[] viewPos = TransformUtil.getAttributePos(schema,
						viewAttributes);
				areas[port] = new ContinuousProbabilisticEquiJoinTISweepArea(
						joinPos, viewPos);
				joinPO.setBetas(areas[port].getBetas(), port);
			}

		}

		joinPO.setAreas(areas);

		int[][] joinAttributePos = new int[2][];

		for (int port = 0; port < 2; port++) {
			int otherPort = port ^ 1;
			Set<Pair<SDFAttribute, SDFAttribute>> neededAttrs = new TreeSet<Pair<SDFAttribute, SDFAttribute>>();

			if (JoinTransformationHelper.checkPredicate(joinPO.getPredicate(),
					neededAttrs, schemas[port], schemas[otherPort])) {

				SDFSchema schema = schemas[port];

				List<SDFAttribute> joinAttributes = new ArrayList<SDFAttribute>();

				for (Pair<SDFAttribute, SDFAttribute> pair : neededAttrs) {
					if (TransformUtil.isContinuousProbabilisticAttribute(pair
							.getE2())) {
						joinAttributes.add(pair.getE1());
					}
				}
				joinAttributePos[port] = TransformUtil.getAttributePos(schema,
						joinAttributes);

				List<SDFAttribute> viewAttributes = new ArrayList<SDFAttribute>(
						schema.getAttributes());
				viewAttributes.removeAll(joinAttributes);
				int[] viewPos = TransformUtil.getAttributePos(schema,
						viewAttributes);
			}

		}

		// ContinuousProbabilisticEquiJoinMergeFunction<ITimeIntervalProbabilistic>
		// mergeFunction = new
		// ContinuousProbabilisticEquiJoinMergeFunction<ITimeIntervalProbabilistic>(
		// operator.getOutputSchema().size(), joinAttributePos);
		//
		// for (int port = 0; port < 2; port++) {
		// mergeFunction.setBetas(operator.getBetas(port), port);
		// mergeFunction.setSigmas(operator.getSigmas(port), port);
		// }
		// operator.setDataMerge(mergeFunction);

		Object[] attributes1 = new Object[] { 1.0, 2.0, 3.0 };
		Object[] attributes2 = new Object[] { 4.0, 5.0, 6.0 };
		Object[] attributes3 = new Object[] { 7.0, 8.0, 9.0 };
		Object[] attributes4 = new Object[] { 10.0, 11.0, 12.0 };
		Object[] attributes5 = new Object[] { 13.0, 14.0, 15.0 };
		Object[] attributes6 = new Object[] { 16.0, 17.0, 18.0 };
		Object[] attributes7 = new Object[] { 19.0, 20.0, 21.0 };

		ProbabilisticTuple<ITimeIntervalProbabilistic> tuple1 = new ProbabilisticTuple<>(
				attributes1, true);
		tuple1.setMetadata(new TimeIntervalProbabilistic());
		tuple1.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeIntervalProbabilistic> tuple2 = new ProbabilisticTuple<>(
				attributes2, true);
		tuple2.setMetadata(new TimeIntervalProbabilistic());
		tuple2.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeIntervalProbabilistic> tuple3 = new ProbabilisticTuple<>(
				attributes3, true);
		tuple3.setMetadata(new TimeIntervalProbabilistic());
		tuple3.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeIntervalProbabilistic> tuple4 = new ProbabilisticTuple<>(
				attributes4, true);
		tuple4.setMetadata(new TimeIntervalProbabilistic());
		tuple4.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeIntervalProbabilistic> tuple5 = new ProbabilisticTuple<>(
				attributes5, true);
		tuple5.setMetadata(new TimeIntervalProbabilistic());
		tuple5.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeIntervalProbabilistic> tuple6 = new ProbabilisticTuple<>(
				attributes6, true);
		tuple6.setMetadata(new TimeIntervalProbabilistic());
		tuple6.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeIntervalProbabilistic> tuple7 = new ProbabilisticTuple<>(
				attributes7, true);
		tuple7.setMetadata(new TimeIntervalProbabilistic());
		tuple7.getMetadata().setStart(PointInTime.currentPointInTime());

		joinPO.process_next(tuple1, 0);
		joinPO.process_next(tuple2, 1);
		joinPO.process_next(tuple3, 0);
		joinPO.process_next(tuple4, 1);
		joinPO.process_next(tuple5, 0);
		joinPO.process_next(tuple6, 1);
		joinPO.process_next(tuple7, 0);
	}
}
