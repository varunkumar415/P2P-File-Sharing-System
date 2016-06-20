/**
* PeerServer class implements Server Functionality for a peer when a peer acts as a server for other Peer
* 
* @author  Varun Kumar [A20365139]
* @version 1.0
* @since   2015-09-07 
*/

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Logger;

class PeerServer implements Runnable {

	private static ServerSocket serverPSocket;
	private static Socket peerclientSocket = null;
	private static String peerDirectory1=null;

	PeerServer() {
		try{
				FileReader freader = new FileReader("config.properties");
				Properties ppt = new Properties();
				ppt.load(freader);
				peerDirectory1=ppt.getProperty("config.peerDirectory1");
				Thread tp = new Thread(this);
				tp.start();
		}
		catch(Exception e)
		{
				e.printStackTrace();
		}
		
	}

	/* run function is called when a new thread is started */
	public void run() 
	{
			try {
					FileReader freader = new FileReader("config.properties");
					Properties ppt = new Properties();
					ppt.load(freader);
					String P1port=ppt.getProperty("config.P1serverPort");
					int P1servPort = Integer.parseInt(P1port);
					
					serverPSocket = new ServerSocket(P1servPort);
					System.out.println("Peer1 Server started");
			} catch (Exception e) {
					System.err.println("Port already in use.");
					System.exit(1);
			}
	
			while (true) {
					try {
							peerclientSocket = serverPSocket.accept();
							//System.out.println("Accepted connection : " + peerclientSocket);
							Thread t1 = new Thread(new PeerServerThread(peerclientSocket,peerDirectory1));
							t1.start();
		
					} catch (Exception e) {
							System.err.println("Error in connection attempt.");
					}
			}
	
	}
}

/* PeerOne Class implements a peer functionality like Register files, Search File, Look Up Files Info and Download File */

public class PeerOne {

	private static Socket sock;
	private static Socket peersock;
	private static String fileName;
	private static BufferedReader stdin;
	private static PrintStream ps;
	//final static String peerDirectory1 = "/home/armaiv/varun/01Latest/Peer1";
	private static String peerDirectory1=null;
	static Logger debug = Logger.getLogger("[VARUN]");

	public static void main(String[] args) throws IOException {
		//debug.info("");
		new PeerServer();
		try {
			
				FileReader freader = new FileReader("config.properties");
				Properties ppt = new Properties();
				ppt.load(freader);
				String Iport=ppt.getProperty("config.IserverPort");
				String IP=ppt.getProperty("config.localhost");
				int IservPort = Integer.parseInt(Iport);
				int choice = 0;
				String strChoice;
				do {
	
					try {
						
							//connecting to Index server
							sock = new Socket(IP, IservPort); 
							stdin = new BufferedReader(new InputStreamReader(System.in));
							ps = new PrintStream(sock.getOutputStream());
					} catch (Exception e) {
							System.err.println("Cannot connect to the server, try again later.");
							System.exit(1);
					}
	
					System.out.println("\n\t\t*******Please Enter Valid Option:******* \n");
					strChoice = selectAction();
					choice = Integer.parseInt(strChoice.trim());
					if (!(choice >= 0 && choice <= 5))
						choice = 0;
					switch (choice) {
					case 1:
						ps.println("1");
						RegisterFiles();
						ps.close();
						sock.close();
						break;
					case 2:
						ps.println("2");
						GetListFiles();
						ps.close();
						sock.close();
						break;
					case 3:
						ps.println("3");
						SearchFile();
						ps.close();
						sock.close();
						break;
					case 4:
						ps.close();
						sock.close();
						DownloadFile();
						break;
					case 5:
						ps.println("5");
						System.exit(1);
						break;
					default:
						System.err.println("Sorry :( Please Enter the Correct Choice..");
						choice = 0;
						break;
					}
	
				} while (choice <= 5);

		} catch (Exception e) {
			System.err.println("not valid input");
		}
		ps.close();
		sock.close();
}

