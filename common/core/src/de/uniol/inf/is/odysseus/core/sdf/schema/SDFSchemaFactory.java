package de.uniol.inf.is.odysseus.core.sdf.schema;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 * This class is used to create instance of SDFSchema
 * 
 * @author Marco Grawunder
 *
 */
@SuppressWarnings("deprecation")
public class SDFSchemaFactory {

	/**
	 * This method can be used to create a new SDFSchema object. <b>This method
	 * should only be used, if there is no input schema (e.g. from an input
	 * operator) </b>. Typically, this method should only be used in access
	 * operators
	 * 
	 * @param uri
	 *            The name of the schema
	 * @param type
	 *            What is the stream type, that this schema provides (typical
	 *            examples are Tuple or KeyValueObject)
	 * @param attributes
	 *            What attributes are part of the schema
	 * @return
	 */
	static public SDFSchema createNewSchema(String uri,
			Class<? extends IStreamObject<?>> type,
			Collection<SDFAttribute> attributes) {
		return new SDFSchema(uri, type, attributes);
	}

	/**
	 * This method can be used to create a new SDFSchema object. <b>This method
	 * should only be used, if there is no input schema (e.g. from an input
	 * operator) </b>. Typically, this method should only be used in access
	 * operators
	 * 
	 * @param uri
	 *            The name of the schema
	 * @param type
	 *            What is the stream type, that this schema provides (typical
	 *            examples are Tuple or KeyValueObject)
	 * @param attributes
	 *            What attributes are part of the schema
	 * @return
	 */
	static public SDFSchema createNewSchema(String uri,
			Class<? extends IStreamObject<?>> type, SDFAttribute... attributes) {
		return new SDFSchema(uri, type, Arrays.asList(attributes));
	}

	/**
	 * This method can be used to create a new SDFSchema object (with tuples as
	 * types). <b>This method should only be used, if there is no input schema
	 * (e.g. from an input operator) </b>. Typically, this method should only be
	 * used in access operators
	 * 
	 * @param uri
	 *            The name of the schema
	 * @param attributes
	 *            What attributes are part of the schema
	 * @return
	 */

	static public SDFSchema createNewTupleSchema(String uri,
			Collection<SDFAttribute> attributes) {
		@SuppressWarnings("unchecked")
		SDFSchema out = new SDFSchema(uri,
				(Class<? extends IStreamObject<?>>) Tuple.class, attributes);
		return out;
	}

	/**
	 * This method can be used to create a new SDFSchema object (with tuples as
	 * types). <b>This method should only be used, if there is no input schema
	 * (e.g. from an input operator) </b>. Typically, this method should only be
	 * used in access operators
	 * 
	 * @param uri
	 *            The name of the schema
	 * @param attributes
	 *            What attributes are part of the schema
	 * @return
	 */

	static public SDFSchema createNewTupleSchema(String uri,
			SDFAttribute... attributes) {
		@SuppressWarnings("unchecked")
		SDFSchema out = new SDFSchema(uri,
				(Class<? extends IStreamObject<?>>) Tuple.class,
				Arrays.asList(attributes));
		return out;
	}

	/**
	 * This method creates a new SDFSchema by replacing the current attributes
	 * 
	 * @param attributes
	 *            the new attribute
	 * @param inputSchema
	 *            the input schema from which the new schema should be derived
	 * @return
	 */
	static public SDFSchema createNewWithAttributes(
			Collection<SDFAttribute> attributes, SDFSchema inputSchema) {
		return new SDFSchema(inputSchema, attributes);
	}

	/**
	 * This method creates a new SDFSchema by replacing the current attributes
	 * and the name of the schema
	 * 
	 * @param name
	 *            the new name the schema should get
	 * @param attributes
	 *            the new attributes of the schema
	 * @param inputSchema
	 *            the input schema from which the new schema should be derived
	 * @return
	 */
	static public SDFSchema createNewWithAttributes(String name,
			Collection<SDFAttribute> attributes, SDFSchema inputSchema) {
		return new SDFSchema(name, inputSchema, attributes);
	}

	/**
	 * This method creates a new SDFSchema by replacing the current attributes
	 * 
	 * @param inputSchema
	 *            the input schema from which the new schema should be derived
	 * @param attributes
	 *            the new attribute
	 * @return
	 */
	static public SDFSchema createNewWithAttributes(SDFSchema inputSchema,
			SDFAttribute... attributes) {
		return new SDFSchema(inputSchema, Arrays.asList(attributes));
	}

	/**
	 * This method creates a new SDFSchema by appending a new attribute at the
	 * end of the current input schema. If multiple attributes should be
	 * appended use crateNewAddAttributes
	 * 
	 * @param attribute
	 *            the single attribute to append
	 * @param inputSchema
	 *            the input schema from which the new schema should be derived
	 * @return
	 */
	static public SDFSchema createNewAddAttribute(SDFAttribute attribute,
			SDFSchema inputSchema) {
		List<SDFAttribute> newAttributes = new ArrayList<>(
				inputSchema.getAttributes());
		newAttributes.add(attribute);
		return new SDFSchema(inputSchema, newAttributes);
	}

	/**
	 * This method creates a new SDFSchema by appending new attributes at the
	 * end of the current input schema. If a single attribute should be appended
	 * use crateNewAddAttribute
	 * 
	 * @param attribute
	 *            the single attribute to append
	 * @param inputSchema
	 *            the input schema from which the new schema should be derived
	 * @return
	 */
	static public SDFSchema createNewAddAttributes(
			Collection<SDFAttribute> attributes, SDFSchema inputSchema) {
		List<SDFAttribute> newAttributes = new ArrayList<>(
				inputSchema.getAttributes());
		newAttributes.addAll(attributes);
		return new SDFSchema(inputSchema, newAttributes);
	}

	/**
	 * This method creates a new SDFSchema with the special flag strictOrder set
	 * (to true or false)
	 * 
	 * @param strictOrder
	 *            If set to true, the order for every case where two elements
	 *            have the same order key (e.g. a time stamp) an additional
	 *            parameter (e.g. hashCode) is used to guarantee the same order
	 *            in every case
	 * @param inputSchema
	 *            the input schema from which the new schema should be derived
	 * @return
	 */
	static public SDFSchema createNewWithStrictOrder(boolean strictOrder,
			SDFSchema inputSchema) {
		SDFSchema output = inputSchema.clone();
		output.setStrictOrder(strictOrder);
		return output;
	}

	/**
	 * This method creates a new SDFSchema where contraints are added
	 * 
	 * @param constraints
	 *            A map of constraints
	 * @param inputSchema
	 *            the input schema from which the new schema should be derived
	 * @return
	 */
	static public SDFSchema createNewWithContraints(
			Map<String, SDFConstraint> constraints, SDFSchema inputSchema) {
		SDFSchema output = inputSchema.clone();
		output.setContraints(constraints);
		return output;
	}
}
