package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.tuple;

import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.ProviderUtil;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.ICarModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.LaserCarModel;

public class LaserTupleGenerator implements ITupleGenerator {

	@Override
	public Object nextTuple(List<ICarModel> state, long timestamp) {
		MVRelationalTuple<?> root = ProviderUtil.createTuple(1);

		MVRelationalTuple<?> scan = ProviderUtil.createTuple(2);
		root.setAttribute(0, scan);

		MVRelationalTuple<?> cars = ProviderUtil.createTuple(state.size());
		for (int i = 0; i < state.size(); i++) {
			LaserCarModel cm = (LaserCarModel)state.get(i);

			MVRelationalTuple<?> car = ProviderUtil.createTuple(10);
			car.setAttribute(0, cm.getType());
			car.setAttribute(1, cm.getId());
			car.setAttribute(2, cm.getPosx());
			car.setAttribute(3, cm.getPosy());
			car.setAttribute(4, cm.getPosz());
			car.setAttribute(5, cm.getHeading());
			car.setAttribute(6, cm.getVelocity());
			car.setAttribute(7, cm.getPosx_np());
			car.setAttribute(8, cm.getPosy_np());
			
			MVRelationalTuple<?> points = ProviderUtil.createTuple(cm.getPosxList().size());
			for (int k = 0; k < cm.getPosxList().size(); k++) {
				MVRelationalTuple<?> point = ProviderUtil.createTuple(2);
				point.setAttribute(0, cm.getPosxList().get(k));
				point.setAttribute(1, cm.getPosyList().get(k));
				
				points.setAttribute(k, point);
			}
			
			car.setAttribute(9, points);

			cars.setAttribute(i, car);
		}

		scan.setAttribute(0, timestamp);
		scan.setAttribute(1, cars);

		return root;
	}

}
