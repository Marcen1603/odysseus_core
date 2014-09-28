package de.uniol.inf.is.odysseus.imagejcv.udf;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bytedeco.javacpp.opencv_core.IplImage;

import javax.swing.JComponent;
import javax.swing.JFrame;

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
public class ShowImageJCVUDF extends JFrame implements IUserDefinedFunction<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>>, ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4077769822100028025L;
	private ImageCanvas canvas;
	private final AtomicBoolean pause = new AtomicBoolean(false);
	private int pos;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(String initString) {
		if (initString != null) {
			try {
				this.pos = Integer.parseInt(initString);
			} catch (Exception e) {
			}
		}
		this.canvas = new ImageCanvas();
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
		this.add(this.canvas, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Tuple<? extends IMetaAttribute> process(Tuple<? extends IMetaAttribute> in, int port) {
		final ImageJCV image = (ImageJCV) in.getAttribute(this.pos);
		if ((this.canvas != null) && (this.canvas.isVisible()) && (!this.pause.get())) {
			synchronized (this.canvas) {
				this.canvas.update(image.getImage());
				try{
				} catch (final Exception e) {
					e.printStackTrace();
				} finally {
				}
			}
		}
		return in;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    public OutputMode getOutputMode() {
        return OutputMode.INPUT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

    }
    
    private static class ImageCanvas extends JComponent {
    	/**
		 * 
		 */
		private static final long serialVersionUID = 5822836049942417968L;
		private IplImage image;
		
		public void update(IplImage image) {
			this.image = image;
			this.repaint();
		}
		
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			AffineTransform tx = new AffineTransform();
			if (image != null) {
				double scalex = ((double) this.getWidth() / (double) this.image.width());
				double scaley = ((double) this.getHeight() / (double) this.image.height());
				tx.scale(scalex, scaley);
				g2.setTransform(tx);
				g2.drawImage(image.getBufferedImage(), 0, 0, image.width(), image.height(), this);
			}
			g2.finalize();
		}
    }
	
}
