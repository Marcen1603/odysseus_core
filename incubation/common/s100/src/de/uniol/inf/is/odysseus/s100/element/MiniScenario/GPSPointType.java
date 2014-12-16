package de.uniol.inf.is.odysseus.s100.element.MiniScenario;

import java.util.Date;

import de.uniol.inf.is.odysseus.s100.element.s100Schema.AbstractFeatureType;
import de.uniol.inf.is.odysseus.s100.element.s100Schema.Point;

/**
 * @author Christoph Dibbern
 * @version 1.0
 * @created 15-Dez-2014 20:43:08
 */
public class GPSPointType extends AbstractFeatureType {

	public String userRemarks;
	public String inputIdentifier;
	public Point ext_ref_1;
	public Date timestamp;

	public GPSPointType(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

}