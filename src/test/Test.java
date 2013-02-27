package test;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

public class Test
{
	private static StringHasher mStringHasher = new StringHasher();
	private static TimeManager mTimeManager;

	public static void main(String[] args)
	{
		mTimeManager = new TimeManager();
		
		System.out.println(generateSignature("7ucbnywa6thu5yrmdd39rz94", "XHfVxRkB"));
	}

	public static String generateSignature(String paramString1, String paramString2)
	{
		StringBuilder buf = new StringBuilder(200);

		String str1 = mTimeManager.getCurrentTimeSeconds();
		buf.delete(0, buf.length());
		buf.append(paramString1).append(paramString2).append(str1);
		String str2 = buf.toString();

		return mStringHasher.createHashedString(str2);
	}
	
	private static class StringHasher
	{
		private final StringBuffer mSigBuffer = new StringBuffer(64);

		public String createHashedString(String paramString)
		{
		  this.mSigBuffer.delete(0, this.mSigBuffer.length());
		  
		  try
		  {
			MessageDigest localMessageDigest = MessageDigest.getInstance("SHA-256");
			localMessageDigest.reset();
			byte[] arrayOfByte = localMessageDigest.digest(paramString.getBytes());
			Object[] arrayOfObject = new Object[1];
			arrayOfObject[0] = new BigInteger(1, arrayOfByte);
			String str = String.format("%x", arrayOfObject);

			this.mSigBuffer.append(str);
			return this.mSigBuffer.toString();
		  }
		  catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
		  {
			while (true)
			  localNoSuchAlgorithmException.printStackTrace();
		  }
		}
	}

	private static class TimeManager
	{
		private long serverTimeDiff;

		public String getCurrentTimeSeconds()
		{
		  return Long.toString((System.currentTimeMillis() + this.serverTimeDiff) / 1000L);
		}

		public void setServerTime(long paramLong)
		{
		  this.serverTimeDiff = (paramLong - System.currentTimeMillis());
		}
	}
}
