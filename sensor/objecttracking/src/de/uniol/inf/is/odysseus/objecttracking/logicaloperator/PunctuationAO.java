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
package de.uniol.inf.is.odysseus.objecttracking.logicaloperator;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;

public class PunctuationAO extends UnaryLogicalOp{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9158544782511839463L;
	int punctuationCount;
	
	public PunctuationAO(){
		super();
	}
	
	public PunctuationAO(int punctuationCount){
		super();
		this.punctuationCount = punctuationCount;
	}
	
	public PunctuationAO(PunctuationAO old){
		this.punctuationCount = old.punctuationCount;
	}
	
	public int getPunctuationCount(){
		return this.punctuationCount;
	}
	
	public void setPunctuationCount(int punctuationCount){
		this.punctuationCount = punctuationCount;
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		// TODO Auto-generated method stub
		return new PunctuationAO(this);
	}

	@Override
	public SDFSchema getOutputSchema() {
		// TODO Auto-generated method stub
		return this.getInputSchema();
	}

}
