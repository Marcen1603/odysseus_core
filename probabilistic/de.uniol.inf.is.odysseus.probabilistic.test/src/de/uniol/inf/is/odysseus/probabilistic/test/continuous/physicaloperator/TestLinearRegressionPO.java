package de.uniol.inf.is.odysseus.probabilistic.test.continuous.physicaloperator;

import java.util.ArrayList;
import java.util.Collection;

import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.LinearRegressionPO;
import de.uniol.inf.is.odysseus.probabilistic.metadata.TimeIntervalProbabilistic;

@SuppressWarnings("unused")
public class TestLinearRegressionPO {

	@Test
	public void testLinearRegression() {
		final Collection<SDFAttribute> attr = new ArrayList<SDFAttribute>();
		attr.add(new SDFAttribute("", "a", SDFDatatype.DOUBLE));
		attr.add(new SDFAttribute("", "b", SDFDatatype.DOUBLE));
		attr.add(new SDFAttribute("", "c", SDFDatatype.DOUBLE));
		attr.add(new SDFAttribute("", "d", SDFDatatype.DOUBLE));

		final SDFSchema schema = new SDFSchema("", attr);
		final Object[] attributes1 = new Object[] { 1.0, 1.0, 6.0, 11.0 };
		final Object[] attributes2 = new Object[] { 1.0, 2.0, 5.0, 12.0 };
		final Object[] attributes3 = new Object[] { 1.0, 3.0, 7.0, 13.0 };
		final Object[] attributes4 = new Object[] { 1.0, 4.0, 8.0, 14.0 };
		final Object[] attributes5 = new Object[] { 1.0, 5.0, 9.0, 15.0 };
		final Object[] attributes6 = new Object[] { 1.0, 6.0, 10.0, 16.0 };

		final ProbabilisticTuple<ITimeInterval> tuple1 = new ProbabilisticTuple<>(
				attributes1, true);
		tuple1.setMetadata(new TimeIntervalProbabilistic());
		tuple1.getMetadata().setStart(PointInTime.currentPointInTime());

		final ProbabilisticTuple<ITimeInterval> tuple2 = new ProbabilisticTuple<>(
				attributes2, true);
		tuple2.setMetadata(new TimeIntervalProbabilistic());
		tuple2.getMetadata().setStart(PointInTime.currentPointInTime());

		final ProbabilisticTuple<ITimeInterval> tuple3 = new ProbabilisticTuple<>(
				attributes3, true);
		tuple3.setMetadata(new TimeIntervalProbabilistic());
		tuple3.getMetadata().setStart(PointInTime.currentPointInTime());

		final ProbabilisticTuple<ITimeInterval> tuple4 = new ProbabilisticTuple<>(
				attributes4, true);
		tuple4.setMetadata(new TimeIntervalProbabilistic());
		tuple4.getMetadata().setStart(PointInTime.currentPointInTime());

		final ProbabilisticTuple<ITimeInterval> tuple5 = new ProbabilisticTuple<>(
				attributes5, true);
		tuple5.setMetadata(new TimeIntervalProbabilistic());
		tuple5.getMetadata().setStart(PointInTime.currentPointInTime());

		final ProbabilisticTuple<ITimeInterval> tuple6 = new ProbabilisticTuple<>(
				attributes6, true);
		tuple6.setMetadata(new TimeIntervalProbabilistic());
		tuple6.getMetadata().setStart(PointInTime.currentPointInTime());

		final LinearRegressionPO<ITimeInterval> linearRegression = new LinearRegressionPO<ITimeInterval>(
				new int[] { 0, 3 }, new int[] { 1, 2 });
		// linearRegression.process_next(tuple1, 0);
		// linearRegression.process_next(tuple2, 0);
		// linearRegression.process_next(tuple3, 0);
		// linearRegression.process_next(tuple4, 0);
		// linearRegression.process_next(tuple5, 0);
		// linearRegression.process_next(tuple6, 0);

	}

}
