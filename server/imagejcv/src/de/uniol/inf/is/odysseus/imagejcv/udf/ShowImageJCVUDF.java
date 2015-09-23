package de.uniol.inf.is.odysseus.imagejcv.udf;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
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
	private static final OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
	
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
		if (!isVisible()) return in;
		
		ImageJCV image = (ImageJCV) in.getAttribute(this.pos);
			
		setTitle(title + " [" + image.getWidth() + "x" + image.getHeight() + "]");
		showImage(converter.convert(image.getImage()));
		
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
