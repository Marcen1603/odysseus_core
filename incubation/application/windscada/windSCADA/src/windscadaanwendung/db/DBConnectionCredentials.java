package windscadaanwendung.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.Bundle;

import windscadaanwendung.Activator;

import com.google.common.collect.Maps;

public class DBConnectionCredentials {

	public static  Map<String, String> load(String path) {
		Map<String, String> credentials = Maps.newHashMap();
		FileReader fileReader;
		Bundle bundle = Activator.getDefault().getBundle();
		URL url = bundle.getResource(path);
		File file;
		try {
			file = new File(FileLocator.toFileURL(url).getPath());
			fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			String str, key, value;
			while ((str = reader.readLine()) != null) {
				key = str.substring(0, str.indexOf("="));
				value = str.substring(str.indexOf("=") + 1);
				credentials.put(key, value);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return credentials;
	}

}
