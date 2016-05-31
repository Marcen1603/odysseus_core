package de.uniol.inf.is.odysseus.memstore.mdastore.functions;

import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStore;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStoreManager;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MDAAddDimFunction extends AbstractFunction<Void> {

	private static final long serialVersionUID = -2155748884930749034L;

	private static final Logger LOG = LoggerFactory.getLogger(MDAAddDimFunction.class);

	private static final InfoService INFO = InfoServiceFactory.getInfoService(MDAAddDimFunction.class);

	/*
	 * 1: store name 2: dimension to add
	 */
	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] { { SDFDatatype.STRING },
			{ SDFDatatype.LIST_DOUBLE } };

	public MDAAddDimFunction() {
		super("MDAAddDim", 2, acceptedTypes, null);
	}

	@Override
	public Void getValue() {
		String name = getInputValue(0);
		Objects.requireNonNull(name);
		List<Double> dim = getInputValue(1);
		Objects.requireNonNull(dim);
		addDim(name, dim);
		return null;
	}
	
	public static void addDim(String storename, List<Double> dim) {
		if (!MDAStoreManager.exists(storename)) {
			INFO.warning("Add dimension to MDA store: Store with name " + storename + " not defined");
			return;
		}
		MDAStore store = MDAStoreManager.get(storename);

		store.addDimension(dim);
		LOG.info("Added dimension " + dim + " to MDAStore " + storename);
	}

}
