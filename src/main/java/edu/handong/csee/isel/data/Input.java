package edu.handong.csee.isel.data;

public class Input {

	public String gitURL = null;

	public String outPath;

	public String gitRemoteURI;

	public String jiraProjectKey;

	public String jiraURL;

	public String projectName;
	
	public int maxSize;
	public int minSize;
	
	public String label;

	public ReferenceType referecneType;

	public Mode mode;
	
	public String BICpath;
	
	public String gitDirectory;

	public String start_date;

	public String end_date;

	public String data_mode;

	public String training_data_path;


	public static enum ReferenceType {
		JIRA, GITHUB, KEYWORD, BICCSV
	}

	public static enum Mode {
		PATCH, BIC, METRIC
	}

}
