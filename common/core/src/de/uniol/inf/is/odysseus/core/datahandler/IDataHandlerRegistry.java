package de.uniol.inf.is.odysseus.core.datahandler;

import java.util.List;

import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface IDataHandlerRegistry {

	IStreamObjectDataHandler<?> getStreamObjectDataHandler(String dataType, SDFSchema schema);

	IDataHandler<?> getDataHandler(SDFDatatype dataType, SDFSchema schema);

	IDataHandler<?> getDataHandler(String dataType, SDFSchema schema);

	boolean containsDataHandler(String dataType);

	IStreamObjectDataHandler<?> getStreamObjectDataHandler(String dataType, List<SDFDatatype> schema);

	IDataHandler<?> getDataHandler(String dataType, List<SDFDatatype> schema);

	ImmutableList<String> getHandlerNames();

	@SuppressWarnings("rawtypes")
	Class<? extends IStreamObject> getCreatedType(String dhandlerText);

	IDataHandler<?> getIDataHandlerClass(String dhandlerText);

	List<String> getStreamableDataHandlerNames();

}
