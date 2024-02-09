package com.dnstest.utils;

import static com.dnstest.constants.FrameworkConstants.FILE_DOWNLOAD_PATH;
import static com.dnstest.constants.FrameworkConstants.TEST_DATA_PATH;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;

/**
 * The Class DataUtils.
 */
public class DataUtils {

	/**
	 * Instantiates a new data utils.
	 */
	private DataUtils() {
	}

	/**
	 * Rename file.
	 *
	 * @param origin      the origin
	 * @param newFileName the new file name
	 * @return the string[]
	 */
	public static String[] renameFile(String origin, String newFileName) {
		String fileType = "";
		String originf = "";
		File f1 = new File("UploadData/" + origin);
		if (origin.contains(".xlsx")) {
			fileType = ".xlsx";
			originf = origin.split(".x")[0];
		} else if (origin.contains(".csv")) {
			fileType = ".csv";
			originf = origin.split(".c")[0];
		} else if (origin.contains(".txt")) {
			fileType = ".txt";
			originf = origin.split(".t")[0];
		}
		File f2 = new File("UploadData/" + originf + "_" + newFileName + fileType);
		f1.renameTo(f2);
		String[] str = { f2.getAbsolutePath(), originf + "_" + newFileName + fileType };
		return str;
	}

	/**
	 * Upload file path.
	 *
	 * @param filename the filename
	 * @return the string
	 */
	public static String uploadFilePath(String filename) {
		String dataFilePath = "UploadData/" + filename;
		File datafile = new File(dataFilePath);
		return datafile.getAbsolutePath();
	}

	/**
	 * Download file path.
	 *
	 * @param filename the filename
	 * @return the string
	 */
	public static String downloadFilePath(String filename) {
		String dataFilePath = "DownloadData/" + filename;
		File datafile = new File(dataFilePath);
		return datafile.getAbsolutePath();
	}

	/**
	 * File path.
	 *
	 * @param folder   the folder
	 * @param filename the filename
	 * @return the string
	 */
	public static String filePath(String folder, String filename) {
		String dataFilePath = folder + File.separator + filename;
		File datafile = new File(dataFilePath);
		return datafile.getAbsolutePath();
	}

	/**
	 * Find file.
	 *
	 * @param directory the directory
	 * @param fileNm    the file nm
	 * @return the string
	 */
	public static String findFile(String directory, String fileNm) {
		File dir = new File(directory);
		String[] children = dir.list();
		String name = null;

		if (children != null) {
			for (int i = 0; i < children.length; i++) {
				if (children[i].contains(fileNm)) {
					name = children[i];
					break;
				}
			}
		}
		return name;
	}

	/** The excel W sheet. */
	private static XSSFSheet excelWSheet;

	/** The excel W book. */
	private static XSSFWorkbook excelWBook;

	/** The cell. */
	private static XSSFCell cell;

	/** The row. */
	private static XSSFRow row;

	/** The formatter. */
	private static DataFormatter formatter = new DataFormatter();

