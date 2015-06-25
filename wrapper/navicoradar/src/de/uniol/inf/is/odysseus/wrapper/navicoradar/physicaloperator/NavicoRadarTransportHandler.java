package de.uniol.inf.is.odysseus.wrapper.navicoradar.physicaloperator;


import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.swing.JComponent;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.ByteBufferWrapper;
import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.command.Command;
import de.uniol.inf.is.odysseus.core.command.ICommandProvider;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.wrapper.navicoradar.SWIG.NavicoRadarWrapper;



public class NavicoRadarTransportHandler extends AbstractPushTransportHandler implements ICommandProvider
{
	public static final String NAME = "NavicoRadar";
	
	@SuppressWarnings("unused")
	private final Logger logger = LoggerFactory.getLogger(NavicoRadarTransportHandler.class);
	private final Object processLock = new Object();

	private NavicoRadarWrapper navico;
	private int antennaHeightMiliMeter;
	private int rangeMeter;
	private String radarSerial;
	private int unlockKeylength;
	
	public NavicoRadarTransportHandler() 
	{
		super();
	}
	
	/**
	 * @param protocolHandler
	 */
	public NavicoRadarTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		super(protocolHandler, options);
		
		radarSerial = "";
	}	

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) 
	{
		return new NavicoRadarTransportHandler(protocolHandler, options);
	}

	@Override public String getName() { return NAME; }
	
	@Override public void processInOpen() throws IOException 
	{
		synchronized (processLock)
		{
			navico = new NavicoRadarWrapper(antennaHeightMiliMeter, rangeMeter, radarSerial, null, unlockKeylength)
			{
				@Override public void onSpokeUpdate(ByteBuffer buffer) 
				{
					NavicoRadarTransportHandler.this.onSpokeUpdate(buffer);
				}

				@Override public void onCat240SpokeUpdate(ByteBuffer buffer) 
				{
					onCat240SpokeUpdate(buffer);
				}
				@Override public void onTargetUpdateTTM(String ttmMessage) 
				{
					NavicoRadarTransportHandler.this.onTargetUpdateTTM(ttmMessage);
				}
				@Override public void onTargetUpdate(ByteBuffer buffer) 
				{
					NavicoRadarTransportHandler.this.onTargetUpdate(buffer);
				}
				@Override public void onPictureUpdate(ByteBuffer buffer) 
				{
					NavicoRadarTransportHandler.this.onPictureUpdate(buffer);
				}
			};
			
			navico.start();			
		}
		
		fireOnConnect();
	}
	
	protected void onTargetUpdate(ByteBuffer target) 
	{
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, "Test target");
		tuple.setAttribute(1, new ByteBufferWrapper(target));
		fireProcess(tuple);
	}

	protected void onSpokeUpdate(ByteBuffer spoke) 
	{
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, "Test spoke");
		tuple.setAttribute(1, new ByteBufferWrapper(spoke));
		fireProcess(tuple);
	}
	
	protected void onTargetUpdateTTM(String ttmMessage) {
		System.out.println("TTM: " + ttmMessage);
/*		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, ttmMessage);
		tuple.setAttribute(1, null);
		fireProcess(tuple);*/
	}	

	protected void onCat240SpokeUpdate(ByteBuffer cat240spoke) 
	{
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, "Test cat240spoke");
		tuple.setAttribute(1, new ByteBufferWrapper(cat240spoke));
		fireProcess(tuple);
	}
	
    private static class ImageCanvas extends JComponent 
    {
		private static final long serialVersionUID = 5812513904485519080L;
		private BufferedImage image;

        public void update(BufferedImage image) 
        {
            this.image = image;
            this.repaint();
        }

        @Override
		public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            AffineTransform tx = new AffineTransform();
            if (image != null) {
                double scalex = ((double) this.getWidth()) / ((double) this.image.getWidth());
                double scaley = ((double) this.getHeight()) / ((double) this.image.getHeight());
                tx.scale(scalex, scaley);
                g2.setTransform(tx);
                g2.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
            }
            g2.finalize();
        }
    }		
	
	int numPictures = 0;
	JFrame frame;
	ImageCanvas canvas;
	BufferedImage img;
	protected void onPictureUpdate(ByteBuffer picture) 
	{
		Tuple<IMetaAttribute> tuple = new Tuple<IMetaAttribute>(2, false);
		tuple.setAttribute(0, "Test picture");
		tuple.setAttribute(1, new ByteBufferWrapper(picture));
		fireProcess(tuple);
		
		if (frame == null)
		{
			img = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_ARGB);
			frame = new JFrame();
			canvas = new ImageCanvas();
		
			frame.setSize(700, 700);
			frame.add(canvas, BorderLayout.CENTER);
			frame.setVisible(true);
		}
		
		if (numPictures % 100 == 0)
		{
			for (int y=0; y<2048; y++)
			for (int x=0; x<2048; x++)
			{
				img.setRGB(x, y, picture.getInt());
			}
			
	
	        canvas.update(img);
		}

