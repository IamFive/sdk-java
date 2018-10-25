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
import com.huawei.openstack4j.model.storage.block.VolumeType;
import com.huawei.openstack4j.openstack.storage.block.domain.AvailabilityZone;
import com.huawei.openstack4j.openstack.storage.block.domain.Extension;
import com.huawei.openstack4j.openstack.storage.block.domain.Volume.Volumes;
import com.huawei.openstack4j.openstack.storage.block.domain.VolumeMeta;
import com.huawei.openstack4j.openstack.storage.block.domain.VolumeMetadata;
import com.huawei.openstack4j.openstack.storage.block.options.VolumeListOptions;

public class EVSVolumeTest extends AbstractTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(EVSVolumeTest.class);
	private static String volumeId;
	private static String typeId;
	private static final String META_KEY = "key1";
	private static final String META_VAL = "value1";

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

		Map<String, String> map = Maps.newHashMap();
		map.put(META_KEY, META_VAL);
		VolumeMetadata metadata = VolumeMetadata.builder().metadata(map).build();
		VolumeMetadata createMeta = osclient.blockStorage().volumes().createOrUpdateMetadata(volumeId, metadata);
		assertTrue(META_VAL.equals(createMeta.getMetadata().get(META_KEY)));

		List<? extends VolumeType> types = osclient.blockStorage().volumes().listVolumeTypes();
		if (types.size() > 0)
			typeId = types.get(0).getId();
	}

	@AfterClass
	public void teardown() {
		ActionResponse delete = osclient.blockStorage().volumes().delete(volumeId);
		assertTrue(delete.isSuccess());

		ActionResponse delete2 = osclient.blockStorage().volumes().deleteMetadata(volumeId, META_KEY);
		assertTrue(delete2.isSuccess());
	}

	@Test
	public void testVolumes() {
		Volumes volumes = osclient.blockStorage().volumes().volumes();
		LOGGER.info("{}", volumes);

		VolumeListOptions options = VolumeListOptions.create().limit(10);
		Volumes volumes2 = osclient.blockStorage().volumes().volumes(options);
		LOGGER.info("{}", volumes2);
	}

	@Test
	public void testGetVolume() {
		Volume volume = osclient.blockStorage().volumes().get(volumeId);
		LOGGER.info("{}", volume);
	}

	@Test
	public void testGetMetadata() {
		VolumeMetadata metadata = osclient.blockStorage().volumes().metadata(volumeId);
		assertTrue(META_VAL.equals(metadata.getMetadata().get(META_KEY)));

		VolumeMeta metadata2 = osclient.blockStorage().volumes().metadata(volumeId, META_KEY);
		assertTrue(META_VAL.equals(metadata2.getMeta().get(META_KEY)));
	}

	@Test
	public void testUpdateMetadata() {
		String newVal = "new value";
		Map<String, String> map = Maps.newHashMap();
		map.put(META_KEY, newVal);
		VolumeMetadata metadata = VolumeMetadata.builder().metadata(map).build();
		VolumeMetadata updateMetadata = osclient.blockStorage().volumes().updateMetadata(volumeId, metadata);
		assertTrue(newVal.equals(updateMetadata.getMetadata().get(META_KEY)));

		map.put(META_KEY, META_VAL);
		VolumeMeta meta = VolumeMeta.builder().meta(map).build();
		metadata.getMetadata().put(META_KEY, META_VAL);
		VolumeMeta updateMetadata2 = osclient.blockStorage().volumes().updateMetadata(volumeId, META_KEY, meta);
		assertTrue(META_VAL.equals(updateMetadata2.getMeta().get(META_KEY)));
	}

	@Test
	public void testSetBootable() {
		ActionResponse resp = osclient.blockStorage().volumes().setBootable(volumeId, true);
		assertTrue(resp.isSuccess());
	}

	@Test
	public void testExtensions() {
		List<? extends Extension> extensions = osclient.blockStorage().volumes().extensions();
		LOGGER.info("{}", extensions);
	}

	@Test
	public void testListVolumeTypes() {
		List<? extends VolumeType> types = osclient.blockStorage().volumes().listVolumeTypes();
		LOGGER.info("{}", types);
	}

	@Test
	public void testGetType() {
		if (Strings.isNullOrEmpty(typeId)) {
			LOGGER.warn("No available type. Skip testGetType");
			return;
		}
		VolumeType type = osclient.blockStorage().volumes().type(typeId);
		LOGGER.info("{}", type);
	}

	@Test
	public void testSetReadonly() {
		ActionResponse resp = osclient.blockStorage().volumes().readOnlyModeUpdate(volumeId, true);
		assertTrue(resp.isSuccess());
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
}
