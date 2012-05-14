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

package de.uniol.inf.is.odysseus.mining.cleaning.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.mining.cleaning.correction.ICorrection;

/**
 * 
 * @author Dennis Geesen
 * Created at: 11.07.2011
 */
public abstract class AbstractCorrectionAO<T, C extends ICorrection<T>> extends BinaryLogicalOp{

	private static final long serialVersionUID = -139230577687015814L;
	private List<C> corrections = new ArrayList<C>();
	
	public AbstractCorrectionAO() {

	}

	public AbstractCorrectionAO(AbstractCorrectionAO<T, C> correctionAO) {
		super(correctionAO);
		this.corrections = correctionAO.corrections;
	}
	

	public List<C> getCorrections() {
		return corrections;
	}

	public void setCorrections(List<C> corrections) {
		this.corrections = corrections;
	}
	
	public void addCorrection(C correction) {
		this.corrections.add(correction);
	}

	

}
