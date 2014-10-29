package AntColony;

/*
Created by Radhika - gives the details of each of mobile request generated;
Tag <christina> - Changes added by Christina.
*/
public class MobileRequest 
{
	//Request number
	private int requestIdentfier;
	//Storage in MB
	private int storageQuantity;
	//CPU parameters
	private  CpuRequest cpuQuantity;
	//Network in MBPS
	private int networkQuantity;
	// Location in latitude and longitude <christina>
	private Location location; // <christina>
	
	
	//Constructors - the constructor has to have Request number minimum.
	
	public MobileRequest(int requestNo) 
	{
		//the default values given are 25MB; linux-64bit-1 GB and 20MBPS
		requestIdentfier= requestNo;
		storageQuantity= 25;
		cpuQuantity = new CpuRequest();
		networkQuantity= 20;
		location = new Location(); // <christina>
	}
	
	public MobileRequest(int requestNo,int requiredStorage, String OSType, int OSBits, int OSRam,int requiredNw, double latitude, double longitude) // <christina>
	{
		requestIdentfier= requestNo;
		storageQuantity=requiredStorage;
		cpuQuantity=new CpuRequest(OSType,OSBits,OSRam);
		networkQuantity=requiredNw;
		location = new Location(latitude, longitude); // <christina>
	}
	
	
	public MobileRequest(int requestNo,int requiredStorage, CpuRequest cpuRequestParameters ,int requiredNw, Location locationParameters) 
	{
		requestIdentfier= requestNo;
		storageQuantity=requiredStorage;
		cpuQuantity=cpuRequestParameters;
		networkQuantity=requiredNw;
		location = locationParameters;
	}

	
	
	//setters and getters
	
	public int getRequestIdentfier() {
		return requestIdentfier;
	}

	public void setRequestIdentfier(int requestIdentfier) {
		this.requestIdentfier = requestIdentfier;
	}

	public int getStorageQuantity() {
		return storageQuantity;
	}

	public void setStorageQuantity(int storageQuantity) {
		this.storageQuantity = storageQuantity;
	}

	public CpuRequest getCpuQuantity() {
		return cpuQuantity;
	}

	public void setCpuQuantity(CpuRequest cpuQuantity) {
		this.cpuQuantity = cpuQuantity;
	}

	public int getNetworkQuantity() {
		return networkQuantity;
	}

	public void setNetworkQuantity(int networkQuantity) {
		this.networkQuantity = networkQuantity;
	}
	
	/* <christina>*/
	public Location getLocation(){
		return location;
	}
	
	public void setLocation(Location loc){
		this.location = loc;
	}
	
	
	

}