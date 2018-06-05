/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.recommendation.data.impl;

import de.uniol.inf.is.odysseus.recommendation.data.UserItemRatingTriple;

/**
 * This implementation is immutable, as long as U, I, R are immutable, or the
 * constructor gets save-copies.
 *
 * @author Cornelius Ludmann
 *
 */
public class ImmutableUserItemRatingTriple<U, I, R> implements
		UserItemRatingTriple<U, I, R> {

	private final U user;
	private final I item;
	private final R rating;

	/**
	 *
	 */
	public ImmutableUserItemRatingTriple(final U user, final I item,
			final R rating) {
		this.user = user;
		this.item = item;
		this.rating = rating;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.data.UserItemRatingTriple#getUser
	 * ()
	 */
	@Override
	public U getUser() {
		return this.user;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.data.UserItemRatingTriple#getItem
	 * ()
	 */
	@Override
	public I getItem() {
		return this.item;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.recommendation.data.UserItemRatingTriple#getRating
	 * ()
	 */
	@Override
	public R getRating() {
		return this.rating;
	}

}
