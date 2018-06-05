package de.uniol.inf.is.odysseus.imagejcv.udf;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;

/**
 * Function for showing an image from type ImageJCV.
 * 
 * @author Kristian Bruns
 */
@UserDefinedFunction(name = "ShowImageJCV")
public class ShowImageJCVUDF extends CanvasFrame implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> 
{
	private static final long serialVersionUID = 4077769822100028025L;
	
	private int pos;
	private String title = "Frame";
	
	public ShowImageJCVUDF() 
	{		 
		super("Frame", 1.0);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(String initString) 
	{
		if (initString != null)
			try 
			{
				String[] parts = initString.split(",");
				
				this.pos = Integer.parseInt(parts[0]);
				
				if (parts.length > 1)
					title = parts[1]; 
					
			} 
			catch (java.lang.Exception e) 
			{
			}
		
		setVisible(true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override public Tuple<? extends IMetaAttribute> process(Tuple<? extends IMetaAttribute> in, int port) 
	{		
		if (isVisible())
		{
			ImageJCV image = (ImageJCV) in.getAttribute(this.pos);			
								
			
			String curTitle = title + " [" + image.getWidth() + "x" + image.getHeight() + "]";
			
	        TimeInterval timeStamp = (TimeInterval)in.getMetadata();
	        if (timeStamp != null)
//	        	curTitle += " lag = " + (System.currentTimeMillis() - timeStamp.getStart().getMainPoint()) + "ms";			
	        	curTitle += " timestamp = " + timeStamp.getStart().getMainPoint() + "ms";
			
	        setTitle(curTitle);
	        
			showImage(new OpenCVFrameConverter.ToIplImage().convert(image.getImage()));
		}
		
		return in;
	}
	
	/**
	 * Returns output mode of this class.
	 * 
	 * @return OutputMode Output mode.
	 */
	@Override public OutputMode getOutputMode() 
	{
        return OutputMode.INPUT;
    }
}
