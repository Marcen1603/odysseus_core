package de.uniol.inf.is.odysseus.incubation.server.hdfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class HdfsTransportHandler extends AbstractTransportHandler {

	private Logger logger = Logger.getLogger(HdfsTransportHandler.class.getName());
	
	public static final String NAME = "HDFS";
	private String fsDefaultName;
	private String pathToFile;
	private String username;
	private boolean append;

	private FSDataOutputStream dfsOutput;
	private FSDataInputStream dfsInput;

	private FileSystem fs;

	public HdfsTransportHandler() {
	}

	public HdfsTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		this.fsDefaultName = options.get("fs.default.name");
		this.pathToFile = options.get("Filename");
		this.append = Boolean.parseBoolean(options.get("append"));
		this.username = options.get("username");
		

	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		
		HdfsTransportHandler instance = new HdfsTransportHandler(protocolHandler, options);

		protocolHandler.setTransportHandler(instance);

		return instance;

	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		createConnection();
		if (!fs.exists(new Path(pathToFile)) || !fs.isFile(new Path(pathToFile)))
			logger.log(Level.FINE,"File does not exist or it is not a file.");
		else
			dfsInput = fs.open(new Path(pathToFile));

	}

	@Override
	public void processOutOpen() throws IOException {
		createConnection();
		if (fs.exists(new Path(pathToFile)) && !append)
			logger.log(Level.FINE,"File already exists!");
		else
			dfsOutput = fs.create(new Path(pathToFile),append);

	}

	@Override
	public void processInClose() throws IOException {

		dfsInput.close();

	}

	@Override
	public void processOutClose() throws IOException {

		dfsOutput.close();

	}

	@Override
	public void send(byte[] message) throws IOException {
		try {
			dfsOutput.write(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public InputStream getInputStream() {
		return this.dfsInput;
	}

	@Override
	public OutputStream getOutputStream() {
		return this.dfsOutput;
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		return false;
	}

	public void createConnection() {

		System.setProperty("HADOOP_USER_NAME", username);

		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", fsDefaultName);
		try {
			fs = FileSystem.get(conf);
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public String getFsDefaultName() {
		return fsDefaultName;
	}

	public void setFsDefaultName(String fsDefaultName) {
		this.fsDefaultName = fsDefaultName;
	}

	public String getPathToFile() {
		return pathToFile;
	}

	public void setPathToFile(String pathToFile) {
		this.pathToFile = pathToFile;
	}

	public boolean isAppend() {
		return append;
	}

	public void setAppend(boolean append) {
		this.append = append;
	}

	public FSDataOutputStream getDfsOutput() {
		return dfsOutput;
	}

	public void setDfsOutput(FSDataOutputStream dfsOutput) {
		this.dfsOutput = dfsOutput;
	}

	public FSDataInputStream getDfsInput() {
		return dfsInput;
	}

	public void setDfsInput(FSDataInputStream dfsInput) {
		this.dfsInput = dfsInput;
	}

	public FileSystem getFs() {
		return fs;
	}

	public void setFs(FileSystem fs) {
		this.fs = fs;
	}


}