	/**
	 * Gets the cell data.
	 *
	 * @param rowNum the row num
	 * @param colNum the col num
	 * @return the cell data
	 */
	public static String getCellData(int rowNum, int colNum) {
		try {
			cell = excelWSheet.getRow(rowNum).getCell(colNum);
			DataFormatter formatter = new DataFormatter();
			return formatter.formatCellValue(cell);
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	/**
	 * Gets the data for dataprovider.
	 *
	 * @param filePath  the file path
	 * @param sheetName the sheet name
	 * @param startRow  the start row
	 * @param startCol  the start col
	 * @return the data for dataprovider
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Object[][] getDataForDataprovider(String filePath, String sheetName, int startRow, int startCol)
			throws IOException {
		String[][] tabArray = null;
		try (FileInputStream excelFile = new FileInputStream(filePath)) {
			excelWBook = new XSSFWorkbook(excelFile);
			excelWSheet = excelWBook.getSheet(sheetName);
			int ci;
			int cj;
			int totalRows = excelWSheet.getLastRowNum();
			row = excelWSheet.getRow(2);
			int totalCols = row.getPhysicalNumberOfCells();
			tabArray = new String[totalRows - 1][totalCols];
			ci = 0;
			for (int i = startRow; i <= totalRows; i++, ci++) {
				cj = 0;
				for (int j = startCol; j < totalCols; j++, cj++) {
					tabArray[ci][cj] = getCellData(i, j);
				}
			}
			return (tabArray);
		}
	}

	/**
	 * Write excel.
	 *
	 * @param filename  the filename
	 * @param sheetname the sheetname
	 * @param cell      the cell
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeExcel(String filename, String sheetname, String[] cell) throws IOException {
		File datafile = new File(filename);
		String fullpath = datafile.getAbsolutePath();
		excelWBook = new XSSFWorkbook(fullpath);
		excelWSheet = excelWBook.getSheet(sheetname);
		int totalRows = excelWSheet.getLastRowNum();
		try (FileInputStream inputStream = new FileInputStream(new File(fullpath))) {
			int rowno = totalRows + 1;
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet firstSheet = workbook.getSheetAt(0);
			XSSFRow row = firstSheet.createRow(rowno);
			for (int i = 0; i < cell.length; i++) {
				row.createCell(i).setCellValue(cell[i]);
			}
			FileOutputStream fos = new FileOutputStream(new File(fullpath));
			workbook.write(fos);
			fos.close();
			workbook.close();
		}
	}

	/**
	 * Creates the XLS file.
	 *
	 * @param filepath  the filepath
	 * @param sheetname the sheetname
	 * @param cell      the cell
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void createXLSFile(String filepath, String sheetname, String[] cell) throws IOException {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			File datafile = new File(filepath);
			String fullpath = datafile.getAbsolutePath();
			XSSFSheet sheet = workbook.createSheet(sheetname);
			XSSFRow row = sheet.createRow(0);
			for (int i = 0; i < cell.length; i++) {
				row.createCell(i).setCellValue(cell[i]);
			}
			FileOutputStream fileOut = new FileOutputStream(fullpath);
			workbook.write(fileOut);
			fileOut.close();
		}
	}

	/**
	 * Gets the total row.
	 *
	 * @param dataFile the data file
	 * @param sheet    the sheet
	 * @return the total row
	 */
	public static int getTotalRow(String dataFile, String sheet) {
		int totalRows = 0;
		try {
			File datafile = new File(dataFile);
			String fullpath = datafile.getAbsolutePath();
			excelWBook = new XSSFWorkbook(fullpath);
			excelWSheet = excelWBook.getSheet(sheet);
			totalRows = excelWSheet.getLastRowNum();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return totalRows;
	}

	/**
	 * Verify cell value.
	 *
	 * @param datafile the datafile
	 * @param sheet    the sheet
	 * @param value    the value
	 * @param i        the i
	 * @param j        the j
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean verifyCellValue(String datafile, String sheet, String value, int i, int j)
			throws IOException {
		boolean serviceFlag = false;
		try (FileInputStream excelFile = new FileInputStream(datafile)) {
			excelWBook = new XSSFWorkbook(excelFile);
			excelWSheet = excelWBook.getSheet(sheet);
			String ser = getCellData(i, j);
			if (ser.equalsIgnoreCase(value.trim()) || value.trim().contains(ser) || ser.contains(value.trim())) {
				serviceFlag = true;
			}
			return serviceFlag;
		}
	}

	/**
	 * Gets the cell value.
	 *
	 * @param datafile the datafile
	 * @param sheet    the sheet
	 * @param i        the i
	 * @param j        the j
	 * @return the cell value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static String getCellValue(String datafile, String sheet, int i, int j) throws IOException {
		try (FileInputStream excelFile = new FileInputStream(datafile)) {
			excelWBook = new XSSFWorkbook(excelFile);
			excelWSheet = excelWBook.getSheet(sheet);
			return getCellData(i, j);
		}
	}

	/**
	 * Verify row wise column value.
	 *
	 * @param dataFile     the data file
	 * @param sheet        the sheet
	 * @param value        the value
	 * @param columnNumber the column number
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean verifyRowWiseColumnValue(String dataFile, String sheet, String value, int columnNumber)
			throws IOException {
		boolean serviceFlag = false;
		String[][] tabArray = null;
		try (FileInputStream excelFile = new FileInputStream(dataFile)) {
			excelWBook = new XSSFWorkbook(excelFile);
			excelWSheet = excelWBook.getSheet(sheet);
			int startRow = 1;
			int ci;
			int cj;
			int totalRows = excelWSheet.getLastRowNum();
			int totalCols = 5;
			tabArray = new String[totalRows][totalCols];
			ci = 0;
			for (int i = startRow; i <= totalRows; i++, ci++) {
				cj = columnNumber;
				int j = columnNumber;
				tabArray[ci][cj] = getCellData(i, j);

				if ((getCellData(i, j)).equalsIgnoreCase(value.trim()) || (getCellData(i, j)).contains(value.trim())) {
					serviceFlag = true;
					break;
				}
			}
			return serviceFlag;
		}
	}

	/**
	 * Removes the row from excel.
	 *
	 * @param dataFile  the data file
	 * @param sheetName the sheet name
	 * @param value     the value
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void removeRowFromExcel(String dataFile, String sheetName, String value) throws IOException {
		try (Workbook wb = WorkbookFactory.create(new FileInputStream(dataFile))) {
			Sheet sheet = wb.getSheet(sheetName);
			@SuppressWarnings("resource")
			Workbook wb2 = new HSSFWorkbook();
			wb2 = wb;
			Row row;
			row = sheet.getRow(0);
			if (row == null)
				row = sheet.getRow(1);
			int lastIndex = sheet.getLastRowNum();
			boolean flag = true;
			int rownum = 0;
			for (int n = 1; n <= sheet.getLastRowNum(); n++) {
				// sheet.getRow(0).getCell(0);
				row = sheet.getRow(n);
				for (int cn = 0; cn < row.getLastCellNum(); cn++) {
					Cell c = row.getCell(cn);
					String text = c.getStringCellValue();
					if (value.equals(text)) {
						flag = false;
						break;
					}
				}
				rownum = n;
				if (flag == false) {
					break;
				}
			}
			if (rownum != 0) {
				row = sheet.getRow(rownum);
				row.setZeroHeight(true);
				sheet.removeRow(row);
				if (rownum + 1 <= lastIndex) {
					sheet.shiftRows(rownum + 1, lastIndex, -1);
				}
				FileOutputStream fileOut = new FileOutputStream(dataFile);
				wb2.write(fileOut);
				fileOut.close();
			}
		}
	}

	/**
	 * Removes the row from excel.
	 *
	 * @param dataFile  the data file
	 * @param sheetName the sheet name
	 * @param rowNumber the row number
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void removeRowFromExcel(String dataFile, String sheetName, int rowNumber) throws IOException {
		try (Workbook wb = WorkbookFactory.create(new FileInputStream(dataFile))) {
			Sheet sheet = wb.getSheet(sheetName);
			@SuppressWarnings("resource")
			Workbook wb2 = new HSSFWorkbook();
			wb2 = wb;
			int lastIndex = sheet.getLastRowNum();
			Row row;
			row = sheet.getRow(rowNumber);
			// row.setZeroHeight(true);
			sheet.removeRow(row);
			if (rowNumber + 1 <= lastIndex) {
				sheet.shiftRows(rowNumber + 1, lastIndex, -1);
			}
			FileOutputStream fileOut = new FileOutputStream(dataFile);
			wb2.write(fileOut);
			fileOut.close();
		}
	}

	/**
	 * Sets the cell data.
	 *
	 * @param dataFile the data file
	 * @param sheet    the sheet
	 * @param result   the result
	 * @param rowNum   the row num
	 * @param colNum   the col num
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void setCellData(String dataFile, String sheet, String result, int rowNum, int colNum)
			throws IOException {
		try (FileInputStream excelFile = new FileInputStream(TEST_DATA_PATH + File.separator + dataFile)) {
			excelWBook = new XSSFWorkbook(excelFile);
			excelWSheet = excelWBook.getSheet(sheet);
			row = excelWSheet.getRow(rowNum);
			if (row == null) {
				row = excelWSheet.createRow(rowNum);
			}
			cell = row.getCell(colNum);
			if (cell == null) {
				cell = row.createCell(colNum);
				cell.setCellValue(result);
			} else {
				cell.setCellValue(result);
			}
			/* Constant variables Test Data path and Test Data file name */
			FileOutputStream fileOut = new FileOutputStream(TEST_DATA_PATH + File.separator + dataFile);
			excelWBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		}
	}

	/**
	 * Creates the zipfile for out put.
	 *
	 * @param folderName the folder name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void createZipfileForOutPut(String folderName) throws IOException {
		String home = System.getProperty("user.home");
		File directory = new File(home + "/Documents/" + "AutomationExecutionReports");
		if (!directory.exists()) {
			directory.mkdir();
		}
		try (FileOutputStream fos = new FileOutputStream(
				home + "/Documents/AutomationExecutionReports/" + folderName + ".zip")) {
			try (ZipOutputStream zos = new ZipOutputStream(fos)) {
				File folder = new File("test-output/automation.html");
				ArrayList<File> filelist = new ArrayList<>();
				listf(folder.getPath(), filelist);
				for (int i = 0; i < filelist.size(); i++) {
					if (filelist.get(i).isFile()) {
						addCopyFile(filelist.get(i).getPath(), zos);
					} else if (filelist.get(i).isDirectory()) {
					}
				}
			}
		}
	}

	/**
	 * Listf.
	 *
	 * @param directoryName the directory name
	 * @param files         the files
	 */
	public static void listf(String directoryName, List<File> files) {
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				files.add(file);
			} else if (file.isDirectory()) {
				listf(file.getPath(), files);
			}
		}
	}

	/**
	 * Adds the copy file.
	 *
	 * @param fileName the file name
	 * @param zos      the zos
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void addCopyFile(String fileName, ZipOutputStream zos) throws IOException {
		try (FileInputStream fis = new FileInputStream(new File(fileName))) {
			ZipEntry zipEntry = new ZipEntry(fileName);
			zos.putNextEntry(zipEntry);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zos.write(bytes, 0, length);
			}
			zos.closeEntry();
		}
	}

	/**
	 * Delete directory.
	 *
	 * @param directoryToBeDeleted the directory to be deleted
	 */
	public static void deleteDirectory(File directoryToBeDeleted) {
		FileUtils.deleteQuietly(directoryToBeDeleted);
	}

	/**
	 * Updated build analysis.
	 *
	 * @param dataFile the data file
	 * @param sheet    the sheet
	 * @param testName the test name
	 * @param result   the result
	 * @param col      the col
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void updatedBuildAnalysis(String dataFile, String sheet, String testName, String result, int col)
			throws IOException {
		String[][] tabArray = null;
		try (FileInputStream excelFile = new FileInputStream(dataFile)) {
			excelWBook = new XSSFWorkbook(excelFile);
			excelWSheet = excelWBook.getSheet(sheet);
			int columnNumber = 1;
			int startRow = 1;
			int ci;
			int cj;
			int totalRows = excelWSheet.getLastRowNum();
			int totalCols = 2;
			tabArray = new String[totalRows][totalCols];
			ci = 0;
			for (int i = startRow; i <= totalRows; i++, ci++) {
				cj = columnNumber;
				int j = columnNumber;
				tabArray[ci][cj] = getCellData(i, j);

				if ((getCellData(i, j)).equalsIgnoreCase(testName.trim())) {
					break;
				}
			}
			row = excelWSheet.getRow(cell.getRowIndex());
			cell = row.createCell(col);
			cell.setCellValue(result);

			FileOutputStream fileOut = new FileOutputStream(dataFile);
			excelWBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		}
	}

	/**
	 * Sets the header date time.
	 *
	 * @param dataFile the data file
	 * @param sheet    the sheet
	 * @param header   the header
	 * @return the int
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static int setHeaderDateTime(String dataFile, String sheet, String header) throws IOException {
		try (FileInputStream excelFile = new FileInputStream(dataFile)) {
			int col = 0;
			excelWBook = new XSSFWorkbook(excelFile);
			excelWSheet = excelWBook.getSheet(sheet);
			row = excelWSheet.getRow(0);
			col = row.getLastCellNum();
			cell = row.getCell(col);
			if (cell == null) {
				cell = row.createCell(col);
				cell.setCellValue(header);
			} else {
				cell.setCellValue(header);
			}
			FileOutputStream fileOut = new FileOutputStream(dataFile);
			excelWBook.write(fileOut);
			fileOut.flush();
			fileOut.close();
			return col;
		}
	}

	/** The Constant BUFFER_SIZE. */
	private static final int BUFFER_SIZE = 4096;

	/**
	 * Unzip.
	 *
	 * @param zipFilePath   the zip file path
	 * @param destDirectory the dest directory
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void unzip(String zipFilePath, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		try (ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath))) {
			ZipEntry entry = zipIn.getNextEntry();
			// iterates over entries in the zip file
			while (entry != null) {
				String filePath = destDirectory + File.separator + entry.getName();
				if (!entry.isDirectory()) {
					// if the entry is a file, extracts it
					extractFile(zipIn, filePath);
				} else {
					// if the entry is a directory, make the directory
					File dir = new File(filePath);
					dir.mkdir();
				}
				zipIn.closeEntry();
				entry = zipIn.getNextEntry();
			}
		}
	}

	/**
	 * Extracts a zip entry (file entry).
	 *
	 * @param zipIn    the zip in
	 * @param filePath the file path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath))) {
			byte[] bytesIn = new byte[BUFFER_SIZE];
			int read = 0;
			while ((read = zipIn.read(bytesIn)) != -1) {
				bos.write(bytesIn, 0, read);
			}
		}
	}

	/**
	 * Gets the column data.
	 *
	 * @param filePath  the file path
	 * @param sheetname the sheetname
	 * @param header    the header
	 * @return the column data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<String> getColumnData(String filePath, String sheetname, String header) throws IOException {
		int rownum = 0;
		ArrayList<String> code = new ArrayList<>();
		formatter = new DataFormatter();
		try (FileInputStream fin = new FileInputStream(filePath)) {
			excelWBook = new XSSFWorkbook(fin);
			excelWSheet = excelWBook.getSheet(sheetname);
			try {
				int rowCount = excelWSheet.getLastRowNum();
				for (int i = 0; i < rowCount; i++) {
					rownum = i + 1;
					code.add(getCellData(rownum, header));
				}
				excelWBook.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return code;
		}
	}

	/**
	 * Gets the column data other file.
	 *
	 * @param filename  the filename
	 * @param splitsign the splitsign
	 * @param header    the header
	 * @return the column data other file
	 */
	public static List<ArrayList<String>> getColumnDataOtherFile(String filename, String splitsign, String[] header) {
		String line = "";
		int loop = 0;
		ArrayList<ArrayList<String>> code = new ArrayList<>(header.length);
		ArrayList<String> c = null;
		ArrayList<Integer> index = new ArrayList<>();
		String[] data = null;

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			while ((line = br.readLine()) != null) {
				if (line.contains(splitsign)) {
					data = line.split(Pattern.quote(splitsign));
					if (loop == 0) {
						System.out.println("-=====-i" + loop);
						for (int i = 0; i < header.length; i++) {
							System.out.println(i + "-===data[i]=-i" + data[i]);
							for (int j = 0; j < data.length; j++) {
								if (data[j].contains(header[i])) {
									index.add(i);
									break;
								}
							}
						}
						loop++;
					} else {
						c = new ArrayList<>();
						for (int i = 0; i < index.size(); i++) {
							c.add(data[i]);
						}
						code.add(c);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * Gets the column data.
	 *
	 * @param filePath  the file path
	 * @param sheetname the sheetname
	 * @param header    the header
	 * @return the column data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<ArrayList<String>> getColumnData(String filePath, String sheetname, String[] header)
			throws IOException {
		int rownum = 0;
		ArrayList<ArrayList<String>> code = new ArrayList<>(header.length);
		ArrayList<String> c = null;
		formatter = new DataFormatter();
		try (FileInputStream fin = new FileInputStream(filePath)) {
			excelWBook = new XSSFWorkbook(fin);
			excelWSheet = excelWBook.getSheet(sheetname);

			int rowCount = excelWSheet.getLastRowNum();
			for (int i = 0; i < rowCount; i++) {
				c = new ArrayList<>();
				rownum = i + 1;
				for (int j = 0; j < header.length; j++) {
					c.add(getCellData(rownum, header[j]));
				}
				code.add(c);
			}
			excelWBook.close();
		}
		return code;
	}

	/**
	 * Gets the column data by column number.
	 *
	 * @param filePath  the file path
	 * @param sheetname the sheetname
	 * @param header    the header
	 * @return the column data by column number
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<String> getColumnDataByColumnNumber(String filePath, String sheetname, int header)
			throws IOException {
		ArrayList<String> code = new ArrayList<>();
		formatter = new DataFormatter();
		try (FileInputStream fin = new FileInputStream(filePath)) {
			excelWBook = new XSSFWorkbook(fin);
			excelWSheet = excelWBook.getSheet(sheetname);
			try {
				for (int i = 0; i < 1; i++) {
					code.add(getCellDataInteger(i, header));
				}
				excelWBook.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return code;
		}
	}

	/**
	 * Gets the cell data.
	 *
	 * @param rowNum the row num
	 * @param header the header
	 * @return the cell data
	 */
	public static String getCellData(int rowNum, String header) {
		try {
			return formatter.formatCellValue(excelWSheet.getRow(rowNum).getCell(readHeader(header)));
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Gets the cell data integer.
	 *
	 * @param rowNum the row num
	 * @param n      the n
	 * @return the cell data integer
	 */
	public static String getCellDataInteger(int rowNum, int n) {
		try {
			return formatter.formatCellValue(excelWSheet.getRow(rowNum).getCell(n));
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Read header.
	 *
	 * @param header the header
	 * @return the int
	 */
	public static int readHeader(String header) {
		try {
			int colNum = excelWSheet.getRow(0).getLastCellNum();
			row = excelWSheet.getRow(0);
			for (int j = 0; j < colNum; j++) {
				cell = row.getCell(j);
				String cellValue = formatter.formatCellValue(cell);
				if (cellValue.equalsIgnoreCase(header)) {
					return j;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * Gets the total header.
	 *
	 * @param dataFile the data file
	 * @param sheet    the sheet
	 * @return the total header
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static int getTotalHeader(String dataFile, String sheet) throws IOException {
		try (FileInputStream excelFile = new FileInputStream(dataFile)) {
			excelWBook = new XSSFWorkbook(excelFile);
			excelWSheet = excelWBook.getSheet(sheet);
			return excelWSheet.getRow(0).getLastCellNum();
		}
	}

	/**
	 * Gets the file count.
	 *
	 * @param directory the directory
	 * @param type      the type
	 * @return the file count
	 */
	public static int getFileCount(String directory, String type) {
		int n = 0;
		File dir = new File(directory);
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith((type))) {
				n++;
			}
		}
		return n;
	}

	/**
	 * Gets the all file data.
	 *
	 * @param filename  the filename
	 * @param splitsign the splitsign
	 * @param field     the field
	 * @return the all file data
	 */
	public static List<String> getAllFileData(String filename, String splitsign, String field) {
		String dataFile = filename;
		String line = "";
		int loop = 0;
		int filedIndex = 0;
		ArrayList<String> code = new ArrayList<>();
		String[] data = null;
		try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
			while ((line = br.readLine()) != null) {
				if (line.contains(splitsign)) {
					data = line.split(Pattern.quote(splitsign));
					if (loop == 0) {
						for (int i = 0; i < data.length; i++) {
							if (data[i].contains(field)) {
								filedIndex = i;
								break;
							}
						}
						loop++;
					} else {
						code.add(data[filedIndex]);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * Gets the total column.
	 *
	 * @param filename  the filename
	 * @param splitsign the splitsign
	 * @return the total column
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static int getTotalColumn(String filename, String splitsign) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line = "";
			String[] data = null;
			while ((line = br.readLine()) != null) {
				if (line.contains(splitsign)) {
					data = line.split(Pattern.quote(splitsign));
				}
				break;
			}
			return data.length;
		}
	}

	/**
	 * Gets the total row other files.
	 *
	 * @param filename the filename
	 * @return the total row other files
	 */
	public static int getTotalRowOtherFiles(String filename) {
		int lineNo = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			while ((br.readLine()) != null) {
				lineNo++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lineNo;
	}

	/**
	 * Gets the no match.
	 *
	 * @param minimum the minimum
	 * @return the no match
	 */
	public static String getNoMatch(String minimum) {
		String code = "";
		for (int i = 1; i < Integer.parseInt(minimum); i++) {
			if (i == 1)
				code = String.valueOf(i);
			else
				code = code + ";" + i;
		}
		return code;
	}

	/**
	 * Gets the low conf.
	 *
	 * @param minimum  the minimum
	 * @param confCode the conf code
	 * @return the low conf
	 */
	public static String getLowConf(String minimum, String confCode) {
		String code = "";
		for (int i = Integer.parseInt(minimum); i <= 10; i++) {
			if (!confCode.equalsIgnoreCase("")) {
				if (!confCode.contains(String.valueOf(i))) {
					if (i == Integer.parseInt(minimum)) {
						code = String.valueOf(i);
					} else {
						if (code.equalsIgnoreCase(""))
							code = String.valueOf(i);
						else
							code = code + ";" + i;
					}
				}
			} else {
				if (i == Integer.parseInt(minimum)) {
					code = String.valueOf(i);
				} else
					code = code + ";" + i;
			}
		}
		return code;
	}

	/**
	 * Gets the column datafromeachrow.
	 *
	 * @param filePath  the file path
	 * @param sheetname the sheetname
	 * @param header    the header
	 * @return the column datafromeachrow
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<String> getColumnDatafromeachrow(String filePath, String sheetname, String header)
			throws IOException {
		int rownum = 0;
		ArrayList<String> code = new ArrayList<>();
		formatter = new DataFormatter();
		try (FileInputStream fin = new FileInputStream(filePath)) {
			excelWBook = new XSSFWorkbook(fin);
			excelWSheet = excelWBook.getSheet(sheetname);

			int rowCount = excelWSheet.getLastRowNum();
			for (int i = 0; i < rowCount; i++) {
				rownum = i + 1;
				code.add(getCellData(rownum, header));
			}
			excelWBook.close();
		}

		return code;
	}

	/**
	 * Gets the last row.
	 *
	 * @param dataFile the data file
	 * @param sheet    the sheet
	 * @return the last row
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static int getLastRow(String dataFile, String sheet) throws IOException {
		try (FileInputStream excelFile = new FileInputStream(dataFile)) {
			excelWBook = new XSSFWorkbook(excelFile);
			excelWSheet = excelWBook.getSheet(sheet);
			return excelWSheet.getLastRowNum();
		}
	}

	/**
	 * Read sheet data.
	 *
	 * @param fileName  the file name
	 * @param sheetName the sheet name
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static List<Map<String, String>> readSheetData(String fileName, String sheetName) throws IOException {
		try (FileInputStream fin = new FileInputStream(TEST_DATA_PATH + File.separator + fileName)) {
			excelWBook = new XSSFWorkbook(fin);
			excelWSheet = excelWBook.getSheet(sheetName);

			List<Map<String, String>> mapLists = new ArrayList<>();
			int lastRowNum = excelWSheet.getLastRowNum();
			row = excelWSheet.getRow(0);

			for (int j = 1; j < row.getLastCellNum(); j++) {
				Map<String, String> myMap = new HashMap<>();

				for (int i = 0; i < lastRowNum + 1; i++) {
					Row r = CellUtil.getRow(i, excelWSheet);
					String key = CellUtil.getCell(r, 0).getStringCellValue();
					String value = CellUtil.getCell(r, j).getStringCellValue();

					myMap.put(key, value);
				}
				excelWBook.close();
				mapLists.add(myMap);
			}
			return mapLists;
		}
	}

	/**
	 * Gets the value of.
	 *
	 * @param readAllData  the read all data
	 * @param rowHeader    the row header
	 * @param columnHeader the column header
	 * @return the value of
	 */
	public static String getValueOf(List<Map<String, String>> readAllData, String rowHeader, String columnHeader) {
		ArrayList<String> header = new ArrayList<>();
		for (Map<String, String> map : readAllData) {
			header.add(map.get("TestData"));
		}
		ArrayList<String> valueList = new ArrayList<>();
		for (Map<String, String> map : readAllData) {
			valueList.add(map.get(rowHeader));
		}
		Map<String, String> myMap = new HashMap<>();
		for (int i = 0; i < header.size(); i++) {
			myMap.put(header.get(i), valueList.get(i));
		}
		return myMap.get(columnHeader);
	}

	/**
	 * Gets the excel file column data.
	 *
	 * @param strFileName   the str file name
	 * @param strSheetName  the str sheet name
	 * @param iColumnNumber the i column number
	 * @return the generated excel file column data as a list of Strings
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<String> getExcelFileColumnData(String strFileName, String strSheetName, int iColumnNumber)
			throws IOException {
		List<String> lstString = new ArrayList<>();
		String strExcelFilePath = FILE_DOWNLOAD_PATH + File.separator + strFileName;
		try (FileInputStream inputStream = new FileInputStream(strExcelFilePath)) {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheet(strSheetName);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Cell cell = row.getCell(iColumnNumber, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				lstString.add(cell.getStringCellValue().trim());
			}
			workbook.close();
		}
		return lstString;
	}

	/**
	 * Verify generated word document.
	 *
	 * @param strType                 the str type
	 * @param strFilePath             the str file path
	 * @param strFileToBeCompared     the str file to be compared
	 * @param strFileToBeComparedWith the str file to be compared with
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean verifyGeneratedWordDocument(String strType, String strFilePath, String strFileToBeCompared,
			String strFileToBeComparedWith) throws IOException {
		if (strType.equalsIgnoreCase("TABLE")) {
			return compareTableInDocument(strFilePath, strFileToBeCompared, strFileToBeComparedWith);
		} else {
			return compareTextInDocument(strFilePath, strFileToBeCompared, strFileToBeComparedWith);
		}
	}

	/**
	 * Compare text in document.
	 *
	 * @param strFilePath             the str file path
	 * @param strFileToBeCompared     the str file to be compared
	 * @param strFileToBeComparedWith the str file to be compared with
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean compareTextInDocument(String strFilePath, String strFileToBeCompared, String strFileToBeComparedWith)
			throws IOException {
		List<Boolean> list = new ArrayList<>();
		// open connection to the generated and expected files
		try (FileInputStream fisGenerated = new FileInputStream(
				FILE_DOWNLOAD_PATH + File.separator + strFileToBeCompared);
				FileInputStream fisExpected = new FileInputStream(
						strFilePath + File.separator + strFileToBeComparedWith)) {
			// load .docx files
			try (XWPFDocument xwpfDocumentGenerated = new XWPFDocument(fisGenerated);
					XWPFDocument xwpfDocumentExpected = new XWPFDocument(fisExpected)) {
				List<XWPFParagraph> lstXwpfParaGenerated = xwpfDocumentGenerated.getParagraphs();
				List<XWPFParagraph> lstXwpfParaExpected = xwpfDocumentExpected.getParagraphs();
				if (lstXwpfParaGenerated.size() == lstXwpfParaExpected.size()) {
					for (int i = 0; i < lstXwpfParaGenerated.size(); i++) {
						// check text in both files are same
						list.add(lstXwpfParaGenerated.get(i).getText()
								.contentEquals(lstXwpfParaExpected.get(i).getText()));
					}
					return !list.contains(false);
				} else {
					return false;
				}
			}
		}
	}

	/**
	 * Compare table in document.
	 *
	 * @param strFilePath             the str file path
	 * @param strFileToBeCompared     the str file to be compared
	 * @param strFileToBeComparedWith the str file to be compared with
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean compareTableInDocument(String strFilePath, String strFileToBeCompared,
			String strFileToBeComparedWith) throws IOException {
		List<Boolean> list = new ArrayList<>();
		// open connection to the generated and expected files
		try (FileInputStream fisGenerated = new FileInputStream(
				FILE_DOWNLOAD_PATH + File.separator + strFileToBeCompared);
				FileInputStream fisExpected = new FileInputStream(
						strFilePath + File.separator + strFileToBeComparedWith)) {
			// load .docx files
			try (XWPFDocument xwpfDocumentGenerated = new XWPFDocument(fisGenerated);
					XWPFDocument xwpfDocumentExpected = new XWPFDocument(fisExpected)) {
				// get different element types present in the document
				Iterator<IBodyElement> bodyElementIteratorGenerated = xwpfDocumentGenerated.getBodyElementsIterator();
				Iterator<IBodyElement> bodyElementIteratorExpected = xwpfDocumentExpected.getBodyElementsIterator();
				while (bodyElementIteratorGenerated.hasNext() && bodyElementIteratorExpected.hasNext()) {
					IBodyElement bodyElementGenerated = bodyElementIteratorGenerated.next();
					IBodyElement bodyElementExpected = bodyElementIteratorExpected.next();

					// check if table present in document
					if (bodyElementExpected.getElementType().name().equalsIgnoreCase("TABLE")) {
						// list of tables in generated file
						List<XWPFTable> lstXwpfTableGenerated = bodyElementGenerated.getBody().getTables();
						// list of tables in expected file
						List<XWPFTable> lstXwpfTableExpected = bodyElementExpected.getBody().getTables();
						if (lstXwpfTableGenerated.size() == lstXwpfTableExpected.size()) {
							for (int i = 0; i < lstXwpfTableGenerated.size(); i++) {
								for (int j = 0; j < lstXwpfTableGenerated.get(i).getRows().size(); j++) {
									// check values of cell in both files are same
									for (int k = 0; k < lstXwpfTableGenerated.get(i).getRow(j).getTableCells()
											.size(); k++) {
										list.add(lstXwpfTableGenerated.get(i).getRow(j).getCell(k).getText()
												.contentEquals(
														lstXwpfTableExpected.get(i).getRow(j).getCell(k).getText()));
									}
								}
							}
						} else {
							list.add(false);
						}
					}
				}
			}
		}
		return !list.contains(false);
	}

	/**
	 * Compare rich text document.
	 *
	 * @param strFilePath             the str file path
	 * @param strFileToBeCompared     the str file to be compared
	 * @param strFileToBeComparedWith the str file to be compared with
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean compareRichTextDocument(String strFilePath, String strFileToBeCompared,
			String strFileToBeComparedWith) throws IOException {
		List<Boolean> list = new ArrayList<>();
		// open connection to the generated and expected files
		try (FileInputStream fisGenerated = new FileInputStream(
				FILE_DOWNLOAD_PATH + File.separator + strFileToBeCompared);
				FileInputStream fisExpected = new FileInputStream(
						strFilePath + File.separator + strFileToBeComparedWith)) {
			// load .docx files
			try (XWPFDocument xwpfDocumentGenerated = new XWPFDocument(fisGenerated);
					XWPFDocument xwpfDocumentExpected = new XWPFDocument(fisExpected)) {
				List<XWPFParagraph> lstXwpfParaGenerated = xwpfDocumentGenerated.getParagraphs();
				List<XWPFParagraph> lstXwpfParaExpected = xwpfDocumentExpected.getParagraphs();
				if (lstXwpfParaGenerated.size() == lstXwpfParaExpected.size()) {
					for (int i = 0; i < lstXwpfParaGenerated.size(); i++) {
						for (int j = 0; j < lstXwpfParaGenerated.get(i).getRuns().size(); j++) {
							list.add(lstXwpfParaGenerated.get(i).getRuns().get(j).isBold() == lstXwpfParaExpected.get(i)
									.getRuns().get(j).isBold());
							list.add(lstXwpfParaGenerated.get(i).getRuns().get(j).isItalic() == lstXwpfParaExpected
									.get(i).getRuns().get(j).isItalic());
							list.add(lstXwpfParaGenerated.get(i).getRuns().get(j)
									.isDoubleStrikeThrough() == lstXwpfParaExpected.get(i).getRuns().get(j)
											.isDoubleStrikeThrough());
							list.add(lstXwpfParaGenerated.get(i).getRuns().get(j).isEmbossed() == lstXwpfParaExpected
									.get(i).getRuns().get(j).isEmbossed());
							list.add(lstXwpfParaGenerated.get(i).getRuns().get(j).isHighlighted() == lstXwpfParaExpected
									.get(i).getRuns().get(j).isHighlighted());
							list.add(lstXwpfParaGenerated.get(i).getRuns().get(j).isImprinted() == lstXwpfParaExpected
									.get(i).getRuns().get(j).isImprinted());
							list.add(lstXwpfParaGenerated.get(i).getRuns().get(j).isShadowed() == lstXwpfParaExpected
									.get(i).getRuns().get(j).isShadowed());
							list.add(lstXwpfParaGenerated.get(i).getRuns().get(j)
									.isStrikeThrough() == lstXwpfParaExpected.get(i).getRuns().get(j)
											.isStrikeThrough());
						}
					}
					return !list.contains(false);
				} else {
					return false;
				}
			}
		}
	}
}
