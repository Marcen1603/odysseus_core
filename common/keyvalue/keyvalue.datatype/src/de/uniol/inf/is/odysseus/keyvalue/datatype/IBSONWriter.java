package de.uniol.inf.is.odysseus.keyvalue.datatype;

public interface IBSONWriter {

	byte[] writeBSONData(KeyValueObject<?> kvObject);

}
