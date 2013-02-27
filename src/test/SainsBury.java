package test;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;


public class SainsBury {
	private static String url="http://www.sainsburys.co.uk";
	
	public static void main(String[] args) throws HttpException, IOException {
		HttpClient client=new HttpClient();
		HttpMethod method=new GetMethod(url);
		// 使用系统提供的默认的恢复策略
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
						new DefaultHttpMethodRetryHandler());
		client.executeMethod(method);
	
		System.out.println(method.getResponseBodyAsString());
		
		System.out.println(client.getParams().getCookiePolicy());
		
	}
}
