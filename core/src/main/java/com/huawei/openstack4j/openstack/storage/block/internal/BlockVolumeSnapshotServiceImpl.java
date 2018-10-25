/*******************************************************************************
 * 	Copyright 2016 ContainX and OpenStack4j                                          
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
package com.huawei.openstack4j.openstack.storage.block.internal;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.huawei.openstack4j.api.Builders;
import com.huawei.openstack4j.api.storage.BlockVolumeSnapshotService;
import com.huawei.openstack4j.model.common.ActionResponse;
import com.huawei.openstack4j.model.storage.block.VolumeSnapshot;
import com.huawei.openstack4j.openstack.storage.block.domain.CinderVolumeSnapshot;
import com.huawei.openstack4j.openstack.storage.block.domain.CinderVolumeSnapshot.VolumeSnapshots;
import com.huawei.openstack4j.openstack.storage.block.domain.Snapshot;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotDetail.SnapshotDetails;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotMeta;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotMetadata;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotRollback;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotUpdate;
import com.huawei.openstack4j.openstack.storage.block.options.SnapshotListOptions;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotDetail;

/**
 * OpenStack (Cinder) Volume Snapshot Operations API Implementation.
 *
 * @author Jeremy Unruh
 */
public class BlockVolumeSnapshotServiceImpl extends BaseBlockStorageServices implements BlockVolumeSnapshotService {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<? extends VolumeSnapshot> list() {
		return get(VolumeSnapshots.class, uri("/snapshots")).execute().getList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<? extends VolumeSnapshot> list(Map<String, String> filteringParams) {
		Invocation<VolumeSnapshots> volumeInvocation = buildInvocation(filteringParams);
		return volumeInvocation.execute().getList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VolumeSnapshot get(String snapshotId) {
		checkNotNull(snapshotId);
		return get(CinderVolumeSnapshot.class, uri("/snapshots/%s", snapshotId)).execute();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActionResponse delete(String snapshotId) {
		checkNotNull(snapshotId);
		return deleteWithResponse(uri("/snapshots/%s", snapshotId)).execute();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ActionResponse update(String snapshotId, String name, String description) {
		checkNotNull(snapshotId);
		if (name == null && description == null)
			return ActionResponse.actionFailed("Both Name and Description are required", 412);

		return put(ActionResponse.class, uri("/snapshots/%s", snapshotId))
				.entity(Builders.volumeSnapshot().name(name).description(description).build()).execute();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VolumeSnapshot create(VolumeSnapshot snapshot) {
		checkNotNull(snapshot);
		checkNotNull(snapshot.getVolumeId());
		return post(CinderVolumeSnapshot.class, uri("/snapshots")).entity(snapshot).execute();
	}

	private Invocation<VolumeSnapshots> buildInvocation(Map<String, String> filteringParams) {
		Invocation<VolumeSnapshots> volumeInvocation = get(VolumeSnapshots.class, "/snapshots");
		if (filteringParams == null) {
			return volumeInvocation;
		} else {
			for (Map.Entry<String, String> entry : filteringParams.entrySet()) {
				volumeInvocation = volumeInvocation.param(entry.getKey(), entry.getValue());
			}
		}
		return volumeInvocation;
	}

	@Override
	public List<? extends SnapshotDetail> snapshots() {
		return get(SnapshotDetails.class, "/snapshots/detail").execute().getList();
	}

	@Override
	public List<? extends SnapshotDetail> snapshots(SnapshotListOptions options) {
		checkNotNull(options, "`options` is required");
		return get(SnapshotDetails.class, "/snapshots/detail").params(options.getOptions()).execute().getList();
	}

	@Override
	public Snapshot create(Snapshot snapshot) {
		checkNotNull(snapshot, "`snapshot` is required");
		checkArgument(!Strings.isNullOrEmpty(snapshot.getVolumeId()), "`volumeId` is required");
		return post(Snapshot.class, "/snapshots").entity(snapshot).execute();
	}

	@Override
	public SnapshotRollback rollback(String snapshotId, SnapshotRollback rollback) {
		checkArgument(!Strings.isNullOrEmpty(snapshotId), "`snapshotId` should not be empty");
		checkNotNull(rollback, "`rollback` is required");
		return post(SnapshotRollback.class, uri("/os-vendor-snapshots/%s/rollback", snapshotId)).entity(rollback)
				.execute();
	}

	@Override
	public SnapshotDetail update(String snapshotId, SnapshotUpdate snapshot) {
		checkArgument(!Strings.isNullOrEmpty(snapshotId), "`snapshotId` should not be empty");
		checkNotNull(snapshot, "`snapshot` is required");
		return put(SnapshotDetail.class, uri("/snapshots/%s", snapshotId)).entity(snapshot).execute();
	}

	@Override
	public SnapshotMetadata createMetadata(String snapshotId, SnapshotMetadata metadata) {
		checkArgument(!Strings.isNullOrEmpty(snapshotId), "`snapshotId` should not be empty");
		checkNotNull(metadata, "`metadata` is required");
		return post(SnapshotMetadata.class, uri("/snapshots/%s/metadata", snapshotId)).entity(metadata).execute();
	}

	@Override
	public SnapshotMetadata metadata(String snapshotId) {
		checkArgument(!Strings.isNullOrEmpty(snapshotId), "`snapshotId` should not be empty");
		return get(SnapshotMetadata.class, uri("/snapshots/%s/metadata", snapshotId)).execute();
	}

	@Override
	public SnapshotMetadata updateMetadata(String snapshotId, SnapshotMetadata metadata) {
		checkArgument(!Strings.isNullOrEmpty(snapshotId), "`snapshotId` should not be empty");
		checkNotNull(metadata, "`metadata` is required");
		return put(SnapshotMetadata.class, uri("/snapshots/%s/metadata", snapshotId)).entity(metadata).execute();
	}

	@Override
	public ActionResponse deleteMetadata(String snapshotId, String key) {
		checkArgument(!Strings.isNullOrEmpty(snapshotId), "`snapshotId` should not be empty");
		checkArgument(!Strings.isNullOrEmpty(key), "`key` should not be empty");
		return deleteWithResponse(uri("/snapshots/%s/metadata/%s", snapshotId, key)).execute();
	}

	@Override
	public SnapshotMeta metadata(String snapshotId, String key) {
		checkArgument(!Strings.isNullOrEmpty(snapshotId), "`snapshotId` should not be empty");
		checkArgument(!Strings.isNullOrEmpty(key), "`key` should not be empty");
		return get(SnapshotMeta.class, uri("/snapshots/%s/metadata/%s", snapshotId, key)).execute();
	}

	@Override
	public SnapshotMeta updateMetadata(String snapshotId, String key, SnapshotMeta metadata) {
		checkArgument(!Strings.isNullOrEmpty(snapshotId), "`snapshotId` should not be empty");
		checkArgument(!Strings.isNullOrEmpty(key), "`key` should not be empty");
		checkNotNull(metadata, "`metadata` is required");
		return put(SnapshotMeta.class, uri("/snapshots/%s/metadata/%s", snapshotId, key)).entity(metadata).execute();
	}

}
