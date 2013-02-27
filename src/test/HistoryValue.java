package test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.mpupa.crawler.Crawler;
import com.mpupa.crawler.nodes.Document;
import com.mpupa.crawler.nodes.Element;
import com.mpupa.crawler.select.Elements;

public class HistoryValue {
	
    
	public static void main(String[] args) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("allPage", "1");
			map.put("page", "1");
			map.put("perPage", "976");
			map.put("sortfield", "103");
			map.put("sorttype", "1");
			Document doc = Crawler
					.connect("http://simu.howbuy.com/fundlist.htm")
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:12.0) Gecko/20100101 Firefox/12.0")
					.data(map).post();
			Elements lis = doc
					.getElementsByAttributeValue("class", "smProlist").first()
					.getElementsByTag("tr");
			int index = 0;
			System.out.println(lis.size());
			for (Element li : lis) {
				if (index > 0) {
					Element a = li.getElementsByTag("a").first();
					String jjName = a.text();//name
					String temp=a.attr("href");
					String root=temp;
					temp=temp.substring(0,temp.lastIndexOf("/"));
					temp=temp.substring(temp.lastIndexOf("/")+1);
					String id="dcjj_"+temp;
					
					//System.out.println(root);
					//System.out.println(id);
					
				//	String id="dcjj_"+temp.substring(temp.lastIndexOf("/")+1).replace(".html", "");
//					String twoUrl="http://simu.howbuy.com"+temp;
//					System.out.println(id);
					
//					StringBuffer sb=new StringBuffer();
//					sb.append("insert into dcjjysb (fund_dm,fund_jjjc,fund_jjqc) values('"+id+"','"+jjName+"',''");
//					sb.append(");\r\n");
//					saveToFile("f:/dcjjysb.txt", sb.toString().getBytes());
					//-------history
//					StringBuffer sb = new StringBuffer();//history value.
//					String lsUrl = temp + "/lsjz";//history url;
//					String startDate = li.getElementsByTag("td").eq(6).text();
//					String endDate = li.getElementsByTag("td").eq(8).text();
//					System.out.println(startDate + "     " + endDate);
//					System.out.println(lsUrl);
//					try{
//						
//						connect(lsUrl, id, startDate, endDate, 0,sb);
//					}catch(Exception ex){
//						continue;
//					}
//					saveToFile("f:/lssj.txt", sb.toString().getBytes());
					
					
				
					//---业绩回报
//					String yjhbUrl=temp+"/yjhb";
//					proYjhb(id,yjhbUrl);
					
					
					//---基金概括
//					String jjgkUrl=temp+"/jjgk";
//					try{
//						//proJjgk(id,jjgkUrl);
//						proJjgk1(id,jjgkUrl);
//						//proYs(id,jjgkUrl);
//					}catch(Exception ex){
//						continue;
//					}
					
					//---分红
					//proFenhong(id,jjgkUrl);
					
					//--重仓股
					try{
						String zcgURl=root+"zxcg";
						prozcg(id,zcgURl);
					}catch(Exception e){
						continue;
					}
					
					//--基金经理
//					try{
//						String manageName=li.getElementsByTag("td").eq(4).text();
//						String manageUrl="http://simu.howbuy.com"+li.getElementsByTag("td").eq(4).first().getElementsByTag("a").first().attr("href");
//						manageUrl=manageUrl.replace(".html", "/jljj.htm");
//						promanage(id,manageName,manageUrl);
//					}catch(Exception e){
//						continue;
//					}
//					
					
					//----------2.
					//proTwo(id,twoUrl);
					
					
					
					
					
				}
				index++;
				System.out.println(index
						+ "================================================");
			}
		} catch (IOException e) {
			
			//e.printStackTrace();
		}

	}

	private static void proTwo(String id, String twoUrl) throws IOException {
		StringBuffer sb=new StringBuffer();
		Document page=Crawler.connect(twoUrl).get();
		
	}

	private static void proJjgk1(String id, String jjgkUrl) throws IOException {
		StringBuffer sb=new StringBuffer();
		Document page=Crawler.connect(jjgkUrl).get();
		Elements tds=page.getElementsByAttributeValue("class", "bgData bgData2").first().getElementsByTag("td");
		System.out.println(tds.size());
		int i=0;
		sb.append("insert into jjjcxxb (fund_dm，fund_tzgw,fund_jjjl,fund_clsj,fund_zzxs,fund_tzcldl,fund_qx,fund_fbq,fund_kfr,fund_str,fund_rgf,fund_glf,fund_shf,fund_fdglf,fund_rgmk,fund_zdzjmk) values('"+id+"'");
		for(Element td:tds){
			if(i%2==1){
				if(i==1||i==7||i==35){
				}else{
					
					//System.out.println(td.text());
					sb.append(",'"+td.text()+"'");
				}
//				if(i==5){
//					String name=td.text();
//					String jjjlUrl="http://simu.howbuy.com"+td.getElementsByTag("a").first().attr("href");
//					jjjlUrl=jjjlUrl.replace(".html", "/jljj.htm");
//					promanage(id,name,jjjlUrl);
				//}
			}
			i++;
		}
		sb.append(");\r\n");
		saveToFile("f:3/jjjcxxb.txt", sb.toString().getBytes());
	}

	private static void proYs(String id, String jjgkUrl) throws IOException {
		StringBuffer sb=new StringBuffer();
		Document page=Crawler.connect(jjgkUrl).get();
		Elements tds=page.getElementsByAttributeValue("class", "bgData bgData2").first().getElementsByTag("td");
		System.out.println(tds.size());
		int i=0;
		sb.append("insert into dcjjysb (fund_dm,fund_jjjc,fund_jjqc) values('"+id+"','"+tds.eq(1).text()+"','"+tds.eq(9).text()+"'");
		sb.append(");\r\n");
		saveToFile("f:/dcjjysb.txt", sb.toString().getBytes());
		
	}

	private static void proFenhong(String id, String jjgkUrl) throws IOException {
		StringBuffer sb=new StringBuffer();
		Document page=Crawler.connect(jjgkUrl).get();
		Elements llis=page.getElementsByAttributeValue("class", "bgData bgData3 lt").first().getElementsByTag("tr");
		Elements rlis=page.getElementsByAttributeValue("class", "bgData bgData3 rt").first().getElementsByTag("tr");
		//System.out.println(llis.size()+"========"+rlis.size());
		int i=0;
		for(Element li:rlis){
			if(i>0){
				Elements ltds=li.getElementsByTag("td");
				Elements rtds=rlis.eq(i).first().getElementsByTag("td");
				
				String fund_ztzr=ltds.eq(1).text();
				String fund_fh=ltds.eq(2).text();
				
				String fund_cfrq=rtds.eq(1).text();
				String fund_cfbl=rtds.eq(2).text();
				sb.append("insert into fhchb (fund_dm,fund_ztzr,fund_fh,fund_cfrq,fund_cfbl) values('"+id+"','"+fund_ztzr+"','"+fund_fh+"','"+fund_cfrq+"','"+fund_cfbl+"');\r\n");
				//System.out.println(fund_ztzr+"==="+fund_fh+"======"+fund_cfrq+"======="+fund_cfbl);
			}
			i++;
		}
		
		saveToFile("f:/fhchb.txt", sb.toString().getBytes());
	}

	private static void promanage(String id, String manageName, String manageUrl) throws IOException {
		StringBuffer sb=new StringBuffer();
		Document page=Crawler.connect(manageUrl).get();
		String company=page.getElementsByAttributeValue("class", "bar2 lt").first().getElementsByTag("a").text();
		String image=page.getElementsByAttributeValue("class", "pic").first().getElementsByTag("img").first().attr("src");
		String pro=page.getElementsByAttributeValue("class", "info rt").first().text();
		company=company.substring(0, company.indexOf(" "));
//		System.out.println(image);
//		System.out.println(pro);
		sb.append("insert into jjglgsb(fund_dm,fund_jjjl,fund_tzglgs,fund_jjjljj,fund_small_p) values('"+id+"','"+manageName+"','"+company+"','"+pro+"','"+image+"');\r\n ");
		saveToFile("f:/jjglgsb.txt", sb.toString().getBytes());
	}

	private static void prozcg(String id, String temp) throws IOException {
		StringBuffer sb=new StringBuffer();
		Document page=Crawler.connect(temp).get();
		Elements tds=page.getElementsByAttributeValue("class", "History Holdings").first().getElementsByTag("table");
		System.out.println(tds.size());
		//		for(Element e:tds){
//			if(!e.attr("class").equals("bg_tit")){
//				Elements tdd=e.getElementsByTag("td");
//				sb.append("insert into ccb (fund_jjdm,fund_gpdm,fund_gpjc,fund_cgs,fund_zltgbl,fund_cgbd,fund_bdgs,fund_bdbl,fund_jzrq) values('"+id+"','"+tdd.eq(1).text()+"','"+tdd.eq(2).text()+"','"+tdd.eq(3).text()+"','"+tdd.eq(4).text()+"','"+tdd.eq(5).text()+"','"+tdd.eq(6).text()+"','"+tdd.eq(7).text()+"','"+tdd.eq(8).text()+"');\r\n");
//			}
//		}
//		saveToFile("f:/ccb.txt", sb.toString().getBytes());
	}

	private static void proJjgk(String id, String jjgkUrl) throws IOException {
		StringBuffer sb=new StringBuffer();
		Document page=Crawler.connect(jjgkUrl).get();
		Elements tds=page.getElementsByAttributeValue("class", "bgData bgData2").first().getElementsByTag("td");
		System.out.println(tds.size());
		int i=0;
		sb.append("insert into jjjcxxb (fund_dm,fund_clsj,fund_tzgw,fund_jjjl,fund_xsbz,fund_kfr,fund_fbq,fund_str,fund_tgyh,fund_rgf,fund_glf,fund_shf,fund_fdglf，fund_rgmk,fund_zdzjmk) values('"+id+"'");
		for(Element td:tds){
			if(i%2==1){
				if(i==1||i==9||i==11||i==17||i==19){
				}else{
					//System.out.println(td.text());
					sb.append(",'"+td.text()+"'");
				}
			}
			i++;
		}
		sb.append(");\r\n");
		saveToFile("f:/jjjcxxb.txt", sb.toString().getBytes());
	}

	private static void proYjhb(String id, String yjhbUrl) throws IOException {
		StringBuffer sb=new StringBuffer();
		Document page=Crawler.connect(yjhbUrl).get();
		Elements lis=page.getElementsByAttributeValue("class", "bar02").first().getElementsByTag("tr");
		for(Element e:lis){
			if(!e.attr("class").equals("bg_tit")){
				Elements tds=e.getElementsByTag("td");
				String fund_sjbq=tds.eq(0).text();
				String fund_syl=tds.eq(1).text();
				String fund_hssb=tds.eq(2).text();
				String fund_pm=tds.eq(3).text();
				sb.append("insert into lshbsjb (fund_dm,fund_sjbq,fund_syl,fund_hssb,fund_pm) values('"+id+"','"+fund_sjbq+"','"+fund_syl+"','"+fund_hssb+"','"+fund_pm+"');\r\n");
			}
		}
		saveToFile("f:/lshbsjb.txt", sb.toString().getBytes());
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

	private static void connect(String lsUrl, String jjName, String startDate,
			String endDate, int k, StringBuffer sb) throws IOException {
		if (!"".equals(startDate) && !"".equals(endDate)&&!"--".equals(endDate)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("startDate", startDate);
			map.put("endDate", endDate);

			Document page = Crawler
					.connect(lsUrl)
					.userAgent(
							"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:12.0) Gecko/20100101 Firefox/12.0")
					.data(map).post();
			int size = page.getElementById("m2").getElementsByTag("tr").size();
			Elements lis1 = page
					.getElementsByAttributeValue("class", "bgData bgData4 lt")
					.first().getElementsByTag("tr");
			int i = 0, j = 0;
			for (Element li : lis1) {
				if (i > k) {
					Elements tds = li.getElementsByTag("td");
					String time = tds.eq(0).text();
					String dwjz = tds.eq(1).text();
					String ljjz = tds.eq(2).text();
					System.out.println(time + "\t\t" + dwjz + "\t\t" + ljjz);
					sb.append("insert into jzsjb (fund_dm,fund_gxsj,fund_jz,fund_ljjj) values('"
							+ jjName
							+ "','"
							+ time
							+ "','"
							+ dwjz
							+ "','"
							+ ljjz + "');\r\n");
				}
				i++;
			}
			Elements lis2 = page
					.getElementsByAttributeValue("class", "bgData bgData4 rt")
					.first().getElementsByTag("tr");
			for (Element li : lis2) {
				if (j > 0) {
					Elements tds = li.getElementsByTag("td");
					String time = tds.eq(0).text();
					String dwjz = tds.eq(1).text();
					String ljjz = tds.eq(2).text();
					System.out.println(time + "\t\t" + dwjz + "\t\t" + ljjz);
					sb.append("insert into jzsjb (fund_dm,fund_gxsj,fund_jz,fund_ljjj) values('"
							+ jjName
							+ "','"
							+ time
							+ "','"
							+ dwjz
							+ "','"
							+ ljjz + "');\r\n");
				}
				j++;
			}
			// ------
			String edate = page.getElementById("m2").getElementsByTag("tr")
					.eq(size - 1).first().getElementsByTag("td").first().text();

			if (size == 102 || startDate.compareTo(edate) < 0) {
				connect(lsUrl, jjName, startDate, edate, 1,sb);
			}
		}

	}

}
