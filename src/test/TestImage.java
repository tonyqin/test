package test;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class TestImage {

	/**
	 *
	 * @author tony
	 * @date 2013-1-25下午1:57:41
	 */

	public static void main(String[] args) {
		List<String> list=new ArrayList<String>();
		list.add("http://www.nts.org.uk/site/images/properties/brd003extsu.jpg");
		list.add("http://www.nts.org.uk/site/images/properties/brd001det.jpg");
		list.add("http://www.nts.org.uk/site/images/properties/brd002extsp.jpg");
		list.add("http://www.nts.org.uk/site/images/properties/brd004ext.jpg");
		list.add("http://www.nts.org.uk/site/images/properties/brd012sp.jpg");
		list.add("http://www.nts.org.uk/site/images/properties/dru002extsp.jpg");
		list.add("http://www.nts.org.uk/site/images/properties/dru001int.jpg");

		for(String url:list){
			downloadImageToPNG(url,"E:/testNtsImage/");
			System.out.println("-----------------------------finish");
		}
	}

	public static InputStream UrlToInputStream(String urls) throws IOException{
		   URL url = new URL(urls);  
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
	        conn.setRequestMethod("GET");  
	        conn.setConnectTimeout(5 * 1000);  
	        InputStream inStream = conn.getInputStream();
			return inStream;  
	}
	
	public static void downloadImageToPNG(String imageUrl,String savePath){
		try {
	           // File fi = new File("E:/fraser_property9a000003.jpg"); //大图文件
				//String url="http://www.nts.org.uk/site/images/properties/brd003extsu.jpg";
				InputStream is=UrlToInputStream(imageUrl);
				String fileName=imageUrl.substring(imageUrl.lastIndexOf("/")+1,imageUrl.lastIndexOf("."));
				File path=new File(savePath);
				if(!path.exists())path.mkdirs();
	            File fo = new File(savePath+fileName+".PNG"); //将要转换出的小图文件

	            AffineTransform transform = new AffineTransform();
	            BufferedImage bis = ImageIO.read(is);

	            int w = bis.getWidth();
	            int h = bis.getHeight();
	            double scale = (double)w/h;

	            int nw = 300;
	            int nh = 200;
//	            if(nh>120) {
//	                nh = 120;
//	                nw = (nh * w) / h;
//	            }

	            double sx = (double)nw / w;
	            double sy = (double)nh / h;

	            transform.setToScale(sx,sy);

	            AffineTransformOp ato = new AffineTransformOp(transform, null);
	            BufferedImage bid = new BufferedImage(nw, nh, BufferedImage.TYPE_3BYTE_BGR);
	            ato.filter(bis,bid);
	            ImageIO.write(bid, "jpeg", fo);
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	}
	
}
