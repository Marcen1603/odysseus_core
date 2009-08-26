package de.uniol.inf.is.odysseus.sourcedescription.sdf.mapping;

import java.util.HashMap;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDataschema;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFMappings;

public class SDFSchemaMappingFactory {
	private static HashMap<String, SDFSchemaMapping> mappingCache = new HashMap<String, SDFSchemaMapping>();

	protected SDFSchemaMappingFactory() {
	}

	public static SDFSchemaMapping getSchemaMapping(String mappingURI,
			String mappingTypeURI, SDFDataschema localSchema,
			SDFDataschema globalSchema) {
		SDFSchemaMapping mapping = mappingCache.get(mappingURI);
		if (mapping == null) {

			while (true) {
				if (mappingTypeURI.equals(SDFMappings.OneToOne)) {
					mapping = new SDFOneToOneMapping(mappingURI, localSchema,
							globalSchema);
					break;
				}
				if (mappingTypeURI.equals(SDFMappings.OneToMany)) {
					mapping = new SDFOneToManyMapping(mappingURI, localSchema,
							globalSchema);
					break;
				}
				if (mappingTypeURI.equals(SDFMappings.ManyToOne)) {
					mapping = new SDFManyToOneMapping(mappingURI, localSchema,
							globalSchema);
					break;
				}
				if (mappingTypeURI.equals(SDFMappings.Conditional)) {
					mapping = new SDFConditionalMapping(mappingURI,
							localSchema, globalSchema);
					break;
				}
				if (mappingTypeURI.equals(SDFMappings.Equivalence)) {
					mapping = new SDFEquivalenceMapping(mappingURI,
							localSchema, globalSchema);
					break;
				}
				if (mappingTypeURI.equals(SDFMappings.Superset)) {
					mapping = new SDFSupersetMapping(mappingURI, localSchema,
							globalSchema);
					break;
				}
				if (mappingTypeURI.equals(SDFMappings.Subset)) {
					mapping = new SDFSubsetMapping(mappingURI, localSchema,
							globalSchema);
					break;
				}
				if (mappingTypeURI.equals(SDFMappings.Common)) {
					mapping = new SDFCommonElementsMapping(mappingURI,
							localSchema, globalSchema);
					break;
				}
				System.out.println("Fehlerhafter Mapping-Type: "
						+ mappingTypeURI);
				break;
			}
			mappingCache.put(mappingURI, mapping);
		}
		return mapping;
	}
}