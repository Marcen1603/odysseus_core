package de.uniol.inf.is.odysseus.s100.common.datahandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.uniol.inf.is.odysseus.core.datahandler.AbstractDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.s100.common.datatype.GM_Point;
import de.uniol.inf.is.odysseus.s100.common.sdf.schema.SDFS100DataType;

/**
 * DataHandler for data type GM_Point.
 * 
 * @author Henrik Surm
 */
public class GMPointDataHandler extends AbstractDataHandler<GM_Point> {
	static protected List<String> 	types = new ArrayList<String>();
	static protected Marshaller 	xmlMarshaller;
	static protected Unmarshaller 	xmlUnmarshaller;
	static 
	{
		GMPointDataHandler.types.add(SDFS100DataType.GM_POINT.getURI());
			    
		try 
		{
			// create JAXB context and instantiate marshaller			
			JAXBContext context = JAXBContext.newInstance(GM_Point.class);
			xmlMarshaller = context.createMarshaller();
			xmlMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			xmlUnmarshaller = context.createUnmarshaller();
		} 
		catch (JAXBException e) 
		{
			e.printStackTrace();
		}		
	}
	
	@Override
	public IDataHandler<GM_Point> getInstance(final SDFSchema schema) {
		return new GMPointDataHandler();
	}
	
	public GMPointDataHandler() {
		super(null);
	}
	
	@Override
	public GM_Point readData(final String string) 
	{
		return null;//this.readData(ByteBuffer.wrap(string.getBytes()));
	}
	
	@Override
	public GM_Point readData(InputStream inputStream) throws IOException 
	{
//		IplImage image = IplImage.createFrom(ImageIO.read(inputStream));
		return null; //new ImageJCV(image);
	}
	
	@Override
	public GM_Point readData(final ByteBuffer buffer) 
	{
		GM_Point point = null;
		
		try
		{
			int len = buffer.getInt();			
			String s = new String(buffer.array(), 4, len, "UTF-8");
			buffer.position(buffer.position() + len);
			point = (GM_Point) xmlUnmarshaller.unmarshal(new StringReader(s));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
				
		return point;//new GM_Point(buffer.getDouble(), buffer.getDouble(), buffer.getDouble());
	}
	
	@Override
	public void writeData(final ByteBuffer buffer, final Object data) 
	{
		GM_Point point = (GM_Point) data;
/*		buffer.putDouble(point.position.longitude);
		buffer.putDouble(point.position.latitude);
		buffer.putDouble(point.position.altitude);*/
		
	    StringWriter sw = new StringWriter();
	    try 
	    {
			xmlMarshaller.marshal(point, sw);
			String s = sw.toString();
			int len = s.length();
			byte[] buf = s.getBytes("UTF-8");
			
			buffer.putInt(buf.length);
			buffer.put(buf);
		} 
	    catch (Exception e) 
	    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	final public List<String> getSupportedDataTypes() {
		return GMPointDataHandler.types;
	}
	
	@Override
	public int memSize(final Object attribute) {
//		final ImageJCV image = (ImageJCV) attribute;
		// TODO: Has this been updates since the change to IplImage?
		return 0; //(2* Integer.SIZE + image.getWidth() * image.getHeight() * Double.SIZE) / 8;
	}
	
	@Override
	public Class<?> createsType() {
		return GM_Point.class;
	}
}
