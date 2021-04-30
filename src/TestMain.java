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

public class TestMain {
    
    public  void runTest() {
    	//CONFIGURE ME
    	String projectID = "flexyGCPIoT";
    	String region = "us-central1";
    	String registryId ="sampleregistryname";
    	String deviceId ="flexyfakeline1";
    	String privateKeyType = "RSA";
    	String privateKey=
    					"-----BEGIN RSA PRIVATE KEY-----\n"
    					+ "MIIEpQIBAAKCAQEA50vAZp24ovZiMWu4h2agxqPTXxlBNZPOq6DiulUkcZuc4SdH\n"
    					+ "n9wDxljO8RCIaaf9vl6A1aV5cMiIHzvmoFI1Bm6Kn8QeZQw6TpPjmdCQrYEzvs1N\n"
    					+ "3RC9AdtxB1ozQ07rgKEZzKHuBZwi+dYlJ14qyExPR0y/CHrOZmSUEb5Lk/LfvHWE\n"
    					+ "r9oi5jMvO2+2RLSjsTdaYwFD99GBzeejBAvg+i6pXn8eYgdN4Nyy6SicDzZaAacS\n"
    					+ "bmv7MWGYJa1EvC1plWO/nWsSuCKvxSo7InXikqOzAJN8L2N/E2kLhM708pI/u6bw\n"
    					+ "HAU4XJvOAYncDxhGr41vPSQd2Fj3QZ7fRX4TnwIDAQABAoIBAQCNBmQGbU5BloZi\n"
    					+ "abK2Y/3Nf+AGEOjwmPGfNdZoFDfHSUFLCt8h+k0W59ktpI34FeSh6Q8WtPEpsitF\n"
    					+ "GAnTYKxSAp5lMXfy1pTKimNwynkcQTXitV0vV+BWPI1bFUVCWeE/qXqIiYcORpgQ\n"
    					+ "yuAWc9UUEWsZJxMnQXGNfu/FLcj5KCjw/q1E4rHu5ptk8LsDeZlgXrM3GRliAwtl\n"
    					+ "zEpD80ATPD2iOnXkwKdF4TZXjO0Ai6/QjH3NGSFRdOjUNTAtx03D0e+LqT/ihj3X\n"
    					+ "Qy+f2r00ItlRVeBeDKrBCpS+w0FjHlCctBEPhAG6r2DZJ/3RioqJka2BK1SVtveO\n"
    					+ "vdI7eljJAoGBAP8GijnJQ5P5JeJYbp2QZ9mNrdwOVYIBh9G7hFeRoS41e8kY4YTB\n"
    					+ "Pku7zxOlxt2YXxnFWhQQSZpbbb0HZlkWjWlTyRzqeN0lfiOHUoNgnwJpk8S3lEDC\n"
    					+ "ooLQVIT9WZS5bG0ie72rSGKvXXLtbttlg3k2iqMQOUrjwfuim3NfAPZtAoGBAOgt\n"
    					+ "//VelVX3wP1lnt1qpw18HsTmfsSGI14TP5PkD0A1nMl8t8hwghmveg7pdsRN0lJs\n"
    					+ "GsbWlafFm6K7uypIwJgY9nO90B9yLGjXkWtVPuezpHBCqN+eg3BTRHX3SP1+4pgH\n"
    					+ "hHr/V0n9a8tMh3Wi8rqSl0nao4iJVXr8Takz7xq7AoGBAO8NOlSgjHAQg6qAKnAY\n"
    					+ "BMCxRd+YsB6FQMMgexV6tROTns0KPZsraTGkgp9wLdFaGwsVKzLTcar3OQ6P+ShZ\n"
    					+ "M4UfvM0WHOVvV6YGGxp7X5HrVVB4pMdvqtXkYtWmhmoaxcAnKsbH37phl787QUb3\n"
    					+ "CR5+OcZQVuQUSqmN6xliXIyhAoGBAKH3ZPUwlBCykqiyeU6QlhSsH3LQMGK8CHQa\n"
    					+ "DWIH2DO8srFEFZj4E2oDpaw9ZSKv/yaMT2miTCgLi/TL7VckBWaVE2fOZB9rsFs6\n"
    					+ "9jDf+M/925qMhe3pUFvNWpbIeNyN5ViU9fAvrB7rcIUTz/NaRuWsWWml4irfUwhG\n"
    					+ "dY6xsfz9AoGAO99Djje4fNbvHOXwBLgeUDyc7hhrfJ2tCJSCq1YzN7flqJMKwpcL\n"
    					+ "vccRw4vNNX/Fb5WdhbiI8Xzm8/lE1x5k0GzArZMoFzncO8DbPL2iXI4xaJxRpzzf\n"
    					+ "XZq2J5UP0KCBzxE/Sd+wP2ZsbCL/xKGDjOgDpDXJ9vWfAo8W48VPgKc=\n"
    					+ "-----END RSA PRIVATE KEY-----";
    	
    	String privateECKey=
    					"-----BEGIN EC PRIVATE KEY-----\n"
    					+ "MHcCAQEEICKJhmxoVFKgqtmD+7dAJ5eK5AQHFHvVOyOr9ipeC0w5oAoGCCqGSM49\n"
    					+ "AwEHoUQDQgAEi4gyu7tLWF07EqTmO5rasgc/OcfiPRG8Zbe/7PAMqEEfl8KYe3Jk\n"
    					+ "ZWz1IJSsvlMnPQGOvyOELi4T9asm+GSZRw==\n"
    					+ "-----END EC PRIVATE KEY-----";

    	gcpDevice device = new gcpDevice(projectID,region,registryId,deviceId,privateKey,privateKeyType);	
    	String jwt = device.createJWT();
    	System.out.println(jwt);
        
    	device = new gcpDevice(projectID,region,registryId,deviceId,privateECKey,"EC");	
    	jwt = device.createJWT();
    	System.out.println(jwt);
    	
        }

    public static void main(String[] args) {
  
        for (int i = 1; i <= 1; ++i) {
            TestMain t = new TestMain();
            t.runTest();
        }
    	
    }

}