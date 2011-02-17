/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
/*
 * Regression tests for ExecHelper
 * Copyright (C) 2005 Stephen Ostermiller
 * http://ostermiller.org/contact.pl?regarding=Java+Utilities
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * See COPYING.TXT for details.
 */
package com.Ostermiller.util;

import java.io.*;

/**
 * Regression test for ExecHelper.  When run, this program
 * should nothing unless an error occurs.
 *
 * More information about this class is available from <a target="_top" href=
 * "http://ostermiller.org/utils/ExecHelper.html">ostermiller.org</a>.
 *
 * @author Stephen Ostermiller http://ostermiller.org/contact.pl?regarding=Java+Utilities
 * @since ostermillerutils 1.06.00
 */
class ExecHelperTests {

	/**
	 * Main method to run test
	 * @param args Command line arguments (ignored
	 */
	public static void main(String args[]){
		try {
			File temp = File.createTempFile("ExecHelperTests", "tmp");
			temp.deleteOnExit();
			String s = createLargeString();
			Writer out = new FileWriter(temp);
			out.write(s);
			out.close();
			ExecHelper eh = ExecHelper.exec(new String[]{"cat", temp.toString()});
			if (!eh.getOutput().equals(s)){
				throw new Exception("Couldn't read file via cat");
			}
			// Test the shell, but only on Unix
			File sh = new File("/bin/sh");
			if (sh.exists()){
				eh = ExecHelper.execUsingShell("sleep 3; echo -n stdin && echo -n stderr 1>&2; exit 11");
				if (!eh.getOutput().equals("stdin")){
					throw new Exception("Couldn't echo to stdin through a shell.");
				}
				if (!eh.getError().equals("stderr")){
					throw new Exception("Couldn't echo to stderr through a shell.");
				}
				if (eh.getStatus() != 11){
					throw new Exception("Expected exit status of 11.");
				}
			}
		} catch (Exception x){
			x.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

	private static String createLargeString(){
		StringBuffer sb = new StringBuffer(40000*26);
		for (int i=0; i<40000; i++){
			sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		}
		return sb.toString();

	}
}
