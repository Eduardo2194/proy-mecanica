package com.example.backend.engine.context;

public class AppNamedThreadLocal<T> extends ThreadLocal<T> {

	private final String name;

	public AppNamedThreadLocal(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
