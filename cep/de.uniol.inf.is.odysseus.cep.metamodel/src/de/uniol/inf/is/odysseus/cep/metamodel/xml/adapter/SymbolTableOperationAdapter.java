package de.uniol.inf.is.odysseus.cep.metamodel.xml.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import de.uniol.inf.is.odysseus.cep.metamodel.Count;
import de.uniol.inf.is.odysseus.cep.metamodel.Max;
import de.uniol.inf.is.odysseus.cep.metamodel.Min;
import de.uniol.inf.is.odysseus.cep.metamodel.Write;
import de.uniol.inf.is.odysseus.cep.metamodel.Sum;
import de.uniol.inf.is.odysseus.cep.metamodel.SymbolTableOperation;

public class SymbolTableOperationAdapter extends XmlAdapter<String, SymbolTableOperation<?>> {

	@Override
	public String marshal(SymbolTableOperation<?> arg0) throws Exception {
		return arg0.getClass().getSimpleName();
	}

	@Override
	@SuppressWarnings("unchecked")
	public SymbolTableOperation<?> unmarshal(String arg0) throws Exception {
		if (arg0.equals("Count"))
			return new Count();
		if (arg0.equals("Max"))
			return new Max();
		if (arg0.equals("Min"))
			return new Min();
		// TODO: Das hier geht so leider schief ...
		if (arg0.equals("Write"))
			return new Write();
		if (arg0.equals("Sum"))
			return new Sum();
		return null;
	}

}
