/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.uniol.inf.is.odysseus.wrapper.baslercamera.swig;

public class BaslerJavaJNI {
  public final static native long new_BaslerCamera(String jarg1);
  public final static native void delete_BaslerCamera(long jarg1);
  public final static native void BaslerCamera_start(long jarg1, BaslerCamera jarg1_) throws java.lang.RuntimeException;
  public final static native void BaslerCamera_stop(long jarg1, BaslerCamera jarg1_);
  public final static native boolean BaslerCamera_grabRGB8(long jarg1, BaslerCamera jarg1_, java.nio.ByteBuffer jarg2, int jarg4, long jarg5) throws java.lang.RuntimeException;
  public final static native int BaslerCamera_getBufferSize(long jarg1, BaslerCamera jarg1_);
  public final static native int BaslerCamera_getImageWidth(long jarg1, BaslerCamera jarg1_);
  public final static native int BaslerCamera_getImageHeight(long jarg1, BaslerCamera jarg1_);
  public final static native int BaslerCamera_getImageChannels(long jarg1, BaslerCamera jarg1_);
  public final static native void BaslerCamera_initializeSystem() throws java.lang.RuntimeException;
  public final static native void BaslerCamera_shutDownSystem();
  public final static native boolean BaslerCamera_isSystemInitialized();
}
