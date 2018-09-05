package de.uniol.inf.is.odysseus.wrapper.snet.communication.util;
/**********************************************************************************
*
* Copyright (c) 2003, 2004 The Regents of the University of Michigan, Trustees of Indiana University,
*                  Board of Trustees of the Leland Stanford, Jr., University, and The MIT Corporation
*
* Licensed under the Educational Community License Version 1.0 (the "License");
* By obtaining, using and/or copying this Original Work, you agree that you have read,
* understand, and will comply with the terms and conditions of the Educational Community License.
* You may obtain a copy of the License at:
*
*      http://cvs.sakaiproject.org/licenses/license_1_0.html
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
* INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
* AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*
**********************************************************************************/


/**
* Byte utilities
*/
public class ByteUtils {


 /**
  * Return a new byte array containing a sub-portion of the source array
  * 
  * @param srcBegin
  *          The beginning index (inclusive)
  * @return The new, populated byte array
  */
 public static byte[] subbytes(byte[] source, int srcBegin) {
   return subbytes(source, srcBegin, source.length);
 }
 /**
  * Return a new byte array containing a sub-portion of the source array
  * 
  * @param srcBegin
  *          The beginning index (inclusive)
  * @param srcEnd
  *          The ending index (exclusive)
  * @return The new, populated byte array
  */
 public static byte[] subbytes(byte[] source, int srcBegin, int srcEnd) {
   byte destination[];

   destination = new byte[srcEnd - srcBegin];
   getBytes(source, srcBegin, srcEnd, destination, 0);

   return destination;
 }


 /**
  * Copies bytes from the source byte array to the destination array
  * 
  * @param source
  *          The source array
  * @param srcBegin
  *          Index of the first source byte to copy
  * @param srcEnd
  *          Index after the last source byte to copy
  * @param destination
  *          The destination array
  * @param dstBegin
  *          The starting offset in the destination array
  */
 public static void getBytes(byte[] source, int srcBegin, int srcEnd, byte[] destination,
     int dstBegin) {
   System.arraycopy(source, srcBegin, destination, dstBegin, srcEnd - srcBegin);
 }

}