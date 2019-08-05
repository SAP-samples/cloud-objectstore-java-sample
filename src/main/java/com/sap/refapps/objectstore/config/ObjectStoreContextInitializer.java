package com.sap.refapps.objectstore.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import com.sap.refapps.objectstore.util.EnvironmentUtil;

public class ObjectStoreContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		final ConfigurableEnvironment applicationEnvironment = applicationContext.getEnvironment();
		final String profileToActive = EnvironmentUtil.getActiveProfile();
        // Active profile is set based on the objectore service connected to. (s3/gcs etc..) 
		applicationEnvironment.addActiveProfile(profileToActive);
	}

}
