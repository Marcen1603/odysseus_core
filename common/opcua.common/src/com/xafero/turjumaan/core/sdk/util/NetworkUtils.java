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

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

/**
 * The networking utilities.
 */
public class NetworkUtils {

	/**
	 * The type of Internet address.
	 */
	public static enum InetAddressType {

		/** Loopback. */
		Loopback,

		/** Internet Protocol version 4. */
		IPv4,

		/** Internet Protocol version 6. */
		IPv6,

		/** It's inactive. */
		Inactive
	}

	/**
	 * Instantiates a new network utility.
	 */
	private NetworkUtils() {
	}

	/**
	 * Gets all Internet addresses.
	 *
	 * @return all network addresses
	 */
	public static List<InetAddress> getAllInetAddresses() {
		return getAllInetAddresses(EnumSet.allOf(InetAddressType.class));
	}

	/**
	 * Gets Internet addresses by their type.
	 *
	 * @param types
	 *            the type(s)
	 * @return all matching Internet addresses
	 */
	public static List<InetAddress> getAllInetAddresses(EnumSet<InetAddressType> types) {
		try {
			List<InetAddress> results = new LinkedList<InetAddress>();
			List<NetworkInterface> intfs = Collections.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface intf : intfs) {
				if (!intf.isUp() && !types.contains(InetAddressType.Inactive))
					continue;
				for (InetAddress addr : Collections.list(intf.getInetAddresses())) {
					if (addr.isLoopbackAddress() && !types.contains(InetAddressType.Loopback))
						continue;
					if (addr instanceof Inet4Address && !types.contains(InetAddressType.IPv4))
						continue;
					if (addr instanceof Inet6Address && !types.contains(InetAddressType.IPv6))
						continue;
					results.add(addr);
				}
			}
			return results;
		} catch (SocketException e) {
			throw new UnsupportedOperationException(e);
		}
	}
}