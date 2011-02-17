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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.description;

import java.util.ArrayList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFAttributeAttributeBindingPair;

public class SDFAttributeAttributeBindingPairList {

    /**
	 * @uml.property  name="attributeAttributeBindingPairs"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFAttributeAttributeBindingPair"
	 */
    private ArrayList<SDFAttributeAttributeBindingPair> attributeAttributeBindingPairs;
    
	public SDFAttributeAttributeBindingPairList(SDFAttributeAttributeBindingPairList elements) {
        attributeAttributeBindingPairs = new ArrayList<SDFAttributeAttributeBindingPair>();
        attributeAttributeBindingPairs.addAll(elements.attributeAttributeBindingPairs);
    }

    public SDFAttributeAttributeBindingPairList() { 
        attributeAttributeBindingPairs = new ArrayList<SDFAttributeAttributeBindingPair>();
    }

    public void addAttributeAttributeBindingPair(SDFAttributeAttributeBindingPair aaPair) {
		attributeAttributeBindingPairs.add(aaPair);
	}

	public SDFAttributeAttributeBindingPair getAttributeAttributeBindingPair(int pos) {
		return (SDFAttributeAttributeBindingPair) attributeAttributeBindingPairs.get(pos);
	}

	public int getAttributeAttributeBindingPairCount() {
		return this.attributeAttributeBindingPairs.size();
	}

    
    public void removeAttributeAttributeBindingPair(int pos) 
    {
    	attributeAttributeBindingPairs.remove(pos);
	}

	
	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer("[");
		for (int i = 0; i < attributeAttributeBindingPairs.size(); i++) {
			ret.append(getAttributeAttributeBindingPair(i).getURI(true) + ",");
		}
		return ret.toString() + "]";
	}

}