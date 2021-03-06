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
package com.huawei.openstack4j.openstack.storage.block.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.huawei.openstack4j.model.ModelEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Volume model
 * 
 * @author bill
 *
 */
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Volume implements ModelEntity {

	private static final long serialVersionUID = -5140670714001601329L;

	/**
	 * List of volume
	 * 
	 * @author bill
	 *
	 */
	@Getter
	@ToString
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Volumes implements ModelEntity {

		private static final long serialVersionUID = 4284061486915503529L;

		private List<Volume> volumes;

		@JsonProperty("volumes_links")
		private List<VolumeLink> volumesLinks;
	}

	/**
	 * The EVS disk ID.
	 */
	private String id;

	/**
	 * The URI of the disk.
	 */
	private List<VolumeLink> links;

	/**
	 * The EVS disk name. The value can contain a maximum of 255 bytes.
	 */
	private String name;
}
