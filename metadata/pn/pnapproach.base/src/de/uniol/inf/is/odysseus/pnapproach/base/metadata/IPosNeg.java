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
package de.uniol.inf.is.odysseus.pnapproach.base.metadata;

import java.io.Serializable;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.PointInTime;

public interface IPosNeg extends IMetaAttribute, Comparable<IPosNeg>, Serializable{
	
	public ElementType getElementType();
	public List<Long> getID();
	public PointInTime getTimestamp();
	
	public void setElementType(ElementType type);
	public void setID(List<Long> id);
	public void setTimestamp(PointInTime timestamp);

}
