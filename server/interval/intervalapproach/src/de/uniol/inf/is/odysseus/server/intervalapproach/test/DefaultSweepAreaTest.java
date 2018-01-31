package de.uniol.inf.is.odysseus.server.intervalapproach.test;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;

// TODO: Make JUnit Test class

public class DefaultSweepAreaTest {

	public static void main(String[] args) {
		DefaultTISweepArea<IStreamObject<? extends ITimeInterval>> sa = new DefaultTISweepArea<>();
		
		List<Tuple<ITimeInterval>> tuples = new ArrayList<>();
		for (int i=0;i<100;i++) {
			Tuple<ITimeInterval> t = new Tuple<ITimeInterval>(1, false);
			t.setAttribute(0, i);
			t.setMetadata(new TimeInterval(new PointInTime(i), new PointInTime(i+1)));
			tuples.add(t);			
		}
		
		testInsertAll(sa, tuples);
		
		
	}

	private static void testInsertAll(DefaultTISweepArea<IStreamObject<? extends ITimeInterval>> sa,
			List<Tuple<ITimeInterval>> tuples) {
		for (int i=1;i<10;i++) {
			if (i != 4) {
				sa.insert(tuples.get(i));		
			}
		}

		System.out.println(sa.toString());

		List<IStreamObject<? extends ITimeInterval>> toInsert = new ArrayList<>();
		toInsert.add(tuples.get(0));
		toInsert.add(tuples.get(1));
		toInsert.add(tuples.get(4));
		toInsert.add(tuples.get(9));
		toInsert.add(tuples.get(10));

		sa.insertAll(toInsert);
		
		System.out.println(sa.toString());
		
		toInsert.clear();
		toInsert.add(tuples.get(5));
		toInsert.add(tuples.get(4));
		toInsert.add(tuples.get(3));
		
		sa.insertAll(toInsert);

		System.out.println(sa.toString());

	}
	
}
