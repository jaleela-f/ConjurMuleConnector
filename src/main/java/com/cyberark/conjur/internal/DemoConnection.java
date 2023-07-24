package com.cyberark.conjur.internal;

import org.mule.runtime.extension.api.annotation.param.Parameter;

/**
 * This class represents an extension connection just as example (there is no
 * real connection with anything here c:).
 */
public final class DemoConnection {

	private final String id;
	//private final String conjurAccount;
	//private final String conjurApplianceUrl;
	//private final String conjurAuthnLogin;
	//private final String conjurApiKey;

	public DemoConnection(String id) {
		this.id = id;
		//this.conjurAccount = conjurAccount;
		//this.conjurApplianceUrl = conjurApplianceUrl;
		//this.conjurAuthnLogin = conjurAuthnLogin;
		//this.conjurApiKey = conjurApiKey;
		System.out.println("CAlling Demo Connection Constructor");
	}

	public String getId() {
		return id;
	}

	
	public void invalidate() {
		// do something to invalidate this connection!
	}
}
