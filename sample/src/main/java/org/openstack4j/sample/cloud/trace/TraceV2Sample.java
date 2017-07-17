/*******************************************************************************
 *  Copyright 2017 Huawei TLD
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
/*******************************************************************************
 *******************************************************************************/
package org.openstack4j.sample.cloud.trace;

import java.util.List;

import org.openstack4j.openstack.cloud.trace.v2.domain.Trace;
import org.openstack4j.openstack.cloud.trace.v2.options.TraceListOptions;
import org.openstack4j.sample.AbstractSample;
import org.testng.annotations.Test;

@Test(suiteName = "CloudTrace/TraceV2/Sample")
public class TraceV2Sample extends AbstractSample {


	@Test(priority = 1)
	public void testListTrace() {
		// 德电环境似乎没有部署这个接口
		TraceListOptions options = TraceListOptions.create().limit(5).user("zhangdong").serviceType("CTS");
		List<Trace> list = osclient.cloudTraceV2().traces().list("system", options);
		
		if (list.size() > 0) {
			Trace trace = list.get(list.size() -1);
			options.marker(trace.getId());
			List<Trace> list2 = osclient.cloudTraceV2().traces().list("system", options);
		}
	}


}
