package de.uniol.inf.is.odysseus.p2p_new.dictionary.impl;

import java.util.List;
import java.util.Map;

import net.jxta.document.Document;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AdvertisementCache {

	private static final int MAX_COUNT = 100;
	private static final long DOC_TOO_OLD_MILLIS = 300 * 1000;

	private static Map<Integer, Document> cachedAdvs = Maps.newHashMap();
	private static Map<Integer, Long> timestamps = Maps.newHashMap();
	private static List<Integer> order = Lists.newLinkedList();

	public synchronized static void add(Integer hash, Document doc) {
		cachedAdvs.put(hash, doc);
		order.add(hash);
		timestamps.put(hash, System.currentTimeMillis());

		if (cachedAdvs.size() > MAX_COUNT) {
			Integer hashToRemove = order.remove(0);
			cachedAdvs.remove(hashToRemove);
			timestamps.remove(hashToRemove);
		}
	}

	public synchronized static Optional<Document> getDocument(Integer hash) {
		Document document = cachedAdvs.get(hash);
		if (document != null && System.currentTimeMillis() - timestamps.get(hash) < DOC_TOO_OLD_MILLIS) {
			return Optional.fromNullable(document);
		}

		return Optional.absent();
	}
}
