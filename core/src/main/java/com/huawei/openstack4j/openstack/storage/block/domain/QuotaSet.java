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

/**
 * Volume quota set model
 * 
 * @author bill
 *
 */
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName("quota_set")
public class QuotaSet implements ModelEntity {

	private static final long serialVersionUID = -5125142937410327024L;

	/**
	 * The tenant ID.
	 */
	private String id;

	/**
	 * The size (GB) reserved for SAS volume. This parameter is made up of key-value
	 * pairs which include reserved, limit, and in_use.
	 */
	@JsonProperty("gigabytes_SAS")
	private Detail gigabytesSAS;

	/**
	 * The number of EVS disks reserved for SATA volume. This parameter is made up
	 * of key-value pairs which include reserved, limit, and in_use.
	 */
	@JsonProperty("volumes_SATA")
	private Detail volumesSATA;

	/**
	 * The total size (GB) of EVS disks and snapshots allowed by the quota. This
	 * parameter is made up of key-value pairs which include reserved, limit, and
	 * in_use.
	 */
	private Detail gigabytes;

	/**
	 * The backup size (GB). This parameter is made up of key-value pairs which
	 * include reserved, limit, and in_use.
	 */
	@JsonProperty("backup_gigabytes")
	private Detail backupGigabytes;

	/**
	 * The number of snapshots reserved for SAS volume. This parameter is made up of
	 * key-value pairs which include reserved, limit, and in_use.
	 */
	@JsonProperty("snapshots_SAS")
	private Detail snapshotsSAS;

	/**
	 * The number of EVS disks reserved for SSD volume. This parameter is made up of
	 * key-value pairs which include reserved, limit, and in_use.
	 */
	@JsonProperty("volumes_SSD")
	private Detail volumesSSD;

	/**
	 * The number of snapshots. This parameter is made up of key-value
	 * pairs which include reserved, limit, and in_use.
	 */
	private Detail snapshots;

	/**
	 * The number of EVS disks reserved for SAS volume. This parameter is made up of
	 * key-value pairs which include reserved, limit, and in_use.
	 */
	@JsonProperty("volumes_SAS")
	private Detail volumesSAS;

	/**
	 * The number of snapshots reserved for SSD volume. This parameter is made up of
	 * key-value pairs which include reserved, limit, and in_use.
	 */
	@JsonProperty("snapshots_SSD")
	private Detail snapshotsSSD;

	/**
	 * The number of EVS disks. This parameter is made up of key-value
	 * pairs which include reserved, limit, and in_use.
	 */
	private Detail volumes;

	/**
	 * The size (GB) reserved for SATA volume. This parameter is made up of
	 * key-value pairs which include reserved, limit, and in_use.
	 */
	@JsonProperty("gigabytes_SATA")
	private Detail gigabytesSATA;

	/**
	 * The number of backups. This parameter is made up of key-value pairs
	 * which include reserved, limit, and in_use.
	 */
	private Detail backups;

	/**
	 * The size (GB) reserved for SSD volume. This parameter is made up of key-value
	 * pairs which include reserved, limit, and in_use.
	 */
	@JsonProperty("gigabytes_SSD")
	private Detail gigabytesSSD;

	/**
	 * The number of snapshots reserved for SATA volume. This parameter is made up
	 * of key-value pairs which include reserved, limit, and in_use.
	 */
	@JsonProperty("snapshots_SATA")
	private Detail snapshotsSATA;

	@Getter
	@ToString
	@NoArgsConstructor
	@AllArgsConstructor
	public class Detail {
		/**
		 * Reserved size
		 */
		private Integer reserved;

		/**
		 * Limit size
		 */
		private Integer limit;

		/**
		 * In-use size
		 */
		@JsonProperty("in_use")
		private Integer inUse;
	}
}
