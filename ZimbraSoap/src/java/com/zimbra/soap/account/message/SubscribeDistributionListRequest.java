/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2011 Zimbra, Inc.
 *
 * The contents of this file are subject to the Zimbra Public License
 * Version 1.3 ("License"); you may not use this file except in
 * compliance with the License.  You may obtain a copy of the License at
 * http://www.zimbra.com/license.
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied.
 * ***** END LICENSE BLOCK *****
 */
package com.zimbra.soap.account.message;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zimbra.common.soap.AccountConstants;
import com.zimbra.soap.account.type.DistributionListSubscribeOp;
import com.zimbra.soap.type.DistributionListSelector;

/**
 * @zm-api-command-auth-required true
 * @zm-api-command-admin-auth-required false
 * @zm-api-command-description Subscribe to or unsubscribe from a distribution list
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name=AccountConstants.E_SUBSCRIBE_DISTRIBUTION_LIST_REQUEST)
public class SubscribeDistributionListRequest {

    /**
     * @zm-api-field-description The operation to perform.
     * <br />
     * <ul>
     * <li> <b>subscribe</b>  : Subscribe the requested account to the distribution list
     * <li> <b>unsubscribe</b>: Unsubscribe the requested account from the distribution list
     * </ul>
     */
    @XmlAttribute(name=AccountConstants.A_OP, required=true)
    private DistributionListSubscribeOp op;

    /**
     * @zm-api-field-description Selector for the distribution list
     */
    @XmlElement(name=AccountConstants.E_DL, required=true)
    private DistributionListSelector dl;

    public SubscribeDistributionListRequest() {
        this((DistributionListSelector) null, (DistributionListSubscribeOp) null);
    }

    public SubscribeDistributionListRequest(DistributionListSelector dl, DistributionListSubscribeOp op) {
        this.setDl(dl);
        this.setOp(op);
    }

    public void setOp(DistributionListSubscribeOp op) { this.op = op; }
    public DistributionListSubscribeOp getOp() { return op; }

    public void setDl(DistributionListSelector dl) { this.dl = dl; }
    public DistributionListSelector getDl() { return dl; }
}
