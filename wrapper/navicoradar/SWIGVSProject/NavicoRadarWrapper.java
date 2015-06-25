/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package de.uniol.inf.is.odysseus.wrapper.navicoradar.SWIG;

public class NavicoRadarWrapper {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected NavicoRadarWrapper(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(NavicoRadarWrapper obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        NavicoRadarJavaJNI.delete_NavicoRadarWrapper(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  protected void swigDirectorDisconnect() {
    swigCMemOwn = false;
    delete();
  }

  public void swigReleaseOwnership() {
    swigCMemOwn = false;
    NavicoRadarJavaJNI.NavicoRadarWrapper_change_ownership(this, swigCPtr, false);
  }

  public void swigTakeOwnership() {
    swigCMemOwn = true;
    NavicoRadarJavaJNI.NavicoRadarWrapper_change_ownership(this, swigCPtr, true);
  }

  public NavicoRadarWrapper(int AntennaHeightMiliMeter, int RangeMeter, String RadarSerial, SWIGTYPE_p_signed_char UnlockKey, int UnlockKeylength) throws java.lang.RuntimeException {
    this(NavicoRadarJavaJNI.new_NavicoRadarWrapper(AntennaHeightMiliMeter, RangeMeter, RadarSerial, SWIGTYPE_p_signed_char.getCPtr(UnlockKey), UnlockKeylength), true);
    NavicoRadarJavaJNI.NavicoRadarWrapper_director_connect(this, swigCPtr, swigCMemOwn, true);
  }

  public void start() {
    NavicoRadarJavaJNI.NavicoRadarWrapper_start(swigCPtr, this);
  }

  public void stop() {
    NavicoRadarJavaJNI.NavicoRadarWrapper_stop(swigCPtr, this);
  }

  public boolean AcquireTargets(int TargetId, int range, int bearing, int bearingtype) {
    return NavicoRadarJavaJNI.NavicoRadarWrapper_AcquireTargets(swigCPtr, this, TargetId, range, bearing, bearingtype);
  }

  public boolean SetBoatSpeed(int speedType, double speed, int headingType, double heading) {
    return NavicoRadarJavaJNI.NavicoRadarWrapper_SetBoatSpeed(swigCPtr, this, speedType, speed, headingType, heading);
  }

  public boolean cancelAll() {
    return NavicoRadarJavaJNI.NavicoRadarWrapper_cancelAll(swigCPtr, this);
  }

  public void onSpokeUpdate(java.nio.ByteBuffer buffer) {
    if (getClass() == NavicoRadarWrapper.class) NavicoRadarJavaJNI.NavicoRadarWrapper_onSpokeUpdate(swigCPtr, this, buffer); else NavicoRadarJavaJNI.NavicoRadarWrapper_onSpokeUpdateSwigExplicitNavicoRadarWrapper(swigCPtr, this, buffer);
  }

  public void onCat240SpokeUpdate(java.nio.ByteBuffer buffer) {
    if (getClass() == NavicoRadarWrapper.class) NavicoRadarJavaJNI.NavicoRadarWrapper_onCat240SpokeUpdate(swigCPtr, this, buffer); else NavicoRadarJavaJNI.NavicoRadarWrapper_onCat240SpokeUpdateSwigExplicitNavicoRadarWrapper(swigCPtr, this, buffer);
  }

  public void onTargetUpdate(java.nio.ByteBuffer buffer) {
    if (getClass() == NavicoRadarWrapper.class) NavicoRadarJavaJNI.NavicoRadarWrapper_onTargetUpdate(swigCPtr, this, buffer); else NavicoRadarJavaJNI.NavicoRadarWrapper_onTargetUpdateSwigExplicitNavicoRadarWrapper(swigCPtr, this, buffer);
  }

  public void onTargetUpdateTTM(String TTMmessage) {
    if (getClass() == NavicoRadarWrapper.class) NavicoRadarJavaJNI.NavicoRadarWrapper_onTargetUpdateTTM(swigCPtr, this, TTMmessage); else NavicoRadarJavaJNI.NavicoRadarWrapper_onTargetUpdateTTMSwigExplicitNavicoRadarWrapper(swigCPtr, this, TTMmessage);
  }

  public void onPictureUpdate(java.nio.ByteBuffer buffer) {
    if (getClass() == NavicoRadarWrapper.class) NavicoRadarJavaJNI.NavicoRadarWrapper_onPictureUpdate(swigCPtr, this, buffer); else NavicoRadarJavaJNI.NavicoRadarWrapper_onPictureUpdateSwigExplicitNavicoRadarWrapper(swigCPtr, this, buffer);
  }

  public void setCat240Out() {
    NavicoRadarJavaJNI.NavicoRadarWrapper_setCat240Out(swigCPtr, this);
  }

  public void delCat240Out() {
    NavicoRadarJavaJNI.NavicoRadarWrapper_delCat240Out(swigCPtr, this);
  }

  public boolean setAntennaHeight(int HeightInMm) {
    return NavicoRadarJavaJNI.NavicoRadarWrapper_setAntennaHeight(swigCPtr, this, HeightInMm);
  }

  public int getAntennaHeight() {
    return NavicoRadarJavaJNI.NavicoRadarWrapper_getAntennaHeight(swigCPtr, this);
  }

  public boolean setRadarRange(int RangeInM) {
    return NavicoRadarJavaJNI.NavicoRadarWrapper_setRadarRange(swigCPtr, this, RangeInM);
  }

  public int getRadarRange() {
    return NavicoRadarJavaJNI.NavicoRadarWrapper_getRadarRange(swigCPtr, this);
  }

  public boolean setDangerDistance(int DistanceInM) {
    return NavicoRadarJavaJNI.NavicoRadarWrapper_setDangerDistance(swigCPtr, this, DistanceInM);
  }

  public int getDangerDistance() {
    return NavicoRadarJavaJNI.NavicoRadarWrapper_getDangerDistance(swigCPtr, this);
  }

  public boolean setDangerTime(int TimeInS) {
    return NavicoRadarJavaJNI.NavicoRadarWrapper_setDangerTime(swigCPtr, this, TimeInS);
  }

  public int getDangerTime() {
    return NavicoRadarJavaJNI.NavicoRadarWrapper_getDangerTime(swigCPtr, this);
  }

}
