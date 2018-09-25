package de.uniol.inf.is.odysseus.rcp.editor.text;

import java.util.HashMap;
import java.util.Map;

public class FileExecutorRegistry {
	private static Map<String, IFileExecutor> fileExecutors = new HashMap<String, IFileExecutor>();

	
	static {
		registerFileExecutor(new OdysseusScriptFileExecutor());
	}
	
	
	public static void registerFileExecutor(IFileExecutor fileExecutor) {
		if (fileExecutor.getFileExtension() == null) {
			throw new IllegalArgumentException("file extension is null");
		} else if (fileExecutor.getFileExtension().length() == 0) {
			throw new IllegalArgumentException("file extension has length=0");
		} else if (fileExecutors.containsKey(fileExecutor.getFileExtension().toLowerCase())) {
			throw new IllegalArgumentException("file extension "+fileExecutor.getFileExtension()+" already registered for "+fileExecutor.getFileExtension().toLowerCase());
		}
		fileExecutors.put(fileExecutor.getFileExtension().toLowerCase(), fileExecutor);
	}
	
	
	public static void unregisterFileExecutor(IFileExecutor fileExecutor) {
		if (fileExecutor.getFileExtension() == null) {
			throw new IllegalArgumentException("file extension is null");
		} else if (fileExecutor.getFileExtension().length() == 0) {
			throw new IllegalArgumentException("file extension has length=0");
		}
		fileExecutors.remove(fileExecutor.getFileExtension().toLowerCase());
	}
	
	public static boolean hasFileExecutor(String fileExtension) {
		return fileExecutors.containsKey(fileExtension.toLowerCase());
	}
	
	public static IFileExecutor getFileExecutor(String fileExtension) {
		if (!hasFileExecutor(fileExtension)) {
			throw new IllegalArgumentException("file executor not found for file extension "+fileExtension);
		}
		return fileExecutors.get(fileExtension.toLowerCase());
	}
	
	
	
}
