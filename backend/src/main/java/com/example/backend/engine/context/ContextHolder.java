package com.example.backend.engine.context;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public abstract class ContextHolder {

	private static final String THREADLOCAL_NAME = "EngineContext";

	private static final ThreadLocal<Map<String, Context>> CONTEXT_HOLDER = new AppNamedThreadLocal<Map<String, Context>>(
			THREADLOCAL_NAME);

	public static void cleanContext() {
		CONTEXT_HOLDER.remove();
	}

	public static void startContext() {
		CONTEXT_HOLDER.remove();
		CONTEXT_HOLDER.set(new HashMap<String, Context>());
	}

	@SuppressWarnings("unchecked")
	public static <T extends Context> T get(Class<T> contextClass) {
		T context = null;
		try {
			Map<String, Context> data = CONTEXT_HOLDER.get();
			if (data == null) {
				startContext();
				data = CONTEXT_HOLDER.get();
			}

			Object result = data.get(contextClass.getName());
			if (result == null) {
				context = contextClass.newInstance();
				data.put(contextClass.getName(), context);
			} else {
				if (contextClass.isInstance(result)) {
					context = (T) result;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return context;
	}

	public static String getFullContextString() {
		StringBuilder sb = new StringBuilder();
		Map<String, Context> data = CONTEXT_HOLDER.get();
		if (data == null) {
			sb.append("null");
		} else {
			sb.append("{ \"contexts\": {");
			boolean isFirst = true;
			for (Map.Entry<String, Context> entryItem : data.entrySet()) {
				String contextName = entryItem.getKey();
				Context contextData = entryItem.getValue();
				sb.append("\"" + contextName + "\": ");
				if (contextData == null) {
					sb.append("null");
				} else {
					sb.append(new Gson().toJson(contextData));
				}
				if (!isFirst) {
					isFirst = false;
					sb.append(",");
				}
			}
			sb.append("} }");
		}
		return sb.toString();
	}
}
