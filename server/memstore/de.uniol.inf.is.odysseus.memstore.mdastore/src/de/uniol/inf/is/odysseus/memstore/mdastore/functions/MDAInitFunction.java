package de.uniol.inf.is.odysseus.memstore.mdastore.functions;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStore;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStoreManager;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MDAInitFunction extends AbstractFunction<Void> {

	private static final long serialVersionUID = 7492772722745497021L;

	private static final Logger LOG = LoggerFactory.getLogger(MDAInitFunction.class);

	/*
	 * 1: store name 2: list of dimensions
	 */
	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] { { SDFDatatype.STRING },
			SDFDatatype.getLists() };

	public MDAInitFunction() {
		super("MDAInit", 2, acceptedTypes, null);
	}

	@Override
	public Void getValue() {
		String name = getInputValue(0);
		Objects.requireNonNull(name);
		if (!MDAStoreManager.exists(name)) {
			MDAStore store = MDAStoreManager.create(name);
			LOG.info("MDAStore " + name + " created");
			List<List<Double>> dims = getInputValue(1);
			Objects.requireNonNull(dims);
			store.initialize(dims);
			LOG.info("MDAStore " + name + " initialized");
		}
		return null;
	}

}