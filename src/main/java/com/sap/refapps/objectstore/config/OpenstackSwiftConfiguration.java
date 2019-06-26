package com.sap.refapps.objectstore.config;

import java.util.Properties;

import org.jclouds.ContextBuilder;
import org.jclouds.blobstore.BlobStoreContext;
import org.jclouds.openstack.keystone.config.KeystoneProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.sap.refapps.objectstore.util.CloudProviders;

/**
 * This is Openstack Swift Credentials Configuration class
 *
 */

@Profile("cloud-swift")
@Configuration
@ConfigurationProperties(prefix = "vcap.services.objectstore-service.credentials")
public class OpenstackSwiftConfiguration {
	
	private String authUrl;
	private String containerName;
	private String password;
	private String project;
	private String projectDomain;
	private String userDomain;
	private String username;
	
	public String getAuthUrl() {
		return authUrl;
	}
	
	public String getContainerName() {
		return containerName;
	}

	public String getPassword() {
		return password;
	}
	
	public String getProject() {
		return project;
	}
	
	public String getProjectDomain() {
		return projectDomain;
	}
	
	public String getUserDomain() {
		return userDomain;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setAuthUrl(final String authUrl) {
		this.authUrl = authUrl;
	}

	public void setContainerName(final String containerName) {
		this.containerName = containerName;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setProject(final String project) {
		this.project = project;
	}

	public void setProjectDomain(final String projectDomain) {
		this.projectDomain = projectDomain;
	}

	public void setUserDomain(final String userDomain) {
		this.userDomain = userDomain;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	/**
	 * The properties required in order to connect to the different provider are different.
	 * @return blobStoreContext
	 */
	public BlobStoreContext getBlobStoreContext() {
		final Properties override = new Properties();
		override.put(KeystoneProperties.KEYSTONE_VERSION, "3");
		override.put(KeystoneProperties.SCOPE, "project:" + this.getProject());
  
		final String credential = this.getPassword();
		final String identity = this.getUserDomain() + ":" + this.getUsername();
		final String authUrl = this.getAuthUrl();
		
		return ContextBuilder.newBuilder(CloudProviders.PROVIDER_SWIFT.toString())
				.endpoint(authUrl+"/v3")
				.credentials(identity,credential)
				.overrides(override)
				.buildApi(BlobStoreContext.class);
	}
}
