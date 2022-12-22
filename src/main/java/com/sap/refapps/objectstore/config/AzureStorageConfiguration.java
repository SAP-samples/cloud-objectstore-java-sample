package com.sap.refapps.objectstore.config;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStoreContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.sap.refapps.objectstore.util.CloudProviders;

import io.pivotal.cfenv.core.CfEnv;

/**
 * This is MS Azure Credentials Configuration class
 *
 */

@Profile("cloud-azure")
@Configuration
public class AzureStorageConfiguration {
	
	private static final String ACCOUNT_NAME = "account_name";
	private static final String SAS_TOKEN = "sas_token";
	private static final String CONTAINER_NAME = "container_name";
	private static final String SERVICE_LABEL = "SERVICE_LABEL";
	
	private String accountName;
	private String containerName;
	private String sasToken;

	public String getContainerName() {
		return containerName;
	}

	@Bean
	public BlobStoreContext getBlobStoreContext() {
		
		var serviceLabel = System.getenv(SERVICE_LABEL);
		var cfenv = new CfEnv();
		var cfService = cfenv.findServiceByLabel(serviceLabel);
		var cfCredentials = cfService.getCredentials();
		
		this.accountName = cfCredentials.getString(ACCOUNT_NAME);
		this.sasToken = cfCredentials.getString(SAS_TOKEN);
		this.containerName = cfCredentials.getString(CONTAINER_NAME);
		
		BlobStoreContext blobStoreContext = ContextBuilder.newBuilder(CloudProviders.PROVIDER_AZURE.toString())
				.credentials(this.accountName, this.sasToken)
				.buildView(BlobStoreContext.class); 
		
		return blobStoreContext;
	}
	
}