package de.uniol.inf.is.odysseus.tc.geoconvert;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.views.AbstractView;

import com.google.inject.Inject;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

import de.uniol.inf.is.odysseus.tc.ISumoParams;
import de.uniol.inf.is.odysseus.tc.service.IXMLService;
import de.uniol.inf.is.odysseus.tc.vehicle.VehicleInfo;

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
