/*******************************************************************************
 * Copyright 2015 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.mep.functions.bit;

import java.util.zip.CRC32;

import de.uniol.inf.is.odysseus.core.collection.BitVector;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * CRC MEP function to calculate the CRC checksum of a bit vector.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class CRCChecksumFunction extends AbstractFunction<BitVector> {
    /**
     * 
     */
    private static final long serialVersionUID = 6843765070417600871L;
    private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[] { SDFDatatype.BITVECTOR } };

    public CRCChecksumFunction() {
        super("crc", 1, accTypes, SDFDatatype.BITVECTOR);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BitVector getValue() {
        CRC32 checksum = new CRC32();
        BitVector vector = getInputValue(0);
        checksum.update(vector.getBytes());
        return BitVector.fromLong(checksum.getValue());
    }

}
