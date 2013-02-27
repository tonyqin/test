package test;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;


public class Tesco {
	
	final String ROOTPATH="https://secure.tesco.com/register/?from=http%3a%2f%2fwww.tesco.com%2fgroceries%2f";
	HttpClient client;
	public Tesco(){
		if(client==null||"".equals(client)){
			client=new HttpClient();
			Protocol myhttps = new Protocol("https", new MySecureProtocolSocketFactory (), 443); 
			Protocol.registerProtocol("https ", myhttps);
		}else{
			try {
				login();
			} catch (HttpException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void login() throws HttpException, IOException {
		GetMethod get=new GetMethod(ROOTPATH);
		get.addRequestHeader("Host","secure.tesco.com");
		get.addRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:17.0) Gecko/17.0 Firefox/17.0");
		get.addRequestHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		get.addRequestHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
		get.addRequestHeader("Accept-Encoding","gzip, deflate");
		get.addRequestHeader("Connection","keep-alive");
	//	get.addRequestHeader("Cookie","sessionTest=True; v=IXgBACo2pYxaJ0K7VEan1MnCDCajWqHdf0kB33INmgoD28VhFazpysiWlQ%2fceA75eYQNOgd%2bcXapYRtW7stpdeKjU7tZXh0PKZtTKsx0ILVFWiRre2TUG%2bT5uc3kQ%2fJTbl82r3jTJX%2fYwyGsHHg%2fnJ60Neo5AHh75aOk5Ml4G%2bgxt8AqFUtmDVzlUHgv8sC9lwl6%2beWOGXNxJKBh%2fdEYZz4k8ES8; u=e30BACqoIs6kdVLMnqFWLkIkROZiUV6dBGCr%2bORyYb02PIOJk4LpC1ngpET4n42vySNF2T7GGNH0rM15GMd6Z5eVn7Lm7aqr8EYkoRvuTvMOq%2f9ADo1YJiWIX3Kpft26ArqZXNkJhd8HogXhyDbT4o0LojfIAHO59cnDg7RNw6jEPkz%2bpAoUMUNe4UviqGi2jfOrG46fzfqfJgD6Eqo6VM5hwARh; t=BawBACrGEv%2fkt01ugYCUEPAGTmXk33Li3ff9AQhy6BkDH6phpUwEyQ2BZzy1GazYuZDiCvfxQc3iQgNsWPDSAn9Xs3Gby8znBTq8p8yqhl0G0Dn2upamGoRZobjuckozBgZdlCWZky5CLTf7oi88KyRxKCuzxA3cTQqXEhSMUOKtK2f8%2fg%3d%3d");
		get.addRequestHeader("Cache-Control","max-age=0");
		client.executeMethod(get);
		String login_page=get.getResponseBodyAsString();
	System.out.println(login_page);
		
//		//----login
//		String login_url="https://secure.tesco.com/register/default.aspx?vstore=0";
//		PostMethod post=new PostMethod(login_url);
//		post.addParameter("form", "fSignin");
//		post.addParameter("form", "http://www.tesco.com/groceries/");
//		post.addParameter("formData", "bmV3UmVnPXRydWUm");
//		post.addParameter("loginID", "claire@m-pupa.com");
//		post.addParameter("password", "mpupa2012");
//		post.addParameter("seamlesswebtag", "");
//		post.addParameter("confirm-signin.x", "0");
//		post.addParameter("confirm-signin.y", "0");
//		
//		post.addRequestHeader("Host","secure.tesco.com");
//		post.addRequestHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:17.0) Gecko/17.0 Firefox/17.0");
//		post.addRequestHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//		post.addRequestHeader("Accept-Language","zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
//		post.addRequestHeader("Accept-Encoding","gzip, deflate");
//		post.addRequestHeader("Connection","keep-alive");
//		post.addRequestHeader("Cookie","v=HHgBACrktYDtpZDhyGy%2fySWtHew%2foEdfdSKQ7SsHZh3Y%2fKdHLoHcEQh5DH6f5SjS%2bpu%2f8KzIDPseey7tKDZFlw3Rz3z9poD7GWra4FFwx4PK9rwwz94IFC4Q5v%2beR7%2by1CuH%2feTNO5jgQiY2vxU0DJ16FKFNz%2f9DwQvktIgB%2fMY7hsICFbyI24Nr%2bSpg3JMagD6mszbRy2s6I%2faLXac6slbKXWtd5AU5y2Lj4yOY1IKDewcZntpUDt5gv1RGpaMHf7UZoCQGhWjCAW%2b4jmpZ%2flGZkk133Xw4MQqCpyny7DDJK7CT1NEuOZquZKF6RZBlc%2b5qeK4y%2fir8ggbpXlhyonCnebh9179AXe5Ov8D13q6V5NmKH%2bhVBHWVn9S6JDycd7dYmo8%3d; CustomerId=9908970514016409451217129746991109; BTCCMS=vZtLHy6axCZ5rKKbDvS1SEdBj5d%2fP%2f6FhbWdqcL1CEkqiyFw3wYCTud3p61WrVjCQ%2bw3I%2brIKoXYWyJyqHjdXQ%3d%3d; PS=CSC=M; SSVars=CustomerName=Miss%2c+Si&basketTotal=&basketCount=&slotStart=&slotEnd=&addrName=; s=BitSt=1%3A0%3A20000010%2C2%3A51943EF%3A1&ver=1&UIMode=online&BSKT=85541871&LAT=2&swvm=0&ANote=&ShpUrl=0; mbox=session#1354509324463-669995#1354513485|PC#1354509324463-669995.18_08#1355721225|check#true#1354511685; s_cc=true; gpv_p15=Tesco%20Homepage; s_nr=1354511624468-New; s_ria=flash%2011%7C; s_sq=tescoukgroceriesprod%3D%2526pid%253DGroceries%25253AHomepage%2526pidt%253D1%2526oid%253Dhttps%25253A//secure.tesco.com/register/signout.aspx%25253Ffrom%25253D/groceries/Default.aspx%2526ot%253DA; s_vi=[CS]v1|285E1604051D19DD-600001038001318A[CE]; u=dH0BACq39ROdt5kGfjj76ODO945EEGKT7itS%2bomnsHkIcaen%2flDqflvn6BXG8NXixHO7W3GVP%2btziXh7TBCrHBUReLKC8OhO1qHQXOs5p2MvYGQmQasOmLi5uQ%2bZ9P%2bivsTIIN0%3d; t=BawBACqZUcl3PqwHm8NAp%2fLZAsKOiB8tWVHLu9Pd%2fUfG4r%2fKtb8cwnCIBubNGOOaksYmFyOBLOSrtT6%2fNQMQ0ctzV6%2fYw9uJWFmys5VpJR3f%2bJ%2f2n0FVV8AIj7Dg%2b%2bEAbaE%2blHU%3d");
//		post.addRequestHeader("Content-Type","application/x-www-form-urlencoded");
//		
//		client.equals(post);
//		Header[] header=post.getResponseHeaders();
//		for(Header he:header){
//			System.out.println(he);
//		}
//		String result=post.getResponseBodyAsString();
//		System.out.println(result);
	}


	public static void main(String[] args) {
		Tesco tesco=new Tesco();
		try {
			tesco.login();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
