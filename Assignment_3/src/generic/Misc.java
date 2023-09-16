package generic;

public class Misc {
	
	public static void printErrorAndExit(String message)
	{
		System.err.println(message);
		System.exit(1);
	}
	
	public static int getIntFromBinaryString(String binaryString) {
		boolean isNegative = binaryString.charAt(0) == '1';
		int value = Integer.parseInt(binaryString, 2);
		if(isNegative) {
			value -= 1;
			String onesComp = Integer.toBinaryString(value);
			String result = "";
			for (int i = 0; i < onesComp.length(); i++) {
				if (onesComp.charAt(i) == '0') {
					result = result + '1';
				} else {
					result = result + '0';
				}
			}
			value = -Integer.parseInt(result, 2);
		}
		return value;
    }
}