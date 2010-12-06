package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.tuple;

import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.ProviderUtil;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.AlternativeCarModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.ICarModel;

public class AlternativeTupleGenerator implements ITupleGenerator {

	@Override
	public Object nextTuple(List<ICarModel> state, long timestamp) {
		MVRelationalTuple<?> root = ProviderUtil.createTuple(1);

		MVRelationalTuple<?> scan = ProviderUtil.createTuple(2);
		root.setAttribute(0, scan);

		MVRelationalTuple<?> cars = ProviderUtil.createTuple(state.size());
		for (int i = 0; i < state.size(); i++) {
			AlternativeCarModel cm = (AlternativeCarModel)state.get(i);

			MVRelationalTuple<?> car = ProviderUtil.createTuple(8);
			car.setAttribute(0, cm.getType());
			car.setAttribute(1, cm.getId());
			car.setAttribute(2, cm.getLaneid());
			car.setAttribute(3, cm.getPosx());
			car.setAttribute(4, cm.getPosy());
			car.setAttribute(5, cm.getHeading());
			car.setAttribute(6, cm.getVelocity());
			car.setAttribute(7, cm.getAcceleration());

			cars.setAttribute(i, car);
		}

		scan.setAttribute(0, timestamp);
		scan.setAttribute(1, cars);

		return root;
	}

}
