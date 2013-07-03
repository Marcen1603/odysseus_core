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
import de.uniol.inf.is.odysseus.probabilistic.continuous.physicaloperator.EMPO;
import de.uniol.inf.is.odysseus.probabilistic.metadata.TimeIntervalProbabilistic;

@SuppressWarnings("unused")
public class TestEM {
	@Test
	public void testEM() {

		Collection<SDFAttribute> attr = new ArrayList<SDFAttribute>();
		attr.add(new SDFAttribute("", "a", SDFDatatype.DOUBLE));
		attr.add(new SDFAttribute("", "b", SDFDatatype.DOUBLE));
		attr.add(new SDFAttribute("", "c", SDFDatatype.DOUBLE));
		attr.add(new SDFAttribute("", "d", SDFDatatype.DOUBLE));

		SDFSchema schema = new SDFSchema("", attr);
		Object[] attributes1 = new Object[] { 1.0, 1.0, 6.0, 11.0 };
		Object[] attributes2 = new Object[] { 2.0, 2.0, 5.0, 12.0 };
		Object[] attributes3 = new Object[] { 3.0, 3.0, 7.0, 13.0 };
		Object[] attributes4 = new Object[] { 4.0, 4.0, 8.0, 14.0 };
		Object[] attributes5 = new Object[] { 5.0, 5.0, 9.0, 15.0 };
		Object[] attributes6 = new Object[] { 6.0, 6.0, 10.0, 16.0 };

		ProbabilisticTuple<ITimeInterval> tuple1 = new ProbabilisticTuple<>(
				attributes1, true);
		tuple1.setMetadata(new TimeIntervalProbabilistic());
		tuple1.getMetadata().setStart(new PointInTime(1));
		tuple1.getMetadata().setEnd(new PointInTime(110));

		ProbabilisticTuple<ITimeInterval> tuple2 = new ProbabilisticTuple<>(
				attributes2, true);
		tuple2.setMetadata(new TimeIntervalProbabilistic());
		tuple2.getMetadata().setStart(new PointInTime(2));
		tuple2.getMetadata().setEnd(new PointInTime(120));

		ProbabilisticTuple<ITimeInterval> tuple3 = new ProbabilisticTuple<>(
				attributes3, true);
		tuple3.setMetadata(new TimeIntervalProbabilistic());
		tuple3.getMetadata().setStart(new PointInTime(3));
		tuple3.getMetadata().setEnd(new PointInTime(130));

		ProbabilisticTuple<ITimeInterval> tuple4 = new ProbabilisticTuple<>(
				attributes4, true);
		tuple4.setMetadata(new TimeIntervalProbabilistic());
		tuple4.getMetadata().setStart(new PointInTime(4));
		tuple4.getMetadata().setEnd(new PointInTime(140));

		ProbabilisticTuple<ITimeInterval> tuple5 = new ProbabilisticTuple<>(
				attributes5, true);
		tuple5.setMetadata(new TimeIntervalProbabilistic());
		tuple5.getMetadata().setStart(new PointInTime(5));
		tuple5.getMetadata().setEnd(new PointInTime(150));

		ProbabilisticTuple<ITimeInterval> tuple6 = new ProbabilisticTuple<>(
				attributes6, true);
		tuple6.setMetadata(new TimeIntervalProbabilistic());
		tuple6.getMetadata().setStart(new PointInTime(6));
		tuple6.getMetadata().setEnd(new PointInTime(160));

		EMPO<ITimeInterval> em = new EMPO<ITimeInterval>(new int[] { 0, 1, 3 },
				3);
		for (int i = 0; i < 100; i++) {
			ProbabilisticTuple<ITimeInterval> tmpTuple1 = tuple1.clone();
			tmpTuple1.getMetadata().setEnd(
					tuple1.getMetadata().getEnd().plus(6));
			tmpTuple1.getMetadata().setStart(
					tuple1.getMetadata().getStart().plus(7));
			tuple1 = tmpTuple1;

			ProbabilisticTuple<ITimeInterval> tmpTuple2 = tuple2.clone();
			tmpTuple2.getMetadata().setEnd(
					tuple2.getMetadata().getEnd().plus(6));
			tmpTuple2.getMetadata().setStart(
					tuple2.getMetadata().getStart().plus(7));
			tuple2 = tmpTuple2;

			ProbabilisticTuple<ITimeInterval> tmpTuple3 = tuple3.clone();
			tmpTuple3.getMetadata().setEnd(
					tuple3.getMetadata().getEnd().plus(6));
			tmpTuple3.getMetadata().setStart(
					tuple3.getMetadata().getStart().plus(7));
			tuple3 = tmpTuple3;

			ProbabilisticTuple<ITimeInterval> tmpTuple4 = tuple4.clone();
			tmpTuple4.getMetadata().setEnd(
					tuple4.getMetadata().getEnd().plus(6));
			tmpTuple4.getMetadata().setStart(
					tuple4.getMetadata().getStart().plus(7));
			tuple4 = tmpTuple4;

			ProbabilisticTuple<ITimeInterval> tmpTuple5 = tuple5.clone();
			tmpTuple5.getMetadata().setEnd(
					tuple5.getMetadata().getEnd().plus(6));
			tmpTuple5.getMetadata().setStart(
					tuple5.getMetadata().getStart().plus(7));
			tuple5 = tmpTuple5;

			ProbabilisticTuple<ITimeInterval> tmpTuple6 = tuple6.clone();
			tmpTuple6.getMetadata().setEnd(
					tuple6.getMetadata().getEnd().plus(6));
			tmpTuple6.getMetadata().setStart(
					tuple6.getMetadata().getStart().plus(7));
			tuple6 = tmpTuple6;

			// em.process_next(tmpTuple1.clone(), 0);
			// em.process_next(tmpTuple2.clone(), 0);
			// em.process_next(tmpTuple3.clone(), 0);
			// em.process_next(tmpTuple4.clone(), 0);
			// em.process_next(tmpTuple5.clone(), 0);
			// em.process_next(tmpTuple6.clone(), 0);

		}

	}

}
