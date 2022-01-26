package org.wso2.carbon.apimgt.gateway.mediators.opa;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.config.xml.AbstractMediatorFactory;
import org.apache.synapse.config.xml.XMLConfigConstants;

import java.util.Properties;

import javax.xml.namespace.QName;

public class OPAMediatorFactory extends AbstractMediatorFactory {

    static final QName OPA_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "opa");
    static final QName SERVER_URL_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "serverUrl");
    static final QName TOKEN_Q = new QName(XMLConfigConstants.SYNAPSE_NAMESPACE, "token");

    @Override
    protected Mediator createSpecificMediator(OMElement omElement, Properties properties) {
        OPAMediator opaMediator = new OPAMediator();
        processAuditStatus(opaMediator, omElement);

        OMAttribute serverUrl = omElement.getAttribute(SERVER_URL_Q);
        if (serverUrl != null) {
            opaMediator.setOpaServerUrl(serverUrl.getAttributeValue());
        }

        OMAttribute opaToken = omElement.getAttribute(TOKEN_Q);
        if (opaToken != null) {
            opaMediator.setOpaToken(opaToken.getAttributeValue());
        }

        addAllCommentChildrenToList(omElement, opaMediator.getCommentsList());
        return opaMediator;
    }

    @Override
    public QName getTagQName() {

        return OPA_Q;
    }
}
