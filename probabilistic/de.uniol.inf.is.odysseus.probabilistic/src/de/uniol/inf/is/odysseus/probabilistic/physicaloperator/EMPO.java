package de.uniol.inf.is.odysseus.probabilistic.physicaloperator;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.metadata.TimeIntervalProbabilistic;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 * @param <T>
 */
public class EMPO<T extends ITimeInterval> extends
		AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> {
	public EMPO() {

	}

	public EMPO(EMPO<T> emPO) {

	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_next(ProbabilisticTuple<T> object, int port) {
		// TODO Auto-generated method stub

	}

	@Override
	public AbstractPipe<ProbabilisticTuple<T>, ProbabilisticTuple<T>> clone() {
		return new EMPO<T>(this);
	}

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
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

		ProbabilisticTuple<ITimeInterval> tuple1 = new ProbabilisticTuple<>(
				attributes1, true);
		tuple1.setMetadata(new TimeIntervalProbabilistic());
		tuple1.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple2 = new ProbabilisticTuple<>(
				attributes2, true);
		tuple2.setMetadata(new TimeIntervalProbabilistic());
		tuple2.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple3 = new ProbabilisticTuple<>(
				attributes3, true);
		tuple3.setMetadata(new TimeIntervalProbabilistic());
		tuple3.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple4 = new ProbabilisticTuple<>(
				attributes4, true);
		tuple4.setMetadata(new TimeIntervalProbabilistic());
		tuple4.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple5 = new ProbabilisticTuple<>(
				attributes5, true);
		tuple5.setMetadata(new TimeIntervalProbabilistic());
		tuple5.getMetadata().setStart(PointInTime.currentPointInTime());

		ProbabilisticTuple<ITimeInterval> tuple6 = new ProbabilisticTuple<>(
				attributes6, true);
		tuple6.setMetadata(new TimeIntervalProbabilistic());
		tuple6.getMetadata().setStart(PointInTime.currentPointInTime());

		EMPO<ITimeInterval> em = new EMPO<ITimeInterval>();
		em.process_next(tuple1, 0);
		em.process_next(tuple2, 0);
		em.process_next(tuple3, 0);
		em.process_next(tuple4, 0);
		em.process_next(tuple5, 0);
		em.process_next(tuple6, 0);

	}
}
