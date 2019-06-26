package com.sap.refapps.objectstore.config;

import java.nio.charset.Charset;
import java.util.Base64;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.domain.Credentials;
import org.jclouds.googlecloud.GoogleCredentialsFromJson;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.google.common.base.Supplier;
import com.sap.refapps.objectstore.util.CloudProviders;

/**
 * This is GCP Credentials Configuration class
 *
 */

@Profile("cloud-gcp")
@Configuration
@ConfigurationProperties(prefix = "vcap.services.objectstore-service.credentials")
public class GoogleCloudPlatformConfiguration {
	
	private String base64EncodedPrivateKeyData;
	private String bucket;
	
	public String getBase64EncodedPrivateKeyData() {
		return base64EncodedPrivateKeyData;
	}
	
	public String getBucket() {
		return bucket;
	}
	
	public void setBase64EncodedPrivateKeyData(final String base64EncodedPrivateKeyData) {
		this.base64EncodedPrivateKeyData = base64EncodedPrivateKeyData;
	}
	
	public void setBucket(final String bucket) {
		this.bucket = bucket;
	}
	
	/**
	 * @return blobStoreContext
	 */
	public BlobStoreContext getBlobStoreContext() {
		final byte[] decodedKey = Base64.getDecoder().decode(this.getBase64EncodedPrivateKeyData());
		final String decodedCredential = new String(decodedKey, Charset.forName("UTF-8"));
		Supplier<Credentials> supplierCredential = new GoogleCredentialsFromJson(decodedCredential);
		BlobStoreContext blobStoreContext = ContextBuilder.newBuilder(CloudProviders.PROVIDER_GCP.toString())
				.credentialsSupplier(supplierCredential).buildView(BlobStoreContext.class);

		return blobStoreContext;
	}
}
