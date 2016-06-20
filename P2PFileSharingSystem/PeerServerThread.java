/**
* PeerServerThread class implements Server Functionality for a peer when a peer acts as a server for other Peer
* 
* @author  Varun Kumar
* @version 1.0
* @since   2015-09-07 
*/

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;

public class PeerServerThread implements Runnable {
	
	private Socket peerSocket;
	private String peerDirectory;
    private BufferedReader in = null;
    
    public PeerServerThread(Socket client, String dirpeer) {
    	this.peerSocket = client;
    	this.peerDirectory=dirpeer;
    }
    
    
    /*run Function will be called when a thread will start*/
	public void run() 
	{
		try {
				
			 	in = new BufferedReader(new InputStreamReader(peerSocket.getInputStream()));
				String FileName=null;
				while ((FileName = in.readLine()) != null)
				{		
					DownloadFile(FileName);
				}
	
			} 
			catch (IOException ex){ }
	}
	
	/*DownloadFile: This Method will be called when a peer request a file from other peer */
	 public void DownloadFile(String fileName) {
		 try {
			 	String filepath = peerDirectory.concat(fileName);
	            File myFile = new File(filepath);
	            byte[] mybytearray = new byte[(int) myFile.length()];
	
	            FileInputStream fis = new FileInputStream(myFile);
	            BufferedInputStream bis = new BufferedInputStream(fis);
	            
	            DataInputStream dis = new DataInputStream(bis);
	            dis.readFully(mybytearray, 0, mybytearray.length);
	
	            //handle file send over socket
	            OutputStream os = peerSocket.getOutputStream();
	
	            //Sending file name to the server
	            DataOutputStream dos = new DataOutputStream(os);
	           
	            dos.writeUTF(myFile.getName());
	            dos.writeLong(mybytearray.length);
	            dos.write(mybytearray, 0, mybytearray.length);
	            dos.flush();
	            //dos.close();
		    
	        } catch (Exception e) {
	            System.err.println("File does not exist!");
	        } 
		
	 }

}//class end