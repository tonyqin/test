package test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.mpupa.crawler.Crawler;
import com.mpupa.crawler.nodes.Document;
import com.mpupa.crawler.nodes.Element;
import com.mpupa.crawler.select.Elements;

public class RenRen {

	static String email = "84629989@qq.com";
	static String pwd = "qwb272503691";
	private static HttpClient client;
	static String userId = "";
	static String requestocken = "";
	static String rtk = "";
	static String statusContent = "hello everyone!";

	// ---------------------
	String ak = "";
	String profilever = "";

	// ------friends---------
	String[] friendList;
	String friendName="";

	RenRen() throws HttpException, IOException {
		if (client == null || "".equals(client)) {
			login(getLoginUrl());
		}
	}

	public static void login(String url) {
		// 创建GET方法的实例
		GetMethod getMethod = new GetMethod(url);
		// 使用系统提供的默认的恢复策略
		getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		try {
			// 执行getMethod
			int statusCode = client.executeMethod(getMethod);
			// System.out.println(statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "
						+ getMethod.getStatusLine());
			}
			// 读取内容
			byte[] responseBody = getMethod.getResponseBody();
			// 处理内容

			// get rtk;
			rtk = Crawler.parse(new String(responseBody)).toString()
					.split("get_check_x")[1];
			rtk = rtk.substring(rtk.indexOf("'") + 1, rtk.indexOf(",") - 1);
			System.out.println(rtk);

			requestocken = getRequestToken(Crawler.parse(
					new String(responseBody)).toString());
			System.out.println(requestocken);

			String uu = new String(responseBody).split("XN.user.id = '")[1];
			userId = uu.substring(0, uu.indexOf("'"));

