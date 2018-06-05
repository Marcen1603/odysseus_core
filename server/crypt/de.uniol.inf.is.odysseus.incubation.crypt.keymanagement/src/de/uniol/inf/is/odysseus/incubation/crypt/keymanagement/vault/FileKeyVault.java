package de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.vault;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.Activator;
import de.uniol.inf.is.odysseus.incubation.crypt.keymanagement.keys.KeyWrapper;

/**
 * This class manages a key Vault, where the Keys are stored in Files. <br>
 * Each Key is stores in one File on the Filesystem.
 * 
 * @author MarkMilster
 *
 */
public class FileKeyVault implements IFileKeyVault {

	private String path;

	/**
	 * Constructor, which specifies the path to the folder, where the keys are
	 * stored in files.
	 * 
	 * @param path
	 */
	public FileKeyVault(String path) {
		this.path = path;
	}

	@Override
	public boolean insertKey(KeyWrapper<?> key) {
		FileOutputStream fileStream;
		try {
			fileStream = new FileOutputStream(this.getAbsoluteKeyPath(key.getId()));
			ObjectOutputStream o = new ObjectOutputStream(fileStream);
			o.writeObject(key);
			o.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public KeyWrapper<?> getKey(int id) {
		FileInputStream fileStream;
		KeyWrapper<?> key = null;
		try {
			fileStream = new FileInputStream(this.getAbsoluteKeyPath(id));
			ObjectInputStream o = new ObjectInputStream(fileStream);
			key = (KeyWrapper<?>) o.readObject();
			o.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return key;
	}

	/**
	 * Returns the absolute path of a key, which is stored in a bundle. 
	 * 
	 * @param id The id of the key, you want to get the absolute path of.
	 * @return The absolute path of the key, specified by the id in the parameter
	 */
	private String getAbsoluteKeyPath(int id) {
		try {
			BundleContext context = Activator.getContext();
			URL url1 = context.getBundle().getEntry(path);
			URL url = FileLocator.toFileURL(url1);
			File file = new File(url.getPath() + id);
			return file.getAbsolutePath();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
