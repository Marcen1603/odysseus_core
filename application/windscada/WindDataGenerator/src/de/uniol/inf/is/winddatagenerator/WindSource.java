package de.uniol.inf.is.winddatagenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * Reads file /data/rank.txt in Bundle that contains the order of iwnd turbine
 * ids which should be simulated by a WindTurbineDataProvider
 * 
 * @author Dennis Nowak
 * 
 */
public class WindSource {

	private static WindSource INSTANCE;
	private BufferedReader reader;

	private WindSource() {
		FileReader fileReader = null;
		Bundle bundle = Platform.getBundle("de.uniol.inf.is.WindDataGenerator");
		URL url = bundle.getResource("/data/rank.txt");
		File file;
		try {
			file = new File(FileLocator.toFileURL(url).getPath());
			fileReader = new FileReader(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		reader = new BufferedReader(fileReader);
	}

	/**
	 * Returns the id of the wind turbine that should be simulated next
	 * 
	 * @return next wind turbine id to create a data generator for
	 */
	public int getWindSourceID() {
		String ids = null;
		try {
			ids = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		int id = Integer.parseInt(ids);
		return id;
	}

	/**
	 * Returns an single instance of WindSource
	 * 
	 * @return an instance of WindSource
	 */
	public static WindSource getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new WindSource();
		}
		return INSTANCE;
	}
}
