package org.sonarqube.neil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Class used to store all of the information for each Project.
 * The most important item here is the findingsList which is a 
 * list of ProjectFinding objects.  
 */
public class Project {
	
	private String key;
	private String refId;
	private String refKey;
	private String name;
	private List<ProjectFinding> findingsList = new ArrayList<>();
	
	public Project(String key, String refId, String refKey, String name)
	{
		this.key = key;
		this.refId = refId;
		this.refKey = refKey;
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getRefKey() {
		return refKey;
	}

	public void setRefKey(String refKey) {
		this.refKey = refKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	/*
	 *  Take the JSONArray returned from the export_findings API and create
	 *  an instance of the ProjectFinding class for each one.
	 */
	public void setFindings(JSONArray exportFindings) {
		for(int i = 0; i < exportFindings.length(); i++)
		{
			JSONObject data = exportFindings.getJSONObject(i);
			//System.out.println("Data is: "+data);
			String status = data.getString("status");
			String type = data.getString("type");
			String severity;
			try
			{
				severity = data.getString("severity");
			} catch (org.json.JSONException e)
			{
				// Hotspots don't have a Severity, so this was a workaround
				// to make sure that Hotspots would still be read in and saved
				if(type.equals("SECURITY_HOTSPOT"))
					severity="SECURITY_HOTSPOT";
				else
					throw e;
			}
			ProjectFinding finding = new ProjectFinding(status, type, severity);
			findingsList.add(finding);
		}
		//System.out.println("findingsList.size is "+findingsList.size());
	}

	/*
	 *  Used to write out all of the ProjectFindings.  This will create a 
	 *  single line in the output file for each of the specific counts
	 *  requested.
	 */
	public void writeFindings(FileWriter fw) throws IOException {
		StringBuilder output = new StringBuilder(name);
		output.append(",");
		output.append(getCount("BUG","BLOCKER"));
		output.append(",");
		output.append(getCount("BUG","CRITICAL"));
		output.append(",");
		output.append(getCount("BUG","MAJOR"));
		output.append(",");
		output.append(getCount("VULNERABILITY","BLOCKER"));
		output.append(",");
		output.append(getCount("VULNERABILITY","CRITICAL"));
		output.append(",");
		output.append(getCount("VULNERABILITY","MAJOR"));
		fw.write(output.toString());
	}

	/*
	 *  This is where all of the filtering to specific issue types and
	 *  severities is handled.  This will get a count of all of the issues
	 *  meeting the specified type and severity provided.
	 */
	private int getCount(String type, String severity) {
		int count = 0;
		for(ProjectFinding finding: findingsList)
		{
			if(finding.getType().equals(type) && finding.getSeverity().equals(severity))
				count++;
		}
		return count;
	}

}
