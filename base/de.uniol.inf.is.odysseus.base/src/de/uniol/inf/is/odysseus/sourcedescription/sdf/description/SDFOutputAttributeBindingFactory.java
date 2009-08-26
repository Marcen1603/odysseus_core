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