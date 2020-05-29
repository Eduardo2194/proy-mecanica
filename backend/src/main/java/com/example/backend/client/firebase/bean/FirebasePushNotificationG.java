package com.example.backend.client.firebase.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @param <T>
 * @see https://firebase.google.com/docs/cloud-messaging/http-server-ref?hl=es-419<br>
 * https://firebase.google.com/docs/cloud-messaging/concept-options?hl=es-419#notifications_and_data_messages
 */
@XmlRootElement
public class FirebasePushNotificationG<T> extends FirebaseBasePushNotification{
	
	private T data;

	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}

}
