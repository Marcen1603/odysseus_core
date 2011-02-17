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
/*	
 * Created on 16.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.uniol.inf.is.odysseus.sourcedescription.sdf.description;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions;

/**
 * @author Marco Grawunder
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SDFInputAttributeBindingFactory {

    private static HashMap<String, SDFNecessityState> neccessityCache = new HashMap<String, SDFNecessityState>();

	protected SDFInputAttributeBindingFactory() {
	}

    
    /**
     * @param string
     * @return
     */
    public static SDFNecessityState getNecessityState(String uri) 
    	throws IllegalArgumentException{
        SDFNecessityState inputAttributeBinding = neccessityCache.get(uri);
		if (inputAttributeBinding == null) {
		    
		    if (uri.equals(SDFIntensionalDescriptions.Required) ||
		        uri.equals(SDFIntensionalDescriptions.Optional)    ){
		        inputAttributeBinding = new SDFNecessityState(uri);
		        neccessityCache.put(uri, inputAttributeBinding);
		    }else{
		        throw new IllegalArgumentException(uri+ "no valid InputAttributeBinding Necessity State");
		    }
		}
		return inputAttributeBinding;
    }

}
