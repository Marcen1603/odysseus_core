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
package de.uniol.inf.is.odysseus.scars.metadata;

import de.uniol.inf.is.odysseus.IClone;
import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public interface IGain extends IMetaAttribute, IClone{

	public void setGain(double[][] newGain);
	public double[][] getGain();
	
	public void setRestrictedGain(double[][] newGain, String[] restrictedList);
	
	public String[] getRestrictedList();
	
	public void setRestrictedList(String[] restrictedList);

	public double getRestrictedGain(int index);
	
	public double getRestrictedGain(String attribute);
}

