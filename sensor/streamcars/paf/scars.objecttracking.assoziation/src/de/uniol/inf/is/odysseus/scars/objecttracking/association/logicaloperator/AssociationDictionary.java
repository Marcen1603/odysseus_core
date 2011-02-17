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
package de.uniol.inf.is.odysseus.scars.objecttracking.association.logicaloperator;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.scars.objecttracking.association.logicaloperator.HypothesisSelectionAO;

public class AssociationDictionary {

  private final static AssociationDictionary instance = new AssociationDictionary();
  private HashMap<String, HypothesisSelectionAO<?>> sources = new HashMap<String, HypothesisSelectionAO<?>>();

  private AssociationDictionary() {
  }

  public static AssociationDictionary getInstance() {
    return instance;
  }

  public void addSource(String opName, HypothesisSelectionAO<?> source) {
    if (!this.sources.containsKey(opName)) {
      this.sources.put(opName, source);
    }
  }

  public HypothesisSelectionAO<?> getSource(String srcName) {
	  return this.sources.get(srcName);
  }
  
  public void clear(){
	  this.sources = new HashMap<String, HypothesisSelectionAO<?>>();
  }
}
