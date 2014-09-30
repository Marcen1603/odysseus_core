package de.uniol.inf.is.odysseus.imagejcv.udf;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bytedeco.javacv.CanvasFrame;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.UserDefinedFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IUserDefinedFunction;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;

/**
 * @author Kristian Bruns
 */
@UserDefinedFunction(name = "ShowImageJCV")
public class ShowImageJCVUDF extends CanvasFrame implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> 
{
	private static final long serialVersionUID = 4077769822100028025L;
	private final AtomicBoolean pause = new AtomicBoolean(false);
	private int pos;
	private Object syncObj = new Object();
	
	public ShowImageJCVUDF() 
	{		 
		super("Frame", 1.0);
		
		double defGamma = CanvasFrame.getDefaultGamma();
		System.out.println("defGamma = " + defGamma);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(String initString) 
	{
		if (initString != null) {
			try {
				this.pos = Integer.parseInt(initString);
			} catch (java.lang.Exception e) {
			}
		}
		
		addWindowListener(new WindowListener()
			{
				@Override public void windowActivated(WindowEvent arg0) 	{}
				@Override public void windowClosing(WindowEvent arg0) 		{}
				@Override public void windowDeactivated(WindowEvent arg0) 	{}
				@Override public void windowDeiconified(WindowEvent arg0) 	{}
				@Override public void windowIconified(WindowEvent arg0) 	{}
				@Override public void windowOpened(WindowEvent arg0)		{}
				
				@Override public void windowClosed(WindowEvent arg0) 		
				{
					synchronized (syncObj)
					{
						pause.set(true);
//						CanvasFrame.this.d
					}					
				}
			});
						
		
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(final KeyEvent event) {
				if (event.getKeyCode() == KeyEvent.VK_SPACE) {
					ShowImageJCVUDF.this.pause.set(!ShowImageJCVUDF.this.pause.get());
				}
			}
			
			@Override
            public void keyReleased(final KeyEvent event) {

            }

            @Override
            public void keyTyped(final KeyEvent event) {

            }
		});
		this.setSize(700, 700);
		this.setVisible(true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tuple<? extends IMetaAttribute> process(Tuple<? extends IMetaAttribute> in, int port) 
	{
		ImageJCV image = (ImageJCV) in.getAttribute(this.pos);
		if (this.canvas.isVisible() && !pause.get()) 
		{
			synchronized (syncObj ) 
			{
				this.showImage(image.getImage());
			}
		}
		return in;
	}
	
	@Override
    public OutputMode getOutputMode() {
        return OutputMode.INPUT;
    }
}
