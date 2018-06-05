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
package de.uniol.inf.is.odysseus.opcua.common.utilities;

import com.inductiveautomation.opcua.stack.core.types.enumerated.ApplicationType;
import com.inductiveautomation.opcua.stack.core.types.enumerated.AttributeWriteMask;
import com.inductiveautomation.opcua.stack.core.types.enumerated.AxisScaleEnumeration;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseDirection;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseResultMask;
import com.inductiveautomation.opcua.stack.core.types.enumerated.ComplianceLevel;
import com.inductiveautomation.opcua.stack.core.types.enumerated.DataChangeTrigger;
import com.inductiveautomation.opcua.stack.core.types.enumerated.DeadbandType;
import com.inductiveautomation.opcua.stack.core.types.enumerated.EnumeratedTestType;
import com.inductiveautomation.opcua.stack.core.types.enumerated.ExceptionDeviationFormat;
import com.inductiveautomation.opcua.stack.core.types.enumerated.FilterOperator;
import com.inductiveautomation.opcua.stack.core.types.enumerated.HistoryUpdateType;
import com.inductiveautomation.opcua.stack.core.types.enumerated.IdType;
import com.inductiveautomation.opcua.stack.core.types.enumerated.MessageSecurityMode;
import com.inductiveautomation.opcua.stack.core.types.enumerated.ModelChangeStructureVerbMask;
import com.inductiveautomation.opcua.stack.core.types.enumerated.MonitoringMode;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NamingRuleType;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeAttributesMask;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeClass;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeIdType;
import com.inductiveautomation.opcua.stack.core.types.enumerated.OpenFileMode;
import com.inductiveautomation.opcua.stack.core.types.enumerated.PerformUpdateType;
import com.inductiveautomation.opcua.stack.core.types.enumerated.RedundancySupport;
import com.inductiveautomation.opcua.stack.core.types.enumerated.SecurityTokenRequestType;
import com.inductiveautomation.opcua.stack.core.types.enumerated.ServerState;
import com.inductiveautomation.opcua.stack.core.types.enumerated.TimestampsToReturn;
import com.inductiveautomation.opcua.stack.core.types.enumerated.UserTokenType;
import com.inductiveautomation.opcua.stack.core.types.structured.*;

/**
 * A helper to force loading and registering of all OPC UA classes.
 */
public final class RegisterClasses {

	/**
	 * Instantiates a new register classes.
	 */
	private RegisterClasses() {
	}

	/**
	 * Register enums.
	 */
	public static void registerEnums() {
		@SuppressWarnings("unused")
		Object o;
		o = ApplicationType.Client;
		o = AttributeWriteMask.AccessLevel;
		o = AxisScaleEnumeration.Linear;
		o = BrowseDirection.Forward;
		o = BrowseResultMask.All;
		o = ComplianceLevel.Certified;
		o = DataChangeTrigger.Status;
		o = DeadbandType.Percent;
		o = EnumeratedTestType.Green;
		o = ExceptionDeviationFormat.AbsoluteValue;
		o = FilterOperator.Between;
		o = HistoryUpdateType.Delete;
		o = IdType.Guid;
		o = MessageSecurityMode.None;
		o = ModelChangeStructureVerbMask.DataTypeChanged;
		o = MonitoringMode.Reporting;
		o = NamingRuleType.Mandatory;
		o = NodeAttributesMask.All;
		o = NodeClass.Method;
		o = NodeIdType.FourByte;
		o = OpenFileMode.Append;
		o = PerformUpdateType.Remove;
		o = RedundancySupport.Hot;
		o = SecurityTokenRequestType.Issue;
		o = ServerState.CommunicationFault;
		o = TimestampsToReturn.Neither;
		o = UserTokenType.Certificate;
	}

