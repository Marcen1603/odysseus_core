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
package de.uniol.inf.is.odysseus.usermanagement;

public class TimeBasedServiceLevelAgreement extends ServiceLevelAgreement implements ITimeBasedServiceLevelAgreement{

	private static final long serialVersionUID = 7881811060507272864L;
	private long timeperiod;

	/**
	 * 
	 * @param name
	 * @param timeperiod in milliseconds
	 */
	public TimeBasedServiceLevelAgreement(String name, long timeperiod) {
		super(name);
		this.timeperiod = timeperiod;
	}
	
	@Override
	public long getTimeperiod() {
		return timeperiod;
	}
	
}
