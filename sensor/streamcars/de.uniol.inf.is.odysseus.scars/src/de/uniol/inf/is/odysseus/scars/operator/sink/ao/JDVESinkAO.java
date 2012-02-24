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
package de.uniol.inf.is.odysseus.scars.operator.sink.ao;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;

public class JDVESinkAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -8687211058868598662L;

	private int port;
	private String hostAdress;
	private String serverType;

	public JDVESinkAO() {

	}

	public JDVESinkAO(JDVESinkAO op) {
		this.port = op.port;
		this.hostAdress = op.hostAdress;
		this.serverType = op.serverType;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public String getHostAdress() {
		return hostAdress;
	}

	public void setHostAdress(String hostAdress) {
		this.hostAdress = hostAdress;
	}

	public String getServerType() {
		return serverType;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	@Override
	public SDFSchema getOutputSchema() {
		return this.getInputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new JDVESinkAO(this);
	}

}
