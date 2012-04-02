package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

public interface ITransformer<IN, OUT> {

	public OUT transform(IN input);

}
