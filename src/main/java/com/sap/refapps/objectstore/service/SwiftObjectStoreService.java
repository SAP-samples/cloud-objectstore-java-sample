package com.sap.refapps.objectstore.service;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.openstack.swift.v1.SwiftApi;
import org.jclouds.openstack.swift.v1.features.ObjectApi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.sap.refapps.objectstore.config.OpenstackSwiftConfiguration;
import com.sap.refapps.objectstore.model.BlobFile;
import com.sap.refapps.objectstore.repository.ObjectStoreRepository;

@Profile("cloud-swift")
@Service
public class SwiftObjectStoreService implements ObjectStoreService {

	private final OpenstackSwiftConfiguration swiftConfig;
	private final ObjectStoreRepository repository;
	private final String containerName;
	private static Logger logger = LoggerFactory.getLogger(SwiftObjectStoreService.class);
	
	@Autowired
	public SwiftObjectStoreService(final OpenstackSwiftConfiguration swiftConfig, ObjectStoreRepository repository) {
		this.swiftConfig = swiftConfig;
		this.repository = repository;
		this.containerName = swiftConfig.getContainerName();
	}
	
	@Override
	public String uploadFile(byte[] bytes, String fileName, String contentType) {
		repository.setContext(swiftConfig.getBlobStoreContext());
		logger.info("Upload started");
		String message = repository.uploadFile(containerName, bytes, fileName, contentType);
		logger.info("upload completed");
		return message;
	}
	
	@Override
	public List<BlobFile> listObjects() {
		repository.setContext(swiftConfig.getBlobStoreContext());
		List<BlobFile> listBlobs = repository.listFiles(containerName);
		return listBlobs;
	}
	
	@Override
	public InputStream getFile(String fileName) {
		repository.setContext(swiftConfig.getBlobStoreContext());
		InputStream inputStream = repository.downloadFile(containerName, fileName);
		return inputStream;
	}
	
	@Override
	public boolean deleteFile(String fileName) {
		boolean isBlobRemoved = false;

		BlobStoreContext context = swiftConfig.getBlobStoreContext();
		SwiftApi swiftApi = context.unwrapApi(SwiftApi.class);
		Set<String> regions = swiftApi.getConfiguredRegions();
		Iterator<String> iterator = regions.iterator();
		String region = iterator.next();
		ObjectApi objectApi = swiftApi.getObjectApi(region, containerName);
		// removing blob
		objectApi.delete(fileName);
		//check if the blob still exists after deletion
		if(!isBlobExist(fileName)){
			isBlobRemoved = true;
		}
		
		return isBlobRemoved;
	}
	
	@Override
	public boolean isBlobExist(String fileName) {
		repository.setContext(swiftConfig.getBlobStoreContext());
		boolean status = repository.isBlobExist(containerName, fileName);
		return status;
	}

}
