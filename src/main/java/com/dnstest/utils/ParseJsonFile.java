package com.dnstest.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.dnstest.exceptions.InvalidJsonFilePathException;
import com.dnstest.exceptions.InvalidNodeParsingJsonException;
import com.google.gson.Gson;

/**
 * The Class ReadJsonFile.
 */
public class ParseJsonFile {

	/**
	 * Read json file.
	 *
	 * @param testDataFile the test data file
	 * @param testName     the test name
	 * @return the Map(Key, Value)
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> readJsonFile(String testDataFile, String testName) {

		Map<String, String> map = new HashMap<>();
		try (FileReader reader = new FileReader(testDataFile);) {

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

			JSONObject testJsonObject = (JSONObject) jsonObject.get(testName);

			map = new Gson().fromJson(testJsonObject.toJSONString(), Map.class);
		} catch (FileNotFoundException e) {
			throw new InvalidJsonFilePathException(
					"Json file '" + testDataFile + "' you are trying to read is not found");
		} catch (NullPointerException e) {
			throw new InvalidNodeParsingJsonException("There is no node present in Json file :'" + testDataFile
					+ "' for the test name : '" + testName + "' you provided");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		return map;
	}

	/**
	 * Extract full json content.
	 *
	 * @param testDataFile the test data file
	 * @return the Map<String, Map<String, String>>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map<String, String>> extractFullJsonContent(String testDataFile) {

		Map<String, Map<String, String>> mapFull = new HashMap<>();
		try (FileReader reader = new FileReader(testDataFile);) {

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

			mapFull = new Gson().fromJson(jsonObject.toJSONString(), Map.class);
		} catch (FileNotFoundException e) {
			throw new InvalidJsonFilePathException(
					"Json file '" + testDataFile + "' you are trying to read is not found");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		return mapFull;
	}

	/**
	 * Extract full json content node wise.
	 *
	 * @param dataFile the data file
	 * @param nodeName the node name
	 * @return the Map<String, Map<String, String>>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Map<String, String>> extractFullJsonContentNodeWise(String dataFile, String nodeName) {

		Map<String, Map<String, String>> mapFull = new HashMap<>();
		try (FileReader reader = new FileReader(dataFile);) {

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);

			JSONObject nodeJsonObject = (JSONObject) jsonObject.get(nodeName);

			mapFull = new Gson().fromJson(nodeJsonObject.toJSONString(), Map.class);
		} catch (FileNotFoundException e) {
			throw new InvalidJsonFilePathException("Json file '" + dataFile + "' you are trying to read is not found");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		return mapFull;
	}
}