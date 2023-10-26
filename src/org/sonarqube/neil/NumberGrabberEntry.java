package org.sonarqube.neil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class NumberGrabberEntry {
	
	/*
	 * main method that gets called to do all of the execution.
	 * This method will call the API to find all of the Projects associated
	 * with a Portfolio, and for each Project, call the API to get all of
	 * the issues for that Project, and get the counts of the issues.
	 * Everything will be written out to a CSV file provided as an argument
	 */
	public static void main(String[] args) {
		if(args.length != 5)
		{
			System.out.println("Expected usage: java -jar PortfolioNumberGrabber.jar {1} {2} {3} {4} {5}\nwhere: \n\t {1} is your sonar token,\n\t {2} is your SonarQube URL,\n\t {3} is the name of the portfolio to count,\n\t {4} is the branch to use\n\t {5} is the file to save results");
			System.exit(0);
		}
		String token = args[0]; // user token to login to Sonar API
		String url = args[1]; // base URL for your SonarQube instance
		String portfolioName = args[2]; // name of the Portfolio to get counts
		String branchName = args[3]; // name of the branch to use for each Project
		String fileName = args[4]; // name of the file to write the output
		
		// call the API to get all of the Projects within a Portfolio
		ApiCaller caller = new ApiCaller(token, url);
		String response = caller.getPortfolio(portfolioName);
		
		JSONObject json = new JSONObject(response);
		// each Project will be part of a components array within the JSON
		JSONArray projects = json.getJSONArray("components");
		FileWriter fw = null;
		try 
		{
			fw = new FileWriter(fileName);
			// write all of the header information for the file
			fw.write(portfolioName+",Bugs,,,Vulnerabilities");
			fw.write("\n");
			fw.write(",Blocker,Critical,Major,Blocker,Critical,Major");
			fw.flush();
			// iterate through each Project to get the counts for each
			for(int i = 0; i < projects.length(); i++)
			{
				String key = projects.getJSONObject(i).getString("key");
				String refId = projects.getJSONObject(i).getString("refId");
				String refKey = projects.getJSONObject(i).getString("refKey");
				String name = projects.getJSONObject(i).getString("name");
				Project project = new Project(key, refId, refKey, name);
				System.out.println("getting findings for "+project.getName());
				String thisBranchName = branchName;
				// call the API to get all of the findings for this Project
				String findings = caller.getProjectFindings(project.getRefKey(), thisBranchName);

				JSONObject findingsJson = new JSONObject(findings);
				JSONArray exportFindings = findingsJson.getJSONArray("export_findings");
				// save the findings to this Project object
				project.setFindings(exportFindings);
				fw.write("\n");
				// write out the findings to the file
				project.writeFindings(fw);
				fw.flush();
			}			
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} finally
		{
			if(fw != null)
			{
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		
	}
	
	private static String getJMonitoringFindings()
	{
		
		try {
			File file = new File("JMonitoringAppFindings.json");
			FileInputStream fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();
			return new String(data, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	

}
