/*
 * ***** BEGIN LICENSE BLOCK *****
 * Zimbra Collaboration Suite Server
 * Copyright (C) 2007, 2008, 2009, 2010 Zimbra, Inc.
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
package com.zimbra.cs.taglib.tag;

import com.zimbra.common.service.ServiceException;
import com.zimbra.client.ZMailbox;
import com.zimbra.cs.taglib.bean.ZTagLibException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;

public class CheckCrumbTag extends ZimbraSimpleTag {

    private String mCrumb;

    public void setCrumb(String crumb) { mCrumb = crumb; }

    public void doTag() throws JspException, IOException {
        try {
            ZMailbox mbox = getMailbox();
            String validCrumb = mbox.getAccountInfo(false).getCrumb();
            if (validCrumb == null || !validCrumb.equals(mCrumb))
                throw ZTagLibException.INVALID_CRUMB("missing valid crumb", null);
        } catch (ServiceException e) {
            throw new JspTagException(e.getMessage(), e);
        }
    }
}
