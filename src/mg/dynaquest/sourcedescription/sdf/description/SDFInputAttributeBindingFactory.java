/*	
 * Created on 16.11.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package mg.dynaquest.sourcedescription.sdf.description;

import java.util.HashMap;

import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFIntensionalDescriptions;

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
