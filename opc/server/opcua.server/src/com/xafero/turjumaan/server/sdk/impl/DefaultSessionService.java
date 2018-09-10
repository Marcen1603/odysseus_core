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

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.core.UaException;
import com.inductiveautomation.opcua.stack.core.application.services.ServiceRequest;
import com.inductiveautomation.opcua.stack.core.application.services.SessionServiceSet;
import com.inductiveautomation.opcua.stack.core.types.builtin.ByteString;
import com.inductiveautomation.opcua.stack.core.types.builtin.DiagnosticInfo;
import com.inductiveautomation.opcua.stack.core.types.builtin.ExtensionObject;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.structured.ActivateSessionRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.ActivateSessionResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.CancelRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.CancelResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.CloseSessionRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.CloseSessionResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.CreateSessionRequest;
import com.inductiveautomation.opcua.stack.core.types.structured.CreateSessionResponse;
import com.inductiveautomation.opcua.stack.core.types.structured.EndpointDescription;
import com.inductiveautomation.opcua.stack.core.types.structured.ResponseHeader;
import com.inductiveautomation.opcua.stack.core.types.structured.SignatureData;
import com.inductiveautomation.opcua.stack.core.types.structured.SignedSoftwareCertificate;

/**
 * The default session service.
 */
public class DefaultSessionService implements SessionServiceSet {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(DefaultSessionService.class);

	/** The certificate. */
	private final X509Certificate certificate;

	/**
	 * Instantiates a new default session service.
	 *
	 * @param certificate
	 *            the certificate
	 */
	public DefaultSessionService(X509Certificate certificate) {
		this.certificate = certificate;
	}

	@Override
	public void onActivateSession(ServiceRequest<ActivateSessionRequest, ActivateSessionResponse> req)
			throws UaException {
		ExtensionObject userId = req.getRequest().getUserIdentityToken();
		String authType = "<NULL>";
		if (userId != null)
			if (userId.getObject() != null)
				authType = userId.getObject().getClass().getSimpleName();
		log.info("ActivateSession --> {}", authType);
		ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
		ByteString serverNonce = req.getSecureChannel().getLocalNonce();
		StatusCode[] results = new StatusCode[] { StatusCode.GOOD };
		DiagnosticInfo[] diagnosticInfos = null;
		ActivateSessionResponse asr = new ActivateSessionResponse(header, serverNonce, results, diagnosticInfos);
		req.setResponse(asr);
	}

	@Override
	public void onCancel(ServiceRequest<CancelRequest, CancelResponse> req) throws UaException {
		NodeId authToken = req.getRequest().getRequestHeader().getAuthenticationToken();
		log.info("Cancel --> {}", authToken);
		ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
		UInteger cancelCount = UInteger.valueOf(0);
		CancelResponse cr = new CancelResponse(header, cancelCount);
		req.setResponse(cr);
	}

	@Override
	public void onCloseSession(ServiceRequest<CloseSessionRequest, CloseSessionResponse> req) throws UaException {
		Boolean delSubscr = req.getRequest().getDeleteSubscriptions();
		log.info("CloseSession --> {}", delSubscr);
		ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
		CloseSessionResponse csr = new CloseSessionResponse(header);
		req.setResponse(csr);
	}

	@Override
	public void onCreateSession(ServiceRequest<CreateSessionRequest, CreateSessionResponse> req) throws UaException {
		try {
			String sessionName = req.getRequest().getSessionName();
			String endpoint = req.getRequest().getEndpointUrl();
			log.info("CreateSession --> '{}' {}", sessionName, endpoint);
			ResponseHeader header = req.createResponseHeader(StatusCode.GOOD);
			NodeId sessionId = new NodeId(11, (int) req.getSecureChannel().getChannelId());
			NodeId authToken = new NodeId(12, (int) UUID.randomUUID().getLeastSignificantBits());
			Double revisedSessionTimeout = req.getRequest().getRequestedSessionTimeout();
			ByteString serverNonce = req.getSecureChannel().getLocalNonce();
			ByteString serverCertificate = new ByteString(certificate.getEncoded());
			EndpointDescription[] serverEndpoints = req.getServer().getEndpointDescriptions();
			SignedSoftwareCertificate[] serverSoftwareCertificates = req.getServer().getSoftwareCertificates();
			SignatureData serverSignature = new SignatureData(certificate.getSigAlgName(),
					new ByteString(certificate.getSignature()));
			UInteger maxRequestMessageSize = UInteger.valueOf(req.getServer().getChannelConfig().getMaxMessageSize());
			CreateSessionResponse csr = new CreateSessionResponse(header, sessionId, authToken, revisedSessionTimeout,
					serverNonce, serverCertificate, serverEndpoints, serverSoftwareCertificates, serverSignature,
					maxRequestMessageSize);
			req.setResponse(csr);
		} catch (CertificateEncodingException e) {
			throw new RuntimeException(e);
		}
	}
}