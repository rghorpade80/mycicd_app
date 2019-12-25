package com.pvmsys.brix.graph.search;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HttpRequest {

	String url = "";
	String type = "GET";

	String pathParams = "";
	String formParams = "";
	private String body = "";
	private Map<String, String> headers = new HashMap<>();

	public HttpRequest(String url, String type) {
		super();
		this.url = url;
		this.type = type;
	}

	public HttpRequest addHeader(String key, String value) {
		headers.put(key, value);
		return this;
	}

	@SuppressWarnings("deprecation")
	public HttpRequest addPathParam(String key, String value) {
		if (pathParams.length() == 0) {
			pathParams = key + "=" + URLEncoder.encode(value);
		} else {
			pathParams += "&" + key + "=" + URLEncoder.encode(value);
		}
		return this;
	}

	@SuppressWarnings("deprecation")
	public HttpRequest addFormParam(String key, String value) {
		if (formParams.length() == 0) {
			formParams = key + "=" + URLEncoder.encode(value);
		} else {
			formParams += "&" + key + "=" + URLEncoder.encode(value);
		}
		return this;
	}

	public HttpRequest setBody(String body) {
		this.body = body;
		return this;
	}

	public JsonObject getResponseAsJsonObject() {
		JsonParser parser = new JsonParser();
		String output = getResponseAsString();
		if (output == null || output.length() == 0) {
			return new JsonObject();
		}
		return (JsonObject) parser.parse(output);
	}

	private HttpURLConnection conn = null;

	public String getHeader(String key) {
		if (conn == null) {
			return null;
		}
		String value = conn.getHeaderField(key);
		return value;
	}

	public String getResponseAsString() {
		StringBuilder builder = new StringBuilder();
		try {
			URL url;
			if (pathParams.length() > 0) {
				url = new URL(this.url + "?" + this.pathParams);
			} else {
				url = new URL(this.url);
			}
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod(this.type);
			for (Entry<String, String> entry : headers.entrySet()) {
				conn.setRequestProperty(entry.getKey(), entry.getValue());
			}

			// Send request
			if (formParams.length() > 0 || body.length() > 0) {
				DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
				if (formParams.length() > 0) {
					wr.writeBytes(formParams);
				} else {
					if (body.length() > 0) {
						wr.writeBytes(body);
					}
				}
				wr.flush();
				wr.close();
			}
			
//			System.out.println(conn.toString());
			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()), "UTF-8"));
			String output = br.readLine();
			while (output != null) {
				builder.append(output);
				output = br.readLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

	public void close() {
		try {
			conn.disconnect();
		} catch (Exception e) {
		}
	}
}
