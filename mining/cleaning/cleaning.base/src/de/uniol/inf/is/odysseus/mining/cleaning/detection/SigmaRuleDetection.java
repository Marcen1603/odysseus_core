/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.mining.cleaning.detection;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * 
 * @author Dennis Geesen
 * Created at: 23.06.2011
 */
public class SigmaRuleDetection extends AbstractRelationalPredicateDetection {

	
	
	private int sigma;

	public SigmaRuleDetection(String attributeName, SDFAttributeList schema, int sigmaabweichung) {
		super(attributeName, schema);
		this.setSigma(sigmaabweichung);
	}	

	@Override
	public String createPredicate() {
		return "";
	}

	public void setSigma(int sigma) {
		this.sigma = sigma;
	}

	public int getSigma() {
		return sigma;
	}

	

}
