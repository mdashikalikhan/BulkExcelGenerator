package com.io.jobs;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.db.connection.ConnectionManager;
import com.resorce.properties.ResourceBundle;

public class ExcelGenerator implements Runnable {

	private String DBUSER;
	private String DBPWD;
	private String DBURL;
	private String FILE_PATH = "d:/reports/";
	private String FILE_NAME = "CLIENTS";
	private int NO_OF_RECORDS = 100000;
	private int NO_OF_WORDS = 1100;
	public ExcelGenerator() {
		ResourceBundle bundle = new ResourceBundle();
		DBUSER = bundle.getValue("DBUSER");
		DBPWD = bundle.getValue("DBPWD");
		DBURL = bundle.getValue("DBURL");
	}
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		try{
			doProcess();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		
		
		long end = System.currentTimeMillis();
		
		System.out.println("Elapsed Time: " + (end-start)/1000);
	}
	
	public void doProcess() {
		String sql ="SELECT CLIENTS_CODE,CLIENTS_NAME, CLIENTS_ADDR1, CLIENTS_ADDR_INV_NUM FROM CLIENTS"
				+ " ";
		Connection conn = null;
		Statement stmt=null;
		ResultSet rs = null;
		
		
		try {
	        if(conn==null || conn.isClosed()) {
	        	conn = ConnectionManager.getConnection(DBUSER, DBPWD, DBURL);
	        }
	        if(conn==null)return;
			stmt = conn.createStatement();
			
			rs = stmt.executeQuery(sql);
			int colCount = rs.getMetaData().getColumnCount();
			
			int fileCounter = 1;
			
			FileChannel rwChannel = null;
			ByteBuffer wrBuf = null;
			
			int countRecord = 0;
			while(rs.next()) {
				countRecord++;
				
				if(countRecord%100000==1) {
					try {
						if(rwChannel !=null && rwChannel.isOpen()) {
							rwChannel.close();
						}
						
					} catch(IOException e) {
						
					}
					
					String file = FILE_PATH + FILE_NAME + fileCounter++ + "_" + ((int)(Math.random() * 100)) + ".csv";
					rwChannel = new RandomAccessFile(file, "rw").getChannel();
					wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, NO_OF_WORDS * NO_OF_RECORDS);
				}
				
				String line="";
				for(int colIndex=1;  colIndex<= colCount; colIndex++) {
					
					line+= "\"" + String.valueOf(rs.getObject(colIndex)) + "\",";
				}
				line+="\n";
				byte[] buffer = line.getBytes();
				wrBuf.put(buffer);
			}
			rwChannel.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			try {
				stmt.close();
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
