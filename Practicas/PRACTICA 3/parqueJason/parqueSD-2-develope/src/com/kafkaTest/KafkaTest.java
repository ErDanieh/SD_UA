package com.kafkaTest;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class KafkaTest {

	public static void main(String[] args) {

		Unirest.setTimeouts(0, 0);
		String usuario = "anderApi";
		String name = "anderNom";
		String pass = "12345";

		String salt = "ParqueSDsalt";
		String hashedPassword = Hashing.sha256().hashString(pass + salt, StandardCharsets.UTF_8).toString();

		String bodyReq = "{\r\n        \"ID\": \"" + usuario + "\",\r\n        \"Nombre\": \"" + name
				+ "\",\r\n        \"Password\": \"" + hashedPassword
				+ "\",\r\n        \"enParque\": 0,\r\n        \"posFila\": null,\r\n        \"posColumna\": null,\r\n        \"color\": \"FFFFF1\"\r\n    }    \r\n";
		try {
			HttpResponse<String> response = Unirest.post("http://localhost:3010/visitantes/")
					.header("Content-Type", "application/json").body(bodyReq).asString();
		} catch (UnirestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
