/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.store;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.util.FileUtils;

public class FileStore<IDType extends Serializable & Comparable<? extends IDType>, STORETYPE extends Serializable>
		implements IStore<IDType, STORETYPE> {

	Logger logger = LoggerFactory.getLogger(FileStore.class);

	private String path;
	private MemoryStore<IDType, STORETYPE> cache = new MemoryStore<IDType, STORETYPE>();
	private Map<IDType, Boolean> serializableTestPassed = new HashMap<IDType, Boolean>();

	public FileStore(String path) throws IOException {
		this.path = path;
		loadCache();
		logger.debug("Loaded from " + path + " " + cache.entrySet().size()
				+ " values");
	}

	@SuppressWarnings("unchecked")
	private void loadCache() throws IOException {
		File f = FileUtils.openOrCreateFile(path);
		ObjectInputStream in = null;
		try {
			in = new OsgiObjectInputStream(new FileInputStream(f));
			IDType key = null;
			try {
				while ((key = (IDType) in.readObject()) != null) {
					// Could be unreadable input
					try {
						STORETYPE element = (STORETYPE) in.readObject();
						cache.put(key, element);
						// Object that have been written must be serializable
						// ;-)
						serializableTestPassed.put(key, Boolean.TRUE);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}
			in.close();
		} catch (EOFException e) {
		}
	}

	private void saveCache() throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
				new File(path)));
		for (Entry<IDType, STORETYPE> e : cache.entrySet()) {
			if (serializableTestPassed.get(e.getKey())) {
				out.writeObject(e.getKey());
				out.writeObject(e.getValue());
			}

		}
		out.close();
	}

	@Override
	public STORETYPE get(IDType id) {
		return cache.get(id);
	}

	@Override
	public void put(IDType id, STORETYPE elem) throws StoreException {
		cache.put(id, elem);
		// Do serializable test for this new object
		try {
			ObjectOutputStream test = new ObjectOutputStream(
					new ByteArrayOutputStream());
			test.writeObject(elem);
			// If no exception is thrown, object is serializable
			serializableTestPassed.put(id, Boolean.TRUE);
			saveCache();
		} catch (Exception e) {
			logger.warn(e.getMessage()
					+ " Tried to store non serializable object " + elem);
			serializableTestPassed.put(id, Boolean.FALSE);
		}
	}

	@Override
	public void clear() throws StoreException {
		cache.clear();
		try {
			saveCache();
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}

	@Override
	public boolean isEmpty() {
		return cache.isEmpty();
	}

	@Override
	public boolean containsKey(IDType key) {
		return cache.containsKey(key);
	}

	@Override
	public STORETYPE remove(IDType id) throws StoreException {
		STORETYPE ret = cache.remove(id);
		try {
			saveCache();
		} catch (IOException e) {
			throw new StoreException(e);
		}
		return ret;
	}

	@Override
	public Set<Entry<IDType, STORETYPE>> entrySet() {
		return cache.entrySet();
	}

	@Override
	public Set<IDType> keySet() {
		return cache.keySet();
	}

	@Override
	public Collection<STORETYPE> values() {
		return cache.values();
	}

	@Override
	public void commit() {
		try {
			saveCache();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
