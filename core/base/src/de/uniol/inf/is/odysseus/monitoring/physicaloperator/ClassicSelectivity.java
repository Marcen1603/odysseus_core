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
package de.uniol.inf.is.odysseus.monitoring.physicaloperator;

import de.uniol.inf.is.odysseus.monitoring.AbstractMonitoringData;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;

/**
 * 
 * Calculates the Selectivity of an Operator by relating the count of the written
 * objects with the <b>product</b> of all read objects. Max Value is 1 (each read object
 * is participated in every produced object --> cartesian product)
 * 
 * @author Marco Grawunder
 *
 */

public class ClassicSelectivity extends Selectivity {

	public ClassicSelectivity(IPhysicalOperator po, int inputPorts) {
		super(po, inputPorts, MonitoringDataTypes.SELECTIVITY.name);
	}

	public ClassicSelectivity(ClassicSelectivity classicSelectivity) {
		super(classicSelectivity);
	}

	@Override
	public Double getValue() {
		return  getWriteCount()/ getReadCountProduct();
	}

	@Override
	public AbstractMonitoringData<Double> clone() {
		return new ClassicSelectivity(this);
	}

}
