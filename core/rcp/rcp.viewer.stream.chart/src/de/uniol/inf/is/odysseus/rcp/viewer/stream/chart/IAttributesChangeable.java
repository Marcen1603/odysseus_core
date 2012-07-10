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
package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart;

import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.schema.IViewableAttribute;

public interface IAttributesChangeable<T> {

	public void chartSettingsChanged();	
	/**
	 * Should return null if everything is ok or a string containing the error message
	 * @param selectAttributes
	 * @return
	 */
	public String isValidSelection(Map<Integer, Set<IViewableAttribute>> selectAttributes);
	public List<IViewableAttribute> getViewableAttributes(int port);
	public List<IViewableAttribute> getChoosenAttributes(int port);
	public void setChoosenAttributes(int port, List<IViewableAttribute> choosenAttributes);
	Set<Integer> getPorts();	
}
