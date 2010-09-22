package de.uniol.inf.is.odysseus.base.store;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class FileStore<IDType,STORETYPE extends IHasId<IDType> & Serializable> {
	
	private String path;
	private MemoryStore<IDType,STORETYPE> cache = new MemoryStore<IDType, STORETYPE>();
	
	public FileStore(String path) throws IOException{
		this.path = path;
		loadCache();
	}

	@SuppressWarnings("unchecked")
	private void loadCache() throws IOException{
		File f = new File(path);
		if (!f.exists()){
			File d = f.getParentFile();
			if (d!= null){
				d.mkdirs();
			}
			f.createNewFile();
			System.out.println("Created new File "+f.getAbsolutePath());
		}else{
			System.out.println("Read from file "+f.getAbsolutePath());
		}
		ObjectInputStream in = null;
		try{
			in = new ObjectInputStream(new FileInputStream(f));
			STORETYPE element = null;
			try {
				while((element = (STORETYPE) in.readObject() )!=null){
					cache.store(element);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			in.close();
		}catch(EOFException e){
			// initial ...
		}
	}

	private void saveCache() throws IOException{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(path)));
		for (STORETYPE e:cache.getAll()){
			out.writeObject(e);
		}
		out.close();
	}
	
	public STORETYPE getByName(IDType id) {
		return cache.getByName(id);
	}

	public void store(STORETYPE elem) throws StoreException {
		cache.store(elem);
		try {
			saveCache();
		} catch (IOException e) {
			throw new StoreException(e);
		}
	}

	public boolean isEmpty() {
		return cache.isEmpty();
	}

}
