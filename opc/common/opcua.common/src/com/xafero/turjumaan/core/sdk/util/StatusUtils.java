/*******************************************************************************
 * Copyright 2016 Georg Berendt
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
package com.xafero.turjumaan.core.sdk.util;

import com.inductiveautomation.opcua.stack.core.StatusCodes;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;

/**
 * The status utilities.
 */
public final class StatusUtils {

	/**
	 * Instantiates a new status utility.
	 */
	private StatusUtils() {
	}

	/**
	 * Convert a status code to a string.
	 *
	 * @param code
	 *            the code
	 * @return the textual representation
	 */
	public static String toString(StatusCode code) {
		String[] lookup = StatusCodes.lookup(code.getValue());
		String name = lookup[0];
		@SuppressWarnings("unused")
		String desc = lookup[1];
		if (name.equals("unknown")) {
			if (code.isBad()) {
				name = "Bad";
				desc = "Something's bad.";
			} else if (code.isOverflowSet()) {
				name = "OverflowSet";
				desc = "Something overflows.";
			} else if (code.isUncertain()) {
				name = "Uncertain";
				desc = "Something's uncertain.";
			} else if (code.isGood()) {
				name = "Good";
				desc = "Everything's fine.";
			}
		}
		return String.format("%s", name);
	}
}