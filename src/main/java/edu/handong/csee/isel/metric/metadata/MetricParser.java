package edu.handong.csee.isel.metric.metadata;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.handong.csee.isel.metric.metadata.CommitUnitInfo;
import edu.handong.csee.isel.metric.metadata.DeveloperExperienceInfo;
import edu.handong.csee.isel.metric.metadata.MetaDataInfo;
import edu.handong.csee.isel.metric.metadata.SourceFileInfo;
import edu.handong.csee.isel.metric.metadata.Utils;

public class MetricParser {
	public void parsePatchContents(final MetaDataInfo metaDataInfo, final String commitHash, final String diffContent,
			final String dataMode, final String trainingDataPath, String removedCommentsFileSource, String key) {

		int numOfDeleteLines = 0; // metricVariable.getNumOfDeleteLines();
		int numOfAddLines = 0; // metricVariable.getNumOfAddLines();
		int distributionOfModifiedLines = 0; // metricVariable.getDistributionOfModifiedLines();

		final List<String> diffLines = Arrays.asList(diffContent.split("\\n"));
		final ArrayList<String> OneCommitAddedLines = new ArrayList<>(); // for making DP training dataset
		final ArrayList<String> RemovedCommentsOneCommitAddedLines = new ArrayList<>(); // for making DP training dataset

		for (int i = 5; i < diffLines.size(); i++) {
			String line = diffLines.get(i);
			if (line.startsWith("-"))
				numOfDeleteLines++;
			else if (line.startsWith("+")) {
				numOfAddLines++;
				String removePlusCharLine = line.replaceAll("\\+", "");
				removePlusCharLine = removePlusCharLine.trim();
				if(!removePlusCharLine.equals("*") && !removePlusCharLine.equals("")) OneCommitAddedLines.add(removePlusCharLine);
			} 
			else if (line.startsWith("@@"))
				distributionOfModifiedLines++;
		}
		// if(OneCommitAddedLines.size() == 0) 
		// Save one commit added lines
		if (dataMode.equals("2")) { // DP mode
			BufferedWriter writer;
			try {
				final String DP_EachCommitTxtFilePath = trainingDataPath + File.separator + key + ".txt";
				
				
				for (final String line : OneCommitAddedLines) {
					if (removedCommentsFileSource.contains(line)) // 이게 제대로 되는건가? 주석이 제거된 커밋에 속한 라인만 저장이 되게 한 건데...
						RemovedCommentsOneCommitAddedLines.add(line); 
				}
				if(RemovedCommentsOneCommitAddedLines.size() != 0){
					writer = new BufferedWriter(new FileWriter(DP_EachCommitTxtFilePath));
					for(String line : RemovedCommentsOneCommitAddedLines){
						writer.write(line + System.lineSeparator());
					}
					writer.close();
				}
				else{
					// System.out.println(key + " This commit has only comments");
				} // This commit has only comments, so we do not consider our dataset
			} catch (final IOException e) {
				e.printStackTrace();
			}

		} 
		else if (dataMode.equals("1")) { // LM mode
			for (final String line : OneCommitAddedLines) {
				if (removedCommentsFileSource.contains(line))
					CommitCollector.AllCommitsAddedLines.add(line);
			}
		}

		metaDataInfo.setNumOfModifyLines(numOfDeleteLines + numOfAddLines);
		metaDataInfo.setNumOfAddLines(numOfAddLines);
		metaDataInfo.setNumOfDeleteLines(numOfDeleteLines);
		metaDataInfo.setDistributionOfModifiedLines(distributionOfModifiedLines);
	}

