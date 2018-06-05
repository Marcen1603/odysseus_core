package de.uniol.inf.is.odysseus.memstore.mdastore.functions;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStore;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStoreManager;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MDARemoveDimFunction extends AbstractFunction<Void> {

	private static final long serialVersionUID = -2155748884930749034L;

	private static final Logger LOG = LoggerFactory.getLogger(MDARemoveDimFunction.class);

	private static final InfoService INFO = InfoServiceFactory.getInfoService(MDARemoveDimFunction.class);

	/*
	 * 1: store name 2: dimension index to remove
	 */
	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] { { SDFDatatype.STRING },
			{ SDFDatatype.INTEGER } };

	public MDARemoveDimFunction() {
		super("MDARemoveDim", 2, acceptedTypes, null);
	}

	@Override
	public Void getValue() {
		String name = getInputValue(0);
		Objects.requireNonNull(name);
		Long index = getInputValue(1);
		Objects.requireNonNull(index);
		removeDim(name, index.intValue());
		return null;
	}

	public static void removeDim(String storename, int dimIndex) {
		if (!MDAStoreManager.exists(storename)) {
			INFO.warning("Remove dimension from MDA store: Store with name " + storename + " not defined");
			return;
		}
		MDAStore store = MDAStoreManager.get(storename);

		store.removeDimension(dimIndex);
		LOG.info("Removed dimension " + dimIndex + " from MDAStore " + storename);
	}

}
