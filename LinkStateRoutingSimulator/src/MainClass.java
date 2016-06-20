/**
* Routing_Algorithm class implements Dijkstr's algorith to find the shortest path and cost between two routers(nodes).
* 
* @author  Varun Kumar [A20365139]
* @version 1.0
* @Date   2015-11-15 
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

class Routing_Algorithm
{

	//method to compute shortest path and cost between two routers
	public  Object Dijkstra_Algorithm(int[][] wtMatrix, int source , int destination ,int action)
	{
		int nCount =0 ;
		int indx = source;
		int[] Node = new int[wtMatrix[0].length];
		Node[nCount] = indx;
		int[] pathLength_Array = wtMatrix[source].clone();
		boolean[] boolArray = new boolean[wtMatrix[0].length];
		int[] pIndx = new int[wtMatrix[0].length];
		for (int i = 0; i < pIndx.length; i++) 
		{
			pIndx[i] = source;
		}
		
		
		boolArray[indx] = true;
		while (nCount < wtMatrix[0].length) 
		{
			
			int min = Integer.MAX_VALUE;
			
			for (int i = 0; i < pathLength_Array.length; i++) {
				if (!boolArray[i] && pathLength_Array[i] != -1 && i != indx) {
			
					if (pathLength_Array[i] < min) {
						min = pathLength_Array[i];
						indx = i;
					}
				}
			}
			if (indx == destination) 
			{
				break;
			}
			boolArray[indx] = true;
			nCount++;
			Node[nCount] = indx;
			
			for (int i = 0; i < pathLength_Array.length; i++) {
				
				if (pathLength_Array[i] == -1 && wtMatrix[indx][i] != -1 && ! boolArray[i]) 
				{
					pathLength_Array[i] = pathLength_Array[indx] + wtMatrix[indx][i];
					pIndx[i] = indx;
				}
				else if (wtMatrix[indx][i] != -1 && pathLength_Array[indx] + wtMatrix[indx][i] < pathLength_Array[i])
				{
					
					pathLength_Array[i] = pathLength_Array[indx] + wtMatrix[indx][i];
					pIndx[i] = indx;
				}
			}
		}
		if(action ==2 )
		{
		System.out.print("The shortest path from " + (source + 1) + " to " + (destination + 1) + " is :: ");
		get_Shortest_Path(pIndx, source, destination, pathLength_Array.length);
		System.out.println();
		System.out.println("The total cost is: "+ (pathLength_Array[destination] - pathLength_Array[source]));

		return pathLength_Array[destination];
		}
		if(action ==1 )
			return get_Interface_Router(pIndx, source, destination, pathLength_Array.length);
		else return null;
		
	}
	
	//method to compute and display shortest path between two routers
	public static void get_Shortest_Path(int[] pIndx, int source, int destination, int path_len)
	{
		int[] path_array = new int[path_len];
		int i = destination;
		path_array[0] = i;
		int m = 1;
		while (pIndx[i] != source) 
		{
			i = pIndx[i];
			path_array[m] = i;
			m++;
		}
		path_array[m] = source;
		
		for (int j = m; j > 0; j--)
		{
			System.out.print(" " + (path_array[j] + 1) + " --> ");
			
		}
		System.out.print(" " + (path_array[0] + 1)) ;
	}	
	
//method to compute and display routing table for a particular router.
public static String get_Interface_Router(int[] pIndx, int source, int destination, int len)
{
	int[] nRouter = new int[len];
	int i = destination;
	nRouter[0] = i;
	int k = 1;
	while (pIndx[i] != source)
	{
		i = pIndx[i];
		nRouter[k] = i;
		k++;
	}
	nRouter[k] = source;
	int hop=0;
	for (int j = k; j > 0; j--)
	{
		
		hop++;
		if(hop==2)
			return "     "+(nRouter[j]+1);
	}

	 if (hop==1)
		{
		 destination = destination+1;
		return "     "+destination;
		}
	 return null;
}
	
	
	

}

/**
* Mainclass implements Link State Routing protocol : implemented different operstions
* 
* @author  Varun Kumar [A20365139]
* @version 1.0
* @Date   2015-11-15 
*/

