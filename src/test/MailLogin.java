package test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory;

import com.mpupa.crawler.Crawler;
import com.mpupa.crawler.nodes.Document;
import com.mpupa.crawler.nodes.Element;
import com.mpupa.crawler.select.Elements;

public class MailLogin {
	private String key="";
	HttpClient loginStatusClient;
	private String userName="aqh518@126.com";
	private String password="qwb272503691";
	public HttpClient getLoginStatusClient() {
		return loginStatusClient;
	}

	public void setLoginStatusClient(HttpClient loginStatusClient) {
		this.loginStatusClient = loginStatusClient;
	}

	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	private HttpClient visit(String url) {
		//新建一个client 对象，并且设置相关参数。
		HttpClient client=new HttpClient();
		client.getParams().setParameter(HttpMethodParams.USER_AGENT,"Mozilla/5.0 (X11; U; Linux i686; zh-CN; rv:1.9.1.2) Gecko/20090803 Fedora/3.5.2-2.fc11 Firefox/3.5.2");//设置信息 
		@SuppressWarnings("deprecation")
		Protocol myhttps = new Protocol("https", new SSLProtocolSocketFactory(), 443);
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		client.getHostConfiguration().setHost("ssl.mail.126.com",443,myhttps);
		Protocol.registerProtocol("https", myhttps);
		//设定post 的相关参数。
		PostMethod  post=new PostMethod(url);
		NameValuePair userName=new NameValuePair("username",this.userName);
		NameValuePair savelogin = new NameValuePair("savelogin", 0+"");
		NameValuePair url2 = new NameValuePair("url2", "http://mail.126.com/errorpage/err_126.htm");
		NameValuePair password = new NameValuePair("password",
				this.password);
		post.setRequestBody(new NameValuePair[] { userName, password, url2,
				password});
		
		try {
			int i=client.executeMethod(post);
			if(i==200){
				String urls=post.getResponseBodyAsString();
				urls=urls.substring(urls.indexOf("http://"),urls.indexOf(";</script>")-1);
				System.out.println(urls);
				setKey(urls.substring(urls.lastIndexOf("=")+1));
				GetMethod get=new GetMethod(urls.replace("main","index"));
				client.executeMethod(get);
				//System.out.println(get.getResponseBodyAsString());
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return client;
	}
	
	
	public static void main(String[] args) throws HttpException, IOException {
		MailLogin mail=new MailLogin();
		mail.setLoginStatusClient(mail.visit("https://ssl.mail.126.com/entry/cgi/ntesdoor?hid=10010102&funcid=loginone&df=webmail126&language=-1&passtype=1&verifycookie=-1&iframe=1&from=web&net=failed&product=mail126&style=-1&race=-2_-2_-2_db&uid=aqh518@126.com"));
		//mail.gotoMainPage("http://twebmail.mail.126.com/js4/index.jsp?sid="+mail.getKey()+"#module=mbox.ListModule_0|{%22fid%22%3A1}");
		//获得收件箱所有邮件
		mail.gotoMainPage("http://twebmail.mail.126.com/js4/s?sid="+mail.getKey()+"&func=mbox:listMessages");
		//mail.ListFriends();
	}
	
	private void ListFriends() throws HttpException, IOException {
		String url="http://tg1a89.mail.126.com/coremail/fcg/ldvcapp?sid="+getKey()+"&listnum=20&tempname=address%2Faddress.htm";
		HttpClient client=getLoginStatusClient();
		GetMethod get=new GetMethod(url);
		client.executeMethod(get);
		System.out.println(get.getResponseBodyAsString());
	}

	private void gotoMainPage(String url) {
		HttpClient client=getLoginStatusClient();
		GetMethod get=new GetMethod(url);
		try {
			client.executeMethod(get);
			//System.out.println(get.getResponseBodyAsString());
			Document doc=Crawler.parse(get.getResponseBodyAsString());
			Elements mids=doc.getElementsByAttributeValue("name", "id");
			
			//getMailConnect(mids.get(1).text());
			int i=0;
			for(Element mid:mids){
				System.out.println(mid.text());
				getMailConnect(mid.text());
				System.out.println((++i)+"--------------");
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void getMailConnect(String mid) throws HttpException, IOException {
		//qE2ygTde+xZ8Y6VqSzSFuTVzg55G8T9H71KcsKV8bTk=
		String url="http://twebmail.mail.126.com/js4/read/readhtml.jsp?sid="+getKey()+"&mid="+mid+"&fid=1";
		HttpClient client=getLoginStatusClient();
		GetMethod get=new GetMethod(url);
		client.executeMethod(get);
		//System.out.println(Crawler.parse(get.getResponseBodyAsString()).text());
		saveToFile("e:/"+this.userName+".txt", (Crawler.parse(get.getResponseBodyAsString()).text()).toString()+"\r\n--------------------------------------\r\n");
	}

	private void saveToFile(String fileName, String data) {
		try {
			new FileOutputStream(fileName, true)
					.write((new String(data) + "\t").getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
