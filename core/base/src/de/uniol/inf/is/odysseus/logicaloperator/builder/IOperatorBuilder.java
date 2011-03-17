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
package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.usermanagement.User;

/**
 * @author Jonas Jacobi
 */
public interface IOperatorBuilder extends Serializable {
	public Set<IParameter<?>> getParameters();

	public ILogicalOperator createOperator();

	public boolean validate();

	public List<Exception> getErrors();

	public void setInputOperator(int inputPort, ILogicalOperator operator,
			int outputPort);

	public int getMinInputOperatorCount();

	public int getMaxInputOperatorCount();
	
	public void setCaller(User caller);

	void setDataDictionary(IDataDictionary dataDictionary);

	public IOperatorBuilder cleanCopy();
}
