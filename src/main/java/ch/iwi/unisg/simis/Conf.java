package ch.iwi.unisg.simis;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Conf {
	public static String sep = File.separator;
	
	public static String fileInputPath = "data" + sep + "input" + sep;
	public static String fileOutputPath = "data" + sep + "output" + sep;
	
	public static String orgGraphPath = fileOutputPath + "orgTestGraph.gv";
	public static String nodeListPath = fileOutputPath + "nodeList.txt";
	
	public static Path MB_SOCIAL_NETWORK_FILE = FileSystems.getDefault().getPath(fileInputPath + "network.dat");
	public static Path SS_SOCIAL_NETWORK_FILE = FileSystems.getDefault().getPath(fileInputPath + "SSnetwork.dat");

	//PF data for IT network
	public static String PF_GF_FILE_NAME 	 = fileInputPath + "PF" + sep + "import_gf-2007-12-04.xls";
	public static String PF_METIFY_FILE_NAME = fileInputPath + "PF" + sep + "import_metify-2007-12-05.xls";
	public static String PF_PFAPAM_FILE_NAME = fileInputPath + "PF" + sep + "import_pfapam-2007-12-04.xls";
	public static String PF_SAP_FILE_NAME 	 = fileInputPath + "PF" + sep + "import_sap-2007-12-17.xls";
}
