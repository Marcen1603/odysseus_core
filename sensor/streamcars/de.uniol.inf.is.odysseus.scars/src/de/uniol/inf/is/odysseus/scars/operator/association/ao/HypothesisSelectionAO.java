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
package de.uniol.inf.is.odysseus.scars.operator.association.ao;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

public class HypothesisSelectionAO<M extends IProbability> extends UnaryLogicalOp {

  private static final long serialVersionUID = 1L;

  private String id;

  private String oldObjListPath;
  private String newObjListPath;

  public HypothesisSelectionAO() {
    super();
  }

  public HypothesisSelectionAO(HypothesisSelectionAO<M> copy) {
    super(copy);
    this.id = copy.id;
    
    this.oldObjListPath = copy.oldObjListPath;
    this.newObjListPath = copy.newObjListPath;
  }

  public String getID() {
    return id;
  }

  public void setID(String iD) {
    id = iD;
  }

  public String getNewObjListPath() {
    return this.newObjListPath;
  }

  public String getOldObjListPath() {
    return this.oldObjListPath;
  }

  public void setNewObjListPath(String newObjListPath) {
    this.newObjListPath = newObjListPath;
  }
  
  public void setOldObjListPath(String oldObjListPath) {
    this.oldObjListPath = oldObjListPath;
  }

  @Override
  public SDFSchema getOutputSchema() {
    return this.getInputSchema();
  }

  @Override
  public AbstractLogicalOperator clone() {
    return new HypothesisSelectionAO<M>(this);
  }
}
