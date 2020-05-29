package com.example.backend.client.firebase;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.example.backend.client.firebase.bean.FirebasePushNotification;
import com.example.backend.client.firebase.bean.FirebasePushNotificationG;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;


public class FirebasePushClient {
	
	private static final Logger LOGGER = LogManager.getLogger(FirebasePushClient.class);

	private static String firebasePushHeaderKey="key=";
	private static String firebasePushHeaderValue="AAAA5Vj_37I:APA91bFaI0is-YxwSifzamHNOXbPqFIunE2XOJg_fapt2Wsxi03rIJd2Fqmia57vzpZZOunDZLzfTwtzksdl18nfL0VR9snvgKz9vxMdSUzrok6X4uUhdFBuxNQsyMfYTRYg4zlbj-HA";//Util.getString("URL_FIREBASE_PUSH_KEY");	
	
	private static WebResource getFirebaseWebResource(){
		WebResource firebaseDBWebResource = null;
		firebaseDBWebResource = createDefaultClient().resource(getFirebasePushURL());
		return firebaseDBWebResource;
	}
	
	
	private static URI getFirebasePushURL(){
		return UriBuilder.fromUri("https://fcm.googleapis.com/fcm/send").build();
	}
	
	private static Client createDefaultClient(){
		ClientConfig clientConfig = new DefaultClientConfig();
		Client client = Client.create(clientConfig);
		//TODO revisar readTimeout
		//TODO revisar connectTimeout
		return client;
	}
	
	public boolean sendPush(FirebasePushNotification pushNotification){
		boolean isSuccessfullySent = false;
		try {
			WebResource firebaseResource = getFirebaseWebResource();
			
			LOGGER.debug("Se armo la sgt ruta: "+firebaseResource.getURI()+" para el envio del push");

			String response = firebaseResource.header("Authorization", firebasePushHeaderKey+firebasePushHeaderValue)
							.accept(MediaType.APPLICATION_JSON)
							.type(MediaType.APPLICATION_JSON)
							.post(String.class, pushNotification);
			
			LOGGER.info("Respuesta del FCM " + response);
			isSuccessfullySent = true;
		} catch (Exception e) {
			LOGGER.error("Ocurrion un error al enviar el push", e);
		}
		return isSuccessfullySent;
	}
	
	public <G extends FirebasePushNotificationG<T>, T> boolean sendPushNotification(G firebasePushNotification) throws JsonGenerationException, JsonMappingException, IOException{
		boolean pushSent = false; 
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = "{}";
		jsonString = objectMapper.writeValueAsString(firebasePushNotification);
		LOGGER.info("Request a firebase : "+jsonString);
		pushSent = sendPush(jsonString);
		return pushSent;
	}
	
	private static boolean sendPush(String jsonPushNotification){
		boolean isSuccessfullySent = false;
		try {
			WebResource firebaseResource = getFirebaseWebResource();
			
			LOGGER.debug("Se armo la sgt ruta: "+firebaseResource.getURI()+" para el envio del push");

			String response = firebaseResource.header("Authorization", firebasePushHeaderKey+firebasePushHeaderValue)
							.accept(MediaType.APPLICATION_JSON)
							.type(MediaType.APPLICATION_JSON)
							.post(String.class, jsonPushNotification);
			
			LOGGER.info("Respuesta del FCM " + response);
			isSuccessfullySent = true;
		} catch (Exception e) {
			LOGGER.error("Ocurrion un error al enviar el push", e);
		}
		return isSuccessfullySent;
	}
}