/*		String fileName = "E:\\radarimages\\image" + numPictures++ + ".raw" ;
		try {
			if (numPictures > 4000)
				Files.copy(new ByteBufferBackedInputStream(picture), new File(fileName).toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        
        numPictures++;
	}
	
	@Override public void processInClose() throws IOException 
	{
		synchronized (processLock)
		{
			fireOnDisconnect();
			
			navico.stop();
			navico = null;
		}
	}

		
    @Override public boolean isSemanticallyEqualImpl(ITransportHandler o) 
    {
    	if(!(o instanceof NavicoRadarTransportHandler)) return false;
    	NavicoRadarTransportHandler other = (NavicoRadarTransportHandler)o;
    	
    	return other.radarSerial.equals(radarSerial);
    }

	@Override public void processOutOpen() throws IOException 
	{
		throw new IllegalArgumentException("Operator is not a Sink");
	}

	@Override public void processOutClose() throws IOException 
	{
		throw new IllegalArgumentException("Operator is not a Sink");
	}

	@Override public void send(byte[] message) throws IOException 
	{
		throw new IllegalArgumentException("Sending Not Supported");
	}
	
	@Override
	public Command getCommandByName(String commandName, SDFSchema schema) 
	{
		switch (commandName)
		{
			case "setownshipdata": 
			{
				final int speedTypeAttribute = schema.findAttributeIndexException("speedType");
				final int speedAttribute = schema.findAttributeIndexException("speed");
				final int headingTypeAttribute = schema.findAttributeIndexException("speedType");
				final int headingAttribute = schema.findAttributeIndexException("speedType");
				
				return new Command()
						 {
							@Override public boolean run(IStreamObject<?> input) 
							{
								int speedType,headingType;
								double speed,heading;
			
			
								if (input instanceof Tuple<?>)
								{
									Tuple<?> tuple = (Tuple<?>) input;								 			
									speedType = ((Number) tuple.getAttribute(speedTypeAttribute)).intValue();
									speed = ((Number) tuple.getAttribute(speedAttribute)).doubleValue();
									headingType= ((Number) tuple.getAttribute(headingTypeAttribute)).intValue();
									heading= ((Number) tuple.getAttribute(headingAttribute)).doubleValue();
								}
								else
								if (input instanceof KeyValueObject)
								{
									KeyValueObject<?> kv = (KeyValueObject<?>)input;
									speedType = (int) kv.getAttribute("speedType");
									speed = (double) kv.getAttribute("speed");
									headingType= (int) kv.getAttribute("headingType");
									heading= (double) kv.getAttribute("heading");
								}
								else
									throw new IllegalArgumentException("Cannot execute command on input type " + input.getClass().getName());
			
								return NavicoRadarTransportHandler.this.navico.SetBoatSpeed(speedType, speed, headingType, heading);
							}
						};
			}
			
			case "settargetdata":	
			{
				final int TargetIdAttribute = schema.findAttributeIndexException("TargetId");
				final int rangeAttribute = schema.findAttributeIndexException("range");
				final int bearingAttribute = schema.findAttributeIndexException("bearing");
				final int bearingtypeAttribute = schema.findAttributeIndexException("bearingtype");
				
				return new Command()
						 {
						 	@Override public boolean run(IStreamObject<?> input) 
						 	{
						 		int TargetId, range, bearing, bearingtype;
						 		
						 		if (input instanceof Tuple<?>)
						 		{
						 			Tuple<?> tuple = (Tuple<?>) input;								 			
						 			TargetId = ((Number) tuple.getAttribute(TargetIdAttribute)).intValue();
						 			range = ((Number) tuple.getAttribute(rangeAttribute)).intValue();
						 			bearing = ((Number) tuple.getAttribute(bearingAttribute)).intValue();
						 			bearingtype = ((Number) tuple.getAttribute(bearingtypeAttribute)).intValue();
						 		}
						 		else
						 		if (input instanceof KeyValueObject)
						 		{
						 			KeyValueObject<?> kv = (KeyValueObject<?>)input;
						 			TargetId = (int) kv.getAttribute("TargetId");
						 			range = (int) kv.getAttribute("range");
						 			bearing = (int) kv.getAttribute("bearing");
						 			bearingtype = (int) kv.getAttribute("bearingtype");
						 		}
						 		else
						 			throw new IllegalArgumentException("Cannot execute command on input type " + input.getClass().getName());
						 		
						 		return NavicoRadarTransportHandler.this.navico.AcquireTargets(TargetId, range, bearing, bearingtype);
							}
						 };					 
			}
			
			default: return null;
		}
	}
}
