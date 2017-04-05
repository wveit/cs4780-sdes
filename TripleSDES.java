
public class TripleSDES {

	public static void main(String[] args){
		
		System.out.println("====================================================================");
		System.out.println("         Part 2 Triple SDES Table");
		System.out.println("--------------------------------------------------------------------");
		System.out.println("  Raw Key 1   |   Raw Key 2   |   Plain Text   |  Cipher Text   ");
		System.out.println("--------------------------------------------------------------------");
		EncryptDemo();
		DecryptDemo();
		System.out.println("====================================================================");
	}
	
	private static void EncryptDemo(){
		byte[][] key1 = new byte[4][];
		byte[][] key2 = new byte[4][];
		byte[][] plaintext = new byte[4][];
		byte[][] ciphertext = new byte[4][];

		key1[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		key1[1] = new byte[]{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		key1[2] = new byte[]{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		key1[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		key2[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		key2[1] = new byte[]{ 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		key2[2] = new byte[]{ 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		key2[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		plaintext[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0 };
		plaintext[1] = new byte[]{ 1, 1, 0, 1, 0, 1, 1, 1 };
		plaintext[2] = new byte[]{ 1, 0, 1, 0, 1, 0, 1, 0 };
		plaintext[3] = new byte[]{ 1, 0, 1, 0, 1, 0, 1, 0 };

		for(int i = 0; i < 4; i++){
			ciphertext[i] = TripleSDES.Encrypt(key1[i], key2[i], plaintext[i]);
			printArray(key1[i]);
			System.out.print("       ");
			printArray(key2[i]);
			System.out.print("       ");
			printArray(plaintext[i]);
			System.out.print("       ");
			printArray(ciphertext[i]);
			System.out.println();
		}
	}


	private static void DecryptDemo(){
		byte[][] key1 = new byte[4][];
		byte[][] key2 = new byte[4][];
		byte[][] plaintext = new byte[4][];
		byte[][] ciphertext = new byte[4][];

		key1[0] = new byte[]{ 1, 0, 0, 0, 1, 0, 1, 1, 1, 0 };
		key1[1] = new byte[]{ 1, 0, 1, 1, 1, 0, 1, 1, 1, 1 };
		key1[2] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		key1[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		key2[0] = new byte[]{ 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		key2[1] = new byte[]{ 0, 1, 1, 0, 1, 0, 1, 1, 1, 0 };
		key2[2] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		key2[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

		ciphertext[0] = new byte[]{ 1, 1, 1, 0, 0, 1, 1, 0 };
		ciphertext[1] = new byte[]{ 0, 1, 0, 1, 0, 0, 0, 0 };
		ciphertext[2] = new byte[]{ 1, 0, 0, 0, 0, 0, 0, 0 };
		ciphertext[3] = new byte[]{ 1, 0, 0, 1, 0, 0, 1, 0 };

		for(int i = 0; i < 4; i++){
			plaintext[i] = TripleSDES.Decrypt(key1[i], key2[i], ciphertext[i]);
			printArray(key1[i]);
			System.out.print("       ");
			printArray(key2[i]);
			System.out.print("       ");
			printArray(plaintext[i]);
			System.out.print("       ");
			printArray(ciphertext[i]);
			System.out.println();
		}
	}

	public static void printArray(byte[] array){
		for(int i = 0; i < array.length; i++)
			System.out.print(array[i]);
	}


	/////////////////////////////////////
	//
	//	Encrypt(...) and Decrypt(...)
	//
	/////////////////////////////////////

	public static byte[] Encrypt( byte[] rawkey1, byte[] rawkey2, byte[] plaintext ){
		return SDES.Encrypt(rawkey1, SDES.Decrypt(rawkey2, SDES.Encrypt(rawkey1, plaintext)));
	}
	public static byte[] Decrypt( byte[] rawkey1, byte[] rawkey2, byte[] ciphertext ){
		return SDES.Decrypt(rawkey1, SDES.Encrypt(rawkey2, SDES.Decrypt(rawkey1, ciphertext)));
	}
}
