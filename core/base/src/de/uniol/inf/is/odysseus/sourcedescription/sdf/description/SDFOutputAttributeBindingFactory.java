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

import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions;

public class SDFOutputAttributeBindingFactory {
	private static HashMap<String,SDFOutputAttributeBinding> attributeBindingCache = new HashMap<String,SDFOutputAttributeBinding>();

	protected SDFOutputAttributeBindingFactory() {
	}

	public static SDFOutputAttributeBinding getOutputAttributeBinding(String URI) {
		SDFOutputAttributeBinding outputAttributeBinding = attributeBindingCache.get(URI);
		if (outputAttributeBinding == null) {
		    if (URI.equals(SDFIntensionalDescriptions.Id)){
                outputAttributeBinding = new SDFOutputAttributeBinding(URI);
                outputAttributeBinding.setIdAttribute(true);
                attributeBindingCache.put(URI, outputAttributeBinding);                
            }else if (URI.equals(SDFIntensionalDescriptions.SortAsc)){
                outputAttributeBinding = new SDFOutputAttributeBinding(URI);
                outputAttributeBinding.setSortedAsc(true);
                outputAttributeBinding.setSortedAsc(false);
                attributeBindingCache.put(URI, outputAttributeBinding);                		         
            }else if(URI.equals(SDFIntensionalDescriptions.SortDesc)){
                outputAttributeBinding = new SDFOutputAttributeBinding(URI);
                outputAttributeBinding.setSortedAsc(false);
                outputAttributeBinding.setSortedDesc(true);
                attributeBindingCache.put(URI, outputAttributeBinding);                		                                     	
            }else if (URI.equals(SDFIntensionalDescriptions.None)){
                outputAttributeBinding = new SDFOutputAttributeBinding(URI);
                attributeBindingCache.put(URI, outputAttributeBinding);
		    }else{
		        System.out.println("Ungueltiges OutputAttributeBinding");   
		    }
		}
		return outputAttributeBinding;
	}
}