package de.uniol.inf.is.odysseus.parser.cql2.generator.cache;

import java.util.ArrayList;
import java.util.Collection;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import java.util.List;

public class SelectCache implements Cache {

//	private final Logger log = LoggerFactory.getLogger(SelectCache.class);
	
	private Collection<SimpleSelect> selects;
	private Collection<MetaInfo> metas;

	public SelectCache() {
		selects = new ArrayList<>();
		metas = new ArrayList<>();
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
		
		if (select != null && !selects.contains(select)) {
			selects.add(select);
			metas.add(null);
		}

	}

	public void add(SimpleSelect select, MetaInfo meta) {
		
		if (select != null && !selects.contains(select)) {
			selects.add(select);
			metas.add(meta);
		}

	}
	
	public void remove(SimpleSelect select) {
		if (selects.contains(select) ) {
			selects.remove(select);
		}
	}

	public static class MetaInfo {
		
		public SimpleSelect containedBy;
		public boolean containedByPredicate;
		
		public MetaInfo(SimpleSelect containedBy, boolean containedByPredicate) {
			super();
			this.containedBy = containedBy;
			this.containedByPredicate = containedByPredicate;
		}
		
	}

	public void addAll(List<SimpleSelect> selects) {
		selects.stream().forEach(e -> add(e));
	}
	
}
