/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.monitoring;

import java.util.Collection;

@SuppressWarnings({"rawtypes"})
public interface IMonitoringDataProvider {
	public Collection<String> getProvidedMonitoringData();

	public boolean providesMonitoringData(String type);
	
	public <T> IMonitoringData<T> getMonitoringData(String type);
	public void createAndAddMonitoringData(
				IPeriodicalMonitoringData item, long period);

	public void addMonitoringData(String type, IMonitoringData<?> item);
	public void removeMonitoringData(String type);

}
