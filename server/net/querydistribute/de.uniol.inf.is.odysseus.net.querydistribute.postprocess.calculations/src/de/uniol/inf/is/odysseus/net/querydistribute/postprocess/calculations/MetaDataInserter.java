package de.uniol.inf.is.odysseus.net.querydistribute.postprocess.calculations;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.metadata.MetadataRegistry;
import de.uniol.inf.is.odysseus.net.querydistribute.ILogicalQueryPart;

/**
 * Helper class to add meta data to access operators.
 *
 * @author Michael Brand
 *
 */
public final class MetaDataInserter {

	public static void insertMetaData(Collection<ILogicalQueryPart> queryParts, Class<? extends IMetaAttribute> metadataToAdd) {
		Objects.requireNonNull(queryParts);
		Objects.requireNonNull(metadataToAdd);
		queryParts
				.forEach(part -> part.getOperators().stream()
						.filter(operator -> operator instanceof AbstractAccessAO)
						.forEach(operator -> insertMetaData((AbstractAccessAO) operator, metadataToAdd)));
	}

	private static void insertMetaData(AbstractAccessAO access, Class<? extends IMetaAttribute> metadataToAdd) {
		List<String> metadataList = access.getOutputSchema().getMetaAttributeNames();
		SortedSet<String> metadataSet = new TreeSet<>(metadataList);
		metadataSet.add(metadataToAdd.getName());
		access.setLocalMetaAttribute(MetadataRegistry.getMetadataType(metadataSet));
	}

}