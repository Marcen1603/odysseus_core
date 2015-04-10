/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * This file is not intended to be easily readable and contains a number of
 * coding conventions designed to improve portability and efficiency. Do not make
 * changes to this file unless you know what you are doing--modify the SWIG
 * interface file instead.
 * ----------------------------------------------------------------------------- */

#ifndef SWIG_NavicoRadarJava_WRAP_H_
#define SWIG_NavicoRadarJava_WRAP_H_

class SwigDirector_NavicoRadarWrapper : public NavicoRadarWrapper, public Swig::Director {

public:
    void swig_connect_director(JNIEnv *jenv, jobject jself, jclass jcls, bool swig_mem_own, bool weak_global);
    SwigDirector_NavicoRadarWrapper(JNIEnv *jenv, int AntennaHeightMiliMeter, int RangeMeter, std::string RadarSerial, signed char *UnlockKey, int UnlockKeylength);
    virtual void onSpokeUpdate(void *buffer, long size);
    virtual void onCat240SpokeUpdate(void *buffer, long size);
    virtual void onTargetUpdate(void *buffer, long size);
public:
    bool swig_overrides(int n) {
      return (n < 3 ? swig_override[n] : false);
    }
protected:
    bool swig_override[3];
};


#endif