	/**
	 * Register structures.
	 */
	public static void registerStructures() {
		new ActivateSessionRequest();
		new ActivateSessionResponse();
		new AddNodesItem();
		new AddNodesRequest();
		new AddNodesResponse();
		new AddNodesResult();
		new AddReferencesItem();
		new AddReferencesRequest();
		new AddReferencesResponse();
		new AggregateConfiguration();
		new AggregateFilter();
		new AggregateFilterResult();
		new Annotation();
		new AnonymousIdentityToken();
		new ApplicationDescription();
		new Argument();
		new ArrayTestType();
		new AttributeOperand();
		new AxisInformation();
		new BrowseDescription();
		new BrowseNextRequest();
		new BrowseNextResponse();
		new BrowsePath();
		new BrowsePathResult();
		new BrowsePathTarget();
		new BrowseRequest();
		new BrowseResponse();
		new BrowseResult();
		new BuildInfo();
		new CallMethodRequest();
		new CallMethodResult();
		new CallRequest();
		new CallResponse();
		new CancelRequest();
		new CancelResponse();
		new ChannelSecurityToken();
		new CloseSecureChannelRequest();
		new CloseSecureChannelResponse();
		new CloseSessionRequest();
		new CloseSessionResponse();
		new ComplexNumberType();
		new CompositeTestType();
		new ContentFilter();
		new ContentFilterElement();
		new ContentFilterElementResult();
		new ContentFilterResult();
		new CreateMonitoredItemsRequest();
		new CreateMonitoredItemsResponse();
		new CreateSessionRequest();
		new CreateSessionResponse();
		new CreateSubscriptionRequest();
		new CreateSubscriptionResponse();
		new DataChangeFilter();
		new DataChangeNotification();
		new DataTypeAttributes();
		new DataTypeNode();
		new DeleteAtTimeDetails();
		new DeleteEventDetails();
		new DeleteMonitoredItemsRequest();
		new DeleteMonitoredItemsResponse();
		new DeleteNodesItem();
		new DeleteNodesRequest();
		new DeleteNodesResponse();
		new DeleteRawModifiedDetails();
		new DeleteReferencesItem();
		new DeleteReferencesRequest();
		new DeleteReferencesResponse();
		new DeleteSubscriptionsRequest();
		new DeleteSubscriptionsResponse();
		new DoubleComplexNumberType();
		new ElementOperand();
		new EndpointConfiguration();
		new EndpointDescription();
		new EndpointUrlListDataType();
		new EnumValueType();
		new EUInformation();
		new EventFieldList();
		new EventFilter();
		new EventFilterResult();
		new EventNotificationList();
		new FilterOperand();
		new FindServersRequest();
		new FindServersResponse();
		new GetEndpointsRequest();
		new GetEndpointsResponse();
		new HistoryData();
		new HistoryEvent();
		new HistoryEventFieldList();
		new HistoryModifiedData();
		new HistoryReadDetails();
		new HistoryReadRequest();
		new HistoryReadResponse();
		new HistoryReadResult();
		new HistoryReadValueId();
		new HistoryUpdateDetails();
		new HistoryUpdateEventResult();
		new HistoryUpdateRequest();
		new HistoryUpdateResponse();
		new HistoryUpdateResult();
		new InstanceNode();
		new IssuedIdentityToken();
		new LiteralOperand();
		new MethodAttributes();
		new MethodNode();
		new ModelChangeStructureDataType();
		new ModificationInfo();
		new ModifyMonitoredItemsRequest();
		new ModifyMonitoredItemsResponse();
		new ModifySubscriptionRequest();
		new ModifySubscriptionResponse();
		new MonitoredItemCreateRequest();
		new MonitoredItemCreateResult();
		new MonitoredItemModifyRequest();
		new MonitoredItemModifyResult();
		new MonitoredItemNotification();
		new MonitoringFilter();
		new MonitoringFilterResult();
		new MonitoringParameters();
		new NetworkGroupDataType();
		new Node();
		new NodeAttributes();
		new NodeReference();
		new NodeTypeDescription();
		new NotificationData();
		new NotificationMessage();
		new ObjectAttributes();
		new ObjectNode();
		new ObjectTypeAttributes();
		new ObjectTypeNode();
		new OpenSecureChannelRequest();
		new OpenSecureChannelResponse();
		new ParsingResult();
		new ProgramDiagnosticDataType();
		new PublishRequest();
		new PublishResponse();
		new QueryDataDescription();
		new QueryDataSet();
		new QueryFirstRequest();
		new QueryFirstResponse();
		new QueryNextRequest();
		new QueryNextResponse();
		new Range();
		new ReadAtTimeDetails();
		new ReadEventDetails();
		new ReadProcessedDetails();
		new ReadRawModifiedDetails();
		new ReadRequest();
		new ReadResponse();
		new ReadValueId();
		new RedundantServerDataType();
		new ReferenceDescription();
		new ReferenceNode();
		new ReferenceTypeAttributes();
		new ReferenceTypeNode();
		new RegisterClasses();
		new RegisteredServer();
		new RegisterNodesRequest();
		new RegisterNodesResponse();
		new RegisterServerRequest();
		new RegisterServerResponse();
		new RelativePath();
		new RelativePathElement();
		new RepublishRequest();
		new RepublishResponse();
		new RequestHeader();
		new ResponseHeader();
		new SamplingIntervalDiagnosticsDataType();
		new ScalarTestType();
		new SemanticChangeStructureDataType();
		new ServerDiagnosticsSummaryDataType();
		new ServerStatusDataType();
		new ServiceCounterDataType();
		new ServiceFault();
		new SessionDiagnosticsDataType();
		new SessionSecurityDiagnosticsDataType();
		new SetMonitoringModeRequest();
		new SetMonitoringModeResponse();
		new SetPublishingModeRequest();
		new SetPublishingModeResponse();
		new SetTriggeringRequest();
		new SetTriggeringResponse();
		new SignatureData();
		new SignedSoftwareCertificate();
		new SimpleAttributeOperand();
		new SoftwareCertificate();
		new StatusChangeNotification();
		new StatusResult();
		new SubscriptionAcknowledgement();
		new SubscriptionDiagnosticsDataType();
		new SupportedProfile();
		new TestStackExRequest();
		new TestStackExResponse();
		new TestStackRequest();
		new TestStackResponse();
		new TimeZoneDataType();
		new TransferResult();
		new TransferSubscriptionsRequest();
		new TransferSubscriptionsResponse();
		new TranslateBrowsePathsToNodeIdsRequest();
		new TranslateBrowsePathsToNodeIdsResponse();
		new TypeNode();
		new UnregisterNodesRequest();
		new UnregisterNodesResponse();
		new UpdateDataDetails();
		new UpdateEventDetails();
		new UpdateStructureDataDetails();
		new UserIdentityToken();
		new UserNameIdentityToken();
		new UserTokenPolicy();
		new VariableAttributes();
		new VariableNode();
		new VariableTypeAttributes();
		new VariableTypeNode();
		new ViewAttributes();
		new ViewDescription();
		new ViewNode();
		new WriteRequest();
		new WriteResponse();
		new WriteValue();
		new X509IdentityToken();
		new XVType();
	}
}