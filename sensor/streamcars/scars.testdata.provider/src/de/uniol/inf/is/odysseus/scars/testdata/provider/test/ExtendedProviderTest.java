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
package de.uniol.inf.is.odysseus.scars.testdata.provider.test;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.scars.testdata.provider.ExtendedProvider;
import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.DefaultOvertakeCalcModel;

public class ExtendedProviderTest {

	public static void main(String[] args) {
		Map<String, String> options = new HashMap<String, String>();
		options.put(ExtendedProvider.CALCMODEL, ExtendedProvider.CALCMODEL_SCARS_OVERTAKE);
//		options.put(ExtendedProvider.SCHEMA, ExtendedProvider.SCHEMA_SCARS_ALTERNATIVE);
//		options.put(ExtendedProvider.SCHEMA, ExtendedProvider.SCHEMA_SCARS_DEFAULT);
		options.put(ExtendedProvider.SCHEMA, ExtendedProvider.SCHEMA_SCARS_LASER);
		options.put(ExtendedProvider.VISIBILITY, ExtendedProvider.VISIBILITY_SCARS_FRONT);
		Map<String, Object> calcModelParams = new HashMap<String, Object>();
		calcModelParams.put(DefaultOvertakeCalcModel.LANE_SHIFT_FACTOR, new Float(1.5));
		ExtendedProvider provider = new ExtendedProvider(options, calcModelParams);
		provider.setDelay(50);
		provider.setNumOfCars(5);
		provider.init();
		for (int i = 0; i < 200; i++) {
			System.out.println(provider.nextTuple());
//			provider.nextTuple();
		}
	}
	
}
