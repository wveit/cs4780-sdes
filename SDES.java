import java.util.Arrays;
public class SDES {


	public static void main(String[] args){
		System.out.println("SDES");
		System.out.println("Encryption");
		SDESEncryptProblems();
		System.out.println("Decryption");
		SDESDecryptProblems();
	}

	private static void SDESEncryptProblems(){
		byte[][] key = new byte[4][];
		byte[][] plaintext = new byte[4][];
		byte[][] ciphertext = new byte[4][];

		key[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		key[1] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		key[2] = new byte[]{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 };
		key[3] = new byte[]{ 0, 0, 0, 0, 0, 1, 1, 1, 1, 1 };

		plaintext[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0 };
		plaintext[1] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1 };
		plaintext[2] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0 };
		plaintext[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1 };

		for(int i = 0; i < 4; i++){
			ciphertext[i] = SDES.Encrypt(key[i], plaintext[i]);
//			System.out.print("plaintext:  ");
//			printArray(plaintext[i]);
			System.out.print("ciphertext: ");
			printArray(ciphertext[i]);
		}
	}

	private static void SDESDecryptProblems(){
		byte[][] key = new byte[4][];
		byte[][] plaintext = new byte[4][];
		byte[][] ciphertext = new byte[4][];

		key[0] = new byte[]{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		key[1] = new byte[]{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		key[2] = new byte[]{ 0, 0, 1, 0, 0, 1, 1, 1, 1, 1 };
		key[3] = new byte[]{ 0, 0, 1, 0, 0, 1, 1, 1, 1, 1 };

		ciphertext[0] = new byte[]{ 0, 0, 0, 1, 1, 1, 0, 0 };
		ciphertext[1] = new byte[]{ 1, 1, 0, 0, 0, 0, 1, 0 };
		ciphertext[2] = new byte[]{ 1, 0, 0, 1, 1, 1, 0, 1 };
		ciphertext[3] = new byte[]{ 1, 0, 0, 1, 0, 0, 0, 0 };

		for(int i = 0; i < 4; i++){
			plaintext[i] = SDES.Decrypt(key[i], ciphertext[i]);
			System.out.print("plaintext:  ");
			printArray(plaintext[i]);
//			System.out.print("ciphertext: ");
//			printArray(ciphertext[i]);
		}
	}

	public static void printArray(byte[] array){
		for(int i = 0; i < array.length; i++)
			System.out.print(array[i]);
		System.out.println();
	}

	/////////////////////////////////////
	//
	//	Encrypt(...) and Decrypt(...)
	//
	/////////////////////////////////////

	public static byte[] Encrypt(byte[] rawkey, byte[] plaintext){
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		generateKeys(rawkey, key1, key2);


		int size = (int) Math.ceil(plaintext.length / 8) * 8;
		byte[] ciphertext = new byte[size];

		for(int i = 0; i < plaintext.length; i += 8){
			byte[] subplaintext = Arrays.copyOfRange(plaintext, i, i+8);
			byte[] temp = EncryptBlock(key1, key2, subplaintext);
			for(int j = 0; j < 8; j++){
				ciphertext[j + i] =  temp[j];
			}
		}
		return ciphertext;
	}
	
	public static byte[] Decrypt(byte[] rawkey, byte[] ciphertext){
		byte[] key1 = new byte[8];
		byte[] key2 = new byte[8];
		generateKeys(rawkey, key1, key2);


		int size = (int) Math.ceil(ciphertext.length / 8) * 8;
		byte[] plaintext = new byte[size];

		for(int i = 0; i < ciphertext.length; i += 8){
			byte[] subciphertext = Arrays.copyOfRange(ciphertext, i, i+8);
			byte[] temp = DecryptBlock(key1, key2, subciphertext);
			for(int j = 0; j < 8; j++){
				plaintext[j + i] =  temp[j];
			}
		}
		return plaintext;
	}


	public static byte[] EncryptBlock(byte[] key1, byte[] key2, byte[] plaintext){
		byte[] temp = initialPermute(plaintext);
		fk(temp, key1);
		temp = switchHalves8(temp);
		fk(temp, key2);
		temp = finalPermute(temp);
		return temp;
	}
	
	public static byte[] DecryptBlock(byte[] key1, byte[] key2, byte[] ciphertext){
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
