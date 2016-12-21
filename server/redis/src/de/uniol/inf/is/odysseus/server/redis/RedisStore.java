package de.uniol.inf.is.odysseus.server.redis;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.objecthandler.ObjectByteConverter;
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.server.store.StoreException;
import redis.clients.jedis.Jedis;

public class RedisStore<STORETYPE> implements IStore<String, STORETYPE> {

	private static final long serialVersionUID = -6268065816400539741L;
	final static String HOST = "host";
	final static String PORT = "port";
	final static String TYPE = "REDIS";

	Jedis jedis;

	@Override
	public IStore<String, STORETYPE> newInstance(OptionMap options) throws StoreException {
		options.checkRequiredException(HOST, PORT);
		return new RedisStore<>(options.get(HOST), options.getInt(PORT, 6379));
	}

	public RedisStore() {
	}

	public RedisStore(String host, int port) {
		jedis = new Jedis(host, port);
	}

	@SuppressWarnings("unchecked")
	@Override
	public STORETYPE get(String id) {
		byte[] ret = jedis.get(id.getBytes());
		Object obj = ObjectByteConverter.bytesToObject(ret);
		return (STORETYPE) obj;
	}

	@Override
	public List<Entry<String, STORETYPE>> getOrderedByKey(long limit) {
		Set<String> keys = keySet();
		List<String> list = new ArrayList<>(keys);
		Collections.sort(list);
		List<Entry<String, STORETYPE>> result = new ArrayList<>();
		if (limit > 0) {
			for (int i = 0; i < limit; i++) {
				String key = list.get(i);
				result.add(Maps.immutableEntry(key, get(key)));
			}
		} else {
			list.forEach((String key) -> result.add(Maps.immutableEntry(key, get(key))));
		}
		return result;
	}

	@Override
	public void put(String id, STORETYPE element) throws StoreException {
		byte[] obj = ObjectByteConverter.objectToBytes(element);
		jedis.set(id.getBytes(), obj);
	}

	@Override
	public STORETYPE remove(String id) throws StoreException {
		STORETYPE ret = get(id);
		jedis.del(id.getBytes());
		return ret;
	}

	@Override
	public Set<Entry<String, STORETYPE>> entrySet() {
		Set<String> keys = this.keySet();
		Set<Entry<String, STORETYPE>> result = new HashSet<>();
		keys.forEach((String key) -> result.add(Maps.immutableEntry(key, get(key))));
		return result;
	}

	@Override
	public Set<String> keySet() {
		return jedis.keys("*");
	}

	@Override
	public Collection<STORETYPE> values() {
		Set<String> keys = this.keySet();
		List<STORETYPE> result = new ArrayList<>();
		keys.forEach((String key) -> result.add(get(key)));
		return result;
	}

	@Override
	public boolean containsKey(String key) {
		return jedis.exists(key.getBytes());
	}

	@Override
	public boolean isEmpty() {
		return jedis.dbSize() == 0;
	}

	@Override
	public void clear() throws StoreException {
		// jedis.keys("*").forEach((String key) -> jedis.del(key));
		String[] keys = jedis.keys("*").toArray(new String[0]);
		if (keys.length > 0) {
			jedis.del(keys);
		}
	}

	@Override
	public void commit() {
		// Nothing to do
	}

	@Override
	public int size() {
		return jedis.keys("*").size();
	}

	@Override
	public void dumpTo(StringBuffer buffer) {
		Set<String> keys = keySet();
		keys.forEach((String key) -> buffer.append(key).append("-->").append(get(key)).append("\n"));
	}

	@Override
	public String getType() {
		return TYPE;
	}

	public static void main(String[] args) {

		RedisStore<Object> factory = new RedisStore<>();
		OptionMap options = new OptionMap();
		options.setOption(HOST, "192.168.1.51");
		options.setOption(PORT, "6379");
		IStore<String, Object> store = factory.newInstance(options);

		StringBuffer buffer;
		buffer = new StringBuffer();
		store.dumpTo(buffer);
		System.out.println(buffer);

		store.clear();

		buffer = new StringBuffer();
		store.dumpTo(buffer);
		System.out.println(buffer);

		String value = "VALUE";
		store.put("v1", value);

		buffer = new StringBuffer();
		store.dumpTo(buffer);
		System.out.println(buffer);

		for (int i = 0; i < 1000; i++) {
			store.put("id" + i, "value_" + i);
		}

		buffer = new StringBuffer();
		store.dumpTo(buffer);
		System.out.println(buffer);

		System.out.println(store.getOrderedByKey(10));

		store.clear();
	}

}
