package de.uniol.inf.is.odysseus.probabilistic.test;

import java.util.ArrayList;
import java.util.Collection;

import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.TimeIntervalProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.LinearRegressionPO;
@SuppressWarnings("unused")
public class TestLinearRegressionPO {



	@Test
	public void testLinearRegression() {
		Collection<SDFAttribute> attr = new ArrayList<SDFAttribute>();
		attr.add(new SDFAttribute("", "a", SDFDatatype.DOUBLE));
		attr.add(new SDFAttribute("", "b", SDFDatatype.DOUBLE));
		attr.add(new SDFAttribute("", "c", SDFDatatype.DOUBLE));
		attr.add(new SDFAttribute("", "d", SDFDatatype.DOUBLE));

		SDFSchema schema = new SDFSchema("", attr);
		Object[] attributes1 = new Object[] { 1.0, 1.0, 6.0, 11.0 };
		Object[] attributes2 = new Object[] { 1.0, 2.0, 5.0, 12.0 };
		Object[] attributes3 = new Object[] { 1.0, 3.0, 7.0, 13.0 };
		Object[] attributes4 = new Object[] { 1.0, 4.0, 8.0, 14.0 };
		Object[] attributes5 = new Object[] { 1.0, 5.0, 9.0, 15.0 };
		Object[] attributes6 = new Object[] { 1.0, 6.0, 10.0, 16.0 };

		ProbabilisticTuple<ITimeInterval> tuple1 = new ProbabilisticTuple<>(attributes1, true);
		tuple1.setMetadata(new TimeIntervalProbabilistic());
		tuple1.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple2 = new ProbabilisticTuple<>(attributes2, true);
		tuple2.setMetadata(new TimeIntervalProbabilistic());
		tuple2.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple3 = new ProbabilisticTuple<>(attributes3, true);
		tuple3.setMetadata(new TimeIntervalProbabilistic());
		tuple3.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple4 = new ProbabilisticTuple<>(attributes4, true);
		tuple4.setMetadata(new TimeIntervalProbabilistic());
		tuple4.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple5 = new ProbabilisticTuple<>(attributes5, true);
		tuple5.setMetadata(new TimeIntervalProbabilistic());
		tuple5.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple6 = new ProbabilisticTuple<>(attributes6, true);
		tuple6.setMetadata(new TimeIntervalProbabilistic());
		tuple6.getMetadata().setStart(PointInTime.currentPointInTime());

		LinearRegressionPO<ITimeInterval> linearRegression = new LinearRegressionPO<ITimeInterval>(new int[] { 0, 3 }, new int[] { 1, 2 });
//		linearRegression.process_next(tuple1, 0);
//		linearRegression.process_next(tuple2, 0);
//		linearRegression.process_next(tuple3, 0);
//		linearRegression.process_next(tuple4, 0);
//		linearRegression.process_next(tuple5, 0);
//		linearRegression.process_next(tuple6, 0);

	}

}
