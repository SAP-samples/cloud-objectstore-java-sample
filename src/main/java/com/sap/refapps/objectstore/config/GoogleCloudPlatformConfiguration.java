package com.sap.refapps.objectstore.config;

import java.nio.charset.Charset;
import java.util.Base64;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.domain.Credentials;
import org.jclouds.googlecloud.GoogleCredentialsFromJson;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.google.common.base.Supplier;
import com.sap.refapps.objectstore.util.CloudProviders;

import io.pivotal.cfenv.core.CfEnv;

/**
 * This is GCP Credentials Configuration class
 *
 */

@Profile("cloud-gcp")
@Configuration
public class GoogleCloudPlatformConfiguration {
	
	private static final String BASE64_ENCODED_PRIVATE_KEY_DATA = "base64EncodedPrivateKeyData";
	private static final String BUCKET = "bucket";
	private static final String SERVICE_LABEL = "SERVICE_LABEL";
	
	private String base64EncodedPrivateKeyData;
	private String bucket;
	
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
		
		this.base64EncodedPrivateKeyData = cfCredentials.getString(BASE64_ENCODED_PRIVATE_KEY_DATA);
		this.bucket = cfCredentials.getString(BUCKET);
		
		final byte[] decodedKey = Base64.getDecoder().decode(this.base64EncodedPrivateKeyData);
		final String decodedCredential = new String(decodedKey, Charset.forName("UTF-8"));
		Supplier<Credentials> supplierCredential = new GoogleCredentialsFromJson(decodedCredential);
		BlobStoreContext blobStoreContext = ContextBuilder.newBuilder(CloudProviders.PROVIDER_GCP.toString())
				.credentialsSupplier(supplierCredential).buildView(BlobStoreContext.class);

		return blobStoreContext;
	}
}
