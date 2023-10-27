# PortfolioNumberGrabber
Note: using this requires SonarQube 9.1 or higher, as one of the APIs used was introduced in that release.

Helper to call the SonarQube API and generate a count of issues for all of the Projects within a specified Portfolio

Precompiled executable PortfolioNumberGenerator.jar is provided.  To run this:

java -jar PortfolioNumberGenerator arg1 arg2 arg3 arg4 arg5

where:
  - arg1 is your Sonar Token to access the API
  - arg2 is your base URL to your SonarQube instance
  - arg3 is the name of the Portfolio you want to get counts for
  - arg4 is the branch name for each Project
  - arg5 is the name of the file you want to write the results into
