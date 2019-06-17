import java.io.File;
import java.net.*;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

class client{
	/**
	 * on defined port communicates with servers on loadBalancer
	 * sends random integer data on every 5 second for simulating the process waiting
	 * you can't send integer as byte array directly
	 * so i take it as BigInteger first
	 * turn it into byte array
	 * then send it to the server
	 * for being continuous in scenario, I am generating random numbers
	 * but if you remove file reading parts comments
	 * this app also can provide file reading
	 * since I am using linux based operating system, it's enough for me to define directory as './'
	 * but of course you can change directory according your system
	 * @throws Exception
	 */
	static DatagramSocket cs;
	public static void main(String[] args) throws Exception{
		byte[] sd = new byte[1024];
		InetAddress ipaddr = InetAddress.getByName("localhost");
		client.cs = new DatagramSocket();
		DatagramPacket sp;
		// File file = new File("./data.txt");
		// Scanner scanner = new Scanner(file);
		// insede while condition instead of true => scanner.hasNext()
		while(true){
			Random rnd = new Random();
			int slpVal = rnd.nextInt(100) + 1;
			// int slpVal = scanner.nextInt();
			sd = BigInteger.valueOf(slpVal).toByteArray();
			sp = new DatagramPacket(sd, sd.length, ipaddr, 9999);
			cs.send(sp);
			client.closeSocket(slpVal);
			System.out.println("Data is sended: " + slpVal);
			Thread.sleep(5000);
		}
	}
	public static void closeSocket(int slpVal){
		if(slpVal==0) {
			System.out.println("All connections are closing down!");
			cs.close();
			System.exit(0);
		}else{ return; }
	}
}
