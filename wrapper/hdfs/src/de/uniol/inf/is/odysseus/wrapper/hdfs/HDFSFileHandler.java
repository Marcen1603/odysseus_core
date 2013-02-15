package de.uniol.inf.is.odysseus.wrapper.hdfs;
import java.io.IOException;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractFileHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;


public class HDFSFileHandler extends AbstractFileHandler {
	
	private FileSystem dfs;

	public HDFSFileHandler() {
	}

	public HDFSFileHandler(IProtocolHandler<?> protocolHandler) {
		super(protocolHandler);
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		Configuration config = new Configuration();
		String paramName = "fs.default.name";
		config.set(paramName, options.get(paramName));
		HDFSFileHandler fileHandler = new HDFSFileHandler(protocolHandler);
		try {
			fileHandler.dfs = FileSystem.get(config);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		fileHandler.filename = options.get("filename");
		fileHandler.append = (options.containsKey("append"))?Boolean.parseBoolean(options.get("append")):false;

		return fileHandler;
	}

	@Override
	public String getName() {
		return "HDFS";
	}

	@Override
	public void processInOpen() throws IOException {
		final Path path = new Path(dfs.getWorkingDirectory()+filename);		
		try {
			in = dfs.open(path);
			fireOnConnect();
		} catch (Exception e) {
			fireOnDisconnect();
			throw e;
		}
	}

	@Override
	public void processOutOpen() throws IOException {
		final Path path = new Path(dfs.getWorkingDirectory()+filename);		
		if (!dfs.isFile(path)){
			if (!dfs.createNewFile(path)){
				throw new IOException("Could not create file "+path);
			}
		}
		if (append){
			out = dfs.append(path);
		}else{
			out = dfs.create(path,true);
		}
		
	}


}
