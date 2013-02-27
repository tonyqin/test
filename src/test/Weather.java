package test;

import java.io.IOException;

import com.mpupa.crawler.Crawler;
import com.mpupa.crawler.nodes.Document;

public class Weather {
    public static void main(String[] args) {
		Document doc;
		try {
			doc = Crawler.connect("http://www.szmb.gov.cn/article/").userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:12.0) Gecko/20100101 Firefox/12.0").get();
			System.out.println(doc.getElementById("inspect_cnt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
