/**
 * 
 */
package de.uniol.inf.is.odysseus.mep.functions.crypt;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class DSAFunction extends AbstractFunction<List<Tuple<?>>> {


    private static final long serialVersionUID = -7234007414686423002L;
    
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][]{
		new SDFDatatype[] { SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.DOUBLE, SDFDatatype.FLOAT}};

    public DSAFunction() {
    	super("dsa",1, accTypes,SDFDatatype.TUPLE);
    }
    
    @Override
    public List<Tuple<?>> getValue() {
        int size = getNumericalInputValue(0).intValue();
        List<Tuple<?>> keys = new ArrayList<Tuple<?>>(2);

        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA");
            keyGen.initialize(size);
            KeyPair keypair = keyGen.genKeyPair();
            PrivateKey privateKey = keypair.getPrivate();

            PublicKey publicKey = keypair.getPublic();
            @SuppressWarnings("rawtypes")
            Tuple<?> privateTuple = new Tuple(new Object[] { DatatypeConverter.printHexBinary(privateKey.getEncoded()) }, false);
            @SuppressWarnings("rawtypes")
            Tuple<?> publicTuple = new Tuple(new Object[] { DatatypeConverter.printHexBinary(publicKey.getEncoded()) }, false);
            keys.add(privateTuple);
            keys.add(publicTuple);
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return keys;
    }


}
