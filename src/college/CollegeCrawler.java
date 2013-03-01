package college;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.mpupa.crawler.Crawler;
import com.mpupa.crawler.nodes.Document;
import com.mpupa.crawler.nodes.Element;
import com.mpupa.crawler.select.Elements;

public class CollegeCrawler {

	/**
	 * 
	 * @author tony
	 * @date 2012-11-21下午12:27:57
	 */
	final private String BASE_URL = "http://daxue.learning.sohu.com";
//test......
	public static void main(String[] args) {
		CollegeCrawler cc = new CollegeCrawler();
		try {
			cc.crawle();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void crawle() throws Exception {
		Document doc = Crawler.connect(BASE_URL).get();
		Elements as = doc
				.getElementsByAttributeValue("class", "college_selector")
				.first().getElementsByTag("a");

		for (Element a : as) {
			String province = a.text().trim();
			String province_link = a.attr("href").trim();
			System.out.println(province + ":\t" + province_link);
			getCollegeByProvince(province, province_link);
		}

	}

	private void getCollegeByProvince(String province, String province_link)
			throws Exception {
		Document doc = Crawler.connect(province_link).get();
		Elements trs = doc.getElementsByAttributeValue("class", "tab_list2")
				.first().getElementsByTag("tr");
		for (int i = 1; i < trs.size(); i++) {
			Element a = trs.get(i).getElementsByTag("a").first();
			String college = a.text().trim();
			String college_link = BASE_URL + a.attr("href")
					+ "introduction.html#introduction";
			System.out.println(college + ":\t" + college_link);
			getCollegeInfo(province,college, college_link);
		}
	}

	private void getCollegeInfo(String province,String college, String college_link) throws Exception
			 {
		try{
			Document doc = Crawler.connect(college_link).get();
			String img=doc.select("dl.dltw").first().select("img").first().attr("src").trim();
			String collegeInfo = doc.getElementsByAttributeValue("class", "ptxt2")
					.first().text();
			//System.out.println(img);
			//System.out.println(collegeInfo);
			String sql="insert into college(province,college,college_img,college_info) values('"+province+"','"+college+"','"+img+"','"+collegeInfo+"');\n";
			saveToFile("f:college/college.sql", sql.getBytes());
		}catch(Exception e){
			System.out.println("出异常了！");
			return;
		}
	}
	
	private static void saveToFile(String fileName, byte[] data) {
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
