/*
 * iBIOMES - Integrated Biomolecular Simulations
 * Copyright (C) 2014  Julien Thibault, University of Utah
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package edu.utah.bmi.biosio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Local CSV file.
 * @author Julien Thibault, University of Utah
 *
 */
public class CSVFile
{	
	private String delimiter = "\\,";
	private String[] headers = null;
	private String absolutePath = null;
	private String name = null;
	private ArrayList<ArrayList<String>> data = null;
	
	/**
	 * Reference new CSV file
	 * @param localPath Local path to the file
	 * @throws IOException
	 */
	public CSVFile(String localPath) throws IOException {
		File file = new File(localPath);
		this.absolutePath = file.getCanonicalPath();
		this.name = file.getName();
		this.parseFile(localPath);
	}

	/**
	 * Get CSV headers
	 * @return CSV header array
	 */
	public String[] getHeaders() {
		return headers;
	}

	/**
	 * Get CSV data as a 2D array of strings
	 * @return 2D array
	 */
	public ArrayList<ArrayList<String>> getData() {
		return data;
	}

	/**
	 * Get CSV file absolute path
	 * @return CSV file absolute path
	 */
	public String getAbsolutePath() {
		return this.absolutePath;
	}

	/**
	 * Get CSV file name
	 * @return CSV file name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Initialize CSV file
	 * @param localPath Path to the file
	 */
	private void parseFile(String localPath){
		
		try{
			this.readCsvFile(localPath);

		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Try to read the header of the file (if its a CSV file)
	 * @return List of column labels
	 * @throws Exception 
	 * @throws IOException
	 */
	private void readCsvFile(String localPath) throws Exception
	{
		BufferedReader br = null;
	    String line = null;
	    int lineNumber = 1;
		try{
		    data = new ArrayList<ArrayList<String>>();
			br = new BufferedReader(new FileReader(localPath));
		    
		    //read first line (CSV header)
		    String headerLine = br.readLine();
		    headers = headerLine.split(delimiter);
		    for (int h=0;h<headers.length;h++){
		    	headers[h] = headers[h].replaceAll("\"", "");
		    }
		    
		    //read dataset
			while (( line = br.readLine()) != null)
	        {
	        	line = line.trim();
	        	if (line.length()>1)
	        	{
	        		ArrayList<String> values = this.readValuesFromLine(line);
	        		data.add(values);
	        	}
	        	lineNumber++;
	        }
	        br.close();
		}
		catch(Exception e){
			if (br!=null){
				try {
					br.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			throw new Exception("Error occurred while reading CSV file '"+localPath+"' (row "+lineNumber+")", e);
		}
	}

	private ArrayList<String> readValuesFromLine(String line)
	{
		ArrayList<String> values = new ArrayList<String>();
		boolean usesQuotes = false;
		int i = 0;
		int j = 0;
		while (i < line.length()){
			//skip spaces
			while (line.charAt(i) == ' '){
				i++;
			}
			//check if it starts with quotes
			usesQuotes = (line.charAt(i) == '"');
			if (usesQuotes){
				i++;
				j = line.indexOf('"', i+1);
				String value = line.substring(i, j).trim();
				values.add(value);
				i = j+1;
				if (i<line.length()){
					while (line.charAt(i) == ' ')
						i++;
				}
			}
			else{
				j = line.indexOf(',', i);
				if (j==-1)
					j=line.length();
				String value = line.substring(i, j).trim();
				//System.out.println(value);
				values.add(value);
				i = j;
			}
			//skip ending comma
			i++;
		}
		//if the line ends with a comma add null value
		if (line.charAt(line.length()-1)==','){
			values.add(null);
		}
		
		return values;
	}

}
