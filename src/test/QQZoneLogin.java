package test;

import java.io.IOException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.cookie.CookieSpec;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class QQZoneLogin {
	
	
	public static void main(String[] args) {
		QQZoneLogin login=new QQZoneLogin();
		try {
			login.login("http://qzone.qq.com/");
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void login(String url) throws HttpException, IOException {
		HttpClient client =new HttpClient();
		CookieSpec cookiespec = CookiePolicy.getDefaultSpec();
		GetMethod method=new GetMethod(url);
		// 使用系统提供的默认的恢复策略
				method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
						new DefaultHttpMethodRetryHandler());
				method.getParams().setParameter("http.useragent","Mozilla/4.0 (compatible; MSIE 5.5; Windows 98)");
		client.executeMethod(method);
		System.out.println(method.getResponseBodyAsString());
	}
}
