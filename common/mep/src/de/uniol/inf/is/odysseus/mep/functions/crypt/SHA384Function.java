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
 * Returns the SHA-384 hash sum of a string.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class SHA384Function extends AbstractFunction<String> {

    /**
     * 
     */
    private static final long serialVersionUID = 2039842591511886794L;
    private static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.STRING };
    private final Charset charset = Charset.forName("UTF8");

    @Override
    public int getArity() {
        return 1;
    }

    @Override
    public SDFDatatype[] getAcceptedTypes(int argPos) {
        if (argPos < 0) {
            throw new IllegalArgumentException("negative argument index not allowed");
        }
        if (argPos >= this.getArity()) {
            throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s): a string");
        }
        return accTypes;
    }

    @Override
    public String getSymbol() {
        return "sha384";
    }

    @Override
    public String getValue() {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-384");
            algorithm.reset();
            algorithm.update(getInputValue(0).toString().getBytes(charset));
            sb.append(DatatypeConverter.printHexBinary(algorithm.digest()));
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    @Override
    public SDFDatatype getReturnType() {
        return SDFDatatype.STRING;
    }

}
