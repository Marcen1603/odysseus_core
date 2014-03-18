/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.crypt;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Returns the SHA-244 hash sum of a string. The SHA-244 is not available in
 * every JDK.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SHA244Function extends AbstractFunction<String> {

	private static final long serialVersionUID = 8609120626336351385L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFDatatype.STRING }};
	private final Charset charset = Charset.forName("UTF8");

	public SHA244Function() {
		super("sha244", 1, accTypes, SDFDatatype.STRING);
	}

	@Override
	public String getValue() {
		StringBuilder sb = new StringBuilder();
		try {
			MessageDigest algorithm = MessageDigest.getInstance("SHA-244");
			algorithm.reset();
			algorithm.update(getInputValue(0).toString().getBytes(charset));
			sb.append(DatatypeConverter.printHexBinary(algorithm.digest()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		return sb.toString();
	}

}