public class MainClass 
{
	static MainClass mainObj = new MainClass();
	static Routing_Algorithm c2Obj = new Routing_Algorithm();
	public static int source_stat = 0;
	public static int deletenode = 0;
	static int costMatrix[][]=null;
	
	@SuppressWarnings("resource")
	//method to create a network topology by reading matrix from a text file
	public  int[][] CreateNetwork() throws IOException 
	{
		
		int mat_Row = 0 , mat_Column = 0;
		String strline[]=null;
		System.out.println("Input original network topology matix data file:");
		Scanner scan = new Scanner(System.in);
		String fileName = scan.next();
		
		try{
		FileReader fReader = new FileReader(fileName);
		BufferedReader breader =  new BufferedReader(fReader);
		
		String rowwise_Str ;
		System.out.println("Review original topology matrix: [Matrix Table]");
		
			while ((rowwise_Str= breader.readLine()) != null)
			{
				mat_Row++;
				strline = rowwise_Str.split("[\\s]+");
				mat_Column= strline.length;
				
			}
		
			//System.out.println("[VARUN] CreateNetwork() row:"+row+" column:"+column+"\n");
			@SuppressWarnings("unused")
			int costMatrix[][]= new int[mat_Row][mat_Column];
			breader.close();
		}
		
		 catch(Exception e)
		{
			System.out.println("[Alert!] Please enter correct file name..");
			int costMatrix[][]=null;
			int input;
			for(;;){
				System.out.println("==================================================");
				System.out.println("CS542 Link State Routing Simulator");
				System.out.println("(1) Create a Network Topology");
				System.out.println("(2) Build a Connection Table");
				System.out.println("(3) Shortest Path to Destination Router");
				System.out.println("(4) Modify a topology");
				System.out.println("(5) Exit");
				System.out.println("==================================================");
				try{
					Scanner sc = new Scanner(System.in);
					input = sc.nextInt();
					
					}catch(InputMismatchException i)
					{
						System.out.println("[Alert!] Please enter a valid option from menu..");
						continue;
					}
				
				switch (input)
				   {
					case 1:
						costMatrix = mainObj.CreateNetwork();
						PrintMatrixTable(costMatrix);
						for(int i=1;i<=costMatrix.length;i++)
						{
							mainObj.Prepare_Routing_Table_For_All(costMatrix,i);
						}	
						
						break;
					case 2:
						if(costMatrix==null)
						{
							System.out.println("[Alert!] Please First Create a Network Topology.. choose option 1 ");
							continue;	
						}
						
						mainObj.Prepare_Routing_Table(costMatrix);
						break;
					case 3:
						if(costMatrix==null)
						{
							System.out.println("[Alert!] Please First Create a Network Topology.. choose option 1 ");
							continue;	
						}
						mainObj.Algorithm(costMatrix, source_stat);
						break;
					case 4:
						if(costMatrix==null)
						{
							System.out.println("[Alert!] Please First Create a Network Topology.. choose option 1 ");
							continue;	
						}
						System.out.println("\nSelect a Router to be Removed");
						Scanner sc1 = new Scanner(System.in);
						int delnode = sc1.nextInt();
						deleteRouter(costMatrix, delnode);
						break;
					case 5:
						System.out.println(" Exit CS542 project. Good Bye!");
						System.exit(0);
						
					default: System.out.println("Invalid input please try again");
					}
			}		

		}
			return ReadFromFile(fileName,mat_Column);
	
	}

	//method to read from a file and store values into a matrix
	public static int[][] ReadFromFile(String filename,int mat_Column) throws NumberFormatException, IOException
	{
		int row=0;
		String rowwise_Str;
		int costmMatrix[][] = new int[mat_Column][mat_Column];
		FileReader fileReader = new FileReader(filename);
		BufferedReader br1 =  new BufferedReader(fileReader);
			
		
		
			while ((rowwise_Str = br1.readLine()) != null)
			{
				String line[] = rowwise_Str.split("[\\s]+");

				for (int i = 0; i < mat_Column; i++) 
				{
					costmMatrix[row][i] = Integer.parseInt(line[i]);
				}
				row++;
			}
		
			br1.close();
		    
			return costmMatrix;	
		
	}

