package de.uniol.inf.is.odysseus.wrapper.baslercamera;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvCreateImageHeader;
import static org.bytedeco.javacpp.opencv_core.cvSize;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.wrapper.baslercamera.swig.BaslerJavaJNI;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception 
	{
		Activator.context = bundleContext;
		try 
		{
			System.loadLibrary("BaslerJava");
			
			
			int width = 99;
			int height = 99;
			int channels = 1;
			
			IplImage img = cvCreateImage(cvSize(width, height), IPL_DEPTH_8U, channels);
			boolean direct = img.getByteBuffer().isDirect();
			int wfill = img.widthStep() - width*channels;
			
			System.out.println("wfill = " + wfill);
			
			
/*			int[] arr = new int[5];
			arr[2] = 567;
			BaslerJavaJNI.BaslerCamera_getImageData3(0, null, arr);
			System.out.println(arr[1]);*/
			
/*			ByteBuffer buf = ByteBuffer.allocateDirect(20);
			BaslerJavaJNI.BaslerCamera_getImageData4(0, null, buf);
			System.out.println(buf.get(1));
			
			int width = 100;
			int height = 100;
			int channels = 4;
			IplImage img = cvCreateImageHeader(cvSize(width, height), IPL_DEPTH_8U, channels);
			cvSetData()
			img.imageData(buf);*/
			
		}
		catch(Exception e) 
		{
			e.printStackTrace();
		}		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception 
	{
		Activator.context = null;
	}

}
