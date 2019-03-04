package com.sap.refapps.objectstore.util;

/**
 * This enum class stores the different Objectstore plans,providers available in SCP
 *  across landscapes. 
 *
 */
public enum CloudProviders {
	
	PROVIDER_AWS ("aws-s3"),
	PROVIDER_GCP ("google-cloud-storage"),
	PROVIDER_SWIFT("openstack-swift"),
	PROFILE_AWS ("cloud-aws"),
	PROFILE_GCP ("cloud-gcp"),
	PROFILE_SWIFT("cloud-swift"),
	PlAN_AWS ("s3-standard"),
	PLAN_GCP ("gcs-standard"),
	PLAN_SWIFT("swift-standard");

	private final String providerName;

	private CloudProviders(final String providerName){
		this.providerName = providerName;
	}

	public String toString() {
		return this.providerName;
	}
}
