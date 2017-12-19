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

public class MDAAddDimWithIndexFunction extends AbstractFunction<Void> {

	private static final long serialVersionUID = -2155748884930749034L;

	private static final Logger LOG = LoggerFactory.getLogger(MDAAddDimWithIndexFunction.class);

	private static final InfoService INFO = InfoServiceFactory.getInfoService(MDAAddDimWithIndexFunction.class);

	/*
	 * 1: store name 2: index of dimension (not replacing, but inserting) 3: dimension to add
	 */
	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] { { SDFDatatype.STRING },
			{ SDFDatatype.INTEGER }, { SDFDatatype.LIST_DOUBLE } };

	public MDAAddDimWithIndexFunction() {
		super("MDAAddDim", 3, acceptedTypes, null);
	}

	@Override
	public Void getValue() {
		String name = getInputValue(0);
		Objects.requireNonNull(name);
		Long index = getInputValue(1);
		Objects.requireNonNull(index);
		List<Double> dim = getInputValue(2);
		Objects.requireNonNull(dim);
		addDim(name, index.intValue(), dim);
		return null;
	}
	
	public static void addDim(String storename, int dimIndex, List<Double> dim) {
		if (!MDAStoreManager.exists(storename)) {
			INFO.warning("Add dimension to MDA store: Store with name " + storename + " not defined");
			return;
		}
		MDAStore store = MDAStoreManager.get(storename);

		store.addDimension(dimIndex, dim);
		LOG.info("Added dimension " + dim + " to MDAStore " + storename);
	}

}
