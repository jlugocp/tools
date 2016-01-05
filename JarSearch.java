import java.util.jar.*;
import java.util.*;
import java.io.*;


public class JarSearch {

	public static final String NL = System.getProperty("line.separator");

	public static void main(String args[]) {
		if (args.length < 2){
			System.out.println("You must provide 1. search directory and 2. search string and optionally 3. showall");
			System.exit(0);
		}
		JarSearch search = new JarSearch();
		try{
			String dir = args[0];
			String searchStr = args[1];
			boolean showDetails = false;
			if (args.length > 2 && args[2].equals("showall")){
				showDetails = true;
			}
			search.searchDir(dir, searchStr, showDetails);//"C:\\Program Files\\IBM\\Application Developer\\plugins\\com.ibm.etools.ctc.binding.java\\runtime\\ctcjava.jar");
		}catch(IOException ioe){
			System.out.println(ioe.toString());
		}
	}

	public static void searchDir(String dirName, String searchStr, boolean showDetails)throws IOException{
		File dir = new File(dirName);
		if (!dir.isDirectory()){
			throw new IOException(dir+" is not a directory !!!");
		}
		File files[] = dir.listFiles();
		for (int i=0; i<files.length; i++){
			if (files[i].isDirectory()){
				searchDir(files[i].getAbsolutePath(), searchStr, showDetails);
			}else{
				if (files[i].getName().endsWith(".jar")){
					searchFile(files[i].getAbsolutePath(), searchStr, showDetails);
				}
			}
		}
	}

	public static void searchFile(String fileName, String searchStr, boolean showDetails)throws IOException{
		boolean outputMessage = false;
		StringBuffer buf = new StringBuffer();
		int index1 = fileName.lastIndexOf("\\");
		int index2 = fileName.lastIndexOf("/");
		int index = -1;
		if (index1 > index2){
			index = index1;
		}else{
			index = index2;
		}
		buf.append(NL);

		buf.append("DIR: ");
		buf.append(fileName.substring(0, index));
		buf.append(NL);

		buf.append("JAR: ");
		buf.append(fileName.substring(index+1));
		buf.append(NL);

		searchStr = searchStr.toLowerCase();

		JarFile jar = new JarFile(fileName);
		Enumeration e = jar.entries();
		while (e.hasMoreElements()){
			JarEntry entry = (JarEntry)e.nextElement();
			String strEntry = entry.getName().toLowerCase();
			if (strEntry.indexOf(searchStr) > -1){
				outputMessage = true;
				if (showDetails){
					buf.append(entry.getName());
					buf.append(NL);
				}
			}
		}
		if (outputMessage){
			System.out.println(buf);
		}
	}
}
