/**********************************************************************************
 * Copyright 2014 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public abstract class AbstractCryptGenerateFunction extends AbstractFunction<List<String>> {
    /**
     *
     */
    private static final long serialVersionUID = 7027761738877513116L;
    private static final SDFDatatype[][] ACC_TPES = new SDFDatatype[][] { SDFDatatype.NUMBERS };
    private final String algorithm;

    public AbstractCryptGenerateFunction(final String algorithm) {
        super(algorithm.replace("-", ""), 1, AbstractCryptGenerateFunction.ACC_TPES, SDFDatatype.LIST_STRING);
        this.algorithm = algorithm;
    }

    @Override
    public List<String> getValue() {
        final int size = this.getNumericalInputValue(0).intValue();
        final List<String> keys = new ArrayList<>(2);

        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(this.algorithm);
            keyGen.initialize(size);
            final KeyPair keypair = keyGen.genKeyPair();
            final PrivateKey privateKey = keypair.getPrivate();

            final PublicKey publicKey = keypair.getPublic();
            final String privateKeyString = DatatypeConverter.printHexBinary(privateKey.getEncoded());
            final String publicKeyString = DatatypeConverter.printHexBinary(publicKey.getEncoded());
            keys.add(privateKeyString);
            keys.add(publicKeyString);
        }
        catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return keys;
    }

}
