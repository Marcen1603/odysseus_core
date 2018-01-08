package de.uniol.inf.is.odysseus.parser.cql2.generator.cache;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;

public class SelectCache implements Cache {

//	private final Logger log = LoggerFactory.getLogger(SelectCache.class);
	
	private Collection<SimpleSelect> selects;

	public SelectCache() {

		selects = new ArrayList<>();

	}

	public Collection<SimpleSelect> getSelects() {
		return selects;
	}

	public SimpleSelect last() {

		if (!selects.isEmpty()) {
			return new ArrayList<>(selects).get(selects.size() - 1);
		}

		return null;
	}

	@Override
	public void flush() {
		selects.clear();
	}

	public void add(SimpleSelect select) {
		
		if (select != null) {
			selects.add(select);
		}

	}

}
