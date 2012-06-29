// Copyright 2012 Citrix Systems, Inc. Licensed under the
// Apache License, Version 2.0 (the "License"); you may not use this
// file except in compliance with the License.  Citrix Systems, Inc.
// reserves all rights not expressly granted by the License.
// You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// 
// Automatically generated by addcopyright.py at 04/03/2012
package com.xensource.xenapi;

import com.xensource.xenapi.Types.BadServerResponse;
import com.xensource.xenapi.Types.VersionException;
import com.xensource.xenapi.Types.XenAPIException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.xmlrpc.XmlRpcException;

/**
 * Management of remote authentication services
 *
 * @author Citrix Systems, Inc.
 */
public class Auth extends XenAPIObject {


    public String toWireString() {
        return null;
    }

    /**
     * This call queries the external directory service to obtain the subject_identifier as a string from the human-readable subject_name
     *
     * @param subjectName The human-readable subject_name, such as a username or a groupname
     * @return the subject_identifier obtained from the external directory service
     */
    public static String getSubjectIdentifier(Connection c, String subjectName) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "auth.get_subject_identifier";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(subjectName)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toString(result);
    }

    /**
     * This call queries the external directory service to obtain the user information (e.g. username, organization etc) from the specified subject_identifier
     *
     * @param subjectIdentifier A string containing the subject_identifier, unique in the external directory service
     * @return key-value pairs containing at least a key called subject_name
     */
    public static Map<String, String> getSubjectInformationFromIdentifier(Connection c, String subjectIdentifier) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "auth.get_subject_information_from_identifier";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(subjectIdentifier)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toMapOfStringString(result);
    }

    /**
     * This calls queries the external directory service to obtain the transitively-closed set of groups that the the subject_identifier is member of.
     *
     * @param subjectIdentifier A string containing the subject_identifier, unique in the external directory service
     * @return set of subject_identifiers that provides the group membership of subject_identifier passed as argument, it contains, recursively, all groups a subject_identifier is member of.
     */
    public static Set<String> getGroupMembership(Connection c, String subjectIdentifier) throws
       BadServerResponse,
       XenAPIException,
       XmlRpcException {
        String method_call = "auth.get_group_membership";
        String session = c.getSessionReference();
        Object[] method_params = {Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(subjectIdentifier)};
        Map response = c.dispatch(method_call, method_params);
        Object result = response.get("Value");
            return Types.toSetOfString(result);
    }

}