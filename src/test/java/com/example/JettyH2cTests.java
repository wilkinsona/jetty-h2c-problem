package com.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequests;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.junit.jupiter.api.Test;

class JettyH2cTests {
	
	@Test
	void test() throws Exception {
		Server server = new Server();
		server.setHandler(new ExampleHandler());

		HttpConfiguration httpConfiguration = new HttpConfiguration();
		ServerConnector serverConnector = new ServerConnector(server,
				new HttpConnectionFactory(httpConfiguration),
				new HTTP2CServerConnectionFactory(httpConfiguration));
		serverConnector.setPort(8080);
		server.setConnectors(new ServerConnector[] { serverConnector });
		
		server.start();
		
		try (CloseableHttpAsyncClient http2Client = HttpAsyncClients.createHttp2Default()) {
			http2Client.start();
			SimpleHttpRequest request = SimpleHttpRequests.get("http://localhost:8080");
			SimpleHttpResponse response = http2Client.execute(request, new FutureCallback<SimpleHttpResponse>() {

				@Override
				public void failed(Exception ex) {
				}

				@Override
				public void completed(SimpleHttpResponse result) {
				}

				@Override
				public void cancelled() {
				}

			}).get();
			assertThat(response.getCode()).isEqualTo(200);
			assertThat(response.getBodyText()).isEqualTo("OK");
		}
		finally {
			server.stop();
		}
	}
	
	static class ExampleHandler extends AbstractHandler {

		@Override
		public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
				throws IOException, ServletException {
			response.getWriter().print("OK");
			response.setStatus(200);
			response.flushBuffer();
		}
		
	}

}
