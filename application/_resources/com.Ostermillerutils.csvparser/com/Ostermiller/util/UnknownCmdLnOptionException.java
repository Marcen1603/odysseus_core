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
 * Copyright (C) 2007 Stephen Ostermiller
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

/**
 * Exception thrown when a command line option that was unexpected is found.
 */
public class UnknownCmdLnOptionException extends CmdLnException {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 6461128374875358108L;

	/**
	 * Constructs an <code>UnknownCmdLnOptionException</code>
	 */
	public UnknownCmdLnOptionException() {
		super("Unknown command line option");
	}

	private String argument;

	/**
	 * Get the argument that caused this exception
	 * @return the argument
	 */
	public String getArgument() {
		return argument;
	}

	/**
	 * Set the argument
	 * @param argument the argument to set
	 * @return this for method chaining
	 */
	UnknownCmdLnOptionException setArgument(String argument) {
		this.argument = argument;
		return this;
	}

	private String option;

	/**
	 * Get the option that caused this exception
	 * @return the option
	 */
	public String getOption() {
		return option;
	}

	/**
	 * Set the option
	 * @param option the option to set
	 * @return this for method chaining
	 */
	UnknownCmdLnOptionException setOption(String option) {
		this.option = option;
		return this;
	}

	@Override public String getMessage(){
		return super.getMessage() + ": " + getArgument() + ": " + getOption();
	}
}
