package com.sap.refapps.objectstore.service;

import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.sap.refapps.objectstore.config.AmazonWebServiceConfiguration;
import com.sap.refapps.objectstore.model.BlobFile;
import com.sap.refapps.objectstore.repository.ObjectStoreRepository;

@Profile("cloud-aws")
@Service
public class AWSObjectStoreService implements ObjectStoreService {

	private final AmazonWebServiceConfiguration awsConfig;
	private final ObjectStoreRepository repository;
	private static Logger logger = LoggerFactory.getLogger(AWSObjectStoreService.class);

	@Autowired
	public AWSObjectStoreService(final AmazonWebServiceConfiguration awsConfig, ObjectStoreRepository repository) {
		this.awsConfig = awsConfig;
		this.repository = repository;
	}

	@Override
	public String uploadFile(byte[] bytes, String fileName, String contentType) {
		repository.setContext(awsConfig.getBlobStoreContext());
		logger.info("Upload started");
		var message = repository.uploadFile(awsConfig.getBucket(), bytes, fileName, contentType);
		logger.info("upload completed");
		return message;
	}

	public List<BlobFile> listObjects() {
		repository.setContext(awsConfig.getBlobStoreContext());
		List<BlobFile> files = repository.listFiles(awsConfig.getBucket());
		return files;
	}

	@Override
	public InputStream getFile(String fileName) {
		repository.setContext(awsConfig.getBlobStoreContext());
		var inputStream = repository.downloadFile(awsConfig.getBucket(), fileName);
		return inputStream;
	}

	@Override
	public boolean deleteFile(String fileName) {
		repository.setContext(awsConfig.getBlobStoreContext());
		var status = repository.deleteFile(awsConfig.getBucket(), fileName);
		return status;

	}

	@Override
	public boolean isBlobExist(String fileName) {
		repository.setContext(awsConfig.getBlobStoreContext());
		var status = repository.isBlobExist(awsConfig.getBucket(), fileName);
		return status;
	}
}
