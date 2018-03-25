/**********************************************************************************
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
package de.uniol.inf.is.odysseus.server.xml.physicaloperator;

import java.util.ArrayList;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactoryConfigurationException;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.predicate.XMLStreamObjectPredicate;

public class XMLEnrichPO<T extends IMetaAttribute> extends AbstractPipe<XMLStreamObject<T>, XMLStreamObject<T>>
		implements IHasPredicate {

	private XMLStreamObjectPredicate<XMLStreamObject<T>> predicate;
	private List<XMLStreamObject<T>> cache = new ArrayList<>();
	private List<XMLStreamObject<T>> buffer = new ArrayList<>();
	private String target;

	private int minSize = 0;

	public XMLEnrichPO(int minimalSize) {
		this.minSize = minimalSize;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public XMLEnrichPO(IPredicate predicate, int minimumSize, String _target) {
		this.predicate = (XMLStreamObjectPredicate<XMLStreamObject<T>>) predicate;
		this.minSize = minimumSize;
		this.target = _target;
	}

	public XMLEnrichPO(XMLEnrichPO<T> po) {
		super(po);
		this.minSize = po.minSize;
		this.predicate = (XMLStreamObjectPredicate<XMLStreamObject<T>>) po.predicate.clone();
	}

	@Override
	public IPredicate<?> getPredicate() {
		return this.predicate;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.buffer.clear();
		this.cache.clear();
	}

	@Override
	protected void process_next(XMLStreamObject<T> object, int port) {
		
		// if port == 0, it is a cached-object
		if (port == 0) {
			this.cache.add(object);
			// check, whether there are enough items in cache to write out the
			// buffer
			if (this.cache.size() >= minSize) {
				synchronized (this.buffer) {
					for (XMLStreamObject<T> buffered : this.buffer) {
						processJoin(buffered);
					}
					this.buffer.clear();
				}
			}
		} else {
			// if we do not have enough items in cache, we put the objects into
			// a buffer
			if (this.cache.size() < minSize) {
				synchronized (this.buffer) {
					buffer.add(object);
				}
			} else {
				// if we have enough, we can enrich the object without waiting
				processJoin(object);
			}
		}

	}

	@SuppressWarnings("unchecked")
	private void processJoin(XMLStreamObject<T> object) {
		try {
			synchronized (cache) {
				
				for (XMLStreamObject<T> cached : cache) {
					//TODO predicate is not right!
					// It cannot be evaluated correctly, because the values of the specified expression are neither
					// retrieved nor evaluated. Currently, the predicate relates to a string comparison.

					if (predicate != null) {
						if (predicate.evaluate(cached, object)) {
							transfer((XMLStreamObject<T>) XMLStreamObject.merge(object, cached, target));
						}
					} else {
						transfer((XMLStreamObject<T>) XMLStreamObject.merge(object, cached, target));
					}
				}
			}
			
		} catch (XPathFactoryConfigurationException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void process_close() {
		super.process_close();
		this.buffer.clear();
		synchronized (cache) {
			this.cache.clear();
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		predicate.processPunctuation(punctuation);
	}

	@Override
	public long getElementsStored1() {
		return cache.size();
	}

	@Override
	public long getElementsStored2() {
		return buffer.size();
	}

	@Override
	public void setPredicate(IPredicate<?> predicate) {
		// TODO Auto-generated method stub
	}
}
