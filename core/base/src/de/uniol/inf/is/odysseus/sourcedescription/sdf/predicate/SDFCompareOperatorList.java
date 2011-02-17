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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

import java.util.ArrayList;

public class SDFCompareOperatorList extends ArrayList<SDFCompareOperator> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 711373188536550923L;

	public SDFCompareOperatorList(SDFCompareOperatorList compareOps) {
		super(compareOps);
	}

	public SDFCompareOperatorList() {
		super();
	}

	public void getXMLRepresentation(String indent, StringBuffer xmlRetValue) {
		for (SDFCompareOperator c: this.toArray(new SDFCompareOperator[this.size()])){
			xmlRetValue.append(indent);
			xmlRetValue.append("<operator>");
			xmlRetValue.append(c.getXMLRepresentation());
			xmlRetValue.append("</operator>\n");
		}
	}
}
