/*
 * Copyright 2005 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.base.field;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.drools.RuntimeDroolsException;
import org.drools.spi.FieldValue;

/**
 * @author etirelli
 *
 */
public class BooleanFieldImpl
    implements
    FieldValue, Externalizable {

    private static final long serialVersionUID = 400L;
    private boolean     value;

    public BooleanFieldImpl() {

    }

    public BooleanFieldImpl(final boolean value) {
        this.value = value;
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        value   = in.readBoolean();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeBoolean(value);
    }

    public Serializable getValue() {
        return this.value ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
	public String toString() {
        return this.value ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
    }

    public boolean getBooleanValue() {
        return this.value;
    }

    public byte getByteValue() {
        throw new RuntimeDroolsException( "Conversion to byte not supported for type boolean" );
    }

    public char getCharValue() {
        throw new RuntimeDroolsException( "Conversion to char not supported for type boolean" );
    }

    public double getDoubleValue() {
        throw new RuntimeDroolsException( "Conversion to double not supported for type boolean" );
    }

    public float getFloatValue() {
        throw new RuntimeDroolsException( "Conversion to float not supported for type boolean" );
    }

    public int getIntValue() {
        throw new RuntimeDroolsException( "Conversion to int not supported for type boolean" );
    }

    public long getLongValue() {
        throw new RuntimeDroolsException( "Conversion to long not supported for type boolean" );
    }

    public short getShortValue() {
        throw new RuntimeDroolsException( "Conversion to short not supported for type boolean" );
    }

    @Override
	public boolean equals(final Object object) {
        if ( this == object ) {
            return true;
        }
        if ( object == null || !(object instanceof BooleanFieldImpl) ) {
            return false;
        }
        final BooleanFieldImpl other = (BooleanFieldImpl) object;

        return this.value == other.value;
    }

    @Override
	public int hashCode() {
        return this.value ? 1 : 0;
    }

    public boolean isNull() {
        return false;
    }

    public boolean isBooleanField() {
        return true;
    }

    public boolean isFloatNumberField() {
        return false;
    }

    public boolean isIntegerNumberField() {
        return false;
    }

    public boolean isObjectField() {
        return false;
    }

    public boolean isCollectionField() {
        return false;
    }

    public boolean isStringField() {
        return false;
    }

	public BigDecimal getBigDecimalValue() {
		throw new RuntimeDroolsException( "Conversion to BigDecimal not supported for type boolean" );
	}

	public BigInteger getBigIntegerValue() {
		throw new RuntimeDroolsException( "Conversion to BigInteger not supported for type boolean" );
	}


}
