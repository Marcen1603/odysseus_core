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
package com.xafero.turjumaan.server.java;

import com.xafero.turjumaan.server.java.api.Description;
import com.xafero.turjumaan.server.java.api.NotCacheable;

/**
 * A wrapper object for the JVM.
 */
@Description("The Java Virtual Machine")
public class JVM {

	/**
	 * Gets the current time.
	 *
	 * @return the current time
	 */
	@NotCacheable
	@Description("Milliseconds between the current time and 1/1/1970")
	public long getCurrentTime() {
		return System.currentTimeMillis();
	}

	/**
	 * Gets the nanosecond time.
	 *
	 * @return the nanosecond time
	 */
	@NotCacheable
	@Description("High-resolution time source in nanoseconds")
	public long getNanoTime() {
		return System.nanoTime();
	}

	/**
	 * Gets the processor count.
	 *
	 * @return the processor count
	 */
	@NotCacheable
	@Description("Maximum number of processors available")
	public int getProcessorCount() {
		return Runtime.getRuntime().availableProcessors();
	}

	/**
	 * Gets the free memory.
	 *
	 * @return the free memory
	 */
	@NotCacheable
	@Description("Amount of free memory")
	public long getFreeMemory() {
		return Runtime.getRuntime().freeMemory();
	}

	/**
	 * Gets the max memory.
	 *
	 * @return the max memory
	 */
	@NotCacheable
	@Description("Maximum amount of memory")
	public long getMaxMemory() {
		return Runtime.getRuntime().maxMemory();
	}

	/**
	 * Gets the total memory.
	 *
	 * @return the total memory
	 */
	@NotCacheable
	@Description("Total amount of memory currently available")
	public long getTotalMemory() {
		return Runtime.getRuntime().totalMemory();
	}

	/**
	 * Gets the Java version.
	 *
	 * @return the Java version
	 */
	@Description("Java version")
	public String getJavaVersion() {
		return System.getProperty("java.runtime.version");
	}

	/**
	 * Gets the locale.
	 *
	 * @return the locale
	 */
	@Description("User's locale")
	public String getLocale() {
		return System.getProperty("user.language") + "_" + System.getProperty("user.country.format");
	}

	/**
	 * Gets the operating system.
	 *
	 * @return the operating system
	 */
	@Description("Operating system")
	public String getOperatingSystem() {
		return System.getProperty("os.name") + " (" + System.getProperty("os.version") + ") "
				+ System.getProperty("os.arch");
	}
}