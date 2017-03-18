
public class SDES {
	public static byte[] Encrypt(byte[] rawkey, byte[] plaintext){
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		generateKeys(rawkey, key1, key2);
		byte[] temp = initialPermute(plaintext);
		fk(temp, key1);
		temp = switchHalves8(temp);
		fk(temp, key2);
		temp = finalPermute(temp);
		return temp;
	}
	
	public static byte[] Decrypt(byte[] rawkey, byte[] ciphertext){
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		generateKeys(rawkey, key1, key2);
		byte[] temp = initialPermute(ciphertext);
		fk(temp, key2);
		temp = switchHalves8(temp);
		fk(temp, key1);
		temp = finalPermute(temp);
		return temp;
	}
	
	/////////////////////////////////////
	//
	//	generateKeys(...)
	//
	/////////////////////////////////////
	
	public static void generateKeys(byte[] rawkey, byte[] k1, byte[] k2){
		byte[] afterP10 = keyGenPermute10(rawkey);
		byte[] afterS1 = keyGenShift(afterP10, 1);
		keyGenPermute10to8(afterS1, k1);
		byte[] afterS2 = keyGenShift(afterS1, 2);
		keyGenPermute10to8(afterS2, k2);
	}
	
	public static byte[] keyGenPermute10(byte[] input){
		/* Add code for input checking here */
		
		byte[] output = new byte[10];
		output[0] = input[2];
		output[1] = input[4];
		output[2] = input[1];
		output[3] = input[6];
		output[4] = input[3];
		output[5] = input[9];
		output[6] = input[0];
		output[7] = input[8];
		output[8] = input[7];
		output[9] = input[5];
		
		return output;
	}
	
	public static byte[] keyGenShift(byte[] input, int shiftAmount){
		/* Add code for input checking here */
		
		byte[] output = new byte[10];
		output[0] = input[(0 + shiftAmount) % 5];
		output[1] = input[(1 + shiftAmount) % 5];
		output[2] = input[(2 + shiftAmount) % 5];
		output[3] = input[(3 + shiftAmount) % 5];
		output[4] = input[(4 + shiftAmount) % 5];
		output[5] = input[(0 + shiftAmount) % 5 + 5];
		output[6] = input[(1 + shiftAmount) % 5 + 5];
		output[7] = input[(2 + shiftAmount) % 5 + 5];
		output[8] = input[(3 + shiftAmount) % 5 + 5];
		output[9] = input[(4 + shiftAmount) % 5 + 5];
		
		return output;
	}
	
	public static void keyGenPermute10to8(byte[] input, byte[] output){
		if(input == null){
			System.out.println("Error: SDES.keyGenPermutation10to8(input, output) got null for input");
			System.exit(1);
		}
		if(output == null){
			System.out.println("Error: SDES.keyGenPermutation10to8(input, output) got null for output");
			System.exit(1);
		}
		if(input.length != 10){
			System.out.println("Error: SDES.keyGenPermutation10to8(input, output) got input of incorrect size: " + input.length + " instead of 10");
			System.exit(1);
		}		
		if(input.length != 10){
			System.out.println("Error: SDES.keyGenPermutation10to8(input, output) got output of incorrect size: " + input.length + " instead of 10");
			System.exit(1);
		}
		
		output[0] = input[5];
		output[1] = input[2];
		output[2] = input[6];
		output[3] = input[3];
		output[4] = input[7];
		output[5] = input[4];
		output[6] = input[9];
		output[7] = input[8];
	}
	
	/////////////////////////////////////
	//
	//	initialPermute(...) and finalPermute(...)
	//
	/////////////////////////////////////
	
	public static byte[] initialPermute(byte[] input){
		/* Add code for input checking here */
		
		byte[] output = new byte[8];
		output[0] = input[1];
		output[1] = input[5];
		output[2] = input[2];
		output[3] = input[0];
		output[4] = input[3];
		output[5] = input[7];
		output[6] = input[4];
		output[7] = input[6];
		
		return output;
	}
	
	public static byte[] finalPermute(byte[] input){
		/* Add code for input checking here */
		
		byte[] output = new byte[8];
		output[0] = input[3];
		output[1] = input[0];
		output[2] = input[2];
		output[3] = input[4];
		output[4] = input[6];
		output[5] = input[1];
		output[6] = input[7];
		output[7] = input[5];
		
		return output;
	}
	
	/////////////////////////////////////
	//
	//	switchHalves8(...)
	//
	/////////////////////////////////////
	
	public static byte[] switchHalves8(byte[] input){
		/* Add code for input checking here */
		
		byte[] output = new byte[8];
		output[0] = input[4];
		output[1] = input[5];
		output[2] = input[6];
		output[3] = input[7];
		output[4] = input[0];
		output[5] = input[1];
		output[6] = input[2];
		output[7] = input[3];
		
		return output;
	}
	
	/////////////////////////////////////
	//
	//	fk(...)
	//
	/////////////////////////////////////
	
	public static void fk(byte[] input, byte[] key){
		byte[] fromF = F(input, key);
		for(int i = 0; i < 4; i++)
			input[i] = (byte)(input[i] ^ fromF[i]);
	}
	
	public static byte[] F(byte[] input, byte[] key){
		/* check input */
		
		byte[] temp = new byte[8];
		temp[0] = input[7];
		temp[1] = input[4];
		temp[2] = input[5];
		temp[3] = input[6];
		temp[4] = input[5];
		temp[5] = input[6];
		temp[6] = input[7];
		temp[7] = input[4];
		
		
		for(int i = 0; i < 8; i++)
			temp[i] = (byte)(temp[i] ^ key[i]);
		
		s0(temp);
		s1(temp);
		
		byte[] output = new byte[4];
		output[0] = temp[1];
		output[1] = temp[3];
		output[2] = temp[2];
		output[3] = temp[0];
		
		return output;
	}
	
	private static void s0(byte[] input){
		byte row = (byte)(input[0] * 2 + input[3]);
		byte col = (byte)(input[1] * 2 + input[2]);
		
		byte value = s0Table[row][col];
		
		input[0] = (byte)(value / 2);
		input[1] = (byte)(value % 2);
	}
	
	private static void s1(byte[] input){
		byte row = (byte)(input[4] * 2 + input[7]);
		byte col = (byte)(input[5] * 2 + input[6]);
		
		byte value = s1Table[row][col];
		
		input[2] = (byte)(value / 2);
		input[3] = (byte)(value % 2);
	}
	
	private static byte[][] s0Table = {
		{1, 0, 3, 2}, 
		{3, 2, 1, 0}, 
		{0, 2, 1, 3}, 
		{3, 1, 3, 2}
	};
	
	private static byte[][] s1Table = {
		{0, 1, 2, 3}, 
		{2, 0, 1, 3}, 
		{3, 0, 1, 0}, 
		{2, 1, 0, 3}
	};
	


}
