/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.metadata;

import java.text.NumberFormat;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class Probabilistic implements IProbabilistic {
	/**
	 * 
	 */
	private static final long serialVersionUID = -147594856639774242L;
	/** The classes. */
	@SuppressWarnings("unchecked")
	public static final Class<? extends IMetaAttribute>[] CLASSES = new Class[] { IProbabilistic.class };
	/** Tuple existence probability. */
	private double existence;

	/**
	 * Default constructor.
	 */
	public Probabilistic() {
		this.existence = 1.0;
	}

	/**
	 * Creates a new {@link Probabilistic} with the given existence value.
	 * 
	 * @param existence
	 *            The existence value
	 */
	public Probabilistic(final double existence) {
		Preconditions.checkArgument(existence >= 0.0 && existence <= 1.0);
		this.existence = existence;
	}

	/**
	 * Clone constructor.
	 * 
	 * @param probability
	 *            The object to copy from
	 */
	public Probabilistic(final Probabilistic probability) {
		this.existence = probability.existence;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.ICSVToString#csvToString(char, java.lang.Character, java.text.NumberFormat, java.text.NumberFormat, boolean)
	 */
	@Override
	public final String csvToString(final char delimiter, final Character textSeperator, final NumberFormat floatingFormatter, final NumberFormat numberFormatter, final boolean withMetadata) {
		if (floatingFormatter != null) {
			return floatingFormatter.format(this.existence);
		} else {
			return "" + this.existence;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.ICSVToString#getCSVHeader(char)
	 */
	@Override
	public final String getCSVHeader(final char delimiter) {
		return "probability";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public final IProbabilistic clone() {
		return new Probabilistic(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic#getExistence ()
	 */
	@Override
	public final double getExistence() {
		return this.existence;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic#setExistence (double)
	 */
	@Override
	public final void setExistence(final double existence) {
		this.existence = existence;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return "TEP: " + this.existence;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute#getClasses()
	 */
	@Override
	public final Class<? extends IMetaAttribute>[] getClasses() {
		return Probabilistic.CLASSES;
	}

}
