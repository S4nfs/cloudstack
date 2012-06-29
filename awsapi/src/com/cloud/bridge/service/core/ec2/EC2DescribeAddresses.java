// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.
/*
 * Copyright (C) 2011 Citrix Systems, Inc.  All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloud.bridge.service.core.ec2;

import java.util.HashSet;

public class EC2DescribeAddresses {
	private EC2AddressFilterSet filterSet;
	private HashSet<String> publicIpsSet = new HashSet<String>();

	public EC2DescribeAddresses() {
	}

	public void addPublicIp( String param ) {
		publicIpsSet.add( param );
	}
	
	/*
	 * inPublicIpSet will return true if the ip is in the set, or if set length is 0
	 */
	public boolean inPublicIpSet(String ip) {
		if (publicIpsSet.isEmpty()) 
			 return true;
		return publicIpsSet.contains(ip);
	}
	
	// probably don't need this silly function...
	public String[] getPublicIpsSet() {
		return publicIpsSet.toArray(new String[0]);
	}

	/**
	 * @return the keyFilterSet
	 */
	public EC2AddressFilterSet getFilterSet() {
		return filterSet;
	}

	/**
	 * @param keyFilterSet the keyFilterSet to set
	 */
	public void setFilterSet(EC2AddressFilterSet keyFilterSet) {
		this.filterSet = keyFilterSet;
	}
}
