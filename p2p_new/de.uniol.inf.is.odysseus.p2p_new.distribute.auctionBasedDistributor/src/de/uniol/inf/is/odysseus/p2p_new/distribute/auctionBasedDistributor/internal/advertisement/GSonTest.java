package de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.internal.advertisement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.p2p_new.distribute.auctionBasedDistributor.model.CostSummary;

public class GSonTest {
	private static final Gson gson = new Gson();
	private static final Type jsonType = new TypeToken<HashMap<String, CostSummary>>(){}.getType();
	
	@Test
	public void deserializeJsonToMap() {
		String str = "{'a':10, 'b':9, 'c':8}";
		final Gson gson = new Gson();
		final Type type = new TypeToken<HashMap<String, Double>>(){}.getType();
		final Map<String, Double> map = gson.fromJson(str, type);
		assertEquals(3, map.keySet().size());
		assertEquals(10, map.get("a"), 0);
	}
	
	@Test
	public void serializeMapToJson() {
		final Gson gson = new Gson();
		final Type type = new TypeToken<HashMap<String, Double>>(){}.getType();
		
		Map<String, Double> map = new HashMap<>();
		map.put("a", 10d);
		map.put("b", 9d);
		map.put("c", 8d);
		
		String json = gson.toJson(map, type);

		final Map<String, Double> map2 = gson.fromJson(json, type);
		assertEquals(map.keySet().size(), map2.keySet().size());
		assertEquals(map.get("b"), map.get("b"), 0);
	}	
	
	@Test
	public void printJsonTest() {
		Map<String, CostSummary> map = Maps.newHashMap();
		map.put("0", new CostSummary(null, 1, 1, createQueryParts2()));
		String json = gson.toJson(map, jsonType);
		assertNotNull(json);
		System.out.println(json);
	}
	
	private ILogicalOperator createQueryParts2() {
		ILogicalOperator stream = new StreamAO();
		ILogicalOperator window = new WindowAO();
		ILogicalOperator select1 = new SelectAO();
		ILogicalOperator select2 = new SelectAO();
		ILogicalOperator union = new UnionAO();
		ILogicalOperator select3 = new SelectAO();
		
		stream.subscribeSink(window, 0, 0, null);
		window.subscribeSink(select1, 0, 0, null);
		window.subscribeSink(select2, 0, 1, null);
		select1.subscribeSink(union, 0, 0, null);
		select2.subscribeSink(union, 1, 0, null);
		union.subscribeSink(select3, 0, 0, null);
		return select3;
	}
}
