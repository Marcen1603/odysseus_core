package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface ITransformer<IN, OUT> {

	/**
	 * Transforms the input to output that will be returned
	 * @param input
	 * @return
	 */
	public OUT transform(IN input);
	
	/**
	 * Creates a new instance of this transformer. 
	 * @param options: potential options needed for this transformer, can be null
	 * @param schema: potential schema, can be null
	 * @return a new instance of this transformer
	 */
	public ITransformer<IN, OUT> getInstance(Map<String, String> options, SDFSchema schema);

	
	public String getName();
	
}
