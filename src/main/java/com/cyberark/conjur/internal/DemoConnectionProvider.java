package com.cyberark.conjur.internal;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.api.connection.ConnectionProvider;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.mule.runtime.api.connection.CachedConnectionProvider;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cyberark.conjur.sdk.AccessToken;
import com.cyberark.conjur.sdk.ApiClient;
import com.cyberark.conjur.sdk.ApiException;
import com.cyberark.conjur.sdk.Configuration;
import com.cyberark.conjur.sdk.endpoint.SecretsApi;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.commons.lang3.StringUtils;

/**
 * This class (as it's name implies) provides connection instances and the
 * funcionality to disconnect and validate those connections.
 * <p>
 * All connection related parameters (values required in order to create a
 * connection) must be declared in the connection providers.
 * <p>
 * This particular example is a {@link PoolingConnectionProvider} which declares
 * that connections resolved by this provider will be pooled and reused. There
 * are other implementations like {@link CachedConnectionProvider} which lazily
 * creates and caches connections or simply {@link ConnectionProvider} if you
 * want a new connection each time something requires one.
 */
public class DemoConnectionProvider implements PoolingConnectionProvider<DemoConnection> {

	private final Logger LOGGER = LoggerFactory.getLogger(DemoConnectionProvider.class);

	/**
	 * A parameter that is always required to be configured.
	 */
	@Parameter
	private String requiredParameter;
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
	private String key;

	/**
	 * A parameter that is not required to be configured by the user.
	 */
	@DisplayName("Friendly Name")
	@Parameter
	@Optional(defaultValue = "100")
	private int optionalParameter;

	@Override
	public DemoConnection connect() throws ConnectionException {

		System.out.println("Calling Demo Connection Provider connect()");

		AccessToken accessToken= null;

		ApiClient client = Configuration.getDefaultApiClient();
		SecretsApi secretsApi = new SecretsApi();

		client.setAccount(conjurAccount);
		client.setBasePath(conjurApplianceUrl);
		client.setUsername(conjurAuthnLogin);
		client.setApiKey(conjurApiKey);

		InputStream sslInputStream = null;
		String sslCertificate = conjurSslCertificate;
		//String certFile = conjurCertFile;
		String secretValue="";

		try {
			if (StringUtils.isNotEmpty(sslCertificate)) {
				System.out.println("SSL Certificate >>>>"+sslCertificate);
				//sslInputStream = new ByteArrayInputStream(sslCertificate.getBytes(StandardCharsets.UTF_8));
				sslInputStream = new FileInputStream(sslCertificate);
			} /*else {
				if (StringUtils.isNotEmpty(certFile))
					sslInputStream = new FileInputStream(certFile);
			}*/

			if (sslInputStream != null) {
				client.setSslCaCert(sslInputStream);
				sslInputStream.close();
			}
			System.out.println("SSL Certificate data >>>>"+sslInputStream.toString());
			accessToken = client.getNewAccessToken();
			String token = accessToken.getHeaderValue();
			client.setAccessToken(token);
			Configuration.setDefaultApiClient(client);
			
			client = secretsApi.getApiClient();
			String account = client.getAccount();
			
			System.out.println("Client after setting token"+ account);
			
			
			secretValue = secretsApi.getSecret(account, "variable" ,key);
			
			System.out.println("Secrets retrieved from conjur "+ secretValue);
		} catch (IOException ex) {
			ex.getMessage();
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
		return new DemoConnection(requiredParameter + ":" + optionalParameter + ":" + conjurAccount + ":"
				+ conjurApplianceUrl + ":" + conjurAuthnLogin + ":" + conjurApiKey + ":" + accessToken + ":" + secretValue) ;
	}

	@Override
	public void disconnect(DemoConnection connection) {
		try {
			connection.invalidate();
		} catch (Exception e) {
			LOGGER.error("Error while disconnecting [" + connection.getId() + "]: " + e.getMessage(), e);
		}
	}

	@Override
	public ConnectionValidationResult validate(DemoConnection connection) {
		return ConnectionValidationResult.success();
	}
}