	//method to invoke dijkstr's algorithm
	public void Algorithm (int matrix[][], int src) throws IOException
	{
		
		int counter=0;
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		System.out.println("Select the destination router:");
		int dest = scan.nextInt();
		for(int i=0;i<matrix.length;i++)
		{
			if(matrix[src-1][i]==-1)
			counter++;
		}
		if(counter==(matrix.length)-1)
		{
			System.out.println("There is no path available from "+src+" to "+dest);
			return;
		}
		if(dest==deletenode)
		{
			while(dest==deletenode)
			{
				System.out.printf("Sorry this Router is disabled.. select some other Router: ");
				dest = scan.nextInt();
			}
		}
		
		if(dest==src)
		{
			System.out.println("The total cost is: 0");
			return;
		}
		c2Obj.Dijkstra_Algorithm(matrix,src-1,dest-1,2);
	}
	
	
	//method to display routing table of a selected router
	public  void Prepare_Routing_Table(int matrix[][])
	{
		
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		int rlen = matrix.length;
		System.out.println("Select a source router: ");
		int source =sc.nextInt(); // take source router
		if(source==deletenode)
		{
			
			
			while(source==deletenode)
			{
				System.out.printf("Sorry this Router is disabled.. select some other Router: ");
				source =sc.nextInt();
			}
			
		}	
		
		source_stat= source;
		
		Object arr[][]=new Object[1][3];
		System.out.println("  Router "+source +" Connection Table");
		System.out.println("  Destination     Interface");
		System.out.println("=======================================");
		System.out.println("   Router "+(source)+"     "+ " -");
		try{
			for(int i=0; i<=rlen; i++)
				{
				if(i==(source-1))
				  { 
					i++;
				  }
				   
					arr[0][0]="Router "+(source);
					arr[0][1]="   Router "+(i+1);
					
					arr[0][2]= c2Obj.Dijkstra_Algorithm(matrix,source-1,i,1);
				
	
					PrintConnectionTable(arr); //inside for Loop
					
				}
	           
			System.out.println("");
			
		}
		catch(Exception e)
		{
			System.out.println();
		}
	}
	
	//method to display routing tables for all available routers
	public  void Prepare_Routing_Table_For_All(int matrix[][], int src)
	{
		
		
		int rlen = matrix.length;
		int sourceNode =src; // take source router
		source_stat= sourceNode;
		
		Object arr[][]=new Object[1][3];
		System.out.println("  Router "+sourceNode +" Connection Table");
		System.out.println("  Destination     Interface");
		System.out.println("=======================================");
		System.out.println("   Router "+(sourceNode)+"     "+ " -");
		try{
			for(int i=0; i<=rlen; i++)
				{
				if(i==(sourceNode-1))
				  { 
					i++;
				  }
				   
					arr[0][0]="Router "+(sourceNode);
					arr[0][1]="   Router "+(i+1);
					
					arr[0][2]= c2Obj.Dijkstra_Algorithm(matrix,sourceNode-1,i,1);
				
	
					PrintConnectionTable(arr); //inside for Loop
					
				}
	           
			System.out.println("");
			
		}
		catch(Exception e)
		{
			System.out.println();
		}
	}
	
	//method to print the connection table 	
	public static void PrintConnectionTable(Object arr[][])
	{
		for(int i=0; i<arr.length; i++)
		{
			for(int j=1; j<arr[0].length; j++)
			{
				System.out.print(arr[i][j]+" ");
			}
			System.out.println();
		}
		
	}
		
	
	//method to display the cost matrix table      
	public static void PrintMatrixTable(int x[][])
	{
		for(int i=0;i<x.length;i++)
		{
			for(int j=0;j<x.length;j++)
			{
				System.out.print(x[i][j] + "\t");
			}
			System.out.println("\n");
		}
	}
	
