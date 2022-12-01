package com.sap.refapps.objectstore.service;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.sap.refapps.objectstore.config.AzureStorageConfiguration;
import com.sap.refapps.objectstore.model.BlobFile;
import com.sap.refapps.objectstore.repository.ObjectStoreRepository;

@Profile("cloud-azure")
@Service
public class AzureObjectStoreService implements ObjectStoreService {

	private final AzureStorageConfiguration azureConfig;
	private final ObjectStoreRepository repository;
	private final String containerName;
	private static Logger logger = LoggerFactory.getLogger(AzureObjectStoreService.class);
	
	@Autowired
	public AzureObjectStoreService(final AzureStorageConfiguration azureConfig, ObjectStoreRepository repository) {
		this.azureConfig = azureConfig;
		this.repository = repository;
		this.containerName = azureConfig.getContainerName();
	}
	
	@Override
	public String uploadFile(byte[] bytes, String fileName, String contentType) {
		repository.setContext(azureConfig.getBlobStoreContext());
		logger.info("Upload started");
		var message = repository.uploadFile(containerName, bytes, fileName, contentType);
		logger.info("Upload completed");
		
		return message;
	}

	public List<BlobFile> listObjects() {
		repository.setContext(azureConfig.getBlobStoreContext());
		logger.info("Retrieving objects from container");
		List<BlobFile> files = repository.listFiles(containerName);
		logger.info("Retrieval of objects completed");
		
		return files;
	}

	@Override
	public InputStream getFile(String fileName) {
		repository.setContext(azureConfig.getBlobStoreContext());
		var inputStream = repository.downloadFile(containerName, fileName);
		
		return inputStream;
	}

	@Override
	public boolean deleteFile(String fileName) {
		repository.setContext(azureConfig.getBlobStoreContext());
		logger.info("Deletion started");
		var status = repository.deleteFile(containerName, fileName);
		logger.info("Deletion completed");
		
		return status;

	}

	@Override
	public boolean isBlobExist(String fileName) {
		repository.setContext(azureConfig.getBlobStoreContext());
		var status = repository.isBlobExist(containerName, fileName);
		
		return status;
	}

}