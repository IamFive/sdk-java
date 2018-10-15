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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.huawei.openstack4j.model.ModelEntity;

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
@JsonRootName("quota_set")
public class QuotaSet implements ModelEntity {

	private static final long serialVersionUID = -5125142937410327024L;

	private String id;

	@JsonProperty("gigabytes_SAS")
	private Detail gigabytesSAS;

	@JsonProperty("volumes_SATA")
	private Detail volumesSATA;

	private Detail gigabytes;

	@JsonProperty("backup_gigabytes")
	private Detail backupGigabytes;

	@JsonProperty("snapshots_SAS")
	private Detail snapshotsSAS;

	@JsonProperty("volumes_SSD")
	private Detail volumesSSD;

	private Detail snapshots;

	@JsonProperty("volumes_SAS")
	private Detail volumesSAS;

	@JsonProperty("snapshots_SSD")
	private Detail snapshotsSSD;

	private Detail volumes;

	@JsonProperty("gigabytes_SATA")
	private Detail gigabytesSATA;

	private Detail backups;

	@JsonProperty("gigabytes_SSD")
	private Detail gigabytesSSD;

	@JsonProperty("snapshots_SATA")
	private Detail snapshotsSATA;

	@Getter
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class Detail {
		private Integer reserved;
		private Integer limit;
		@JsonProperty("in_use")
		private Integer inUse;
	}
}
