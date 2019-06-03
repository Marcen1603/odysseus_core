package de.uniol.inf.is.odysseus.mep.functions.inet;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function to create an InetIP4 address from bytes.
 * 
 * @author Michael BRand (michael.brand@uol.de)
 *
 */
public class IP4AddressFromBytesFunction extends AbstractFunction<Inet4Address> {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = -5568058924253622108L;

	/**
	 * The name of the MEP function to be used in query languages.
	 */
	private static final String name = "IP4AddressFromBytes";

	/**
	 * The amount of input values.
	 */
	private static final int numInputs = 1;

	/**
	 * The expected data types of the inputs. One row for each input. Different data
	 * types in a row mark different possible data types for the input.
	 */
	private static final SDFDatatype[][] inputTypes = new SDFDatatype[][] { { SDFDatatype.LIST_BYTE } };

	/**
	 * The data type of the outputs.
	 */
	private static final SDFDatatype outputType = SDFDatatype.OBJECT;

	/**
	 * Creates a new MEP function.
	 */
	public IP4AddressFromBytesFunction() {
		super(name, numInputs, inputTypes, outputType);
	}

	@Override
	public Inet4Address getValue() {
		List<Byte> inputBytes = getInputValue(0);
		byte[] addressBytes = new byte[4];
		for (int i = 0; i < inputBytes.size(); i++) {
			addressBytes[i] = inputBytes.get(i);
		}
		try {
			return (Inet4Address) Inet4Address.getByAddress(addressBytes);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return outputType;
	}

}