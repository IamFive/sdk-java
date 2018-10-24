/*******************************************************************************
 * 	Copyright 2018 Huawei Technologies Co., Ltd.                                          
 * 	                                                                                 
 * 	Licensed under the Apache License, Version 2.0 (the "License"); you may not      
 * 	use this file except in compliance with the License. You may obtain a copy of    
 * 	the License at                                                                   
 * 	                                                                                 
 * 	    http://www.apache.org/licenses/LICENSE-2.0                                   
 * 	                                                                                 
 * 	Unless required by applicable law or agreed to in writing, software              
 * 	distributed under the License is distributed on an "AS IS" BASIS, WITHOUT        
 * 	WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the         
 * 	License for the specific language governing permissions and limitations under    
 * 	the License.                                                                     
 *******************************************************************************/
package com.huawei.openstack4j.functional.storage.block;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.huawei.openstack4j.api.types.ServiceType;
import com.huawei.openstack4j.core.transport.Config;
import com.huawei.openstack4j.functional.AbstractTest;
import com.huawei.openstack4j.openstack.identity.internal.OverridableEndpointURLResolver;
import com.huawei.openstack4j.openstack.storage.block.domain.Version;

public class EVSVersionTest extends AbstractTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(EVSVersionTest.class);

	@BeforeClass
	public void setUp() {
		OverridableEndpointURLResolver endpointResolver = new OverridableEndpointURLResolver();
		endpointResolver.addOverrideEndpoint(ServiceType.BLOCK_STORAGE, "https://evs.cn-north-1.myhuaweicloud.com");
		Config config = Config.newConfig().withEndpointURLResolver(endpointResolver);
		osclient.withConfig(config);
	}

	@Test
	public void testVersions() {
		List<? extends Version> versions = osclient.blockStorage().volumes().versions();
		LOGGER.info("{}", versions);
	}
	
	@Test
	public void testVersionsV2() {
		List<? extends Version> versions = osclient.blockStorage().volumes().versionsV2();
		LOGGER.info("{}", versions);
	}
}
