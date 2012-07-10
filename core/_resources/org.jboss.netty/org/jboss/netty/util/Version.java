/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
// DO NOT MODIFY - WILL BE OVERWRITTEN DURING THE BUILD PROCESS
package org.jboss.netty.util;
/**
 * Provides the version information of Netty.
 * @apiviz.landmark
 */
public final class Version {
 /** The version identifier. */
 public static final String ID = "3.3.1.Final-unknown";
 /** Prints out the version identifier to stdout. */
 public static void main(String[] args) { System.out.println(ID); }
 private Version() { super(); }
}
