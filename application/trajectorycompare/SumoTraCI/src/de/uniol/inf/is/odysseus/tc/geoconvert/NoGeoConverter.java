package de.uniol.inf.is.odysseus.tc.geoconvert;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.tc.ISumoParams;
import de.uniol.inf.is.odysseus.tc.service.IXMLService;

public class NoGeoConverter extends AbstractGeoConverter {

	@Inject
	public NoGeoConverter(IXMLService xmlService, ISumoParams sumoParams) {
		super(xmlService, sumoParams);
	}

	@Override
	protected String getToReference() {
		return this.projParameter;
	}

}