	public void parseSourceInfo(final MetaDataInfo metaDataInfo, final HashMap<String, SourceFileInfo> sourceFileInfo,
			final String sourceFileName, final String authorId, final boolean isBuggyCommit, final String commitTime,
			final String commitHash, final CommitUnitInfo commitUnitInfo, final String fileSource) throws Exception {
		SourceFileInfo aSourceFileInfo;// 소스파일 정보 인스탄스

		if (sourceFileInfo.containsKey(sourceFileName) == false) {// 처음 만들어진 소스 파일 일 때
			aSourceFileInfo = new SourceFileInfo();
			sourceFileInfo.put(sourceFileName, aSourceFileInfo);
			aSourceFileInfo.setMakeDate(commitTime);
			aSourceFileInfo.setPreviousCommitDate(commitTime);
			aSourceFileInfo.setPreviousCommitHash(commitHash);
		} else { // 이미 존재하는 소스 파일 일 때
			aSourceFileInfo = sourceFileInfo.get(sourceFileName);// 정보 가져옴
		}

		aSourceFileInfo.setNumOfModify();// 소스파일 수정 횟수 +
		aSourceFileInfo.setDeveloper(authorId);// 소스파일 수정한 개발자 이름 저장(tree set)

		if (isBuggyCommit == true) {// 소스파일이 버그를 가진 전적 횟수 +
			aSourceFileInfo.setNumOfBIC();
		}

		metaDataInfo.setFileAge(Utils.calDate(aSourceFileInfo.getMakeDate(), commitTime));// 오늘 날짜와 생성 날짜 비교하여 파일나이 저장
		metaDataInfo.setSumOfSourceRevision(aSourceFileInfo.getNumOfModify());// 소스파일 총 수정 횟수
		metaDataInfo.setSumOfDeveloper(aSourceFileInfo.getDeveloper().size());// 소스파일을 수정한 개발자 수(중복 허용 안함)
		metaDataInfo.setNumOfBIC(aSourceFileInfo.getNumOfBIC());// (버그를 수정한 횟수)
		metaDataInfo
				.setTimeBetweenLastAndCurrentCommitDate(Utils.calDate(aSourceFileInfo.getPreviousCommitDate(), commitTime));
		metaDataInfo.setLinesOfCodeBeforeTheChange(Arrays.asList(fileSource.split("\\n")).size());

		commitUnitInfo.setPreviousCommitHashs(aSourceFileInfo.getPreviousCommitHash());

		aSourceFileInfo.setPreviousCommitDate(commitTime);
		aSourceFileInfo.setPreviousCommitHash(commitHash);
	}

	public void parseDeveloperInfo(final MetaDataInfo metaDataInfo, final HashMap<String, Integer> developerExperience,
			final String authorId) {

		if (developerExperience.containsKey(authorId) == false) {
			developerExperience.put(authorId, 1);
		} else {
			developerExperience.put(authorId, developerExperience.get(authorId) + 1);
		}

		metaDataInfo.setDeveloperExperience(developerExperience.get(authorId));

	}

	public void parseCommitUnitInfo(final CommitUnitInfo commitUnitInfo, final String sourcePath, final String key) {
		final String[] pathToken = sourcePath.split("/");
		final String subsystem = pathToken[0];
		final String file = pathToken[pathToken.length - 1];
		String directorie = null;

		final Pattern pattern = Pattern.compile("(.+)/.+");
		final Matcher matcher = pattern.matcher(sourcePath);
		while (matcher.find()) {
			directorie = matcher.group(1);
		}

		commitUnitInfo.setKey(key);
		commitUnitInfo.setSubsystems(subsystem);
		commitUnitInfo.setDirectories(directorie);
		commitUnitInfo.setFiles(file);
	}

	public void computeDeveloperInfo(final HashMap<String, DeveloperExperienceInfo> developerExperience,
			final String authorId, final String commitTime) {
		final String[] TimeToken = commitTime.split("-");
		final int year = Integer.parseInt(TimeToken[0]) + 1;
		float REXP = 0;

		if (developerExperience.containsKey(authorId) == false)
			return;

		final TreeMap<Integer, Integer> recentExperience = developerExperience.get(authorId).getRecentExperiences();

		final Set<Map.Entry<Integer, Integer>> entries = recentExperience.entrySet();
		for (final Map.Entry<Integer, Integer> entry : entries) {
			final int key = entry.getKey();
			final int denominator = year - key;
			final int numerator = entry.getValue();
			REXP = REXP + (float)numerator/denominator;
		}
		developerExperience.get(authorId).setREXP(REXP);
		
	}
}
