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
package de.uniol.inf.is.odysseus.core.streamconnection;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;


public interface IStreamElementListener<In> {

	public void streamElementRecieved(IPhysicalOperator senderOperator, In element, int port );
	public void punctuationElementRecieved(IPhysicalOperator senderOperator, IPunctuation point, int port);
	public void securityPunctuationElementRecieved(IPhysicalOperator senderOperator, ISecurityPunctuation sp, int port );
	
}
