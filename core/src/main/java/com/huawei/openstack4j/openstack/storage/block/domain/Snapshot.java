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

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.huawei.openstack4j.model.ModelEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Volume snapshot model
 * 
 * @author bill
 *
 */
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("snapshot")
public class Snapshot implements ModelEntity {

	private static final long serialVersionUID = -2566970864787489914L;

	/**
	 * The UUID of the source EVS disk.
	 */
	@JsonProperty("volume_id")
	private String volumeId;

	/**
	 * The flag for forcibly creating a snapshot. The default value is
	 * false.
	 * 
	 * If this parameter is set to false and the disk is in the attaching state, the
	 * snapshot cannot be forcibly created. If this parameter is set to true and the
	 * disk is in the attaching state, the snapshot can be forcibly created.
	 */
	private Boolean force;

	/**
	 * The metadata of the EVS snapshot.
	 */
	private Map<String, String> metadata;

	/**
	 * The description of the EVS snapshot. The value can be null. The
	 * value can contain a maximum of 255 bytes.
	 */
	private String description;

	/**
	 * The name of the EVS snapshot. The value can contain a maximum of
	 * 255 bytes.
	 */
	private String name;
}
