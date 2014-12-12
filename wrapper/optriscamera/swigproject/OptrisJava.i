/* File : OptrisJava.i */
%module OptrisJava

%include "std_string.i"
%include "various.i"
%include "exception.i"
%include "carrays.i"

%array_class(int, intArray);

%typemap(throws, throws="java.lang.RuntimeException") std::exception
{
  jclass excep = jenv->FindClass("java/lang/RuntimeException");
  if (excep)
    jenv->ThrowNew(excep, $1.what());
  return $null;
}
/* %typemap(javabase) std::ios_base::failure "java.lang.Exception"; */


%{
#include "OptrisCamera.h"
%}

%include "OptrisCamera.h"
