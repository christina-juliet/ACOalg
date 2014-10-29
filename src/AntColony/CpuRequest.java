package AntColony;
/*
 Created by Radhika - gives the details of the CPU request;
 */
public class CpuRequest {
	
	//OS type - Linux etc
	private String operatingSystem;
	
	//Os system bit value value is 32 or 64
	private int bitsOfOS;
	
	//OS ram specification in GB
	private int ramQuantity;
	
	//Constructors
	//Default constructor giving default values
	CpuRequest()
	{
		operatingSystem = "Ubuntu";
		bitsOfOS=64;
		ramQuantity=1;
		
	}
	
	CpuRequest(String OsName, int bitsRequested, int ramRequest)
	{
		operatingSystem = OsName;
		bitsOfOS=bitsRequested;
		ramQuantity=ramRequest;
	}
	
	//getters and setters
	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public int getBitsOfOS() {
		return bitsOfOS;
	}

	public void setBitsOfOS(int bitsOfOS) {
		this.bitsOfOS = bitsOfOS;
	}

	public int getRamQuantity() {
		return ramQuantity;
	}

	public void setRamQuantity(int ramQuantity) {
		this.ramQuantity = ramQuantity;
	}
}