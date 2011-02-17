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

import java.util.HashMap;
import java.util.Map;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFCompareOperatorList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions;

public class SDFInputPattern extends SDFPattern {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9008171306859489049L;

	public SDFInputPattern(){
		super();
	}
	
	public SDFInputPattern(String inputPatternURI) {
		super(inputPatternURI);
	}
	
	public SDFInputPattern(SDFPattern pattern) {
		super(pattern);
	}
	
	
	public Map<SDFAttribute, SDFCompareOperatorList> getAttributeCompareOperatorAssignment(){
		Map<SDFAttribute, SDFCompareOperatorList> ret = new HashMap<SDFAttribute, SDFCompareOperatorList>();
		for (int i=0;i<getAttributeAttributeBindingPairCount();i++){
			SDFInputAttributeBinding iAttributeBinding = (SDFInputAttributeBinding) getAttributeAttributeBindingPair(i).getAttributeBinding();
			ret.put(getAttributeAttributeBindingPair(i).getAttribute(),iAttributeBinding.getCompareOps());
		}
		return ret;
	}
	
    /**
     * @return
     */
    public SDFAttributeAttributeBindingPairList getRequiredInAttrAttributeBindingPairs() {
        SDFAttributeAttributeBindingPairList ret = new SDFAttributeAttributeBindingPairList();
        for (int i=0;i<elements.getAttributeAttributeBindingPairCount();i++){
           SDFAttributeAttributeBindingPair aap = elements.getAttributeAttributeBindingPair(i);
           if (((SDFInputAttributeBinding)aap.getAttributeBinding()).getNecessity().equals(SDFIntensionalDescriptions.Required)){
               ret.addAttributeAttributeBindingPair(aap);
           }
        }
        return ret;
    }
	
    /**
     * Ermittelt eine Menge aller notwendigen Eingabeattribute
     * @return 
     */
    public SDFAttributeList getRequiredInAttributes() {
        SDFAttributeList reqAttribs = new SDFAttributeList();
        for (int i=0;i<elements.getAttributeAttributeBindingPairCount();i++){
           SDFAttributeAttributeBindingPair aap = elements.getAttributeAttributeBindingPair(i);
           if (((SDFInputAttributeBinding)aap.getAttributeBinding()).getNecessity().equals(SDFIntensionalDescriptions.Required)){
               reqAttribs.addAttribute(aap.getAttribute());
           }
        }
        return reqAttribs;
    }
	
	
	
    /**
     * @param leftInputPattern
     * @param rightInputPattern
     * @return
     * 
     */
    public static SDFInputPattern union(SDFInputPattern leftInputPattern, SDFInputPattern rightInputPattern) 
    		throws SDFPatternNotCompatible{
        SDFInputPattern newPattern = new SDFInputPattern(leftInputPattern);
        doUnion(rightInputPattern, newPattern);
        return newPattern;
    }
    
    


}
