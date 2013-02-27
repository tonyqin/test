package test;

import java.io.IOException;

import com.mpupa.crawler.Crawler;
import com.mpupa.crawler.nodes.Document;
import com.mpupa.crawler.nodes.Element;
import com.mpupa.crawler.select.Elements;

public class Tianya {
	
	public static void main(String[] args) {
		String url="http://www.tianya.cn/techforum/articleslist/0/16.shtml";
		int i=0;
		try {
			while(true){
				Document doc=Crawler.connect(url).get();
				Elements es=doc.getElementById("postlistwrapper").getElementsByAttributeValue("name", "adsp_list_post_info_b");
				for(Element e:es){
					String detail=e.getElementsByTag("a").first().attr("href");
					System.out.println("详细页面链接地址：\t"+detail);
					
					String title=e.getElementsByTag("a").first().text();
					System.out.println("标题:\t"+title);
					String author=e.getElementsByAttributeValue("class", "author").text();
					System.out.println("作者名：\t"+author);
					
					String visitNum=e.getElementsByAttributeValue("class", "tviewre w_c").first().text();
					System.out.println("访问数:\t"+visitNum);
					
					String reply=e.getElementsByAttributeValue("class", "treplay w_c").first().text();
					System.out.println("回复数:\t"+reply);
					
					String updateTime=e.getElementsByAttributeValue("class", "ttime w_c").first().text();
					System.out.println("更新时间:\t"+updateTime);
					
					connect(detail);
					System.out.println("-------------------------"+(++i)+"-------");
				}
				try{
					int size=doc.getElementsByAttributeValue("class", "ftarea-r").first().getElementsByTag("a").size();
					url=doc.getElementsByAttributeValue("class", "ftarea-r").first().getElementsByTag("a").eq(size-1).attr("href");
				}catch(Exception e){
					break;
				}
				
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void connect(String detail) {
		try {
			int i=1;
			int size=1;
			while(true){
				
				Document page=Crawler.connect(detail).get();
				Element content=page.getElementById("pContentDiv");
				Elements items=content.getElementsByAttributeValue("class", "item");
				for(Element item:items){
					String head=item.getElementsByAttributeValue("class", "vcard").text();
					head=head.substring(head.indexOf("作者"));
					System.out.println("\t头部信息："+head);
					
					String floor=item.getElementsByAttributeValue("class", "floor").first().text();
					System.out.println("楼层数:\t"+floor);
					String rcont=item.getElementsByAttributeValue("class", "post").text();
					System.out.println("\t回复内容:\t"+rcont);
					System.out.println("\t===================");
				}
				try{
				if(size==1){
					String num=page.getElementById("pageForm").getElementsByTag("span").first().text();
					size=Integer.parseInt(num.replace("共", "").replaceAll("页", ""));
				}
				System.out.println("共"+size+"页===第"+i+"页");
					String first=detail.substring(0,detail.lastIndexOf("/"));
					first=first.substring(0,first.lastIndexOf("/")+1);
					String second=detail.substring(detail.lastIndexOf("/"));
					i=i+1;
					detail=first+i+second;
					//System.out.println(detail);
					if(i>size)break;
				}catch(Exception e){
					break;
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return;
		}
		
	}
}

