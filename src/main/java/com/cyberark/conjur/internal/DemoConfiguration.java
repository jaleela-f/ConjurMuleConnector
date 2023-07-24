package com.cyberark.conjur.internal;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.Parameter;

/**
 * This class represents an extension configuration, values set in this class
 * are commonly used across multiple operations since they represent something
 * core from the extension.
 */
@Operations(DemoOperations.class)
@ConnectionProviders(DemoConnectionProvider.class)
public class DemoConfiguration {

	@Parameter
	private String configId;

	public String getConfigId() {
		return configId;
	}

	@Parameter
	private String conjurAccount;
	@Parameter
	private String conjurApplianceUrl;
	@Parameter
	private String conjurAuthnLogin;
	@Parameter
	private String conjurApiKey;
	@Parameter
	private String conjurSslCertificate;
	@Parameter
	private String conjurCertFile;

	public String getConjurAccount() {
		return conjurAccount;
	}

	public String getConjurApplianceUrl() {
		return conjurApplianceUrl;
	}

	public String getConjurAuthnLogin() {
		return conjurAuthnLogin;
	}

	public String getConjurApiKey() {
		return conjurApiKey;
	}

	public String getConjurSslCertificate() {
		return conjurSslCertificate;
	}

	public String getConjurCertFile() {
		return conjurCertFile;
	}

}
