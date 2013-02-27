package test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.mpupa.crawler.Crawler;
import com.mpupa.crawler.nodes.Document;
import com.mpupa.crawler.nodes.Element;
import com.mpupa.crawler.select.Elements;

public class Pwork {
	static StringBuffer sb=new StringBuffer();
	public static void main(String[] args) {
		
		try {
			String sourceUrl="http://data.simuwang.com/product.php?page=1";
			int pageNum=1;
			while(true){
				Document doc = Crawler.connect(sourceUrl)
						.userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:12.0) Gecko/20100101 Firefox/12.0").get();
				Elements e = doc
						.getElementsByAttributeValue("id", "data_tb").first().getElementsByTag("tbody").first().getElementsByTag("tr");
				System.out.println(e.size());
				int i = 0;
				for (Element ee : e) {
					//if (i > 0) {
						String temp = ee.toString();
						if (temp.indexOf("id=") != -1) {
							String url = "http://data.simuwang.com/"
									+ ee.getElementsByTag("a").first().attr("href");
							String jiancheng=ee.getElementsByTag("a").first().text();
							System.out.println(jiancheng);
							connect(url,jiancheng);
						}
					//}
					i++;
					System.out.println(i + "------------");
				}
				pageNum++;
				if(pageNum==157)break;
				sourceUrl=sourceUrl.substring(0,sourceUrl.indexOf("=")+1)+pageNum;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
//		 try {
//		 connect("http://data.simuwang.com/fund_info.php?id=1388");
//		 } catch (IOException e) {
//		 // TODO Auto-generated catch block
//		 e.printStackTrace();
//		 }
	}

	public static void connect(String url, String jiancheng) throws IOException {
		Document page = Crawler.connect(url).get();
		String id = url.substring(url.indexOf("=") + 1).trim();
		// System.out.println(page);
		
		processJjysb(page,id,jiancheng);
		// ---------------------1,product Fund

		processProductPerformance(page, id);

		
		// --------------------2,Ji jin zhong chang gu
		processAwkwardnessOfFund(page,id);

		// --------------------3,product info
		processProductInfo(page,id);

		// --------------------4,history...
		processHistoricalNet(page,id);
		// --------------------5,about fund manager
		try{
			processFundManager(page,id);
		}catch(Exception ex){
			System.out.println("No manager。。。。。。。。");
		}
	}

	private static String processRating(Document page, String id) {
		Elements infos = page.getElementsByAttributeValue("class", "ResInfo")
				.first().getElementsByTag("tr");
		// 获得期间标志信息
				StringBuffer tt=new StringBuffer();
				Elements temp = infos.first().getElementsByTag("td");
				//String nowPeriod = temp.eq(1).text();
				//String lastPeriod = temp.eq(2).text();
				String nowRating=infos.eq(infos.size()-1).first().getElementsByTag("td").eq(1).first().getElementsByTag("img").size()+"";
				String lastRating=infos.eq(infos.size()-1).first().getElementsByTag("td").eq(2).first().getElementsByTag("img").size()+"";
				System.out.println(nowRating+"----"+lastRating);
				//tt.append("insert into pjb(fund_dm,fund_aefpj,fund_yf) values('dcjj_"+id+"','"+nowRating+"','"+nowPeriod+"');\n");
				//tt.append("insert into pjb(fund_dm,fund_aefpj,fund_yf) values('dcjj_"+id+"','"+lastRating+"','"+lastPeriod+"');\n");
				//saveToFile("d:/pjb.txt", tt.toString().getBytes());
				return nowRating+"--"+lastRating;
	}

	private static void processJjysb(Document page, String id, String jiancheng) {
		Elements infos = page.getElementsByAttributeValue("class", "ResInfo")
				.eq(2).first().getElementsByTag("td");
		StringBuffer tt=new StringBuffer();
		int i = 0;
		tt.append("insert into jjysb(fund_dm,fund_jjjc,fund_jjqc) values('dcjj_"+id+"','"+jiancheng+"',");
		for (Element info : infos) {
			if(i==1){
				String fieldValue = info.text();
				tt.append("'"+fieldValue+"'");
				System.out.println(fieldValue);
				break;
			}
			i++;
		}
		tt.append(");\r\n");
		saveToFile("f:/jjysb.txt", tt.toString().getBytes());
		
	}

	private static void processFundManager(Document page, String id) throws IOException {
		System.out.println("------------------------manager------------------");
		Element manager=page.getElementsByAttributeValue("class", "ManInfo").first();
		//manager name
		String mName=manager.getElementsByTag("a").eq(1).text();
		System.out.println(mName);
		//profile
		String managerUrl=manager.getElementsByTag("a").first().attr("href");
		String mInfo=manager.getElementsByTag("p").first().text();
		System.out.println(mInfo);
		//smallImg
		String managerSPhoto=manager.getElementsByTag("img").first().attr("src");
		System.out.println(managerSPhoto);
		downLoadImg(managerSPhoto,id+"Small");
		connectToManager(managerUrl,mName,mInfo,managerSPhoto,id);
	}

	private static void connectToManager(String managerUrl, String mName, String mInfo, String managerSPhoto, String id) {
		StringBuffer tt=new StringBuffer();
		try {
			Document mpage=Crawler.connect(managerUrl).get();
			//largeImg
			String managerLPhoto="http://www.simuwang.com/"+mpage.getElementsByAttributeValue("class", "renwu_tu").first().getElementsByTag("img").attr("src");
			System.out.println(managerLPhoto);
			downLoadImg(managerLPhoto,id+"Large");
			//company profile
			int size=mpage.getElementsByAttributeValue("class", "middle").first().child(0).getElementsByTag("span").size();
			String mcompany=mpage.getElementsByAttributeValue("class", "middle").first().child(0).getElementsByTag("span").eq(size-1).text();
			System.out.println(mcompany.replaceAll(" ",""));
			//conception
			String fg_wenzi="";
			try{
				fg_wenzi=mpage.getElementsByAttributeValue("class", "fg_wenzi").text();
			}catch(Exception ex){
				fg_wenzi="";
			}
			
			tt.append("insert into jjglgsb(fund_dm,fund_jjjl,fund_tzglgs,fund_jjjljj,fund_tzln,fund_small_p,fund_large_p) values('dcjj_"+id+"','"+mName+"','"+mcompany.replaceAll(" ","")+"','"+mInfo+"','"+fg_wenzi+"','"+managerSPhoto+"','"+managerLPhoto+"');\r\n");
			System.out.println(fg_wenzi);
		} catch (IOException e) {
			System.out.println("...........");
			return;
		}
		saveToFile("f:/jjglgsb.txt", tt.toString().getBytes());
	}

	private static void processHistoricalNet(Document page, String id) {
		int size = page.getElementsByAttributeValue("class", "timeInfo").size();
		Elements infos = page.getElementsByAttributeValue("class", "timeInfo")
				.eq(size - 1).first().getElementsByTag("tr");
		StringBuffer tt=new StringBuffer();
		int i=0;
		for (Element info : infos) {
			if(i>0){
				Elements temp = info.getElementsByTag("td");
				String data = temp.eq(0).text() + "~~" + temp.eq(1).text() + "~~"
						+ temp.eq(2).text();
				tt.append("insert into jzsjb(fund_dm,fund_gxsj,fund_jz,fund_ljjj) values('dcjj_"+id+
						"','" +temp.eq(0).text()+
						"','" +temp.eq(1).text()+
						"','" +temp.eq(2).text()+
						"');\r\n");
			}
			i++;
		}
		
		saveToFile("f:/jzsjb.txt", tt.toString().getBytes());

	}

	private static void processProductInfo(Document page, String id) {
		Elements infos = page.getElementsByAttributeValue("class", "ResInfo")
				.eq(2).first().getElementsByTag("td");
		StringBuffer tt=new StringBuffer();
		int i = 0;
		System.out.println("**************product data***************");
		tt.append("insert into jjjcxxb(fund_dm,fund_tzgw,fund_jjjl,fund_cplx,fund_clsj,fund_str,fund_qx,fund_zqjjr,fund_tgyh,fund_rgmk,fund_csgm,fund_fbq,fund_kfr,fund_rgf,fund_shf,fund_glf,fund_yjbc) values(");
		for (Element info : infos) {
			if (i % 2 == 0) {
				String fieldName = info.text();
				//System.out.print(fieldName + "~~");
			} else {
				
				//tt.append(tee);
				if(i>2){
					String fieldValue = info.text();
					//System.out.print(fieldValue + "\n");
					tt.append(",'"+fieldValue+"'");
				}else{
					tt.append("'dcjj_"+id+"'");
				}
			}
			i++;
		}
		tt.append(");\r\n");
		saveToFile("f:/jjjcxxb.txt", tt.toString().getBytes());
		//System.out.println("********************************");
	}

	private static void processAwkwardnessOfFund(Document page, String id) {
		Elements infos = page.getElementsByAttributeValue("class", "ResInfo")
				.eq(1).first().getElementsByTag("tr");
		StringBuffer tt=new StringBuffer();
		System.out.println("------------ji jin zhong cang gu-----------");
		int index=0;
		for (Element info : infos) {
			if(index>0){
				Elements temp = info.getElementsByTag("td");
				String data = temp.eq(0).text() + "~~" + temp.eq(1).text() + "~~"
						+ temp.eq(2).text() + "~~" + temp.eq(3).text();
				System.out.println(data);
				tt.append("insert  into ccb(fund_jjdm,fund_gpdm,fund_gpjc,fund_cgs) values('dcjj_"+id+"','"+temp.eq(1).text()+"','"+temp.eq(2).text()+"','"+temp.eq(3).text()+"');\r\n");
				System.out.println("-----------------------------");
			}
			index++;
		}
		saveToFile("f:/ccb.txt", tt.toString().getBytes());
	}

	private static void processProductPerformance(Document page, String id) {
		Elements infos = page.getElementsByAttributeValue("class", "ResInfo")
				.first().getElementsByTag("tr");
		System.out.println("qjbz:\t sjbq:\t syl:\t hs300:\t pm:\t");
		int i = 0;
		// get the info of qjbz
		Elements temp = infos.first().getElementsByTag("td");
		String nowPeriod = temp.eq(1).text();
		String lastPeriod = temp.eq(2).text();
		StringBuffer tt=new StringBuffer();
		String postData0 = nowPeriod.substring(nowPeriod.indexOf("(") + 1,
				nowPeriod.indexOf(")"));
		String postData1 = lastPeriod.substring(lastPeriod.indexOf("(") + 1,
				lastPeriod.indexOf(")"));
		// get pm info
		String result = getRanking(id, postData0, postData1);
		System.out.println(result);
		//--rating
		 String rating=processRating(page, id);
		 
		//---------rank
		String[] rankInfo,nowRankInfo,lastRankInfo;
		if(result!=null){
			 rankInfo = result.split("@@");
			 nowRankInfo = rankInfo[0].split("~~");
			 lastRankInfo = rankInfo[1].split("~~");
		}else{
			nowRankInfo=null;
			lastRankInfo=null;
		}
		for (Element info : infos) {
			if (i > 1 && i < infos.size() - 1) {
				Elements tds = info.getElementsByTag("td");
				String timeMark = info.getElementsByTag("td").first().text();
				// split now and last.
				String nowRank,lastRank;
				if(nowRankInfo==null){
					nowRank="";
					lastRank="";
				}else{
					nowRank=nowRankInfo[i - 2];
					lastRank=lastRankInfo[i - 2];
				}
				String data0 = nowPeriod + "~~" + timeMark + "~~"
						+ info.getElementsByTag("td").eq(1).text() + "~~"
						+ info.getElementsByTag("td").eq(2).text() + "~~"
						+ nowRank;
				String data1 = lastPeriod + "~~" + timeMark + "~~"
						+ info.getElementsByTag("td").eq(4).text() + "~~"
						+ info.getElementsByTag("td").eq(5).text() + "~~"
						+ lastRank;
				
				tt.append("insert into lshbsjb(fund_dm,fund_qjbz,fund_sjbq,fund_syl,fund_hssb,fund_pm,fund_aefpj)values('dcjj_"+id+"','"+nowPeriod+"','"+timeMark+"','"+info.getElementsByTag("td").eq(1).text()+"','"+info.getElementsByTag("td").eq(2).text()+"','"+nowRank+"','"+rating.split("--")[0]+"');\r\n");
				tt.append("insert into lshbsjb(fund_dm,fund_qjbz,fund_sjbq,fund_syl,fund_hssb,fund_pm,fund_aefpj)values('dcjj_"+id+"','"+lastPeriod+"','"+timeMark+"','"+info.getElementsByTag("td").eq(4).text()+"','"+info.getElementsByTag("td").eq(5).text()+"','"+lastRank+"','"+rating.split("--")[1]+"');\r\n");
				//System.out.println(data0);
			//	System.out.println(data1);
			//	sb.append(data0+"\n");
			//	sb.append(data1+"\n");
			//	sb.append("-----------------\n");
				System.out.println("-----------------");
			}
			i++;
		}
		saveToFile("f:/lshbsjb.txt", tt.toString().getBytes());
	}

	private static String getRanking(String id, String postData0,
			String postData1) {
		String result = "";
		// System.out.println(id+"\t"+postData0+"\t"+postData1);
		Map<String, String> map = new HashMap<String, String>();
		map.put("fund_id", id);
		map.put("data_tm_str", postData0 + "," + postData1);
		try {
			Document doc = Crawler
					.connect("http://data.simuwang.com/ajax/get_fund_rank.php")
					.data(map).userAgent("postData0").post();
			String tempData = doc.text();
			String data0="",data1="";
			try{
				data0 = tempData.substring(tempData.indexOf(postData0),
						tempData.indexOf(postData1));
				data0 = data0.substring(0, data0.lastIndexOf("}"));

				data1 = tempData.substring(tempData.indexOf(postData1));
				data1 = data1.substring(0, data1.indexOf("}"));
			}catch(Exception ex){
				return null;
			}
			

			String newdata0 = processRank(data0);
			String newdata1 = processRank(data1);
			result = newdata0.substring(0, newdata0.lastIndexOf("~~")) + "@@"
					+ newdata1.substring(0, newdata1.lastIndexOf("~~"));
		} catch (IOException e) {
			return null;
		}
		return result;
	}

	private static String processRank(String data0) {
		String[] data0_ = data0.split(",");
		String newdata0 = "";
		for (String s : data0_) {
			s = s.substring(s.lastIndexOf(":") + 1).replaceAll("\"", "");
			newdata0 += s + "~~";
		}
		return newdata0;
	}
	
	public static void saveToFile(String fileName, byte[] data) {
		try {
			new FileOutputStream(fileName, true)
					.write((new String(data) + "\t").getBytes());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void downLoadImg(String urls, String name) throws IOException{
		   URL url = new URL(urls);  
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	        conn.setRequestMethod("GET");  
	        conn.setConnectTimeout(5 * 1000);  
	        InputStream inStream = conn.getInputStream();  
	        byte[] data = readInputStream(inStream);  
	        File imageFile = new File("f:/image/"+name+".jpg");  
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