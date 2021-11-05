/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.carbon.apimgt.gateway.inbound.websocket.response;

import org.json.JSONObject;
import org.wso2.carbon.apimgt.gateway.handlers.graphQL.GraphQLConstants;
import org.wso2.carbon.apimgt.gateway.inbound.InboundMessageContext;
import org.wso2.carbon.apimgt.gateway.dto.GraphQLOperationDTO;
import org.wso2.carbon.apimgt.gateway.inbound.websocket.InboundProcessorResponseDTO;
import org.wso2.carbon.apimgt.gateway.inbound.websocket.utils.InboundWebsocketProcessorUtil;

/**
 * A GraphQL subscriptions specific extension of ResponseProcessor. This class intercepts the inbound websocket
 * execution path of graphQL subscription responses (subscribe messages).
 */
public class GraphQLResponseProcessor extends ResponseProcessor {

    /**
     * Handle inbound websocket responses of GraphQL subscriptions and perform authentication, authorization
     * and throttling. This identifies operation from the subscription responses using the unique message id parameter.
     *
     * @param msgSize               Message size of graphQL subscription response payload
     * @param msgText               The GraphQL subscription response payload text
     * @param inboundMessageContext InboundMessageContext
     * @return InboundProcessorResponseDTO
     */
    @Override
    public InboundProcessorResponseDTO handleResponse(int msgSize, String msgText,
                                                      InboundMessageContext inboundMessageContext) {

        InboundProcessorResponseDTO responseDTO =
                InboundWebsocketProcessorUtil.authenticateToken(inboundMessageContext);
        JSONObject graphQLMsg = new JSONObject(msgText);
        if (checkIfSubscribeMessageResponse(graphQLMsg)) {
            if (graphQLMsg.getString(GraphQLConstants.SubscriptionConstants.PAYLOAD_FIELD_NAME_ID) != null) {
                GraphQLOperationDTO graphQLOperationDTO =
                        inboundMessageContext.getVerbInfoForGraphQLMsgId(
                                graphQLMsg.getString(GraphQLConstants.SubscriptionConstants.PAYLOAD_FIELD_NAME_ID));
                // validate scopes based on subscription payload
                responseDTO = InboundWebsocketProcessorUtil
                        .validateScopes(inboundMessageContext, graphQLOperationDTO.getOperation());
                if (!responseDTO.isError()) {
                    //throttle for matching resource
                    return InboundWebsocketProcessorUtil.doThrottle(msgSize, graphQLOperationDTO.getVerbInfoDTO(),
                            inboundMessageContext);
                }
            } else {
                responseDTO = InboundWebsocketProcessorUtil
                        .getBadRequestFrameErrorDTO("Missing mandatory id field in the message");
            }
        }
        return responseDTO;
    }

    /**
     * Check if messages is valid subscription operation execution result. Payload should consist 'type' field and its
     * value equal to either of 'data' or 'next'. The value 'data' is used in 'subscriptions-transport-ws'
     * protocol and 'next' is used in 'graphql-ws' protocol.
     *
     * @param graphQLMsg GraphQL message
     * @return true if valid operation
     */
    private boolean checkIfSubscribeMessageResponse(JSONObject graphQLMsg) {
        return graphQLMsg.getString(GraphQLConstants.SubscriptionConstants.PAYLOAD_FIELD_NAME_TYPE) != null
                && GraphQLConstants.SubscriptionConstants.PAYLOAD_FIELD_NAME_ARRAY_FOR_DATA.contains(
                graphQLMsg.getString(GraphQLConstants.SubscriptionConstants.PAYLOAD_FIELD_NAME_TYPE));
    }
}
