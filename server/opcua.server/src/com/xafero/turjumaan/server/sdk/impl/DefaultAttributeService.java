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
import com.inductiveautomation.opcua.stack.core.application.services.AttributeServiceSet;
import com.inductiveautomation.opcua.stack.core.application.services.ServiceRequest;
import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.DiagnosticInfo;
import com.inductiveautomation.opcua.stack.core.types.builtin.ExtensionObject;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.enumerated.TimestampsToReturn;
import com.inductiveautomation.opcua.stack.core.types.structured.HistoryReadRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.HistoryReadResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.HistoryReadValueId;
import com.inductiveautomation.opcua.stack.core.types.structured.HistoryUpdateRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.HistoryUpdateResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.ReadRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.ReadResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.ReadValueId;
import com.inductiveautomation.opcua.stack.core.types.structured.ResponseHeader;
import com.inductiveautomation.opcua.stack.core.types.structured.WriteRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.WriteResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.WriteValue;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.sdk.api.INodeReader;
import com.xafero.turjumaan.server.sdk.api.INodeWriter;
import com.xafero.turjumaan.server.sdk.util.LogUtils;

/**
 * The default attribute service.
 */
public class DefaultAttributeService implements AttributeServiceSet {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(DefaultAttributeService.class);

	/** The reader. */
	private final INodeReader reader;

	/** The writer. */
	private final INodeWriter writer;

	/**
	 * Instantiates a new default attribute service.
	 *
	 * @param reader
	 *            the reader
	 * @param writer
	 *            the writer
	 */
	public DefaultAttributeService(INodeReader reader, INodeWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}

	@Override
	public void onHistoryRead(ServiceRequest<HistoryReadRequest, HistoryReadResponse> req) throws UaException {
		TimestampsToReturn ttr = req.getRequest().getTimestampsToReturn();
		HistoryReadValueId[] ntr = req.getRequest().getNodesToRead();
		log.info("HistoryRead --> {} {}", LogUtils.toString(ntr), ttr);
	}

	@Override
	public void onHistoryUpdate(ServiceRequest<HistoryUpdateRequest, HistoryUpdateResponse> req) throws UaException {
		ExtensionObject[] hri = req.getRequest().getHistoryUpdateDetails();
		log.info("HistoryUpdate --> {}", LogUtils.toString(hri));
	}

	@Override
	public void onRead(ServiceRequest<ReadRequest, ReadResponse> req) throws UaException {
		Double age = req.getRequest().getMaxAge();
		ReadValueId[] ntr = req.getRequest().getNodesToRead();
		TimestampsToReturn ttr = req.getRequest().getTimestampsToReturn();
		log.info("Read --> {} {} {}", LogUtils.toString(ntr), age, ttr);
		ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
		DataValue[] results = new DataValue[ntr.length];
		for (int i = 0; i < ntr.length; i++) {
			ReadValueId rvi = ntr[i];
			results[i] = readDataValue(rvi);
		}
		DiagnosticInfo[] diagnosticInfos = null;
		ReadResponse rr = new ReadResponse(header, results, diagnosticInfos);
		req.setResponse(rr);
	}

	/**
	 * Read data value.
	 *
	 * @param rvi
	 *            the read value id
	 * @return the data value
	 */
	private DataValue readDataValue(ReadValueId rvi) {
		return readDataValue(rvi.getNodeId(), AttributeIds.findById(rvi.getAttributeId()));
	}

	/**
	 * Read data value.
	 *
	 * @param nodeId
	 *            the node id
	 * @param attrId
	 *            the attribute id
	 * @return the data value
	 */
	private DataValue readDataValue(NodeId nodeId, AttributeIds attrId) {
		return reader.read(nodeId, attrId);
	}

	@Override
	public void onWrite(ServiceRequest<WriteRequest, WriteResponse> req) throws UaException {
		WriteValue[] ntw = req.getRequest().getNodesToWrite();
		log.info("Write --> {}", LogUtils.toString(ntw));
		StatusCode[] results = new StatusCode[ntw.length];
		int idx = 0;
		for (WriteValue wv : ntw) {
			NodeId id = wv.getNodeId();
			AttributeIds attr = AttributeIds.findById(wv.getAttributeId());
			DataValue val = wv.getValue();
			writer.write(id, attr, val);
			results[idx++] = StatusCode.GOOD;
		}
		ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
		DiagnosticInfo[] diagInfos = null;
		WriteResponse wr = new WriteResponse(header, results, diagInfos);
		req.setResponse(wr);
	}
}