			// System.out.println(new String(responseBody,"UTF-8"));
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address!");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("找不到相关地址");
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
	}

	private static String getLoginUrl() throws HttpException, IOException {
		client = new HttpClient();
		HttpMethod method = readyToLogin(); // 使用 POST 方式提交数据
		client.executeMethod(method); // 打印服务器返回的状态
		String response = new String(method.getResponseBody(), "GBK");
		String result = "http:"
				+ response.substring(response.lastIndexOf(":") + 1,
						response.lastIndexOf("\""));
		method.releaseConnection();
		return result;
	}

	// login in the xiaonei.com
	private static HttpMethod readyToLogin() {
		PostMethod post = new PostMethod(
				"http://www.renren.com/ajaxLogin/login");
		NameValuePair userName = new NameValuePair("email", email);
		NameValuePair password = new NameValuePair("password", pwd);
		NameValuePair icode = new NameValuePair("icode", "");
		NameValuePair origURL = new NameValuePair("origURL",
				"http://www.renren.com/home");
		NameValuePair domain = new NameValuePair("domain", "renren.com");
		NameValuePair key_id = new NameValuePair("key_id", "1");
		NameValuePair _rtk = new NameValuePair("_rtk", "17408aaa");
		post.setRequestBody(new NameValuePair[] { userName, password, icode,
				origURL, domain, key_id, _rtk });
		return post;
	}

	// ready to status.
	private static HttpMethod status() throws UnsupportedEncodingException {
		PostMethod post = new PostMethod("http://shell.renren.com/" + userId
				+ "/status");
		NameValuePair requestToken = new NameValuePair("requestToken",
				requestocken);
		NameValuePair hostid = new NameValuePair("hostid", userId);
		NameValuePair content = new NameValuePair("content", new String(
				statusContent.getBytes(), "iso-8859-1"));
		NameValuePair channel = new NameValuePair("channel", "renren");
		NameValuePair _rtk = new NameValuePair("_rtk", rtk);
		post.setRequestBody(new NameValuePair[] { requestToken, hostid,
				content, channel, _rtk });
		return post;
	}

	// get request token.
	public static String getRequestToken(String homeHtml) {
		Pattern pattern = Pattern.compile("requesttoken=(-?[1-9][0-9]*)");
		Matcher m = pattern.matcher(homeHtml);
		if (m.find()) {
			return m.group(1);
		}
		return null;
	}

	// set status.
	@SuppressWarnings("unused")
	private void setStatus() throws HttpException, IOException {
		HttpMethod method = status(); // 使用 POST 方式提交数据
		client.executeMethod(method);
		System.out.println(method.getStatusLine()); // 打印结果页面
		method.releaseConnection();
	}

	@SuppressWarnings("unused")
	private void Message(String url, String message, int ids)
			throws HttpException, IOException {
		PostMethod post = new PostMethod("http://gossip.renren.com/gossip.do");
		NameValuePair requestToken = new NameValuePair("body", message);
		NameValuePair curpage = new NameValuePair("curpage", "");
		NameValuePair from = new NameValuePair("from", "main");
		NameValuePair id = new NameValuePair("id", ids + "");
		NameValuePair cc = new NameValuePair("cc", ids + "");
		NameValuePair ak = new NameValuePair("ak", this.ak);
		NameValuePair cccc = new NameValuePair("cccc", "");

		NameValuePair tsc = new NameValuePair("tsc", "");
		NameValuePair profilever = new NameValuePair("profilever",
				this.profilever);
		NameValuePair headUrl = new NameValuePair("headUrl", "");
		NameValuePair largeUrl = new NameValuePair("largeUrl", "");

		NameValuePair reToken = new NameValuePair("requestToken", requestocken);
		NameValuePair _rtk = new NameValuePair("_rtk", rtk);
		NameValuePair ref = new NameValuePair("ref", url);
		NameValuePair only_to_me = new NameValuePair("only_to_me", "0");

		post.setRequestBody(new NameValuePair[] { requestToken, curpage, from,
				id, cc, ak, cccc, tsc, profilever, headUrl, largeUrl, reToken,
				_rtk, ref, only_to_me });

		int status = client.executeMethod(post);
		System.out.println(status);
		System.out.println(new String(post.getResponseBody()));
		// post.releaseConnection();
	}

	@SuppressWarnings("unused")
	private void visit(String url) {

		GetMethod method = new GetMethod(url);
		// 使用系统提供的默认的恢复策略
//		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
//				new DefaultHttpMethodRetryHandler());
		try {
			int status = client.executeMethod(method);
			System.out.println(status);

//			 try{
//				 ak = Crawler.parse(new String(method.getResponseBody(), "utf-8"))
//							.getElementsByAttributeValue("name", "ak").first().val();
//					System.out.println(ak);
//
//					profilever = Crawler
//							.parse(new String(method.getResponseBody(), "utf-8"))
//							.getElementsByAttributeValue("id", "profilever").val();
//					System.out.println(profilever);
//			 }catch(Exception ex){
//				 return;
//			 }
			
			// System.out.println(new String(method.getResponseBody(),
			// "utf-8"));
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		method.releaseConnection();
	}

	public static void main(String[] args) throws HttpException, IOException,
			InterruptedException {
		RenRen renren = new RenRen();

		// int id = 222301563;
	 //for(int id=222302573;id<322352673;id++){
	//	 System.out.println(id + "---------");
//		// for(int i=0;i<100;i++){
		// renren.visit("http://www.renren.com/" + id);
	 //}
		// renren.Message("http://www.renren.com/" + id, "hello,i5.", id);
		// Thread.sleep(500);
		// }
		renren.setStatus();
//		renren.getFriends();
//		for (int j = 176; j < renren.friendList.length; j++) {
////			// renren.visit("http://www.renren.com/" + renren.friendList[j]);
	  		//	renren.getFriendsPhotos(renren.friendList[j]);
//		try {
//				renren.getFriendsPhotos(renren.friendList[j]);
//			if("225792694".equals(renren.friendList[j])){
//				System.out.println(j+"=====");
//				break;
//			}
//			renren.getArticle(renren.friendList[j]);
//				//	renren.getProfile(renren.friendList[j]);
//			} catch (Exception ex) {
//				//ex.printStackTrace();
//				continue;
//			}
			//renren.getArticle("223526280");
	//	renren.getFriendsPhotos(renren.friendList[j]);
			// renren.Message("http://www.renren.com/" + renren.friendList[j],
			// "hello,i5.", renren.friendList[j]);
			// System.out.println("--------------");
		//}
		// }

	}

	private void getProfile(String id, String friendName) throws HttpException, IOException {
		Document doc=requestByGetMethod("http://www.renren.com/"+id+"?v=info_ajax");
		String result=doc.getElementsByAttributeValue("class", "info-edit-sections").first().text();
		if(result.indexOf("联系方式")!=-1){
			result=result.substring(0,result.indexOf("联系方式"));
		}
		//friendName="高书慧";
		saveToFile("f:/renren/"+friendName+".doc","----------------------------------------\n".getBytes());
		saveToFile("f:/renren/"+friendName+".doc",result.getBytes());
		System.out.println(result);
	}

	// 得到所有好友的信息。
	@SuppressWarnings("unused")
	private void getFriends() throws HttpException, IOException {
		GetMethod method = new GetMethod(
				"http://friend.renren.com/myfriendlistx.do");
		// 使用系统提供的默认的恢复策略
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler());
		int status = client.executeMethod(method);
		System.out.println(status);
		// System.out.println(new String(method.getResponseBody(), "utf-8"));
		// 获得好友列表
		String friends = new String(method.getResponseBody(), "utf-8");
		friends = friends.substring(friends.indexOf("friends=["),
				friends.indexOf("var hotfriends"));
		friendList = new String[friends.split("\"id\":").length - 1];
		for (int s = 1; s < friends.split("\"id\":").length; s++) {
			friendList[s - 1] = friends.split("\"id\":")[s].substring(0,
					friends.split("\"id\":")[s].indexOf(","));
		}
	}

	// 得到好友的所有相册中的相片地址
	@SuppressWarnings("unused")
	private void getFriendsPhotos(String id) throws HttpException, IOException {
		
		String friendUrl = "http://photo.renren.com/getalbumlist.do?id=" + id;
		Document doc = requestByGetMethod(friendUrl);
		friendName = doc.getElementsByAttributeValue("class", "nav-tabs")
				.first().getElementsByTag("a").first().text();
		System.out.println(friendName);
		//saveToFile("f:renren/"+friendName+".doc","--------------------------------\n".getBytes());
		Elements lis = doc.getElementsByAttributeValue("class", "album-home")
				.first().getElementsByTag("li");
		System.out.println(doc
				.getElementsByAttributeValue("class", "album-home").first()
				.getElementsByTag("li").size());
		for (Element e : lis) {
			String AlbumUrl = e.getElementsByTag("a").first().attr("href");
			String title = e.getElementsByTag("a").eq(1).text();
			//saveToFile("f:renren/"+friendName+".doc",(title+"\n").getBytes());
			//System.out.println(title);
			try {
				getPhotosFromAlbum(AlbumUrl,friendName,id,title);
			} catch (Exception ex) {
				continue;
			}

			System.out.println("------------------------");
			// break;
		}
		//getProfile(id,friendName);
	}

	private void getPhotosFromAlbum(String albumUrl,String name, String id, String title) throws HttpException,
			IOException {
		Document doc = requestByGetMethod(albumUrl);
		Elements photoList = doc
				.getElementsByAttributeValue("class", "photo-list my-list")
				.first().getElementsByTag("a");
		
		File f=new File("f:renrenImg/"+name+"_"+id+"/"+title);
		if(!f.exists()){
			f.mkdirs();
		}
		
		int index=0;
		for (Element e : photoList) {
			String photoUrl = e.attr("href");
			Document page = requestByGetMethod(photoUrl);
			System.out.println(page.getElementById("photo").attr("src"));
			
			
			
			String path=f.getAbsolutePath()+"/"+index+".jpg";
			
			downLoadImg(page.getElementById("photo").attr("src"),path);
			index++;
			//saveToFile("f:renren/"+name+".doc",(page.getElementById("photo").attr("src")+"\n").getBytes());
		}
	}

	public Document requestByGetMethod(String url) throws HttpException,
			IOException {
		Document result = null;
		GetMethod get = new GetMethod(url);
		int status = client.executeMethod(get);
		if (status == HttpStatus.SC_OK) {
			result = Crawler.parse(get.getResponseBodyAsString());
		}
		return result;
	}

	// 关于日志的
	private void getArticle(String id) throws HttpException, IOException {
		String articleUrl = "http://blog.renren.com/blog/" + id
				+ "/friends?from=friendEnd";
		int i=1;
		while (true) {
			Document doc = requestByGetMethod(articleUrl);
			if("".equals(friendName)){
				friendName=doc.getElementsByAttributeValue("class", "nav-tabs Third").first().getElementsByTag("a").first().text();
			}
			Elements artList = doc
					.getElementsByAttributeValue("class", "blog-home").first()
					.getElementsByAttributeValue("class", "list-blog");
			if(artList.size()==0)break;
			System.out.println(artList.size());
			System.out.println();
			
			for (Element e : artList) {
				try{
					System.out.println("--------------------------分割线---------------------------------");
					 saveToFile("f:renren/"+friendName+".doc", "--------------------------分割线---------------------------------\n".getBytes());
					String detail = e
							.getElementsByAttributeValue("class",
									"stat-article bottom-margin").first()
							.getElementsByTag("a").first().attr("href");
					getArticleDetails(detail,friendName);
				}catch(Exception ex){
					continue;
				}
				
			}
			i=i+1;
			articleUrl = "http://blog.renren.com/blog/"+id+"/friends?curpage="+i+"&year=0&month=0&selitem=";
		}
	}

	// 到文章的具体页面获取信息。
	private void getArticleDetails(String detail, String name) throws HttpException,
			IOException {
		Document articles = requestByGetMethod(detail);
		String title = articles
				.getElementsByAttributeValue("class", "title-article").first()
				.getElementsByTag("strong").first().text();
		String time = articles
				.getElementsByAttributeValue("class", "title-article").first()
				.getElementsByAttributeValue("class", "timestamp").first()
				.text();
		String content = articles
				.getElementsByAttributeValue("class", "text-article").first()
				.text();
		saveToFile("f:renren/"+name+".doc",(title+"\n").getBytes());
		saveToFile("f:renren/"+name+".doc",(time+"\n").getBytes());
		saveToFile("f:renren/"+name+".doc",(content+"\n").getBytes());
		System.out.println(title);
		System.out.println(time);
		System.out.println(content);
	}
	public static void saveToFile(String fileName, byte[] data) {
		try {
			// File file=new File(fileName);
			// true == append else not.
			new FileOutputStream(fileName, true)
					.write((new String(data) + "\t").getBytes());
			//System.out.println("write into the file successfully !");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void downLoadImg(String urls,String path) throws IOException{
		   URL url = new URL(urls);  
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	        conn.setRequestMethod("GET");  
	        conn.setConnectTimeout(5 * 1000);  
	        InputStream inStream = conn.getInputStream();  
	        byte[] data = readInputStream(inStream);  
	        File imageFile = new File(path);  
	        FileOutputStream outStream = new FileOutputStream(imageFile);  
	        outStream.write(data);  
	        outStream.close();  
	}

	private static byte[] readInputStream(InputStream inStream) throws IOException {
		 ByteArrayOutputStream outStream = new ByteArrayOutputStream();  
	        byte[] buffer = new byte[1024];  
	        int len = 0;  
	        while( (len=inStream.read(buffer)) != -1 ){  
	            outStream.write(buffer, 0, len);  
	        }  
	        inStream.close();  
	        return outStream.toByteArray();  
	}
}