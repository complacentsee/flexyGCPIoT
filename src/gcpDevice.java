/******************************************************************************
 * Copyright 2021 Complacentsee
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Calendar;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.signers.RSADigestSigner;
import org.bouncycastle.crypto.util.PrivateKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.UrlBase64;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;


public class gcpDevice {
	private String projectID;
	private String region;
	private String registryId;
	private String deviceId;
	private String privateKey;
	private String jwt;
	private String endPoint = "mqtt.2030.ltsapis.goog";
	private int jwt_exp_secs = 86400;
	private long jwt_iat = 0;
	private long jwt_exp_time = 0;
	
	//Constructor for gcpJWT 
	public gcpDevice(String projectID, String region, String registryId, String deviceID, String privateKey) {
		this.projectID = projectID;
		this.region = region;
		this.registryId = registryId;
		this.deviceId = deviceID;
		this.privateKey = privateKey;
		
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
		    Security.addProvider(new BouncyCastleProvider());
		}
		}
	
	private String _CreateJWT() throws
		NoSuchAlgorithmException, InvalidKeySpecException, 
		InvalidKeyException, SignatureException {
		
		  // header: base64_encode("{\"alg\":\"ES256\",\"typ\":\"JWT\"}") + "."
		
		String jwt_construction = "";
		
		String header_payload_base64 =
		//	    "eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCJ9.";
		//		This is the hard coded for RS256
				"eyJhbGciOiAiUlMyNTYiLCJ0eXAiOiAiSldUIn0.";
		
		String payload_str = "{\"iat\":" + (int) this.jwt_iat
					+ ",\"exp\":" + (int) this.jwt_exp_time
				    + ",\"aud\":\"" + this.projectID + "\"}";		  
		  
		ByteArrayOutputStream payload_base64 = new ByteArrayOutputStream();
		  
		try {
			UrlBase64.encode(payload_str.getBytes(),payload_base64);
			payload_str = payload_base64.toString();
			int trimloc = payload_str.indexOf(".");
			if(trimloc > 0) {
				payload_str = payload_str.substring(0,trimloc);
			}
			} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			}
		  
		jwt_construction = header_payload_base64 + payload_str;
	        
	        PrivateKey prikey;
	        Signature sig = Signature.getInstance("SHA256withRSA");
			//RSADigestSigner signer = new RSADigestSigner(new SHA256Digest());
	        //AsymmetricKeyParameter keyparam;
	        byte[] signat = {};
			
			try {
				prikey = getPemPrivateKey(this.privateKey);
		        sig.initSign(prikey);
		        sig.update(jwt_construction.getBytes());
			//	signer.init(true, getPemPrivateParameter(this.privateKey));
			//	signer.update(jwt_construction.getBytes(), 0, jwt_construction.length());
			//	try {
			//	    signat = signer.generateSignature();
			//	} catch (Exception ex) {
			//	    throw new RuntimeException("Cannot generate RSA signature. " + ex.getMessage(), ex);
			//	}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        byte [] signature = sig.sign();
	        
			ByteArrayOutputStream signature_base64 = new ByteArrayOutputStream();
			
			try {
				//UrlBase64.encode(signat,signature_base64);	Known working.
				UrlBase64.encode(signature,signature_base64);
				payload_str = signature_base64.toString();
				int trimloc = payload_str.indexOf(".");
				if(trimloc > 0) {
					payload_str = payload_str.substring(0,trimloc);
				}
				} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				}
			
	        
		return jwt_construction + "." + payload_str;
	}
	
	/**
	   *
	   * The method returns a signed jwt at the the current expire time range
	   * of 24 hours, or whatever was most recently set. 
	   * @return signed jwt
	   *
	*/
	public String createJWT() {
		this.jwt_iat = (Calendar.getInstance().getTimeInMillis()/1000L);
		this.jwt_exp_time = this.jwt_iat + this.jwt_exp_secs;
		  try {
			this.jwt = _CreateJWT();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return jwt;
	}

	/**
	   *
	   * The method returns a signed jwt at the the specified exp_in_secs
	   * @param exp_in_secs the specified lenth of time in seconds before the token should expire
	   * @return the signed jwt
	   *
	*/
	public String createJWT(int exp_in_secs) {
		this.jwt_exp_secs = exp_in_secs;
		  try {
			jwt = _CreateJWT();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return jwt;
	}
	
	
	public String getJWT() {
		if(this.jwt_exp_time <= (Calendar.getInstance().getTimeInMillis()/1000L)) {
		this.jwt = createJWT();
		}
		return this.jwt;
	}
	
	public String getBasePath() {
		return "/v1/projects/" + this.projectID + "/locations/" + this.region +
			   	"/registries/" + this.registryId + "/devices/" + this.deviceId;
	}		
	
	public String getClientId() {
		return "projects/" + this.projectID + "/locations/" + this.region +
				"/registries/" + this.registryId + "/devices/" + this.deviceId;
	}		
	
	public String getConfigTopic() {
		return "/devices/" + this.deviceId + "/config";
	}	
	
	public String getCommandsTopic() {
		return "/devices/" + this.deviceId + "/commands/#";
	}	
	
	public String getDeviceId() {
		return this.deviceId;
	}
	
	public String getEventsTopic() {
		return "/devices/" + this.deviceId + "/events";
	}
	
	public String getStateTopic() {
		return "/devices/" + this.deviceId + "/state";
	}
	
	public String getConfigPath(int version) {
		return this.getBasePath() + "/config?local_version=" + version;
	}
	
	public String getLastConfigPath() {
		return this.getConfigPath(0);
	}
	
	
	public String getSendTelemetryPath() {
		return this.getBasePath() + ":publishEvent";
	}
	
	
	public String getSetStatePath() {
		return this.getBasePath() + ":setState";
	}
	
	public void setJwtExpSecs(long exptime) {
		this.jwt_exp_time = exptime;
	}
	
	public long getJwtExpSecs() {
		return this.jwt_exp_secs;
	}
	
	public void setJwtExpTime(long exptime) {
		this.jwt_exp_time = exptime;
	}
	
	public long getJwtExpTime() {
		return this.jwt_exp_time;
	}
	
	public String getEndPoint() {
		return this.endPoint;
	}
	
	private static PrivateKey getPemPrivateKey(String mKey) throws Exception {
	    PEMParser pemParser = new PEMParser(new StringReader(mKey));
	    final PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();
	    final byte[] encoded = pemKeyPair.getPrivateKeyInfo().getEncoded();
	    KeyFactory kf = KeyFactory.getInstance("RSA");
	    return kf.generatePrivate(new PKCS8EncodedKeySpec(encoded));

	}
	
	private static AsymmetricKeyParameter getPemPrivateParameter(String mKey) throws Exception {
	    PEMParser pemParser = new PEMParser(new StringReader(mKey));
	    final PEMKeyPair pemKeyPair = (PEMKeyPair) pemParser.readObject();
		AsymmetricKeyParameter privKey = PrivateKeyFactory.createKey(pemKeyPair.getPrivateKeyInfo());
	    return privKey;

	}
}
