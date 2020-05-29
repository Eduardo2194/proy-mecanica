package com.example.backend.client.firebase.bean;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @see https://firebase.google.com/docs/cloud-messaging/http-server-ref?hl=es-419<br>
 * https://firebase.google.com/docs/cloud-messaging/concept-options?hl=es-419#notifications_and_data_messages
 */
@XmlRootElement
public class FirebasePushNotification {
	
	private String to;
	private FirebaseNotification notification;
	private FirebaseData data;
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public FirebaseNotification getNotification() {
		return notification;
	}
	public void setNotification(FirebaseNotification notification) {
		this.notification = notification;
	}
	public FirebaseData getData() {
		return data;
	}
	public void setData(FirebaseData data) {
		this.data = data;
	}

}
