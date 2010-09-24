package de.uniol.inf.is.odysseus.base.store;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.Set;

import org.dynamicjava.osgi.classloading_utils.OsgiEnvironmentClassLoader;

import de.uniol.inf.is.odysseus.base.Activator;

public class FileStore<IDType extends Serializable & Comparable<? extends IDType>, STORETYPE extends Serializable>
		implements IStore<IDType, STORETYPE> {

	private String path;
	private MemoryStore<IDType, STORETYPE> cache = new MemoryStore<IDType, STORETYPE>();
	private OsgiEnvironmentClassLoader cl;

	public FileStore(String path) throws IOException {
		this.path = path;
		ClassLoader curCl = Thread.currentThread().getContextClassLoader();
		this.cl = new OsgiEnvironmentClassLoader(Activator.getBundleContext(),
				curCl, Activator.getBundleContext().getBundle());
		loadCache();
	}

	@SuppressWarnings("unchecked")
	private void loadCache() throws IOException {
		ClassLoader curCl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(this.cl);
		File f = new File(path);
		if (!f.exists()) {
			File d = f.getParentFile();
			if (d != null) {
				d.mkdirs();
			}
			f.createNewFile();
			System.out.println("Created new File " + f.getAbsolutePath());
		} else {
			System.out.println("Read from file " + f.getAbsolutePath());
		}
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream(f));
			IDType key = null;
			try {
				while ((key = (IDType) in.readObject()) != null) {
					STORETYPE element = (STORETYPE) in.readObject();
					// System.out.println("READ "+key+" "+element);
					cache.put(key, element);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			in.close();
		} catch (EOFException e) {
			// initial ...
		}
		Thread.currentThread().setContextClassLoader(curCl);
	}

	private void saveCache() throws IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
				new File(path)));
		for (Entry<IDType, STORETYPE> e : cache.entrySet()) {
			out.writeObject(e.getKey());
			out.writeObject(e.getValue());
			// System.out.println("WRITTEN "+e.getKey()+" "+e.getValue());
		}
		out.close();
	}

	public STORETYPE get(IDType id) {
		return cache.get(id);
	}

	public void put(IDType id, STORETYPE elem) throws StoreException {
		cache.put(id, elem);
		try {
			saveCache();
		} catch (IOException e) {
			throw new StoreException(e);
		}
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

}
