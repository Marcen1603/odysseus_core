package de.uniol.inf.is.odysseus.wrapper.dds.dds;
import com.rti.dds.dynamicdata.DynamicData;


public interface IDDSDataReader<T> {
	T getValue(DynamicData data, String name, int pos);
}
