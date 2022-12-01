package com.sap.refapps.objectstore.service;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.sap.refapps.objectstore.config.GoogleCloudPlatformConfiguration;
import com.sap.refapps.objectstore.model.BlobFile;
import com.sap.refapps.objectstore.repository.ObjectStoreRepository;

@Profile("cloud-gcp")
@Service
public class GCPObjectStoreService implements ObjectStoreService {

	private final GoogleCloudPlatformConfiguration gcpConfig;
	private final ObjectStoreRepository repository;
	private final String containerName;
	private static Logger logger = LoggerFactory.getLogger(GCPObjectStoreService.class);
	
	@Autowired
	public GCPObjectStoreService(final GoogleCloudPlatformConfiguration gcpConfig, final ObjectStoreRepository repository) {
		this.gcpConfig = gcpConfig;
		this.repository = repository;
		this.containerName = gcpConfig.getBucket();
	}
	
	@Override
	public String uploadFile(byte[] bytes, String fileName, String contentType) {
		repository.setContext(gcpConfig.getBlobStoreContext());
		logger.info("Upload started");
		var message = repository.uploadFile(containerName, bytes, fileName, contentType);
		logger.info("upload completed");
		return message;
	}
	
	public List<BlobFile> listObjects() {
		repository.setContext(gcpConfig.getBlobStoreContext());
		List<BlobFile> listBlobs = repository.listFiles(containerName);
		return listBlobs;
	}
	
	@Override
	public InputStream getFile(String fileName) {
		repository.setContext(gcpConfig.getBlobStoreContext());
		var inputStream = repository.downloadFile(containerName, fileName);
		return inputStream;
	}
	
	@Override
	public boolean deleteFile(String fileName) {
		repository.setContext(gcpConfig.getBlobStoreContext());
		var blobRemove = repository.deleteFile(containerName, fileName);
		return blobRemove;

	}
	
	@Override
	public boolean isBlobExist(String fileName) {
		repository.setContext(gcpConfig.getBlobStoreContext());
		var blobExist = repository.isBlobExist(containerName, fileName);
		return blobExist;
	}

}
