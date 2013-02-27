package tecent;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.mpupa.crawler.Crawler;
import com.mpupa.crawler.nodes.Document;

public class OAuth {
	private HttpClient client;
	private String APP_ID="801296657";
	private String APP_SECRET="59d26e32e13f9bb994aaeb3efd06a4bf";
	
	
	public String getCode() throws ClientProtocolException, IOException{
		String url="https://open.t.qq.com/cgi-bin/oauth2/authorize?client_id="+APP_ID+"&response_type=code&redirect_uri=http://www.baidu.com";
		Document doc=Crawler.connect(url).get();
		System.out.println(doc);
		return null;
	}
	
	
	public void OAuth(){
		if(null==client){
			client=new DefaultHttpClient();
		}
	}
	
	
	public static void main(String[] args) {
		OAuth oa=new OAuth();
		try {
			oa.getCode();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
