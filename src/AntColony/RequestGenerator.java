package AntColony;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
//import com.amazonaws.services.ec2.model.*;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.DefaultRequest;
import com.amazonaws.Request;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AttachVolumeRequest;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateKeyPairResult;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateTagsRequest;
import com.amazonaws.services.ec2.model.CreateVolumeRequest;
import com.amazonaws.services.ec2.model.CreateVolumeResult;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeImagesResult;
import com.amazonaws.services.ec2.model.DescribeInstanceAttributeRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeKeyPairsResult;
import com.amazonaws.services.ec2.model.Image;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.KeyPair;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.Volume;
import com.amazonaws.services.ec2.model.CreateKeyPairRequest;
import com.amazonaws.services.identitymanagement.model.CreateAccessKeyRequest;
import com.amazonaws.util.StringUtils;


import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;

public class RequestGenerator {	
	//Set the maximum number of requests  
	static int maxNumberOfRequests = 5;
	static int loopCount=1;
	static int randomNumberCount = 1;
	static ArrayList<Integer> randomNumbersList = new ArrayList<Integer>();   
	static AmazonEC2      ec2;	
	static class Task {  
        public void run() {        	
            //variable to store selected random number
            int selectedRandomNumber;           
            int[]  Storage1, Network1;
            String[]  CPU1;
            CPU1 = new String[]{"RedHat Linux,64,1", "Ubuntu,64,1", "SUSE Linux,64,1", "Windows,64,1", "Amazon Linux, 64,1"};
            //Storage in MB
            Storage1 = new int[]{5,15,20,7,10};
            //NW is MBPS
            Network1 = new int[]{20,40,50,25,15};
            //Location in latitude and longitude
            String[] Location1;
            Location1 = new String[]{"33.0,84.0", "47.0,122.0", "42.0,83.0", "39.0,104.0", "42.0,71.0"};
                                    
            //start the loop to generate 5 mobile service requests
              while(loopCount <= maxNumberOfRequests){
            	//generating random numbers without duplicates
            	if (randomNumberCount>5) { 
            		randomNumberCount = 1; 
            		randomNumbersList.clear();
            	}
            	
            	if (randomNumberCount==1) {
            		
            		for(int i = 1; i <= 5; i++)  {     
            			randomNumbersList.add(i);  
            		} 
            		
        			Collections.shuffle(randomNumbersList);
        	   	}        		             		 
            	selectedRandomNumber = (int) randomNumbersList.get((randomNumberCount-1));        		
        		//printing request details, delay time and request generated time
                System.out.println("Mobile Service Request# "+ loopCount);
                System.out.println("CPU: "+CPU1[selectedRandomNumber-1]+"\nStorage: "+Storage1[selectedRandomNumber-1]+"\nBandwidth: "+Network1[selectedRandomNumber-1]+"\nLocation: "+Location1[selectedRandomNumber-1]);
                System.out.println("Request generated at " +new Date()+"\n");                                                

                String CPUParameter = CPU1[selectedRandomNumber-1];
                //splitting to get OS type
                String OSType= CPUParameter.substring(0,CPUParameter.indexOf(','));
                CPUParameter=CPUParameter.substring(CPUParameter.indexOf(',')+1);
                //splitting to get OS bit size
                String OSBit= CPUParameter.substring(0,CPUParameter.indexOf(','));
                int OSBit1 = Integer.parseInt(OSBit.trim());
                //splitting to get Ram Size
                String OSRam=CPUParameter.substring(CPUParameter.indexOf(',')+1);
                int OSRam1 = Integer.parseInt(OSRam.trim());
                
                String LocationParameter = Location1[selectedRandomNumber-1];
                //splitting to get latitude
                String latitude = LocationParameter.substring(0,LocationParameter.indexOf(','));
                double latitude1 = Double.parseDouble(latitude.trim());
                //splitting to get longitude
                String longitude= LocationParameter.substring(LocationParameter.indexOf(',')+1);
                double longitude1 = Double.parseDouble(longitude.trim());                
                
                //forming the mobile request
                MobileRequest request = new MobileRequest(loopCount, Storage1[selectedRandomNumber-1], OSType, OSBit1, OSRam1, Network1[selectedRandomNumber-1], latitude1, longitude1);
                
                // The mobile service request is sent to the Ant Colony Optimization algorithm for resource allocation
                System.out.println("Using Ant Colony Optimization ...");
                int cloudID;
            	ArrayList<String> instanceToStart = new ArrayList<String>();
            	ArrayList<String> instanceToStop = new ArrayList<String>();
                try {
                long start = System.currentTimeMillis();
                AntColony AntColony = new AntColony(request);
                cloudID = AntColony.start();
                AWSCredentials credentials = null;
                try {
                    credentials = new ProfileCredentialsProvider().getCredentials();
                } catch (Exception e) {
                throw new AmazonClientException(
                            "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format."+e);
                }
                
                ec2 = new AmazonEC2Client(credentials);
                try {
                    DescribeAvailabilityZonesResult availabilityZonesResult = ec2.describeAvailabilityZones();
                    System.out.println("You have access to " + availabilityZonesResult.getAvailabilityZones().size() +
                            " Availability Zones.");
                    for (int i=0; i<availabilityZonesResult.getAvailabilityZones().size(); i++)
                    {
                    	
                    	System.out.println("You have access to " + availabilityZonesResult.getAvailabilityZones().get(i).getRegionName());
                    }

                    DescribeInstancesResult describeInstancesRequest = ec2.describeInstances();
                    List<Reservation> reservations = describeInstancesRequest.getReservations();
                    Set<Instance> instances = new HashSet<Instance>();
                    
                    for (Reservation reservation : reservations) {
                        instances.addAll(reservation.getInstances());
                    }

                    System.out.println("You have " + instances.size() + " Amazon EC2 instance(s) running.");
                    
                switch (cloudID)
        		{
        		case 1:
        		{
        			ec2.setEndpoint("https://ec2.us-west-1.amazonaws.com");
        			instanceToStart.add("i-8e6c0d44");
        			instanceToStop.add("i-8e6c0d44");
        			break;
        		}
        		case 2:
        		{
        			ec2.setEndpoint("https://ec2.us-west-2.amazonaws.com");
        			instanceToStart.add("i-3788793b");
        			instanceToStop.add("i-3788793b");
        			break;
        		}
        		case 3:
        		{
        			ec2.setEndpoint("https://ec2.us-east-1.amazonaws.com");
        			instanceToStart.add("i-108fbbfd");
        			instanceToStop.add("i-108fbbfd");
        			break;
        		}
        		case 4:
        		{
        			ec2.setEndpoint("https://ec2.eu-west-1.amazonaws.com");
        			instanceToStart.add("i-73bd8e30");
        			instanceToStop.add("i-73bd8e30");
        			break;
        		}
        		case 5:
        		{
        			ec2.setEndpoint("https://ec2.ap-southeast-1.amazonaws.com");
        			instanceToStart.add("i-f35f9a3e");
        			instanceToStop.add("i-f35f9a3e");
        			break;
        		}
        		default:
        		{
        			break;
        		}
        		}
        		
        		StartInstancesRequest startInsR = new StartInstancesRequest();
        		startInsR.setInstanceIds(instanceToStart);
        		StartInstancesResult result = ec2.startInstances(startInsR);
            	ec2.startInstances(startInsR);
            	System.out.println("Started Instance# "+instanceToStart.get(0));
            	//instanceLaunchTime = instance.withInstanceID(instanceToStart.get(0)).getLaunchTime();
            	Thread.sleep(50000);
        		StopInstancesRequest stopInsR = new StopInstancesRequest();
        		stopInsR.setInstanceIds(instanceToStop);
        		ec2.stopInstances(stopInsR);
        		System.out.println("Stopped Instance# "+instanceToStop.get(0));
        		System.out.println("***********************************************************");               
                }
                catch (AmazonServiceException ase) {
                    System.out.println("Caught Exception: " + ase.getMessage());
                    System.out.println("Reponse Status Code: " + ase.getStatusCode());
                    System.out.println("Error Code: " + ase.getErrorCode());
                    System.out.println("Request ID: " + ase.getRequestId());
                }
                }
                catch(IOException ioe)
                {
                	System.out.println("IOException: "+ioe);
                }
                catch(InterruptedException ie)
                {
                	System.out.println("Interrupted Exception: "+ie);
                }
                catch(ExecutionException ee)
                {
                	System.out.println("Execution Exception: "+ee);
                }
                loopCount++;
                randomNumberCount++;
                 
                }
        }
    }
    

    public static void main(String[] args) throws Exception {
    	
        new Task().run();
    	
    }
}