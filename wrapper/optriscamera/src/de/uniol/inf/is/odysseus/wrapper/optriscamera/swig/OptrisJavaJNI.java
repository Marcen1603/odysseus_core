/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.uniol.inf.is.odysseus.wrapper.optriscamera.swig;

public class OptrisJavaJNI {
  public final static native long new_intArray(int jarg1);
  public final static native void delete_intArray(long jarg1);
  public final static native int intArray_getitem(long jarg1, intArray jarg1_, int jarg2);
  public final static native void intArray_setitem(long jarg1, intArray jarg1_, int jarg2, int jarg3);
  public final static native long intArray_cast(long jarg1, intArray jarg1_);
  public final static native long intArray_frompointer(long jarg1);
  public final static native long new_OptrisCamera(String jarg1, String jarg2) throws java.lang.RuntimeException;
  public final static native void delete_OptrisCamera(long jarg1);
  public final static native void OptrisCamera_start(long jarg1, OptrisCamera jarg1_);
  public final static native void OptrisCamera_stop(long jarg1, OptrisCamera jarg1_);
  public final static native int OptrisCamera_getImageWidth(long jarg1, OptrisCamera jarg1_);
  public final static native int OptrisCamera_getImageHeight(long jarg1, OptrisCamera jarg1_);
}
