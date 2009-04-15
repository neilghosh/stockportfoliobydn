package ng;

public class util {
	
	public static String getString( String str ,String startstr ,int gap1  ,  String endstr , int gap2)
	{
		int baseIndex = str.indexOf(startstr);
		if (baseIndex == -1)
		{
			return "N/A";
		}
		else
		{
			System.out.print(baseIndex);
			int len = str.indexOf(endstr, baseIndex+gap1)-baseIndex-gap1-gap2;
			System.out.print(len);
			return str.substring(baseIndex+gap1 , baseIndex+gap1+len);
		}
	}

}
