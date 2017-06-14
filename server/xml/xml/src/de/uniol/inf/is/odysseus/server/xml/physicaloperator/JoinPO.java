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

import de.uniol.inf.is.odysseus.core.Order;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IDataMergeFunction;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.metadata.UseRightInputMetadata;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicate;
import de.uniol.inf.is.odysseus.server.xml.XMLStreamObject;
import de.uniol.inf.is.odysseus.server.xml.predicate.XMLStreamObjectPredicate;

/**
 * @author Dennis Geesen
 *
 */
public class JoinPO<T extends IMetaAttribute> extends AbstractPipe<XMLStreamObject<T>, XMLStreamObject<T>> implements IHasPredicate
{

	private XMLStreamObjectPredicate<XMLStreamObject> predicate;
	private List<XMLStreamObject> cache = new ArrayList<>();
	private List<XMLStreamObject> buffer = new ArrayList<>();
	private String target;

	private int minSize = 0;

	public JoinPO(int minimalSize)
	{
		this.minSize = minimalSize;
	}

	@SuppressWarnings("unchecked")
	public JoinPO(IPredicate predicate, int minimumSize, String _target)
	{
		this.predicate = (XMLStreamObjectPredicate<XMLStreamObject>) predicate;
		this.minSize = minimumSize;
		this.target = _target;
	}

	/**
	 * @param enrichPO
	 */
	public JoinPO(JoinPO<T> po)
	{
		super(po);
		this.minSize = po.minSize;
		this.predicate = (XMLStreamObjectPredicate<XMLStreamObject>) po.predicate.clone();
	}

	@Override
	public IPredicate<?> getPredicate()
	{
		return this.predicate;
	}

	@Override
	public OutputMode getOutputMode()
	{
		return OutputMode.NEW_ELEMENT;
	}

	@Override
	protected void process_open() throws OpenFailedException
	{
		super.process_open();
		this.buffer.clear();
		this.cache.clear();
	}

	@Override
	protected void process_next(XMLStreamObject<T> object, int port)
	{
		// if port == 0, it is a cached-object
		if (port == 0)
		{
			this.cache.add(object);
			// check, whether there are enough items in cache to write out the
			// buffer
			if (this.cache.size() >= minSize)
			{
				synchronized (this.buffer)
				{
					for (XMLStreamObject buffered : this.buffer)
					{
						processJoin(buffered);
					}
					this.buffer.clear();
				}
			}
		} else
		{
			// if we do not have enough items in cache, we put the objects into
			// a buffer
			if (this.cache.size() < minSize)
			{
				synchronized (this.buffer)
				{
					buffer.add(object);
				}
			} else
			{
				// if we have enough, we can enrich the object without waiting
				processJoin(object);
			}
		}

	}

	private void processJoin(XMLStreamObject object)
	{
		synchronized (cache)
		{
			for (XMLStreamObject cached : this.cache)
			{
				if (this.predicate.evaluate(cached, object))
				{
					XMLStreamObject enriched = XMLStreamObject.merge(object, cached, this.target);
					transfer(enriched);
				}
			}
		}
	}

	@Override
	protected void process_close()
	{
		super.process_close();
		this.buffer.clear();
		synchronized (cache)
		{
			this.cache.clear();
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port)
	{
		predicate.processPunctuation(punctuation);
	}

	@Override
	public long getElementsStored1()
	{
		return cache.size();
	}

	@Override
	public long getElementsStored2()
	{
		return buffer.size();
	}

	@Override
	public void setPredicate(IPredicate<?> predicate)
	{
		// TODO Auto-generated method stub

	}
}
