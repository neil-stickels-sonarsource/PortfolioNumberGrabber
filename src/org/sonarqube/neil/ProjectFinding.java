package org.sonarqube.neil;

/*
 * Class used to store all of the ProjectFindings from the export_findings
 * API.  There is much much more data that could be saved, but based on the
 * use case, only the type and severity seemed like it was needed.  I added
 * the status as well in case any inspection of Hotspots was desired.
 *
 */
public class ProjectFinding {
	
	private String status;
	private String type;
	private String severity;
	
	public ProjectFinding()
	{
		
	}
	
	public ProjectFinding(String status, String type, String severity)
	{
		this.status = status;
		this.type = type;
		this.severity = severity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

}
