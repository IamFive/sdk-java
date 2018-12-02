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

import static org.testng.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.huawei.openstack4j.api.Builders;
import com.huawei.openstack4j.functional.AbstractTest;
import com.huawei.openstack4j.functional.Retry;
import com.huawei.openstack4j.model.common.ActionResponse;
import com.huawei.openstack4j.model.storage.block.Volume;
import com.huawei.openstack4j.model.storage.block.Volume.Status;
import com.huawei.openstack4j.model.storage.block.VolumeSnapshot;
import com.huawei.openstack4j.openstack.storage.block.domain.AvailabilityZone;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotDetail;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotMeta;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotMetadata;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotRollback;
import com.huawei.openstack4j.openstack.storage.block.domain.SnapshotUpdate;
import com.huawei.openstack4j.openstack.storage.block.options.SnapshotListOptions;

public class EVSSnapshotTest extends AbstractTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(EVSSnapshotTest.class);

	private static String volumeId;
	private static String snapshotId;
	private static final String META_KEY = "key1";
	private static final String META_VAL = "value1";
	private static final String SNAPSHOT_NAME = "SNAPSHOT-NAME";

	@BeforeClass
	public void setUp() {
		List<? extends AvailabilityZone> zones = osclient.blockStorage().zones().list();
		AvailabilityZone zone = null;
		for (AvailabilityZone az : zones) {
			if (az.getZoneState().getAvailable())
				zone = az;
		}
		assertTrue(zone != null && !Strings.isNullOrEmpty(zone.getZoneName()), "No usable availability zone");

		Volume volume = Builders.volume().zone(zone.getZoneName()).size(40).build();
		Volume create = osclient.blockStorage().volumes().create(volume);
		waitVolumeAvailable(create.getId());
		volumeId = create.getId();

		VolumeSnapshot snapshot = Builders.volumeSnapshot().volume(volumeId).name(SNAPSHOT_NAME).build();
		VolumeSnapshot createSnapshot = osclient.blockStorage().snapshots().create(snapshot);
		waitSnapshotAvailable(createSnapshot.getId());
		snapshotId = createSnapshot.getId();

		Map<String, String> map = Maps.newHashMap();
		map.put(META_KEY, META_VAL);
		SnapshotMetadata metadata = SnapshotMetadata.builder().metadata(map).build();
		SnapshotMetadata createMetadata = osclient.blockStorage().snapshots().createMetadata(snapshotId, metadata);
		assertTrue(META_VAL.equals(createMetadata.getMetadata().get(META_KEY)));
	}

	@AfterClass
	public void teardown() {
		waitVolumeAvailable(volumeId);
		waitSnapshotAvailable(snapshotId);
		ActionResponse resp = osclient.blockStorage().snapshots().deleteMetadata(snapshotId, META_KEY);
		ActionResponse resp2 = osclient.blockStorage().snapshots().delete(snapshotId);
		waitSnapshotDelete(snapshotId);
		ActionResponse resp3 = osclient.blockStorage().volumes().delete(volumeId);

		assertTrue(resp.isSuccess());
		assertTrue(resp2.isSuccess());
		assertTrue(resp3.isSuccess());
	}

	@Test
	public void testSnapshots() {
		List<? extends SnapshotDetail> snapshots = osclient.blockStorage().snapshots().snapshots();
		LOGGER.info("{}", snapshots);

		SnapshotListOptions options = SnapshotListOptions.create().limit(10);
		List<? extends SnapshotDetail> snapshots2 = osclient.blockStorage().snapshots().snapshots(options);
		LOGGER.info("{}", snapshots2);
	}

	@Test
	public void testUpdateSnapshot() {
		String newName = "new name";
		SnapshotUpdate snapshot = SnapshotUpdate.builder().name(newName).build();
		SnapshotDetail update = osclient.blockStorage().snapshots().update(snapshotId, snapshot);
		assertTrue(newName.equals(update.getName()));
	}

	@Test
	public void testRollbackSnapshot() {
		SnapshotRollback rollback = SnapshotRollback.builder().volumeId(volumeId).build();
		SnapshotRollback rollback2 = osclient.blockStorage().snapshots().rollback(snapshotId, rollback);
		assertTrue(volumeId.equals(rollback2.getVolumeId()));
	}

	@Test
	public void testGetMetadata() {
		SnapshotMetadata metadata = osclient.blockStorage().snapshots().metadata(snapshotId);
		assertTrue(META_VAL.equals(metadata.getMetadata().get(META_KEY)));

		SnapshotMeta metadata2 = osclient.blockStorage().snapshots().metadata(snapshotId, META_KEY);
		assertTrue(META_VAL.equals(metadata2.getMeta().get(META_KEY)));
	}

	@Test
	public void testUpdateMetadata() {
		String newVal = "new value";
		Map<String, String> map = Maps.newHashMap();
		map.put(META_KEY, newVal);
		SnapshotMetadata metadata = SnapshotMetadata.builder().metadata(map).build();
		SnapshotMetadata updateMetadata = osclient.blockStorage().snapshots().updateMetadata(snapshotId, metadata);
		assertTrue(newVal.equals(updateMetadata.getMetadata().get(META_KEY)));

		map.put(META_KEY, META_VAL);
		SnapshotMeta meta = SnapshotMeta.builder().meta(map).build();
		SnapshotMeta updateMetadata2 = osclient.blockStorage().snapshots().updateMetadata(snapshotId, META_KEY, meta);
		assertTrue(META_VAL.equals(updateMetadata2.getMeta().get(META_KEY)));
	}

	private void waitVolumeAvailable(String volumeId) {
		Retry retry = new Retry() {

			@Override
			public Object run() {
				LOGGER.info("-------------------- WAIT FOR VOLUME[{}] AVAILABLE--------------------", volumeId);
				Volume volume = osclient.blockStorage().volumes().get(volumeId);
				if (volume != null && Status.AVAILABLE.equals(volume.getStatus()))
					return volume.getStatus();
				else
					return null;
			}

			@Override
			public Integer maxRetryTimes() {
				return 12;
			}

			@Override
			public Integer delay() {
				return 5;
			}
		};

		Status volumeStatus = (Status) this.retry(retry);
		if (volumeStatus == null || !Status.AVAILABLE.equals(volumeStatus)) {
			throw new RuntimeException(String.format("[%s]Volume is not available", volumeId));
		}
	}

	private void waitSnapshotAvailable(String snapshotId) {
		Retry retry = new Retry() {

			@Override
			public Object run() {
				LOGGER.info("-------------------- WAIT FOR SNAPSHOT[{}] AVAILABLE--------------------", snapshotId);
				VolumeSnapshot snapshot = osclient.blockStorage().snapshots().get(snapshotId);
				if (snapshot != null && Status.AVAILABLE.equals(snapshot.getStatus()))
					return snapshot.getStatus();
				else
					return null;
			}

			@Override
			public Integer maxRetryTimes() {
				return 12;
			}

			@Override
			public Integer delay() {
				return 5;
			}
		};

		Status status = (Status) this.retry(retry);
		if (status == null || !Status.AVAILABLE.equals(status)) {
			throw new RuntimeException(String.format("[%s]Snapshot is not available", snapshotId));
		}
	}

	private void waitSnapshotDelete(String snapshotId) {
		Retry retry = new Retry() {
			@Override
			public Object run() {
				LOGGER.info("-------------------- WAIT FOR SNAPSHOT[{}] DELETE--------------------", snapshotId);
				VolumeSnapshot snapshot = osclient.blockStorage().snapshots().get(snapshotId);
				if (snapshot == null)
					return true;
				else
					return null;
			}

			@Override
			public Integer maxRetryTimes() {
				return 12;
			}

			@Override
			public Integer delay() {
				return 5;
			}
		};
		Boolean isDel = (Boolean) this.retry(retry);
		if (!isDel) {
			throw new RuntimeException(String.format("[%s]Snapshot's deletion failed", snapshotId));
		}
	}
}
