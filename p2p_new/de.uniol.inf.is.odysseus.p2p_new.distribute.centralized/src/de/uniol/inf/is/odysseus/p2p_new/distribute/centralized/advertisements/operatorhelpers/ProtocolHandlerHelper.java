package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolHandlerRegistry;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisement;

public class ProtocolHandlerHelper {
	private final static String ACCESSPATTERN_TAG = "accesspattern";
	private final static String DIRECTION_TAG = "direction";
	private final static String PROTOCOL_HANDLER_NAME_TAG = "protocolhandlername";
	private final static String DATAHANDLER_SCHEMA_TAG = "datahandlerschema";
	private final static String DATAHANDLER_SUPPORTEDTYPES_TAG = "datahandlersupportedtypes";
	private final static String OPTIONS_TAG = "protocolhandleroptions";
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static StructuredDocument generateProtocolHandlerStatement(IProtocolHandler protocolHandler, MimeMediaType mimeType) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPlanAdvertisement.getAdvertisementType());
		if(protocolHandler instanceof AbstractProtocolHandler) {
			AbstractProtocolHandler ph = (AbstractProtocolHandler)protocolHandler;
			String direction = ph.getDirection().toString();
			result.appendChild(result.createElement(DIRECTION_TAG,direction.toString()));
			result.appendChild(result.createElement(PROTOCOL_HANDLER_NAME_TAG,ph.getName()));
			result.appendChild(result.createElement(ACCESSPATTERN_TAG,ph.getAccess().toString()));
			IDataHandler dh = ph.getDataHandler();
			SDFSchema dataHandlerSchema = dh.getSchema();
			List<String> supportedDataTypes = dh.getSupportedDataTypes();
			String supportedDataTypesAsString = Arrays.toString(supportedDataTypes.toArray(new String[0]));
			result.appendChild(result.createElement(DATAHANDLER_SUPPORTEDTYPES_TAG,supportedDataTypesAsString));
			result.appendChild(result.createElement(DATAHANDLER_SCHEMA_TAG,SchemaHelper.createOutputSchemaStatement(dataHandlerSchema, mimeType).toString()));
			result.appendChild(result.createElement(OPTIONS_TAG,ph.getOptionsMap().toString()));
		}
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	public static IProtocolHandler createProtocolHandlerFromStatement(TextElement<?> statement) {
		Enumeration<? extends TextElement<?>> elements = statement.getChildren();
		ITransportDirection direction = null;
		IAccessPattern accessPattern = null;
		String protocolHandlerName = "";
		SDFSchema dataHandlerSchema = null;
		String[] supportedDataTypes = null;
		Map<String,String> options = null;
		
		while(elements.hasMoreElements()) {
			TextElement elem = elements.nextElement();
			if(elem.getName().equals(DIRECTION_TAG)) {
				direction = ITransportDirection.valueOf(elem.getTextValue());
			} else if(elem.getName().equals(PROTOCOL_HANDLER_NAME_TAG)) {
				protocolHandlerName = elem.getTextValue();
			} else if(elem.getName().equals(ACCESSPATTERN_TAG)) {
				accessPattern = IAccessPattern.valueOf(elem.getTextValue());
			} else if(elem.getName().equals(DATAHANDLER_SCHEMA_TAG)) {
				dataHandlerSchema = SchemaHelper.createSchemaFromStatement(elem);
			} else if(elem.getName().equals(DATAHANDLER_SUPPORTEDTYPES_TAG)) {
				supportedDataTypes = Tools.fromStringToStringArray(elem.getTextValue());
			} else if(elem.getName().equals(OPTIONS_TAG)) {
				options = Tools.fromStringToMap(elem.getTextValue());
			}
		}
		// this assumes, that there are no overlaps between the supported datatypes of different handlers
		IDataHandler dh = DataHandlerRegistry.getDataHandler(supportedDataTypes[0], dataHandlerSchema);
		return ProtocolHandlerRegistry.getInstance(protocolHandlerName, direction, accessPattern, options, dh);
	}
}
