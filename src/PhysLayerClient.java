//Andrew Tek
//CS 380 Project 2

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class PhysLayerClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		 try (Socket socket = new Socket("codebank.xyz", 38002)) {
			 	System.out.println("Connected to server...");
			 	int value;
			 	InputStream is = socket.getInputStream();
			 	String message = "";
			 	OutputStream out = socket.getOutputStream();
			 	int total = 0;
			 	double baseline;
			 	byte messageBits[] ;
	            for (int i = 0; i < 64; i++) {   //get running total of the preamble of values
		            total += is.read();
	            }
	            baseline = (double)total / 64;
	            System.out.println("Established Baseline: " + baseline);
	            for (int i = 0;i < 320; i++) {
	            	value = is.read();
	            	if (value > baseline)  //compare incoming values with baseline
	            		message = message.concat("1");
	            	else 
	            		message = message.concat("0");
	            	
	            }
	            
	            message = decode(message); //decode NRZI.. check previous bit
	            message = fiveToFour(message); //Use look up table to convert 5 bits to 4
	            byte [] byteMessage = new byte [32];
	            System.out.print("Bytes Received:");
	            for (int i = 0; i < message.length(); i += 8) { //store valus into byte array
	            	byte tmp =  (byte)Integer.parseUnsignedInt(message.substring(i, i + 8), 2);
	            	byteMessage [i / 8] = tmp;
	            	System.out.print(tmp);
	            }
	            out.write(byteMessage); //write to server
	            if (is.read() == 1)  //read response from sever
	            	System.out.println("\nResponse Good!");
	            else
	            	System.out.println("\nIncorrect.");
	            System.out.println("Disconnecting...");
		 }

	}
	public static String decode (String s) {
		String ret = "";
		char [] ch = s.toCharArray();
		ret = ret + ch[0];
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
