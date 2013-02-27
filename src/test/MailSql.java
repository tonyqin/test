package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class MailSql {
	public static void main(String[] args) throws FileNotFoundException {
		BufferedReader reader = new BufferedReader(
				new FileReader("f:/xh-2.txt"));
		process(reader);
	}

	/** print one file, given an open BufferedReader */
	public static void process(BufferedReader is) {
		StringBuffer sb=new StringBuffer();
		try {
			String inputLine;
			int i=0;
			while ((inputLine = is.readLine()) != null) {
				if(inputLine.indexOf("@126.com")!=-1){
					String temp=inputLine.substring(0,inputLine.indexOf("@126.com")+8);
					sb.append("insert into mail (mail) values ('"+temp+"') ;\r\n");
					System.out.println((++i)+"----------------");
				}
			}
			saveToFile("f:/sqls.txt", sb.toString().getBytes());
			is.close();
		} catch (IOException e) {
			System.out.println("IOException: " + e);
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
