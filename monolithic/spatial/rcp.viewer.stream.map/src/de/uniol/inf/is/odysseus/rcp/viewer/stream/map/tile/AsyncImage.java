package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.tile;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.vividsolutions.jts.geom.Envelope;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.ScreenManager;

public final class AsyncImage implements Runnable {

	private static final Logger log = Logger.getLogger(AsyncImage.class.getName());
	public static final int IMAGEFETCHER_THREADS = 8;
	private static BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>();
	private static ThreadFactory threadFactory = new ThreadFactory() {
		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setName("Async Image Loader " + t.getId() + " " + System.identityHashCode(t));
			t.setDaemon(true);
			return t;
		}
	};
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(IMAGEFETCHER_THREADS, 16, 2, TimeUnit.SECONDS, workQueue, threadFactory);

	private final AtomicReference<ImageData> imageData = new AtomicReference<ImageData>();
	private Image image; // might as well be thread-local
	private Image parentImage;
	private FutureTask<Boolean> task;
	private final TileServer tileServer;
	private final int x, y, z;
	private final Envelope env;

	@SuppressWarnings("unused")
	private long time = 0; 
	
	@SuppressWarnings("unused")
	private ScreenManager manager;
	// private Display display;
	private AsyncImage parent;

	public AsyncImage(ScreenManager manager, TileServer tileServer, int x, int y, int z, Envelope env) {
		this.tileServer = tileServer;
		this.x = x;
		this.y = y;
		this.z = z;
		this.env = env;
		this.manager = manager;
		// display = manager.getCanvas().getDisplay();
		task = new FutureTask<Boolean>(this, Boolean.TRUE);
		executor.execute(task);
	}

	@Override
	public void run() {
		String url = tileServer.getTileString(x, y, z);
		if (z != tileServer.getCurrentZoom()) {
			// System.err.println("pending load killed: " + url);
			tileServer.getCache().remove(tileServer, x, y, z);
			return;
		}
		try {
			// System.err.println("fetch " + url);
			// Thread.sleep(2000);
			if (imageData.get() == null) {
				URLConnection connection = new URL(url).openConnection();
				InputStream in = connection.getInputStream();
				imageData.set(new ImageData(in));
			}
			if (imageData.get() != null){
				tileServer.getLayer().updateTile(this);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, "failed to load imagedata from url: " + url, e);
		}
	}

	public ImageData getImageData() {
		return imageData.get();
	}

	public Image getImage(Display display) {
		if (image == null) {
			if (imageData.get() != null) {
				// This should done for raster image with transparency
				// for(int x = 0; x < imageData.get().width; x++){
				// for(int y = 0; y < imageData.get().height; y++){
				// imageData.get().setAlpha(x,y,100);
				// }
				// }
				 this.tileServer.getCache().remove(tileServer, this.x, this.y,
				 this.z);
				image = new Image(display, imageData.get());
			} else if (this.parent != null) {
				if (this.parentImage == null && this.parent.image != null) {
					if (!this.parent.image.isDisposed()) {
						Rectangle bounds = this.parent.image.getBounds();
						int u = 0;
						int v = 0;
						if (parent.x * 2 < this.x)
							u = bounds.width / 2;
						if (parent.y * 2 < this.y)
							v = bounds.height / 2;
						this.parentImage = new Image(Display.getCurrent(), bounds.width / 2, bounds.height / 2);
						GC gc = new GC(this.parent.image);
						gc.copyArea(this.parentImage, u, v);
						gc.dispose();
					}
				}
				return this.parentImage;
			}
		} else {
			if (this.parentImage != null)
				this.parentImage.dispose();
			this.parentImage = null;
		}

		return image;
	}

	public void dispose() {
		// checkThread(display);
		if (image != null) {
			// System.err.println("disposing: " + getTileString(tileServer, x,
			// y, z));
			image.dispose();
		}
		if (parentImage != null) {
			// System.err.println("disposing: " + getTileString(tileServer, x,
			// y, z));
			parentImage.dispose();
		}
	}

	// private void checkThread(Display display) {
	// // jdk 1.6 bug from checkWidget still fails here
	// if (display.getThread() != Thread.currentThread()) {
	// throw new IllegalStateException("wrong thread to pick up the image");
	// }
	// }

	public Envelope getEnvelope() {
		// TODO Auto-generated method stub
		return env;
	}

	public void setParent(AsyncImage parent) {
		this.parent = parent;

	}
}