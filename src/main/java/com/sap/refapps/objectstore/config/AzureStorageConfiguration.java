package com.sap.refapps.objectstore.config;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStoreContext;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.sap.refapps.objectstore.util.CloudProviders;

/**
 * This is MS Azure Credentials Configuration class
 *
 */

@Profile("cloud-azure")
@Configuration
@ConfigurationProperties(prefix = "vcap.services.objectstore-service.credentials")
public class AzureStorageConfiguration {
	
	private String accountName;
	private String containerName;
	private String sasToken;

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public String getSasToken() {
		return sasToken;
	}

	public void setSasToken(String sasToken) {
		this.sasToken = sasToken;
	}

	@Bean
	public BlobStoreContext getBlobStoreContext() {
		
		BlobStoreContext blobStoreContext = ContextBuilder.newBuilder(CloudProviders.PROVIDER_AZURE.toString())
				.credentials(this.getAccountName(), this.getSasToken())
				.buildView(BlobStoreContext.class); 
		
		return blobStoreContext;
	}
	
}