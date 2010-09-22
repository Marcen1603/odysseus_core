package de.uniol.inf.is.odysseus.base.usermanagement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUserStore implements IUserStore {

	private String path;
	private MemoryUserStore cache = new MemoryUserStore();
	
	public FileUserStore(String path) throws IOException{
		this.path = path;
		loadCache();
	}

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
			System.out.println("Read users from file "+f.getAbsolutePath());
		}
		BufferedReader in = new BufferedReader(new FileReader(f));
		String line;
		while((line = in.readLine())!=null){
			cache.storeUser(new User(line));
		}
		in.close();
	}

	private void saveCache() throws IOException{
		BufferedWriter out = new BufferedWriter(new FileWriter(new File(path)));
		for (User user:cache.getUsers()){
			out.write(user.toString()+System.getProperty("line.separator"));
		}
		out.close();
	}
	
	@Override
	public User getUserByName(String username) {
		return cache.getUserByName(username);
	}

	@Override
	public void storeUser(User user) throws UserStoreException {
		cache.storeUser(user);
		try {
			saveCache();
		} catch (IOException e) {
			throw new UserStoreException(e);
		}
	}

	@Override
	public boolean isEmpty() {
		return cache.isEmpty();
	}

}
