package com.sap.refapps.objectstore.config;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStoreContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.sap.refapps.objectstore.util.CloudProviders;

import io.pivotal.cfenv.core.CfEnv;

/**
 * This is AWS Credentials Configuration class
 *
 */

@Profile("cloud-aws")
@Configuration
public class AmazonWebServiceConfiguration {
	
	private static final String ACCESS_KEY_ID = "access_key_id";
	private static final String SECRET_ACCESS_KEY = "secret_access_key";
	private static final String BUCKET = "bucket";
	private static final String SERVICE_LABEL = "SERVICE_LABEL";
	
	private String accessKeyId;
	private String bucket;
	private String secretAccessKey;
	
	public String getBucket() {
		return bucket;
	}

	/**
	 * @return blobStoreContext
	 */
	public BlobStoreContext getBlobStoreContext() {
		
		var serviceLabel = System.getenv(SERVICE_LABEL);
		var cfenv = new CfEnv();
		var cfService = cfenv.findServiceByLabel(serviceLabel);
		var cfCredentials = cfService.getCredentials();
		
		this.accessKeyId = cfCredentials.getString(ACCESS_KEY_ID);
		this.secretAccessKey = cfCredentials.getString(SECRET_ACCESS_KEY);
		this.bucket = cfCredentials.getString(BUCKET);
		
		return ContextBuilder.newBuilder(CloudProviders.PROVIDER_AWS.toString())
				.credentials(this.accessKeyId, this.secretAccessKey)
				.buildView(BlobStoreContext.class);
	}
	
}