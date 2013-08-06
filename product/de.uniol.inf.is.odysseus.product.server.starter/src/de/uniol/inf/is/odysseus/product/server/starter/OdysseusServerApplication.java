package de.uniol.inf.is.odysseus.product.server.starter;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.osgi.framework.BundleContext;

public class OdysseusServerApplication implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		System.out.println("Starting Odysseus application");
		startBundles(context.getBrandingBundle().getBundleContext());		
		synchronized (this) {
			this.wait();
		}
		System.out.println("Odysseus terminated");
		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		synchronized (this) {
			notifyAll();
		}
	}
	
	
	private static void startBundles(final BundleContext context) {
//		Thread t = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e1) {
//					e1.printStackTrace();
//				}
//				for (Bundle bundle : context.getBundles()) {
//					boolean isFragment = bundle.getHeaders().get(
//							Constants.FRAGMENT_HOST) != null;
//					if (bundle != context.getBundle() && !isFragment
//							&& bundle.getState() == Bundle.RESOLVED) {
//						try {
//							bundle.start();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			}
//		});
//		t.start();
	}

}