	//method to remove (down) a router from a network topology
	public static void deleteRouter(int matrix[][], int delrouter)
	{
		    try{
		    deletenode = delrouter;
			int rows = matrix.length;
	        int columns = matrix.length;
	        int updatedMatrix[][] = new int[rows][columns];

	        int REMOVE_ROW = delrouter-1;
	        int REMOVE_COLUMN = delrouter-1;
	        int a = 0;
	        for( int i = 0; i < rows; ++i)
	        {
	            int b = 0;
	            for( int j = 0; j < columns; ++j)
	            {
	               // if ( j == REMOVE_COLUMN)
	                   // continue;
	            	if ( i == REMOVE_ROW || j == REMOVE_COLUMN)
	            		updatedMatrix[a][b] = -1;
	            	//if(p==REMOVE_ROW && q == REMOVE_COLUMN)
	            		//updatedMatrix[p][q] = 0;
	            	else
	                updatedMatrix[a][b] = matrix[i][j];
	                ++b;
	            }

	            ++a;
	        }
	        System.out.println("Updated Matrix Table:");
	        PrintMatrixTable(updatedMatrix);
	        for( int i = 0; i < columns; ++i)
            {
	        	for(int j = 0; j< columns; ++j)
	        	{
	        		matrix[i][j]=updatedMatrix[i][j];
	        	}
            }
	        //checking shortest path, cost, connection table after the modification of network topology.
	        mainObj.Prepare_Routing_Table(updatedMatrix);
	        mainObj.Algorithm(updatedMatrix, source_stat);
		    }
		    catch(Exception e)
		    {
		    	e.printStackTrace();
		    }
	        
	        
	        
	}
	
	
	// Main Method to perform all operations
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception 
	{
		 System.out.println("Date: "+new Date() +"\n"+"Name: "+ "Varun Kumar" +"\n");	
		//int costMatrix[][]=null;
		int userInput;
		
		for(;;){
			System.out.println("==================================================");
			System.out.println("CS542 Link State Routing Simulator");
			System.out.println("(1) Create a Network Topology");
			System.out.println("(2) Build a Connection Table");
			System.out.println("(3) Shortest Path to Destination Router");
			System.out.println("(4) Modify a topology");
			System.out.println("(5) Exit");
			System.out.println("==================================================");
			
			//@SuppressWarnings("resource")
			try{
			Scanner sc = new Scanner(System.in);
			userInput = sc.nextInt();
			
			
			}catch(InputMismatchException i)
			{
				System.out.println("[Alert!] Please enter a valid option from menu..");
				continue;
			}
			
			switch (userInput)
			   {
				case 1:
					costMatrix = mainObj.CreateNetwork();
					PrintMatrixTable(costMatrix);
					for(int i=1;i<=costMatrix.length;i++)
					{
						mainObj.Prepare_Routing_Table_For_All(costMatrix,i);
					}	
					break;
				case 2:
					if(costMatrix==null)
					{
						System.out.println("[Alert!] Please First Create a Network Topology.. choose option 1 ");
						continue;	
					}
					mainObj.Prepare_Routing_Table(costMatrix);
					break;
				case 3:
					if(costMatrix==null)
					{
						System.out.println("[Alert!] Please First Create a Network Topology.. choose option 1 ");
						continue;	
					}
					mainObj.Algorithm(costMatrix, source_stat);
					break;
					
				case 4:
					if(costMatrix==null)
					{
						System.out.println("[Alert!] Please First Create a Network Topology.. choose option 1 ");
						continue;	
					}
					System.out.println("\nSelect a Router to be Removed");
					Scanner sc1 = new Scanner(System.in);
					int delnode = sc1.nextInt();
					deleteRouter(costMatrix, delnode);
					break;
				case 5:
					System.out.println(" Exit CS542 project. Good Bye!");
					System.exit(0);
					
				default: System.out.println("Invalid input please try again");
				}
		}		
		
	}
	
	
}