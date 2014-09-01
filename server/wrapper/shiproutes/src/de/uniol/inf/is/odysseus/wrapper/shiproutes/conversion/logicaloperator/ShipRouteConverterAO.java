package de.uniol.inf.is.odysseus.wrapper.shiproutes.conversion.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.EnumParameter;

@LogicalOperator(name = "SHIPROUTECONVERTER", minInputPorts = 1, maxInputPorts = 1, doc = "This operator is used to convert ship route messages into IEC messages and vice versa.", category = { LogicalOperatorCategory.PROCESSING })
public class ShipRouteConverterAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1L;
	private ConversionType conversionType;

	public ShipRouteConverterAO() {
		super();
	}

	public ShipRouteConverterAO(ShipRouteConverterAO converterAO) {
		super(converterAO);
		this.conversionType = converterAO.getConversionType();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ShipRouteConverterAO(this);
	}

	@Parameter(name = "conversionType", type = EnumParameter.class, isList = false, optional = false, doc = "The conversion type between shipRoute messages: JSON_TO_IEC, JSON_NMEA_TO_IVEF, IEC_TO_JSON_ROUTE, IEC_TO_JSON_MANOEUVRE, IEC_TO_JSON_PREDICTION, IEC_NMEA_TO_IVEF")
	public void setConversionType(ConversionType conversionType) {
		this.conversionType = conversionType;
	}
	
	public ConversionType getConversionType() {
		return this.conversionType;
	}

}
