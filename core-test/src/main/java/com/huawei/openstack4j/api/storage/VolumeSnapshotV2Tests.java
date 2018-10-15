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
package com.huawei.openstack4j.api.storage;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

import com.google.common.collect.Maps;
import com.huawei.openstack4j.api.AbstractTest;
import com.huawei.openstack4j.model.common.ActionResponse;
import com.huawei.openstack4j.openstack.storage.block.domain.Snapshot;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotDetail;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotMeta;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotMetadata;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotRollback;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotUpdate;
import com.huawei.openstack4j.openstack.storage.block.options.SnapshotListOptions;

@Test(suiteName = "EVS/Snapshots", enabled = true)
public class VolumeSnapshotV2Tests extends AbstractTest {

	private static final String JSON_SNAPSHOT_CREATE = "/storage/v2/snapshot_create.json";
	private static final String JSON_SNAPSHOTS = "/storage/v2/snapshots.json";
	private static final String JSON_SNAPSHOT_ROLLBACK = "/storage/v2/snapshot_rollback.json";
	private static final String JSON_SNAPSHOT_UPDATE = "/storage/v2/snapshot_update.json";
	private static final String JSON_SNAPSHOT_METADATA_CREATE = "/storage/v2/snapshot_metadata_create.json";
	private static final String JSON_SNAPSHOT_METADATAS = "/storage/v2/snapshot_metadatas.json";
	private static final String JSON_SNAPSHOT_METADATA_UPDATE = "/storage/v2/snapshot_metadata_update.json";
	private static final String JSON_SNAPSHOT_METADATA = "/storage/v2/snapshot_metadata.json";

	public void create() throws IOException {
		respondWith(JSON_SNAPSHOT_CREATE);

		String volumeId = "5aa119a8-d25b-45a7-8d1b-88e127885635";
		Snapshot snapshot = Snapshot.builder().volumeId(volumeId).build();
		Snapshot create = osv3().blockStorage().snapshots().create(snapshot);

		assertTrue(create != null);
		assertTrue(volumeId.equals(create.getVolumeId()));
	}

	public void snapshots() throws IOException {
		respondWith(JSON_SNAPSHOTS);

		List<? extends SnapshotDetail> snapshots = osv3().blockStorage().snapshots().snapshots();

		assertTrue(snapshots.size() > 0);
		assertTrue("b836dc3d-4e10-4ea4-a34c-8f6b0460a583".equals(snapshots.get(0).getId()));
	}

	public void snapshotsWithOptions() throws IOException {
		respondWith(JSON_SNAPSHOTS);

		SnapshotListOptions options = SnapshotListOptions.create();
		List<? extends SnapshotDetail> snapshots = osv3().blockStorage().snapshots().snapshots(options);

		assertTrue(snapshots.size() > 0);
		assertTrue("b836dc3d-4e10-4ea4-a34c-8f6b0460a583".equals(snapshots.get(0).getId()));
	}

	public void rollback() throws IOException {
		respondWith(JSON_SNAPSHOT_ROLLBACK);

		String snapshotId = "snapshotId";
		String volumeId = "5aa119a8-d25b-45a7-8d1b-88e127885635";
		SnapshotRollback rollback = SnapshotRollback.builder().volumeId(volumeId).build();
		SnapshotRollback resp = osv3().blockStorage().snapshots().rollback(snapshotId, rollback);

		assertTrue(resp != null);
		assertTrue(volumeId.equals(resp.getVolumeId()));
	}

	public void update() throws IOException {
		respondWith(JSON_SNAPSHOT_UPDATE);

		String snapshotId = "f9faf7df-fdc1-4093-9ef3-5cba06eef995";
		String name = "snap-001";
		SnapshotUpdate snapshot = SnapshotUpdate.builder().name(name).build();
		SnapshotDetail update = osv3().blockStorage().snapshots().update(snapshotId, snapshot);

		assertTrue(update != null);
		assertTrue(snapshotId.equals(update.getId()));
	}

	public void createMetadata() throws IOException {
		respondWith(JSON_SNAPSHOT_METADATA_CREATE);

		String snapshotId = "snapshotId";
		Map<String, String> map = Maps.newHashMap();
		map.put("key1", "value1");
		map.put("key2", "value2");
		SnapshotMetadata metadata = SnapshotMetadata.builder().metadata(map).build();
		SnapshotMetadata createMetadata = osv3().blockStorage().snapshots().createMetadata(snapshotId, metadata);

		assertTrue(createMetadata != null);
		assertTrue("value1".equals(createMetadata.getMetadata().get("key1")));
	}

	public void metadata() throws IOException {
		respondWith(JSON_SNAPSHOT_METADATAS);

		String snapshotId = "snapshotId";
		SnapshotMetadata metadata = osv3().blockStorage().snapshots().metadata(snapshotId);

		assertTrue(metadata != null);
		assertTrue("value1".equals(metadata.getMetadata().get("key1")));
	}

	public void updateMetadata() throws IOException {
		respondWith(JSON_SNAPSHOT_METADATA_UPDATE);

		String snapshotId = "snapshotId";
		Map<String, String> map = Maps.newHashMap();
		map.put("key1", "value1");
		map.put("key2", "value2");
		SnapshotMetadata metadata = SnapshotMetadata.builder().metadata(map).build();
		SnapshotMetadata updateMetadata = osv3().blockStorage().snapshots().updateMetadata(snapshotId, metadata);

		assertTrue(updateMetadata != null);
		assertTrue("value1".equals(updateMetadata.getMetadata().get("key1")));
	}

	public void deleteMetadata() {
		respondWith(200);

		String key = "key";
		String snapshotId = "snapshotId";
		ActionResponse resp = osv3().blockStorage().snapshots().deleteMetadata(snapshotId, key);

		assertTrue(resp.isSuccess());
	}

	public void metadataWithKey() throws IOException {
		respondWith(JSON_SNAPSHOT_METADATA);

		String key = "key1";
		String snapshotId = "snapshotId";
		SnapshotMeta metadata = osv3().blockStorage().snapshots().metadata(snapshotId, key);

		assertTrue(metadata != null);
		assertTrue("value1".equals(metadata.getMeta().get(key)));
	}

	public void updateMetadataWithKey() throws IOException {
		respondWith(JSON_SNAPSHOT_METADATA);

		String key = "key1";
		String value = "value1";
		String snapshotId = "snapshotId";
		Map<String, String> map = Maps.newHashMap();
		map.put(key, value);
		SnapshotMeta metadata = SnapshotMeta.builder().meta(map).build();
		SnapshotMeta updateMetadata = osv3().blockStorage().snapshots().updateMetadata(snapshotId, key, metadata);

		assertTrue(updateMetadata != null);
		assertTrue(value.equals(updateMetadata.getMeta().get(key)));
	}

	@Override
	protected Service service() {
		return Service.BLOCK_STORAGE;
	}

}
