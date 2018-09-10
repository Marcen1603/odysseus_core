package de.uniol.inf.is.odysseus.memstore.mdastore.functions;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStoreManager;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MDADropFunction extends AbstractFunction<Void> {

	private static final long serialVersionUID = -3294899746279792492L;

	private static final Logger LOG = LoggerFactory.getLogger(MDADropFunction.class);

	/*
	 * 1: store name
	 */
	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] { { SDFDatatype.STRING } };

	public MDADropFunction() {
		super("MDADrop", 1, acceptedTypes, null);
	}

	@Override
	public Void getValue() {
		String name = getInputValue(0);
		Objects.requireNonNull(name);
		if (MDAStoreManager.exists(name)) {
			MDAStoreManager.remove(name);
			LOG.info("MDAStore " + name + " removed");
		}
		return null;
	}

}