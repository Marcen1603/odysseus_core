package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.TextElement;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.PhysicalQueryPlanAdvertisement;

public class SchemaHelper {
	private static String OUTPUTSCHEMA_URI_TAG = "outputSchemaURI";
	private static String ATTRIBUTE_TAG = "attribute";
	private static String ATTRIBUTE_SOURCENAME_TAG = "attribute_sourcename";
	private static String ATTRIBUTENAME_TAG = "attribute_name";
	private static String ATTRIBUTE_DATATYPE_TAG = "attribute_datatype";
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static StructuredDocument createOutputSchemataStatement(IPhysicalOperator o,MimeMediaType mimeType) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPlanAdvertisement.getAdvertisementType());
		// Append the output-Schemata individually and tag them with the port number
		for(Entry<Integer,SDFSchema> schemaEntry : o.getOutputSchemas().entrySet()) {
			result.appendChild(result.createElement(schemaEntry.getKey(), createOutputSchemaStatement(schemaEntry.getValue(),mimeType)));
		}
		return result;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static StructuredDocument createOutputSchemaStatement(SDFSchema schema, MimeMediaType mimeType) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPlanAdvertisement.getAdvertisementType());
		
		// OutputSchemata consist of Attributes, which are added one by one as found
		for(SDFAttribute attribute : schema.getAttributes()) {
			result.appendChild(result.createElement(ATTRIBUTE_TAG, createSDFAttributeStatement(attribute,mimeType)));
		}
		// we need the URI as well
		result.appendChild(result.createElement(OUTPUTSCHEMA_URI_TAG,schema.getURI()));
		
		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static StructuredDocument createSDFAttributeStatement(SDFAttribute attribute, MimeMediaType mimeType) {
		StructuredDocument result = StructuredDocumentFactory.newStructuredDocument(mimeType,PhysicalQueryPlanAdvertisement.getAdvertisementType());
		
		// Attributes are defined by their names, the datatype and the name of the source
		result.appendChild(result.createElement(ATTRIBUTE_SOURCENAME_TAG,attribute.getSourceName()));
		result.appendChild(result.createElement(ATTRIBUTENAME_TAG,attribute.getAttributeName()));
		result.appendChild(result.createElement(ATTRIBUTE_DATATYPE_TAG,attribute.getDatatype().getURI()));
		
		return result;
	}
	
	public static Map<Integer,SDFSchema> createSchemataFromStatement(TextElement<?> statement) {
		Map<Integer,SDFSchema> schemata = new TreeMap<Integer,SDFSchema>();
		Enumeration<? extends TextElement<?>> elems = statement.getChildren();
		while(elems.hasMoreElements()) {
			TextElement<?> elem = elems.nextElement();
			// the name of the element is the portnumber, the content describes the actual schema
			int portNumber = Integer.parseInt(elem.getName());
			SDFSchema schema = createSchemaFromStatement(elem);
			schemata.put(portNumber, schema);
		}
		return schemata;
	}
	
	public static SDFSchema createSchemaFromStatement(TextElement<?> statement) {

		String uri = "";
		Set<SDFAttribute> attributes = new TreeSet<SDFAttribute>();
		Enumeration<? extends TextElement<?>> elems = statement.getChildren();
		while(elems.hasMoreElements()) {
			TextElement<?> elem = elems.nextElement();
			String tag = elem.getName();
			if(tag.equals(OUTPUTSCHEMA_URI_TAG)) {
				uri = elem.getTextValue();
			} else if(tag.equals(ATTRIBUTE_TAG)) {
				attributes.add(createAttributeFromStatement(elem));
			}
		}
		// FIXME: Tuple set a default!
		SDFSchema schema = new SDFSchema(uri,Tuple.class,attributes);
		return schema;
	}
	
	public static SDFAttribute createAttributeFromStatement(TextElement<?> statement) {
		Enumeration<? extends TextElement<?>> elems = statement.getChildren();
		String sourceName = "";
		String attributeName = "";
		SDFDatatype dataType = null;
		
		while(elems.hasMoreElements()) {
			TextElement<?> elem = elems.nextElement();
			String tag = elem.getName();
			if(tag.equals(ATTRIBUTE_SOURCENAME_TAG)) {
				sourceName = elem.getTextValue();
			} else if(tag.equals(ATTRIBUTENAME_TAG)) {
				attributeName = elem.getTextValue();
			} else if(tag.equals(ATTRIBUTE_DATATYPE_TAG)) {
				dataType = new SDFDatatype(elem.getTextValue());
			}
		}
		SDFAttribute result = new SDFAttribute(sourceName, attributeName, dataType);
		return result;
	}
}
