package de.uniol.inf.is.odysseus.logicaloperator.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

public class ResolvedSDFAttributeParameter extends
		AbstractParameter<SDFAttribute> {

	  Logger _logger = null;
	  Logger getLogger(){
	    if (_logger == null){
	      _logger = LoggerFactory.getLogger(ResolvedSDFAttributeParameter.class);
	    }
	    return _logger;
	  }
	
	public ResolvedSDFAttributeParameter(String name, REQUIREMENT requirement) {
		super(name, requirement);
	}

	@Override
	protected void internalAssignment() {
		if (getAttributeResolver() == null) {
			throw new RuntimeException("missing attribute resolver");
		}
		try {
			SDFAttribute attribute = getAttributeResolver().getAttribute(
					(String) this.inputValue);
			
			setValue(attribute);
		} catch( Exception ex ) {
			throw new RuntimeException("cannot assign attribute value", ex);
		}
	}

}
