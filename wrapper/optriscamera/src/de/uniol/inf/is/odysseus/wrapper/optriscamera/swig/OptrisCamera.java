/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.uniol.inf.is.odysseus.wrapper.optriscamera.swig;

public class OptrisCamera {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected OptrisCamera(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(OptrisCamera obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        OptrisJavaJNI.delete_OptrisCamera(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public OptrisCamera(String instanceName, String ethernetAddr) throws java.lang.RuntimeException {
    this(OptrisJavaJNI.new_OptrisCamera(instanceName, ethernetAddr), true);
  }

  public void start() throws java.lang.RuntimeException {
    OptrisJavaJNI.OptrisCamera_start(swigCPtr, this);
  }

  public void stop() {
    OptrisJavaJNI.OptrisCamera_stop(swigCPtr, this);
  }

  public void setFrameBuffer(java.nio.ByteBuffer buffer) {
    OptrisJavaJNI.OptrisCamera_setFrameBuffer(swigCPtr, this, buffer);
  }

  public void delFrameCallback() {
    OptrisJavaJNI.OptrisCamera_delFrameCallback(swigCPtr, this);
  }

  public void setFrameCallback(FrameCallback frameCallback) {
    OptrisJavaJNI.OptrisCamera_setFrameCallback(swigCPtr, this, FrameCallback.getCPtr(frameCallback), frameCallback);
  }

  public int getBufferSize() {
    return OptrisJavaJNI.OptrisCamera_getBufferSize(swigCPtr, this);
  }

  public int getImageWidth() {
    return OptrisJavaJNI.OptrisCamera_getImageWidth(swigCPtr, this);
  }

  public int getImageHeight() {
    return OptrisJavaJNI.OptrisCamera_getImageHeight(swigCPtr, this);
  }

}
