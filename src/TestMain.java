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

import java.util.Random;
import com.ewon.ewonitf.EWException;
import com.ewon.ewonitf.MqttClient;
import com.ewon.ewonitf.MqttMessage;

public class TestMain {
	
    class EwonMqtt extends MqttClient {
        public EwonMqtt(String endpoint, String clientID) throws Exception {
            super(clientID, endpoint);
            this.setOption("port", "8883");
            this.setOption("log", "1");
            this.setOption("keepalive", "30");
            this.setOption("cafile", "/usr/root-CA.crt");
            this.setOption("certfile", "/usr/my.cert.pem");
            this.setOption("keyfile", "/usr/my.private.key");
            this.connect();
        }

        public void callMqttEvent(int arg0) {
        }
    }
    
    class gcpIoTMqtt extends MqttClient {
        public gcpIoTMqtt(gcpDevice device) throws Exception {
        	super(device.getClientId(), device.getEndPoint());
            this.setOption("username", "");
            this.setOption("password", device.getJWT());
            this.setOption("port", "8883");
            this.setOption("log", "1");
            this.setOption("keepalive", "30");
            this.setOption("cafile", "/usr/root-CA.crt");
            this.connect();
        }
        public void callMqttEvent(int arg0) {
        }
    }
    
    public  void runTest() {
    	//CONFIGURE ME
    	String projectID = "";
    	String region = "";
    	String registryId ="";
    	String deviceId ="";
    	String privateKey="";
    	
    	gcpDevice device = new gcpDevice(projectID,region,registryId,deviceId,privateKey);
    	gcpIoTMqtt client = null;
    	
        try {
            client = new gcpIoTMqtt(device);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String json = "";
        Random r = new Random();
        for(int i = 0; i < 6; i++) {
            json = "{";
            for(int j = 0; j < 20; j++) {
                json += "\"tag" + j + "\": " + r.nextInt(1000);
                if(j < 19)
                    json += ", ";
            }
            json += "}";
            
            MqttMessage message = new MqttMessage(device.getSendTelemetryPath(), json);
            try {
                client.publish(message, 0, false);
            } catch (EWException e) {
                e.printStackTrace();
            }
            
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        TestMain t = new TestMain();
        t.runTest();
    }

}