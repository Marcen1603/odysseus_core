package mg.dynaquest.sourcedescription.sdm;

import java.util.*;
import mg.dynaquest.sourcedescription.sdf.description.SDFAccessPattern;
import mg.dynaquest.sourcedescription.sdf.description.SDFSource;
import mg.dynaquest.sourcedescription.sdf.description.SDFSourceDescription;
import mg.dynaquest.sourcedescription.sdf.function.SDFFunction;
import mg.dynaquest.sourcedescription.sdf.mapping.SDFSchemaMapping;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFDataschema;
import mg.dynaquest.sourcedescription.sdf.schema.SDFDatatype;
import mg.dynaquest.sourcedescription.sdf.schema.SDFEntity;

/**
 * Diese Schnittstelle kapselt den Zugriff auf die Quellenbeschreibungen Author:
 * $Author: grawund $ Date: $Date: 2004/09/16 08:57:13 $ Version: $Revision: 1.3 $
 * Log: $Log: SourceDescriptionManager.java,v $
 * Log: Revision 1.3  2004/09/16 08:57:13  grawund
 * Log: Quellcode durch Eclipse formatiert
 * Log: Log: Revision 1.2 2004/03/26
 * 13:57:59 grawund Log: no message Log: Log: Revision 1.1 2004/03/09 14:54:19
 * grawund Log: *** empty log message *** Log: Log: Revision 1.3 2004/03/09
 * 13:40:17 grawund Log: *** empty log message *** Log: Log: Revision 1.2
 * 2003/11/27 15:47:02 grawund Log: no message Log: Log: Revision 1.1 2003/08/20
 * 13:43:52 grawund Log: SDM-Klassen Log:
 */

public interface SourceDescriptionManager {
	/**
	 * Liefert eine Menge von Quellen, die jeweils mindestens eines der
	 * Attribute aus Attributes enthalten
	 */
	public Collection<String> getSourcesWithAttributeFromList(SDFAttributeList attributes)
			throws Exception;

	public SDFSource getSourceForLocalAttribute(SDFAttribute attribute) throws Exception;
	
	/** Liefere zu einer URI die entsprechende Quellenbeschreibung */
	public SDFSourceDescription getSourceDescription(String URI)
			throws Exception;

	public SDFDatatype getDatatype(String URI) throws Exception;

	public SDFAttribute getAttribute(String URI) throws Exception;

	public SDFConstant getConstant(String URI) throws Exception;

	public SDFAttributeList getAttributeSet(String URI) throws Exception;

	public SDFConstantList getConstantSet(int setID) throws Exception;

	public SDFEntity getEntity(String URI) throws Exception;

	public SDFFunction getFunction(String URI) throws Exception;

	public SDFSchemaMapping getSchemaMapping(String mappingURI,
			SDFDataschema localSchema) throws Exception;

	public SDFDataschema getSchema(String URI) throws Exception;

	public SDFAccessPattern getAccessPattern(String URI) throws Exception;

}