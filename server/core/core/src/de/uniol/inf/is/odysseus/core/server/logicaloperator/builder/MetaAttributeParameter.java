package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;

public class MetaAttributeParameter extends AbstractParameter<IMetaAttribute> {

	private static final long serialVersionUID = -5988817494072361095L;

	@Override
	protected void internalAssignment() {
		IMetaAttribute metaAttribute = null;
		if (inputValue instanceof String) {

			metaAttribute = MetadataRegistry
					.getMetadataTypeByName((String) inputValue);
			if (metaAttribute == null) {
				metaAttribute = MetadataRegistry
						.getMetadataType((String) inputValue);
			}

		} else if(inputValue instanceof List) {
			
				@SuppressWarnings("unchecked")
				List<String> list = (List<String>) inputValue;
				SortedSet<String> sortedSet = new TreeSet<>();
				for (String e : list) {
					IMetaAttribute m = MetadataRegistry.tryCreateMetadataInstance(e);
	
					sortedSet.addAll(MetadataRegistry.toClassNames(Arrays.asList(m.getClasses())));
				}
				metaAttribute = MetadataRegistry.getMetadataType(sortedSet);
			}
			
			else {
				String typeOfInputValue = inputValue.getClass().getSimpleName();
				metaAttribute = MetadataRegistry
						.getMetadataTypeByName(typeOfInputValue);
				if (metaAttribute == null) {
					metaAttribute = MetadataRegistry
							.getMetadataType(typeOfInputValue);
			}
		}

		if (metaAttribute == null) {
			throw new IllegalArgumentException(
					"Cannot find meta attribute type " + inputValue);
		}

		setValue(metaAttribute);
		return;

	}
	
	@Override
	protected String getPQLStringInternal() {
		return getPQLString(getValue());
	}
	
	static public String getPQLString(IMetaAttribute m){
		StringBuilder sb = new StringBuilder();
		// 29.8. Changed: Return list of base types (instead of combined type)
		
		List<SDFMetaSchema> schemas = m.getSchema();
		Iterator<SDFMetaSchema> iter = schemas.iterator();
		sb.append("[");
		sb.append("'"+iter.next().getURIWithoutQualName()+"'");
		while(iter.hasNext()){
			sb.append(",'"+iter.next().getURIWithoutQualName()+"'");
		}		
		
		sb.append("]");

		return sb.toString();
		
	}

}