		public static String selectAction() throws IOException {
		System.out.println("1. Register Files to Index Server");
		System.out.println("2. Look UP Files Details");
		System.out.println("3. Search File");
		System.out.println("4. Download File from Peer");
		System.out.println("5. Exit\n");

		return stdin.readLine();
	}

	/* RegisterFiles: This Method implements client functionality to register files from a directory to Indexing Server */
	public static void RegisterFiles() throws IOException 
	{
		try {
				
				PrintWriter pwriter = new PrintWriter(new OutputStreamWriter(
				sock.getOutputStream()));
				BufferedReader breader = new BufferedReader(new InputStreamReader(
				sock.getInputStream()));
				
				FileReader freader = new FileReader("config.properties");
				Properties ppt = new Properties();
				ppt.load(freader);
				peerDirectory1=ppt.getProperty("config.peerDirectory1");
				
				File file = new File(peerDirectory1);
				File filearray[] = file.listFiles();
	
				Arrays.sort(filearray);
				for (int i = 0; i < filearray.length; i++)
					pwriter.println(":" + filearray[i].getName());
					
				pwriter.println("~");// end of writing
				pwriter.flush();
				while ((breader.read()) != '~')
					System.out.println(breader.readLine());
	
			} catch (IOException ex) {
				System.err.println(ex);
			}
	}
	
	
	/* GetListFiles: This method implements to retrieve files information from Indexing server */
	public static void GetListFiles() 
	{
		try {
				BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				System.out.println("-----------------------------------------------------------------------");
				while ((br.read()) != '~')
						System.out.println(br.readLine());
			} catch (IOException ex) {
				System.err.println(ex);
			}
	}
	
	
	/* SearchFile: This method implements to retrieve a file(requested from peer) information from Indexing server */
	public static void SearchFile() 
	{
	
			try {
					PrintWriter pwriter = new PrintWriter(new OutputStreamWriter(
					sock.getOutputStream())); // s-c
					BufferedReader br = new BufferedReader(new InputStreamReader(
					sock.getInputStream())); // c-s
					System.err.print("Enter File name: \n");
					fileName = stdin.readLine();
					pwriter.println(":" + fileName);
					pwriter.println("~");// end of writing
					pwriter.flush();
					while ((br.read()) != '~')
					System.out.println(br.readLine());
			} catch (IOException ex) {
				System.err.println(ex);
			}
	}
	
	/* DownloadFile: This method implements to a file(requested from peer) from other Peer */
	public static void DownloadFile() 
	{
			try {
				
					System.out.print("Enter Peer IP Address: ");
					String peerIP = stdin.readLine();
					System.out.print("Enter Peer PORT: ");
					String peerPORT = stdin.readLine();
					int port = Integer.parseInt(peerPORT);
					System.out.println("Enter File Name..to start download..");
					String fileName = stdin.readLine();
		
					peersock = new Socket(peerIP, port); //
					PrintWriter pwriter = new PrintWriter(new OutputStreamWriter(peersock.getOutputStream()));
					BufferedReader br = new BufferedReader(new InputStreamReader(peersock.getInputStream()));
					pwriter.println(""+fileName);
					pwriter.flush();
					
					
					int bytesRead;
					InputStream in = peersock.getInputStream();	
					DataInputStream clientData = new DataInputStream(in);
					fileName = clientData.readUTF();
					OutputStream output = new FileOutputStream((fileName));
					long size = clientData.readLong();
					byte[] buffer = new byte[1024];
					while (size > 0 && (bytesRead = clientData.read(buffer, 0,(int) Math.min(buffer.length, size))) != -1)
					{
						output.write(buffer, 0, bytesRead);
						size -= bytesRead;
					}
					output.flush();
					output.close();
				    peersock.close();
					in.close();
					System.out.println("File " + fileName + " Download completed :)");
	
			} catch (IOException ex) {
				System.err.println("File does not exist!");
			}
			
	}
}