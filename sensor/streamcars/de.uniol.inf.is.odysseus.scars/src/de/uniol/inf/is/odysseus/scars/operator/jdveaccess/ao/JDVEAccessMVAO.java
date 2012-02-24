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
package de.uniol.inf.is.odysseus.scars.operator.jdveaccess.ao;

import de.uniol.inf.is.odysseus.core.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;

public class JDVEAccessMVAO extends AccessAO {
	
	private static final long serialVersionUID = 1L;
	private String objectListPath = "";
	
	
	public String getObjectListPath() {
		return this.objectListPath;
	}
	
	public void setObjectListPath(String objectListPath) {
		this.objectListPath = objectListPath;
	}
	
	public JDVEAccessMVAO(SDFSource sdfSource) {
		super(sdfSource);
	}

	public JDVEAccessMVAO(JDVEAccessMVAO sensorAccessAO) {
		super(sensorAccessAO);
		this.objectListPath = sensorAccessAO.objectListPath;
	}	

	@Override
	public JDVEAccessMVAO clone() {
		return new JDVEAccessMVAO(this);
	}
}
