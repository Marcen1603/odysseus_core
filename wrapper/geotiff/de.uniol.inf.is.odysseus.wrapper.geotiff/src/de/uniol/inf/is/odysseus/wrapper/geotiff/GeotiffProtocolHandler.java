/**
 * This is a protocol handler for processing Geotiff images.
 * It depends on libraries from Geotools framework:
 * http://docs.geotools.org/stable/userguide/library/coverage/geotiff.html
 *  
 */
package de.uniol.inf.is.odysseus.wrapper.geotiff;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;


/**
 * @author Mazen Salous
 *
 */
public class GeotiffProtocolHandler extends AbstractProtocolHandler<KeyValueObject<? extends IMetaAttribute>> {
	
	/** Logger for this class. */
	private final Logger LOG = LoggerFactory.getLogger(GeotiffProtocolHandler.class);
	/**Pull-based members*/
	protected InputStream inputStreamReader = null; //The incoming TiffImage as a stream
	protected OutputStream outputStreamWriter = null; //This is used to write the incoming tiffImage onto the tempTiff
	protected File tempTiff; //This is required because the GeotiffReader requires the TiffImage as an file, not as a stream.
	/** The relative path of the tempTiff*/
	private final static String tiffPath = GeotiffProtocolHandler.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "\\tiff\\temp.tif";
	/** The TIFF relevant elements */
	byte[] geotiffBytes;
	GeoTiffReader geotiffreader;
	GridCoverage2D coverage = null;
	
	/** Key-value representation for he ImageTiff object*/
	private KeyValueObject<? extends IMetaAttribute> next = null;
	/** Delay on GenericPull. */
	private long delay = 0;
	
	//Constructors
	public GeotiffProtocolHandler() {
	}

	public GeotiffProtocolHandler(ITransportDirection direction,
			IAccessPattern access,
			IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler, OptionMap options) {
		super(direction, access, dataHandler, options);
		if (options.containsKey("delay")) {
			delay = options.getInt("delay",0);
		}
	}

	@Override
	public IProtocolHandler<KeyValueObject<? extends IMetaAttribute>> createInstance(
			ITransportDirection direction, IAccessPattern access,
			OptionMap options,
			IDataHandler<KeyValueObject<? extends IMetaAttribute>> dataHandler) {
		GeotiffProtocolHandler instance = new GeotiffProtocolHandler(direction, access, dataHandler, options);
		return instance;
	}

	@Override
	public String getName() {
		return "Geotiff";
	}
	
	@Override
    public ITransportExchangePattern getExchangePattern() {
        return ITransportExchangePattern.InOut;
    }

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return (other instanceof GeotiffProtocolHandler);
	}
	
	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		if ( (this.getAccessPattern().equals(IAccessPattern.PULL)) || 
			 (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL) ) )
			 this.inputStreamReader = getTransportHandler().getInputStream();
	}
	
	@Override
	public void close() throws IOException {
		if(this.inputStreamReader.available() != 0)
			this.inputStreamReader.close();
	}
	
	@Override
	public boolean hasNext() throws IOException {
		if (this.inputStreamReader == null || this.inputStreamReader.available() == 0) {
			return false;
		}
		//TODO
		//To know the relevant data (from this.tiffreader and this.coverage) for processing and set them in "next".
		int processedBytes = processTiff(true);
		return processedBytes != -1;
	}

	@Override
	public KeyValueObject<? extends IMetaAttribute> getNext()
			throws IOException {
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return next;
	}

	@Override
	public void process(ByteBuffer message) {
		//TODO 
  		//The image could be sent in many consecutive packets
  		//in such case, we should gather the bytes by checking the last packet of the received image
		this.geotiffBytes = new byte[message.limit()];
		message.get(this.geotiffBytes);
		processTiff(false);
		KeyValueObject<? extends IMetaAttribute> map = new KeyValueObject<>();
		//TODO
	    //To know the relevant data (from this.tiffreader and this.coverage) for processing and set them in the map.
		getTransfer().transfer(map);
	}
	
	@Override
	public void write(KeyValueObject<? extends IMetaAttribute> object) throws IOException {
		try { 
			getTransportHandler().send(this.geotiffBytes);
		} 
		catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	/**
	 * This method handles the TiffImage
	 * First, it writes the incoming stream as TiffImage onto the tempTif, because
	 * the geoTifReader requires the TiffImage as a file not as a stream, then
	 * we could determine the relevant elements from this.geotiffreader and this.coverage,
	 * and we can do the required processing on such elements. 
	 * 
	 * @return the number of bytes written onto tempTiff
	 */
	private int processTiff(boolean pullBased){
		try {
			//Initialize outputStreamWriter
			this.outputStreamWriter = new FileOutputStream(new File(tiffPath));
			//PullBased: Pull the bytes from the InputStream 
			if(pullBased)
				pullData();
			//Write
			this.outputStreamWriter.write(this.geotiffBytes);
			//Close outputStreamWriter
			this.outputStreamWriter.close();
			//tempTiff
			this.tempTiff = new File(tiffPath);
			//Main elements for processing:
			//1. GeotiffReader
			this.geotiffreader = new GeoTiffReader(this.tempTiff, new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
			//2. Coverage
			AbstractGridFormat format = GridFormatFinder.findFormat(this.tempTiff);
		    GridCoverage2DReader coveragereader = format.getReader(this.tempTiff);
		    this.coverage = coveragereader.read(null);
		    //Remove tempTiff
		    this.tempTiff.deleteOnExit();
		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return this.geotiffBytes.length;
	}
	
	/**
	 * This method pulls the bytes from the pullBased input (File) to be handled
	 */
	
	private void pullData(){
		//Pull the bytes from the InputStream into this.geotiffBytes
		try {
			this.geotiffBytes = new byte[this.inputStreamReader.available()];
			this.inputStreamReader.read(this.geotiffBytes);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
			e.printStackTrace();
		}
	}
}
