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
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.UrlBase64;


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
		
		//if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
		    Security.addProvider(new BouncyCastleProvider());
		//}
		}
	
	private String _CreateJWT() throws
		NoSuchAlgorithmException, InvalidKeySpecException, 
		InvalidKeyException, SignatureException {
		  String payload = "{\"iat\":" + (int) this.jwt_iat
				    + ",\"exp\":" + (int) this.jwt_exp_time
				    + ",\"aud\":\"" + this.projectID + "\"}";

		  // header: base64_encode("{\"alg\":\"ES256\",\"typ\":\"JWT\"}") + "."
		  String header_payload_base64 =
			      "eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCJ9." + UrlBase64.encode(payload.getBytes());

			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(this.privateKey.getBytes());  	
			
	        KeyFactory kf = KeyFactory.getInstance("RSA");
	        PrivateKey privateKey = kf.generatePrivate(spec);
	        
	        
	        Signature sig = Signature.getInstance("SHA256withECDSA");
	        sig.initSign(privateKey);
	        sig.update(header_payload_base64.getBytes());
	        byte [] signature = sig.sign();
	        
		return header_payload_base64 + "." + UrlBase64.encode(signature);
	}
	
	//NEED TO CONFIRM OPERATION OF THESE TWO FUNCTIONS. SPECIFICALLY THE CURRENT TIME
	//jwt_exp_time should be expire time in seconds from EPOCH / UTC. 
	//current time is the earliest the jwt would be valid.
	
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
	
	//THESE FUNCTIONS ARE NOT TESTED BUT TEMPORARILY ASSUMPED TO BE CORRECT. 
	//Not all of these functions are needed today. They mirror the functionality
	//of the google iot cpp classes for arduino projects. 
	
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
		return this.getBasePath() + "setState";
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
	
	
	
	
	
}
