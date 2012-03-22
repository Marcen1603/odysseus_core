/** Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.uniol.inf.is.odysseus.application.storing.model;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.application.storing.controller.RecordEntry;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.store.MemoryStore;
import de.uniol.inf.is.odysseus.core.server.store.StoreException;
import de.uniol.inf.is.odysseus.core.server.util.FileUtils;

/**
 * 
 * @author Dennis Geesen Created at: 11.11.2011
 */
// public class RecordingStore extends FileStore<String, RecordEntry> {
public class RecordingStore {

	private static final String PATH_TO_STORE = OdysseusConfiguration.get("odysseusHome") + "recordings.store";

	private MemoryStore<String, RecordEntry> cache = new MemoryStore<String, RecordEntry>();

	public RecordingStore() throws IOException {
		loadCache();
	}

	private void loadCache() throws IOException {
		File f = FileUtils.openOrCreateFile(PATH_TO_STORE);
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(f));
			String key = null;
			try {
				while ((key = (String) in.readObject()) != null) {
					RecordEntry element = (RecordEntry) in.readObject();
					cache.put(key, element);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			in.close();
		} catch (EOFException e) {
			// initial ...
		}

	}

	public Map<String, RecordEntry> getMap(){
		Map<String, RecordEntry> m = new HashMap<String, RecordEntry>();
		for(Entry<String, RecordEntry> e : this.cache.entrySet()){
			m.put(e.getKey(), e.getValue());
		}		
		return m;
	}
	
	public void storeRecordEntry(RecordEntry record) {
		cache.put(record.getName(), record);
		try {
			saveCache();
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}

	private void saveCache() throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(PATH_TO_STORE)));
		for (Entry<String, RecordEntry> e : cache.entrySet()) {
			out.writeObject(e.getKey());
			out.writeObject(e.getValue());
		}
		out.close();
	}
	
	public void clear() throws StoreException {
		cache.clear();
		try {
			saveCache();
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}

	public boolean isEmpty() {
		return cache.isEmpty();
	}

	public boolean containsKey(String key) {
		return cache.containsKey(key);
	}

	public RecordEntry remove(String id) throws StoreException {
		RecordEntry ret = cache.remove(id);
		try {
			saveCache();
		} catch (IOException e) {
			throw new StoreException(e);
		}
		return ret;
	}

	
	public Set<Entry<String, RecordEntry>> entrySet() {
		return cache.entrySet();
	}

	
	public Set<String> keySet() {
		return cache.keySet();
	}

	
	public Collection<RecordEntry> values() {
		return cache.values();
	}

}
