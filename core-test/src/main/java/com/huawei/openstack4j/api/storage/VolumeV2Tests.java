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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import com.google.common.collect.Maps;
import com.huawei.openstack4j.api.AbstractTest;
import com.huawei.openstack4j.model.common.ActionResponse;
import com.huawei.openstack4j.model.storage.block.Volume;
import com.huawei.openstack4j.model.storage.block.VolumeType;
import com.huawei.openstack4j.openstack.storage.block.domain.Extension;
import com.huawei.openstack4j.openstack.storage.block.domain.QuotaSet;
import com.huawei.openstack4j.openstack.storage.block.domain.Version;
import com.huawei.openstack4j.openstack.storage.block.domain.Volume.Volumes;
import com.huawei.openstack4j.openstack.storage.block.domain.VolumeMeta;
import com.huawei.openstack4j.openstack.storage.block.domain.VolumeMetadata;
import com.huawei.openstack4j.openstack.storage.block.options.VolumeListOptions;

@Test(suiteName = "EVS/Volumes", enabled = true)
public class VolumeV2Tests extends AbstractTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(VolumeV2Tests.class);

	private static final String JSON_QUOTA_SET = "/storage/v2/os_quota_set.json";
	private static final String JSON_VOLUMES_1 = "/storage/v2/volumes1.json";

	private static final String JSON_VOLUME = "/storage/v2/volume1.json";

	private static final String JSON_VOLUME_METADATA_UPDATE = "/storage/v2/volume_metadata_update.json";

	private static final String JSON_VOLUME_METADATA = "/storage/v2/volume_metadata.json";

	private static final String JSON_VOLUME_METADATA_CREATE = "/storage/v2/volume_metadata_create.json";

	private static final String JSON_VOLUME_METADATAS = "/storage/v2/volume_metadatas.json";

	private static final String JSON_VOLUME_METADATA_UPDATE_WITH_KEY = "/storage/v2/volume_metadata_update_with_key.json";

	private static final String JSON_EXTENSIONS = "/storage/v2/extensions.json";

	private static final String JSON_TYPES = "/storage/v2/types.json";

	private static final String JSON_TYPE = "/storage/v2/type.json";

	private static final String JSON_VERSIONS = "/storage/v2/versions.json";

	private static final String JSON_VERSIONS_V2 = "/storage/v2/versions_v2.json";

	public void osQuotaSet() throws IOException {
		respondWith(JSON_QUOTA_SET);
		QuotaSet quota = osv3().blockStorage().quotaSets().get();
		LOGGER.info("{}", quota);

		assertTrue(quota != null);
		assertTrue(quota.getGigabytesSAS() != null);
		assertTrue(quota.getGigabytesSAS().getInUse() == 21);
	}

	public void volumes() throws IOException {
		respondWith(JSON_VOLUMES_1);
		Volumes volumes = osv3().blockStorage().volumes().volumes();
		LOGGER.info("{}", volumes);

		assertTrue(volumes.getVolumes().size() == 3);
		assertTrue(volumes.getVolumes().get(0) != null);
		assertTrue("6b604cef-9bd8-4f5a-ae56-45839e6e1f0a".equals(volumes.getVolumes().get(0).getId()));
	}

	public void volumesWithOptions() throws IOException {
		respondWith(JSON_VOLUMES_1);
		VolumeListOptions options = VolumeListOptions.create();
		Volumes volumes = osv3().blockStorage().volumes().volumes(options);
		LOGGER.info("{}", volumes);

		assertTrue(volumes.getVolumes().size() == 3);
		assertTrue(volumes.getVolumes().get(0) != null);
		assertTrue("6b604cef-9bd8-4f5a-ae56-45839e6e1f0a".equals(volumes.getVolumes().get(0).getId()));
	}

	public void volume() throws IOException {
		respondWith(JSON_VOLUME);
		String volumeId = "591ac654-26d8-41be-bb77-4f90699d2d41";
		Volume volume = osv3().blockStorage().volumes().get(volumeId);

		assertTrue(volume != null);
		assertTrue(volumeId.equals(volume.getId()));
	}

	public void updateMetadata() throws IOException {
		respondWith(JSON_VOLUME_METADATA_UPDATE);

		String volumeId = "volumeId";
		Map<String, String> map = Maps.newHashMap();
		map.put("key", "value");
		VolumeMetadata metadata = VolumeMetadata.builder().metadata(map).build();
		VolumeMetadata updateMetadata = osv3().blockStorage().volumes().updateMetadata(volumeId, metadata);

		assertTrue(updateMetadata != null);
		assertTrue("value1".equals(updateMetadata.getMetadata().get("key1")));
	}

	public void deleteMetadata() {
		respondWith(200);

		String key = "key";
		String volumeId = "volumeId";
		ActionResponse resp = osv3().blockStorage().volumes().deleteMetadata(volumeId, key);

		assertTrue(resp.isSuccess());
	}

	public void metadataWithKey() throws IOException {
		respondWith(JSON_VOLUME_METADATA);

		String key = "key";
		String volumeId = "volumeId";
		VolumeMeta metadata = osv3().blockStorage().volumes().metadata(volumeId, key);

		assertTrue(metadata != null);
		assertTrue("value1".equals(metadata.getMeta().get("key1")));
	}

	public void createOrUpdateMetadata() throws IOException {
		respondWith(JSON_VOLUME_METADATA_CREATE);

		String volumeId = "volumeId";
		Map<String, String> map = Maps.newHashMap();
		map.put("key1", "value1");
		map.put("key2", "value2");
		VolumeMetadata metadata = VolumeMetadata.builder().metadata(map).build();
		VolumeMetadata createMetadata = osv3().blockStorage().volumes().createOrUpdateMetadata(volumeId, metadata);

		assertTrue(createMetadata != null);
		assertTrue("value1".equals(createMetadata.getMetadata().get("key1")));
		assertTrue("value2".equals(createMetadata.getMetadata().get("key2")));
	}

	public void metadata() throws IOException {
		respondWith(JSON_VOLUME_METADATAS);

		String volumeId = "volumeId";
		VolumeMetadata metadata = osv3().blockStorage().volumes().metadata(volumeId);

		assertTrue(metadata != null);
		assertTrue("value1".equals(metadata.getMetadata().get("key1")));
		assertTrue("value2".equals(metadata.getMetadata().get("key2")));
	}

	public void updateMetadataWithKey() throws IOException {
		respondWith(JSON_VOLUME_METADATA_UPDATE_WITH_KEY);

		String key = "key";
		String volumeId = "volumeId";
		Map<String, String> map = Maps.newHashMap();
		map.put("key1", "value1");
		VolumeMeta metadata = VolumeMeta.builder().meta(map).build();
		VolumeMeta updateMetadata = osv3().blockStorage().volumes().updateMetadata(volumeId, key, metadata);

		assertTrue(updateMetadata != null);
		assertTrue("value1".equals(updateMetadata.getMeta().get("key1")));
	}

	public void extensions() throws IOException {
		respondWith(JSON_EXTENSIONS);

		List<? extends Extension> extensions = osv3().blockStorage().volumes().extensions();

		assertTrue(extensions.size() > 0);
		assertTrue("SchedulerHints".equals(extensions.get(0).getName()));
	}

	public void types() throws IOException {
		respondWith(JSON_TYPES);

		List<? extends VolumeType> types = osv3().blockStorage().volumes().listVolumeTypes();

		assertTrue(types.size() > 0);
		assertTrue("6c81c680-df58-4512-81e7-ecf66d160638".equals(types.get(0).getId()));
	}

	public void type() throws IOException {
		respondWith(JSON_TYPE);

		String typeId = "ea6e3c13-aac5-46e0-b280-745ed272e662";
		VolumeType type = osv3().blockStorage().volumes().type(typeId);

		assertTrue(type != null);
		assertTrue(typeId.equals(type.getId()));
	}

	public void versions() throws IOException {
		respondWith(JSON_VERSIONS);

		List<? extends Version> versions = osv3().blockStorage().volumes().versions();

		assertTrue(versions.size() > 0);
		assertTrue("v1.0".equals(versions.get(0).getId()));
	}

	public void versionsV2() throws IOException {
		respondWith(JSON_VERSIONS_V2);

		List<? extends Version> versions = osv3().blockStorage().volumes().versionsV2();

		assertTrue(versions.size() > 0);
		assertTrue("v2.0".equals(versions.get(0).getId()));
	}

	public void setBootable() {
		respondWith(200);

		String volumeId = "volumeId";
		boolean bootable = true;
		ActionResponse resp = osv3().blockStorage().volumes().setBootable(volumeId, bootable);

		assertTrue(resp.isSuccess());
	}

	public void setReadonly() {
		respondWith(200);

		String volumeId = "volumeId";
		boolean readonly = true;
		ActionResponse resp = osv3().blockStorage().volumes().readOnlyModeUpdate(volumeId, readonly);

		assertTrue(resp.isSuccess());
	}

	@Override
	protected Service service() {
		return Service.BLOCK_STORAGE;
	}
}
