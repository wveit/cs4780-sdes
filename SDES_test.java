
public class SDES_test {
	public static void main(String[] args){
		testSDES();
	}
	
	private static void testKeyGen(){
		byte[] rawkey = { 1, 0, 1, 0, 0, 0, 0, 0, 1, 0 };
		byte[] key1 = { 1, 0, 1, 0, 0, 1, 0, 0 };
		byte[] key2 = { 0, 1, 0, 0, 0, 0, 1, 1 };
		byte[] k1 = new byte[8];
		byte[] k2 = new byte[8];
		SDES.generateKeys(rawkey, k1, k2);
		System.out.println("key1 success: " + compare(k1, key1));
		System.out.println("key2 success: " + compare(k2, key2));
	}
	
	private static void testShift10(){
		byte[] input = { 0, 0, 0, 0, 0, 0, 1, 0, 1, 0};
		printArray(input);
		byte[] output = SDES.keyGenShift(input, 5);
		printArray(output);
	}
	
	private static void testSDES(){
		byte[][] key = new byte[4][];
		byte[][] plaintext = new byte[4][];
		byte[][] ciphertext = new byte[4][];
		
		key[0] = new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		key[1] = new byte[]{ 1, 1, 1, 0, 0, 0, 1, 1, 1, 0 };
		key[2] = new byte[]{ 1, 1, 1, 0, 0, 0, 1, 1, 1, 0 };
		key[3] = new byte[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
		
		plaintext[0] = new byte[]{ 1, 0, 1, 0, 1, 0, 1, 0 };
		plaintext[1] = new byte[]{ 1, 0, 1, 0, 1, 0, 1, 0 };
		plaintext[2] = new byte[]{ 0, 1, 0, 1, 0, 1, 0, 1 };
		plaintext[3] = new byte[]{ 1, 0, 1, 0, 1, 0, 1, 0 };
		
		ciphertext[0] = new byte[]{ 0, 0, 0, 1, 0, 0, 0, 1 };
		ciphertext[1] = new byte[]{ 1, 1, 0, 0, 1, 0, 1, 0 };
		ciphertext[2] = new byte[]{ 0, 1, 1, 1, 0, 0, 0, 0 };
		ciphertext[3] = new byte[]{ 0, 0, 0, 0, 0, 1, 0, 0 };
		
		for(int i = 0; i < 4; i++){
			byte[] output = SDES.Encrypt(key[i], plaintext[i]);
			boolean success = compare(output, ciphertext[i]);
			System.out.println("Encrypt test[" + i + "]: " + (success ? "success": "failure"));
			
			System.out.println("-----------------");
			printArray(output);
			printArray(ciphertext[i]);
			System.out.println("-----------------");
			
			output = SDES.Decrypt(key[i], ciphertext[i]);
			success = compare(output, plaintext[i]);
			System.out.println("Decrypt test[" + i + "]: " + (success ? "success": "failure"));

			System.out.println("-----------------");
			printArray(output);
			printArray(plaintext[i]);
			System.out.println("-----------------");
		}
	}
	
	public static void printArray(byte[] array){
		for(int i = 0; i < array.length; i++)
			System.out.print(array[i] + " ");
		System.out.println();
	}
	
	public static boolean compare(byte[] array1, byte[] array2){
		if(array1 == null || array2 == null)
			return false;
		if(array1.length != array2.length)
			return false;
		for(int i = 0; i < array1.length; i++)
			if(array1[i] != array2[i])
				return false;
		
		return true;
	}
	
}
