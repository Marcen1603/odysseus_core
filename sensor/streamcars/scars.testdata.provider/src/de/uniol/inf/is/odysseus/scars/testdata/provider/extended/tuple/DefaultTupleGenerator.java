/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.tuple;

import java.util.List;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.ProviderUtil;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.DefaultCarModel;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel.ICarModel;

public class DefaultTupleGenerator implements ITupleGenerator {

	@Override
	public Object nextTuple(List<ICarModel> state, long timestamp) {
		MVRelationalTuple<?> root = ProviderUtil.createTuple(1);

		MVRelationalTuple<?> scan = ProviderUtil.createTuple(2);
		root.setAttribute(0, scan);

		MVRelationalTuple<?> cars = ProviderUtil.createTuple(state.size());
		for (int i = 0; i < state.size(); i++) {
			DefaultCarModel cm = (DefaultCarModel)state.get(i);

			MVRelationalTuple<?> car = ProviderUtil.createTuple(12);
			car.setAttribute(0, cm.getType());
			car.setAttribute(1, cm.getId());
			car.setAttribute(2, cm.getLaneid());
			car.setAttribute(3, cm.getPosx());
			car.setAttribute(4, cm.getPosy());
			car.setAttribute(5, cm.getPosz());
			car.setAttribute(6, cm.getRoll());
			car.setAttribute(7, cm.getPitch());
			car.setAttribute(8, cm.getHeading());
			car.setAttribute(9, cm.getVelocity());
			car.setAttribute(10, cm.getLength());
			car.setAttribute(11, cm.getWidth());

			cars.setAttribute(i, car);
		}

		scan.setAttribute(0, timestamp);
		scan.setAttribute(1, cars);

		return root;
	}

}
