package org.sonarqube.neil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
 * Class used to make all of the API calls to SonarQube for this project
 * It is expected that you will pass in the Sonar Token and the base URL
 * for your SonarQube instance to this class.  This will obviously need
 * to be able to connect to your SonarQube host from wherever this is running.
 */
public class ApiCaller {
	
	private final String token;
	private final String sonarURL;
	
	public ApiCaller(String token, String url)
	{
		this.token = token;
		// fix if the URL ends with a shash or not.  If it does, take it off
		if(url.endsWith("/"))
			url = url.substring(0, url.length()-1);
		this.sonarURL = url;
	}
	
	public String getPortfolio(String name) 
	{
		return hitApi(sonarURL+"/api/components/tree?component="+name, "Get Portfolio Tree");
	}

	public String getProjectFindings(String key, String branchName) {
		return hitApi(sonarURL+"/api/projects/export_findings?project="+key+"&branch="+branchName, "Export Project Findings");
	}
	
	private String hitApi(String urlToHit, String method)
	{
		try {
			URL url = new URL(urlToHit);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("Authorization", "Bearer "+token);
			conn.setRequestMethod("GET");
			if(conn.getResponseCode() != 200)
			{
				System.out.println("Got a code "+conn.getResponseCode()+" trying to hit "+urlToHit);
				throw new RuntimeException(method+" failed! HTTP error code "+conn.getResponseCode());
			}
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder response = new StringBuilder();
			String line;
			while((line = br.readLine()) != null)
				response.append(line);
			conn.disconnect();
			return response.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;		
	}

}
