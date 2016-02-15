package ch.iwi.unisg.simis;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import org.apache.poi.hssf.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.*;


public class Util {
	
	public static void storeAsGV(ArrayList<AgentAgentLink> links, String fileName)
	{
		try{
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			writer.println("digraph g{");
			Iterator<AgentAgentLink> it = links.iterator();
			for(AgentAgentLink curLink : links){
				curLink = it.next();
				writer.println("   " + String.valueOf(curLink.sourceID) + " -> " + 
						String.valueOf(curLink.targetID) + " [weight=" + curLink.weight + "]");
				writer.flush();
			}
			writer.println("}");
			writer.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public static void storeNodeList(double[] nodes, String fileName)
	{
		try{
			String out = "";
			for(int i = 0; i < nodes.length; i++){
				out += String.format("%.2f ", nodes[i]);
			}
			out += System.getProperty("line.separator");
			if(!Files.exists(Paths.get(fileName)))
				Files.createFile(Paths.get(fileName));
			Files.write(Paths.get(fileName), out.getBytes(), StandardOpenOption.APPEND);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public static ITNetwork readPFData(
			String gfFileName, 
			String metifyFileName, 
			String pfapamFileName, 
			String sapFileName)
	throws IOException{
		FileInputStream inputStream = new FileInputStream(new File(pfapamFileName));
		Workbook workbook = new HSSFWorkbook(inputStream);
		Sheet applikationSheet = workbook.getSheet("applikation");
		Iterator<Row> itRow = applikationSheet.iterator();
		
		ArrayList<ITNode> nodes = new ArrayList<ITNode>();
		//TODO: Maybe go through pfapam-Quartiere first 
		
		int rowCounter = 0;
		while(itRow.hasNext()){
			Row curRow = itRow.next();
			rowCounter++;
			//Treat header row differently
			if(rowCounter == 1){
				//0 APPL_ID, 1 QUAR_ID, 2 NAME, 3 ABKUERZUNG, 4 SAPNR, 5 BEMERKUNG, 6 TYP, 7 SD_NAME, 8 BETREIBER
			}else{
				int appId = (int)curRow.getCell(0).getNumericCellValue();
				String appName = curRow.getCell(2).getStringCellValue();
				String SAPNr = curRow.getCell(4).getStringCellValue();
				int quartierId = (int)curRow.getCell(1).getNumericCellValue();
				if(SAPNr == "keine nr")
					SAPNr = null;
				ITNode itNode = new ITNode(appId, appName, SAPNr, quartierId);
				nodes.add(itNode);
			}
		}
		
		
		
		workbook.close();
		inputStream.close();
		
		return new ITNetwork(null, null, null, nodes);
		
		
	}
	
}
