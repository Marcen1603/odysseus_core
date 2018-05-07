package de.uniol.inf.is.odysseus.parser.cql2.test;

import org.junit.Test;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.impl.SimpleSelectImpl;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.OperatorCache;

public class OperatorCacheTest {

	@Test
	public void test1() {
		
		OperatorCache oc = new OperatorCache();
		
		String id = oc.add(new SimpleSelectImpl(), "SELECT({predicate='bid.auction==7||bid.auction==20||bid.auction==21||bid.auction==59||bid.auction==87'},bid)");
		String r = oc.add(new SimpleSelectImpl(), "MAP({expressions=['auction','price']}," + id + ")");
		
		
	}
	
}
