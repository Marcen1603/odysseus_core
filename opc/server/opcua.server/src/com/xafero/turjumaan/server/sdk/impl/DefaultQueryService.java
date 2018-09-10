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
package com.xafero.turjumaan.server.sdk.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.core.UaException;
import com.inductiveautomation.opcua.stack.core.application.services.QueryServiceSet;
import com.inductiveautomation.opcua.stack.core.application.services.ServiceRequest;
import com.inductiveautomation.opcua.stack.core.types.builtin.ByteString;
import com.inductiveautomation.opcua.stack.core.types.structured.ContentFilter;
import com.inductiveautomation.opcua.stack.core.types.structured.QueryFirstRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.QueryFirstResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.QueryNextRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.QueryNextResponse;

/**
 * The default query service.
 */
public class DefaultQueryService implements QueryServiceSet {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(DefaultQueryService.class);

	@Override
	public void onQueryFirst(ServiceRequest<QueryFirstRequest, QueryFirstResponse> req) throws UaException {
		ContentFilter filter = req.getRequest().getFilter();
		log.info("QueryFirst --> {}", filter);
	}

	@Override
	public void onQueryNext(ServiceRequest<QueryNextRequest, QueryNextResponse> req) throws UaException {
		ByteString cp = req.getRequest().getContinuationPoint();
		log.info("QueryNext --> {}", cp);
	}
}