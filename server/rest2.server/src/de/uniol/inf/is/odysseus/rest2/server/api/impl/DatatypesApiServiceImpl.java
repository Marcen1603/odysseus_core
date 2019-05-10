package de.uniol.inf.is.odysseus.rest2.server.api.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rest2.common.model.Attribute;
import de.uniol.inf.is.odysseus.rest2.common.model.Datatype;
import de.uniol.inf.is.odysseus.rest2.common.model.Metaschema;
import de.uniol.inf.is.odysseus.rest2.common.model.Datatype.TypeEnum;
import de.uniol.inf.is.odysseus.rest2.common.model.Schema;
import de.uniol.inf.is.odysseus.rest2.server.ExecutorServiceBinding;
import de.uniol.inf.is.odysseus.rest2.server.api.DatatypesApi;
import de.uniol.inf.is.odysseus.rest2.server.api.DatatypesApiService;

/**
 * This class provides the implementation for the REST service
 * {@link DatatypesApi} that returns the available datatypes.
 * 
 * @author Cornelius A. Ludmann
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaMSF4JServerCodegen", date = "2019-02-07T16:12:00.919Z[GMT]")
public class DatatypesApiServiceImpl extends DatatypesApiService {

	@Override
	public Response datatypesGet(Optional<ISession> session) {

		if (!session.isPresent()) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		IDataDictionaryWritable dd = ExecutorServiceBinding.getExecutor().getDataDictionary(session.get());
		List<Datatype> datatypes = dd.getDatatypes().stream().map(datatype -> transform(datatype))
				.sorted(Comparator.comparing(Datatype::getUri)).collect(Collectors.toList());
		return Response.ok().entity(datatypes).build();
	}

	/**
	 * Transforms a {@link SDFDatatype} object to the DTO {@link Datatype}.
	 * 
	 * @param datatype The source {@link SDFDatatype} object.
	 * @return The resulting {@link Datatype} object.
	 */
	static Datatype transform(SDFDatatype datatype) {
		Objects.requireNonNull(datatype);
		final Datatype result = new Datatype();
		result.setUri(datatype.getURI());
		result.setType(TypeEnum.fromValue(datatype.getType().name()));
		if (datatype.getSubType() != null) {
			result.setSubtype(transform(datatype.getSubType()));
		}
		if (datatype.getSchema() != null) {
			result.setSubschema(transform(datatype.getSchema()));
		}
		return result;
	}

	/**
	 * Transforms a {@link SDFSchema} object to the DTO {@link Schema}.
	 * 
	 * @param schema The source {@link SDFSchema} object.
	 * @return The resulting {@link Schema} object.
	 */
	static Schema transform(SDFSchema schema) {
		Objects.requireNonNull(schema);
		final Schema result = new Schema();
		result.setUri(schema.getURI());
		result.setTypeClass(schema.getType().getName());
		result.setAttributes(
				schema.getAttributes().stream().map(DatatypesApiServiceImpl::transform).collect(Collectors.toList()));
		List<SDFMetaSchema> metaschemas = schema.getMetaschema();
		if (metaschemas != null) {
			List<Metaschema> convMetaSchema = new ArrayList<>();
			for (SDFMetaSchema metaSch:metaschemas) {
				Metaschema metaSchema = new Metaschema();
				Schema m1 = transform(metaSch);
				metaSchema.setMetaattributeClass(metaSch.getMetaAttribute().getName());
				metaSchema.setAttributes(m1.getAttributes());
				metaSchema.setTypeClass(m1.getTypeClass());
				metaSchema.setUri(m1.getUri());
				convMetaSchema.add(metaSchema);
			}
			result.setMetaschema(convMetaSchema);
		}
		
		return result;
	}

	/**
	 * Transforms a {@link SDFAttribute} object to the DTO {@link Attribute}.
	 * 
	 * @param attribute The source {@link SDFAttribute} object.
	 * @return The resulting {@link Attribute} object.
	 */
	static Attribute transform(SDFAttribute attribute) {
		Objects.requireNonNull(attribute);
		final Attribute result = new Attribute();
		result.setAttributename(attribute.getAttributeName());
		result.setSourcename(attribute.getSourceName());
		result.setDatatype(transform(attribute.getDatatype()));
		if (attribute.getDatatype().getSchema() != null) {
			result.setSubschema(transform(attribute.getDatatype().getSchema()));
		}
		return result;
	}
}
