package de.uniol.inf.is.odysseus.incubation.server.hdfs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

	public static final String NAME = "HDFS";
	private String fsDefaultName;
	private String pathToFile;
	private boolean append;

	private FSDataOutputStream dfsOutput;
	private FSDataInputStream dfsInput;

	private Configuration conf;
	private FileSystem fs;

	public HdfsTransportHandler(){}
	
	public HdfsTransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		this.fsDefaultName = options.get("fs.default.name");
		this.pathToFile = options.get("Filename");
		this.append = Boolean.parseBoolean(options.get("append"));

	}

	public void createConnection() {
		
		System.setProperty("HADOOP_USER_NAME", "hduser");
		
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", fsDefaultName);
		try {
			fs = FileSystem.get(conf);
		} catch (IOException e) {

			e.printStackTrace();
		}

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
			System.out.println("File doesn't exist!");
		else
			dfsInput = fs.open(new Path(pathToFile));

	}

	@Override
	public void processOutOpen() throws IOException {
		createConnection();
		if (!fs.exists(new Path(pathToFile)))
			System.out.println("File already exists!");
		else
			dfsOutput = fs.create(new Path(pathToFile));

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
	
	public void write(byte[] buffer){
		try {
			dfsOutput.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public byte[] read(byte[] buffer){
		try {
			dfsInput.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return buffer;
	}

}
