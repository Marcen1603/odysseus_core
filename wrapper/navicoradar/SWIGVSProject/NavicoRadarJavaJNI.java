/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.uniol.inf.is.odysseus.wrapper.navicoradar.SWIG;

public class NavicoRadarJavaJNI {
  public final static native int NUM_OF_SAMPLES_get();
  public final static native long new_NavicoRadarWrapper(int jarg1, int jarg2, String jarg3, long jarg4, int jarg5) throws java.lang.RuntimeException;
  public final static native void delete_NavicoRadarWrapper(long jarg1);
  public final static native void NavicoRadarWrapper_start(long jarg1, NavicoRadarWrapper jarg1_);
  public final static native void NavicoRadarWrapper_stop(long jarg1, NavicoRadarWrapper jarg1_);
  public final static native boolean NavicoRadarWrapper_AcquireTargets(long jarg1, NavicoRadarWrapper jarg1_, int jarg2, int jarg3, int jarg4, int jarg5);
  public final static native boolean NavicoRadarWrapper_SetBoatSpeed(long jarg1, NavicoRadarWrapper jarg1_, int jarg2, double jarg3, int jarg4, double jarg5);
  public final static native boolean NavicoRadarWrapper_cancelAll(long jarg1, NavicoRadarWrapper jarg1_);
  public final static native void NavicoRadarWrapper_onSpokeUpdate(long jarg1, NavicoRadarWrapper jarg1_, java.nio.ByteBuffer jarg2);
  public final static native void NavicoRadarWrapper_onSpokeUpdateSwigExplicitNavicoRadarWrapper(long jarg1, NavicoRadarWrapper jarg1_, java.nio.ByteBuffer jarg2);
  public final static native void NavicoRadarWrapper_onCat240SpokeUpdate(long jarg1, NavicoRadarWrapper jarg1_, java.nio.ByteBuffer jarg2);
  public final static native void NavicoRadarWrapper_onCat240SpokeUpdateSwigExplicitNavicoRadarWrapper(long jarg1, NavicoRadarWrapper jarg1_, java.nio.ByteBuffer jarg2);
  public final static native void NavicoRadarWrapper_onTargetUpdate(long jarg1, NavicoRadarWrapper jarg1_, java.nio.ByteBuffer jarg2);
  public final static native void NavicoRadarWrapper_onTargetUpdateSwigExplicitNavicoRadarWrapper(long jarg1, NavicoRadarWrapper jarg1_, java.nio.ByteBuffer jarg2);
  public final static native void NavicoRadarWrapper_onTargetUpdateTTM(long jarg1, NavicoRadarWrapper jarg1_, String jarg2);
  public final static native void NavicoRadarWrapper_onTargetUpdateTTMSwigExplicitNavicoRadarWrapper(long jarg1, NavicoRadarWrapper jarg1_, String jarg2);
  public final static native void NavicoRadarWrapper_onPictureUpdate(long jarg1, NavicoRadarWrapper jarg1_, java.nio.ByteBuffer jarg2);
  public final static native void NavicoRadarWrapper_onPictureUpdateSwigExplicitNavicoRadarWrapper(long jarg1, NavicoRadarWrapper jarg1_, java.nio.ByteBuffer jarg2);
  public final static native void NavicoRadarWrapper_setCat240Out(long jarg1, NavicoRadarWrapper jarg1_);
  public final static native void NavicoRadarWrapper_delCat240Out(long jarg1, NavicoRadarWrapper jarg1_);
  public final static native boolean NavicoRadarWrapper_setAntennaHeight(long jarg1, NavicoRadarWrapper jarg1_, int jarg2);
  public final static native int NavicoRadarWrapper_getAntennaHeight(long jarg1, NavicoRadarWrapper jarg1_);
  public final static native boolean NavicoRadarWrapper_setRadarRange(long jarg1, NavicoRadarWrapper jarg1_, int jarg2);
  public final static native int NavicoRadarWrapper_getRadarRange(long jarg1, NavicoRadarWrapper jarg1_);
  public final static native boolean NavicoRadarWrapper_setDangerDistance(long jarg1, NavicoRadarWrapper jarg1_, int jarg2);
  public final static native int NavicoRadarWrapper_getDangerDistance(long jarg1, NavicoRadarWrapper jarg1_);
  public final static native boolean NavicoRadarWrapper_setDangerTime(long jarg1, NavicoRadarWrapper jarg1_, int jarg2);
  public final static native int NavicoRadarWrapper_getDangerTime(long jarg1, NavicoRadarWrapper jarg1_);
  public final static native void NavicoRadarWrapper_director_connect(NavicoRadarWrapper obj, long cptr, boolean mem_own, boolean weak_global);
  public final static native void NavicoRadarWrapper_change_ownership(NavicoRadarWrapper obj, long cptr, boolean take_or_release);

  public static void SwigDirector_NavicoRadarWrapper_onSpokeUpdate(NavicoRadarWrapper self, java.nio.ByteBuffer buffer) {
    self.onSpokeUpdate(buffer);
  }
  public static void SwigDirector_NavicoRadarWrapper_onCat240SpokeUpdate(NavicoRadarWrapper self, java.nio.ByteBuffer buffer) {
    self.onCat240SpokeUpdate(buffer);
  }
  public static void SwigDirector_NavicoRadarWrapper_onTargetUpdate(NavicoRadarWrapper self, java.nio.ByteBuffer buffer) {
    self.onTargetUpdate(buffer);
  }
  public static void SwigDirector_NavicoRadarWrapper_onTargetUpdateTTM(NavicoRadarWrapper self, String TTMmessage) {
    self.onTargetUpdateTTM(TTMmessage);
  }
  public static void SwigDirector_NavicoRadarWrapper_onPictureUpdate(NavicoRadarWrapper self, java.nio.ByteBuffer buffer) {
    self.onPictureUpdate(buffer);
  }

  private final static native void swig_module_init();
  static {
    swig_module_init();
  }
}
