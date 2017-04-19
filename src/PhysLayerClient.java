import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class PhysLayerClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		 try (Socket socket = new Socket("codebank.xyz", 38002)) {
			 	int value;
			 	InputStream is = socket.getInputStream();
			 	String message = "";
			 	 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
	        	BufferedReader br = new BufferedReader(isr);
			 	System.out.println("Bytes Received:");
			 	int total = 0;
			 	double baseline;
			 	byte messageBits[] ;
	            for (int i = 0; i < 64; i++) { 
		            total += isr.read();
	            }
	            baseline = (double)total / 64;
	            System.out.println(baseline);
	            System.out.println("HERE:");
	            for (int i = 0;i < 320; i++) {
	            	value = isr.read();
	            	//System.out.println(value1 + ",     " + i + "     " + message);
	            	if (value > baseline) 
	            		message = message.concat("1");
	            	else 
	            		message = message.concat("0");
	            	
	            }
	            
	            System.out.println("MESSAGE:" + message);
	            message = decode(message);
	            System.out.println(message.length());
	            System.out.println("NEW MESSAGE: " + message);
	            message = fiveToFour(message);
	            System.out.println("4 bit Message: " + message);
	            System.out.println("Size: " + message.length());
	            byte [] byteMessage = new byte [32];
	            for (int i = 0; i < message.length(); i += 8) {
	            	byte tmp =  (byte)Integer.parseUnsignedInt(message.substring(i, i + 8), 2);
	            	byteMessage [i / 8] = tmp;
	            	out.write(tmp);
	            	//byteMessage [i / 8] = Byte.parseByte(message.substring(i, i + 8), 2);
	            	System.out.println(byteMessage[i / 8]);
	            }
	            System.out.println("trying...");
	            value = is.read();
	            System.out.println("HERE IS THE RESULT: " + value);
		 }

	}
	public static String decode (String s) {
		String ret = "";
		char [] ch = s.toCharArray();
		ret = ret + ch[0];
		System.out.println("LENGTH: " + ch.length);
		for (int i = 1; i < ch.length; i++) {
			if (ch[i] == ch[i - 1]) 
				ret = ret.concat("0");
			else
				ret = ret.concat("1");
		}
		return ret;
	}
	public static String fiveToFour (String s) {
		String ret = "";
		String tmp;
		for (int i = 0; i < s.length(); i += 5) {
			tmp = s.substring(i, i + 5);
			switch(tmp) {
			case "11110":
				tmp = "0000";
				break;
			case "01001":
				tmp = "0001";
				break;
			case "10100":
				tmp = "0010";
				break;
			case "10101":
				tmp = "0011";
				break;
			case "01010":
				tmp = "0100";
				break;
			case "01011":
				tmp = "0101";
				break;
			case "01110":
				tmp = "0110";
				break;
			case "01111":
				tmp = "0111";
				break;
			case "10010":
				tmp = "1000";
				break;
			case "10011":
				tmp = "1001";
				break;
			case "10110":
				tmp = "1010";
				break;
			case "10111":
				tmp = "1011";
				break;
			case "11010":
				tmp = "1100";
				break;
			case "11011":
				tmp = "1101";
				break;
			case "11100":
				tmp = "1110";
				break;
			case "11101":
				tmp = "1111";
				break;
			default: tmp = "ERROR";
			}
			ret = ret.concat(tmp);
		}
		
		return ret;
	}

}
