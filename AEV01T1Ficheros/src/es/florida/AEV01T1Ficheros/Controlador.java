package es.florida.AEV01T1Ficheros;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.text.Normalizer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class Controlador {
	String pathInput = "";

	/**
	 * Method that returns formatted paths and subpaths
	 * @param path String containing the all the paths
	 * @return subPath, formatted string to show the path and subpaths
	 */
	public static String getDirectoryListing(String path) {
		File myFile = new File(path);
		String subPath="";
		if(!myFile.exists()) {
			return "Couldn't find " + path;
		}

		if(myFile.isDirectory()) {
    		subPath = getSubPaths(myFile);
    		
    		return subPath;
    	}else {
    		subPath = subPath +  myFile.getAbsolutePath();
    	}
        	
	return subPath;
	}
	
	/**
	 * Method that searches in itself to find all the paths inside f
	 * @param f File or files to search it's subpaths
	 * @return String with the list of the subpaths of f
	 */
	public static String getSubPaths(File f) {
		String aux = "";
		File[] subFiles = f.listFiles();
		String fileFormat;
		
		aux = aux + f.getName() ;
		aux = aux + "\n\t";
		for (File file : subFiles) {
			if(file.isDirectory()) {
				aux = aux + getSubPaths(file)+"\n";
			}else {
				Date longDate = new Date(file.lastModified());
				String lastMod = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(longDate);
				
				fileFormat = "("+ file.length()+" KB - "+ lastMod +")";
				
				aux = aux + "\t" + file.getName() + fileFormat +"\n\t" ;
			}
		}
		
		return aux;
	}

	/**
	 * Overload of the method with the same name for returning more complex format for searching matches and replacements
	 * @param f file to be loaded
	 * @param matchesToSearch string that will be searched
	 * @param caseSensitiveCheckbox boolean to check if the case sensitive is enabled
	 * @param replaceButtonAction boolean to check if the wanted action is replace
	 * @param matchesToReplace string that contains the regex to be substituted
	 * @param accentCheckboxSelected boolean to check if the accent sensitive is enabled
	 * @return Complex format of subpaths with its consequent replacements or matches
	 * @throws IOException
	 */
	public static String getSubPaths(File f, String matchesToSearch, boolean caseSensitiveCheckbox, boolean replaceButtonAction, String matchesToReplace, boolean accentCheckboxSelected) throws IOException {
		String aux = "";
		File[] subFiles = f.listFiles();
				
		aux = aux + f.getName() ;
		aux = aux + "\n\t";
		for (File file : subFiles) {
			if(file.isDirectory()) {
				aux = aux + getSubPaths(file,matchesToSearch,caseSensitiveCheckbox, replaceButtonAction, matchesToReplace, accentCheckboxSelected)+"\n";
			}else {

				int numberOfMatches = getMatches(file, matchesToSearch, caseSensitiveCheckbox, replaceButtonAction, matchesToReplace, accentCheckboxSelected);
				if(replaceButtonAction) {
					aux = aux + "\t" + file.getName() + " (" + numberOfMatches +" replacements)\n\t" ;
				}else {
					aux = aux + "\t" + file.getName() + " (" + numberOfMatches +" matches)\n\t" ;
				}
			}
		}
		
		return aux;
	}
	

	/**
	 * Method that return the number of matches for each file 
	 * @param f file to be loaded
	 * @param matchesToSearch string that will be searched
	 * @param caseSensitiveCheckbox boolean to check if the case sensitive is enabled
	 * @param replaceButtonAction boolean to check if the wanted action is replace
	 * @param matchesToReplace string that contains the regex to be substituted
	 * @param accentCheckboxSelected boolean to check if the accent sensitive is enabled
	 * @return int with the number of matches for each file
	 * @throws IOException
	 */
	public static int getMatches(File f, String matchesToSearch, boolean caseSensitiveCheckbox, 
			boolean replaceButtonAction, String matchesToReplace, boolean accentCheckboxSelected) throws IOException {
	      int numberOfMatches = 0;
	      List<String> fileLines = null;
			if(f.getName().endsWith(".pdf")) {
				numberOfMatches = getMatchesInPDF(f, matchesToSearch, caseSensitiveCheckbox, accentCheckboxSelected);
				return numberOfMatches;
			}
			
	        try {
	            fileLines = Files.readAllLines(f.toPath());
	        } catch (IOException e) {
	            e.getMessage();
	        }finally {
	        	if(fileLines == null) {
	        		return 0;
	        	}
	        }

	        if (!accentCheckboxSelected) {
	            matchesToSearch = normalizeText(matchesToSearch);
	        }

	        String regex = Pattern.quote(matchesToSearch);
	        if (!caseSensitiveCheckbox) {
	            regex = "(?i)" + regex;
	        }

	        Pattern pattern = Pattern.compile(regex);
	        for (String line : fileLines) {
	            if (!accentCheckboxSelected) {
	                line = normalizeText(line);
	            }
	            Matcher matcher = pattern.matcher(line);
	            while (matcher.find()) {
	                numberOfMatches++;
	            }
	        }
	        boolean isJavaFile = f.getName().endsWith(".java");
	        if (numberOfMatches >= 1 && replaceButtonAction && f.canWrite() && !isJavaFile) {
	            replaceMatches(f, matchesToReplace, matchesToSearch, caseSensitiveCheckbox, accentCheckboxSelected);
	        }

	        return numberOfMatches;
	    
	}
	
	
	/**
	 * Method that replaces matchesToSearch for matchesToReplace
	 * @param f file to be loaded
	 * @param matchesToReplace string that contains the regex to be substituted
	 * @param matchesToSearch string that will be searched
	 * @param caseSensitiveCheckbox boolean to check if the case sensitive is enabled
	 * @param accentCheckboxSelected boolean to check if the accent sensitive is enabled
	 */
	public static void replaceMatches(File f, String matchesToReplace, String matchesToSearch,
            boolean caseSensitiveCheckbox, boolean accentCheckboxSelected) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(f.getParent() + "/" + f.getName().replace(".txt", "_copia.txt")));
			
			if (!accentCheckboxSelected) {
				matchesToSearch = normalizeText(matchesToSearch);
			}

			String regex = caseSensitiveCheckbox ? Pattern.quote(matchesToSearch) : "(?i)" + Pattern.quote(matchesToSearch);
			
			String line = br.readLine();
			
			while (line != null) {
				if (!accentCheckboxSelected) {
					line = normalizeText(line);
				}
				
				String newLine = line.replaceAll(regex, matchesToReplace);
				bw.write(newLine + "\n");
				line = br.readLine();
			}

			br.close();
			bw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
}
 

	
	/**
	 * @param f file to be loaded
	 * @param matchesToSearch string that will be searched
	 * @param caseSensitiveCheckbox boolean to check if the case sensitive is enabled
	 * @param accentCheckboxSelected boolean to check if the accent sensitive is enabled
	 * @return int with the number of matches for each PDF file
	 * @throws IOException
	 */
	public static int getMatchesInPDF(File f, String matchesToSearch, boolean caseSensitiveCheckbox, 
			boolean accentCheckboxSelected) throws IOException {
		int numberOfMatches = 0;

		PDDocument document = PDDocument.load(f);
		String pdfText = new PDFTextStripper().getText(document);
		document.close();

		if (accentCheckboxSelected) {
		    matchesToSearch = normalizeText(matchesToSearch);
		    pdfText = normalizeText(pdfText);
		}

		String regex = Pattern.quote(matchesToSearch);
		if (!caseSensitiveCheckbox) {
		    regex = "(?i)" + regex;
		}

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(pdfText);

		while (matcher.find()) {
		    numberOfMatches++;
		}

		return numberOfMatches;

		
	}
	
	
	public static String normalizeText(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}













