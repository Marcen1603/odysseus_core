package de.uniol.inf.is.odysseus.sports.mep;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.peer.ddc.DDCKey;
import de.uniol.inf.is.odysseus.peer.ddc.IDistributedDataContainer;
import de.uniol.inf.is.odysseus.peer.ddc.MissingDDCEntryException;

public class AccessToDCCFunction extends AbstractFunction<String>{
	
	private static final long serialVersionUID = -1899807987181546880L;
	
	/**
	 * The logger for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(AccessToDCCFunction.class);
	
	protected static IDistributedDataContainer ddc;

	
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
		{ SDFDatatype.STRING } };

	public AccessToDCCFunction() {
	        super("fromDDC", 1, accTypes, SDFDatatype.STRING);
	    }
	
	
	/**
	 * Binds a DDC. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param ddc
	 *            The DDC to bind. <br />
	 *            Must be not null.
	 */
	public static void bindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		AccessToDCCFunction.ddc = ddc;
		AccessToDCCFunction.LOG.debug("Bound {} as a DDC", ddc.getClass()
				.getSimpleName());

	}

	/**
	 * Removes the binding for a DDC. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param ddc
	 *            The DDC to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindDDC(IDistributedDataContainer ddc) {

		Preconditions.checkNotNull(ddc, "The DDC to bind must be not null!");
		if (AccessToDCCFunction.ddc == ddc) {

			AccessToDCCFunction.ddc = null;
			AccessToDCCFunction.LOG.debug("Unbound {} as a DDC", ddc.getClass()
					.getSimpleName());

		}

	}

	@Override
	public String getValue() {
		DDCKey searchDDCKey;
		String ddcValue = "";
		
		String inputString =  getInputValue(0).toString();
		String[] ddcKeys = inputString.split(",");
	
		if(ddcKeys.length > 1){
			searchDDCKey = new DDCKey(ddcKeys);
		}else{
			searchDDCKey = new DDCKey(inputString);
		}
		
		try {
			ddcValue = AccessToDCCFunction.ddc.getValue(searchDDCKey);
		} catch (MissingDDCEntryException e) {
			e.printStackTrace();
		}
	
		return ddcValue;
	}

}
