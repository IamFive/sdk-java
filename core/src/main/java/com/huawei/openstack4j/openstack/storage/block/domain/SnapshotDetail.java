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
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.huawei.openstack4j.model.ModelEntity;
import com.huawei.openstack4j.openstack.common.ListResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("snapshot")
public class SnapshotDetail implements ModelEntity {

	private static final long serialVersionUID = -8325002507169341310L;

	private String id;

	/**
	 * @see <a href=
	 *      "https://support.huaweicloud.com/en-us/api-evs/en-us_topic_0051803386.html">https://support.huaweicloud.com/en-us/api-evs/en-us_topic_0051803386.html</a>
	 */
	private String status;

	private String name;

	private String description;

	@JsonProperty("created_at")
	private String createdAt;

	@JsonProperty("update_at")
	private String updateAt;

	private Map<String, String> metadata;

	@JsonProperty("volume_id")
	private String volumeId;

	private Integer size;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("os-extended-snapshot-attributes:project_id")
	private String projectId;

	@JsonInclude(Include.NON_NULL)
	@JsonProperty("os-extended-snapshot-attributes:progress")
	private String progress;

	@Getter
	@ToString
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class SnapshotDetails extends ListResult<SnapshotDetail> {

		private static final long serialVersionUID = 6377852399569111975L;

		@JsonProperty("snapshots_links")
		private List<Map<String, String>> snapshotsLinks;

		private List<SnapshotDetail> snapshots;

		@Override
		protected List<SnapshotDetail> value() {
			return snapshots;
		}
	}
}
