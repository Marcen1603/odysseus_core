package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile;

import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.util.projection.ProjectionUtil;

public final class AsyncImage implements Runnable {
	
    private static final Logger log = Logger.getLogger(AsyncImage.class.getName());

	
    private final AtomicReference<ImageData> imageData = new AtomicReference<ImageData>();
    private Image image; // might as well be thread-local
    private FutureTask<Boolean> task;
    private volatile long stamp = 0;
    private final TileServer tileServer;
    private final int x, y, z;

    private ScreenManager manager;
    private Display display;
    
    public static final int IMAGEFETCHER_THREADS = 4;
    private BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
    private ThreadFactory threadFactory = new ThreadFactory( ) {
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("Async Image Loader " + t.getId() + " " + System.identityHashCode(t));
            t.setDaemon(true);
            return t;
        }
    };
    
    private ThreadPoolExecutor executor = new ThreadPoolExecutor(IMAGEFETCHER_THREADS, 16, 2, TimeUnit.SECONDS, workQueue, threadFactory);
    
    
    public AsyncImage(ScreenManager manager, TileServer tileServer, int x, int y, int z) {
        this.tileServer = tileServer;
        this.x = x;
        this.y = y;
        this.z = z;
        this.manager = manager;
        display = manager.getCanvas().getDisplay();
        this.stamp = manager.getTransformation().getZoomStamp().longValue();
        task = new FutureTask<Boolean>(this, Boolean.TRUE);
        executor.execute(task);
    }
    
    public void run() {
        String url = ProjectionUtil.getTileString(tileServer, x, y, z);
        if (stamp != manager.getTransformation().getZoomStamp().longValue()) {
            //System.err.println("pending load killed: " + url);
            try {
                // here is a race, we just live with.
                if (!display.isDisposed()) {
                	display.asyncExec(new Runnable() {
                        public void run() {
                        	tileServer.getCache().remove(tileServer, x, y, z);
                        }
                    });
                }
            } catch (SWTException e) {
                log.log(Level.INFO, "swt exception during redraw display-race, ignoring");
            }
            
            return;
        }
        try {
            //System.err.println("fetch " + url);
            //Thread.sleep(2000);
            InputStream in = new URL(url).openConnection().getInputStream();
            imageData.set(new ImageData(in));
            try {
                // here is a race, we just live with.
                if (!display.isDisposed()) {
                	display.asyncExec(new Runnable() {
                        public void run() {
                        	manager.getCanvas().redraw();
                        }
                    });
                }
            } catch (SWTException e) {
                log.log(Level.INFO, "swt exception during redraw display-race, ignoring");
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "failed to load imagedata from url: " + url, e);
        }
    }
    
    public ImageData getImageData(Device device) {
        return imageData.get();
    }
    
    public Image getImage(Display display) {
        checkThread(display);
        if (image == null && imageData.get() != null) {
        	
        	for(int x = 0; x <  imageData.get().width; x++){
        		for(int y = 0; y <  imageData.get().height; y++){
        			imageData.get().setAlpha(x,y,100);
        		}
        	}
        	
            image = new Image(display, imageData.get());
        }
        return image;
    }
    
    public void dispose(Display display) {
        checkThread(display);
        if (image != null) {
            //System.err.println("disposing: " + getTileString(tileServer, x, y, z));
            image.dispose();
        }
    }
    
    private void checkThread(Display display) {
        // jdk 1.6 bug from checkWidget still fails here
        if (display.getThread() != Thread.currentThread()) {
            throw new IllegalStateException("wrong thread to pick up the image");
        }
    }
}