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

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

public class SDFOrOperator extends SDFLogicalOperator {
	/**
	 * 
	 */
	private static final long serialVersionUID = -97278497055824968L;

	public SDFOrOperator(String URI) {
		super(URI);
	}

	public SDFOrOperator(SDFOrOperator sdfOrOperator) {
		super(sdfOrOperator);
	}

	@Override
	public String toString() {
		return "OR";
	}

    /* (non-Javadoc)
     * @see de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFLogicalOperator#evaluate(boolean, boolean)
     */
    @Override
	public boolean evaluate(boolean left, boolean right) {
        return (left||right);
    }
    
    @Override
    public SDFOrOperator clone() {
    	return new SDFOrOperator(this);
    }
}