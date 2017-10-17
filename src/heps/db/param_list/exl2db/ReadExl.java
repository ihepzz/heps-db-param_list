/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heps.db.param_list.exl2db;

import heps.db.param_list.exl2db.tools.FileTools;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hpsf.MarkUnsupportedException;
import org.apache.poi.hpsf.NoPropertySetStreamException;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author lv
 * @author chu
 */
public class ReadExl {

    private SummaryInformation si;

    public static Workbook getWorkbook(String filePath) {
        if (filePath == null || "".equals(filePath)) {
            System.out.println("Warning: Please assign the specific path of the spreadsheet!");
            return null;
        } else {
            FileInputStream inp = FileTools.getFileInputStream(filePath);
            Workbook wb = FileTools.getWorkbook(inp);
            return wb;
        }
    }

    public void checkData(Workbook wb, String sheetName) {
        Sheet sheet = wb.getSheet(sheetName);
        for (int i = 3; i <= sheet.getLastRowNum(); i++) {
            if (sheet.getRow(i) != null) {
                System.out.print("第" + (i + 1) + "行数据\t");
                for (int j = 0; j < 16; j++) {
                    Cell cell = sheet.getRow(i).getCell(j);
                    if (sheet.getRow(i).getCell(j) != null) {

                        switch (cell.getCellTypeEnum()) {
                            case NUMERIC:
                                System.out.print("NUMERIC类型数据" + j + "\t");
                                break;
                            case BLANK:
                                System.out.print("空类型" + j + cell.getStringCellValue() + "\t");
                                break;
                            case STRING:
                                System.out.print("字符串" + j + "\t");
                                break;
                            case FORMULA:
                                System.out.print("公式" + j + "\t");
                                break;
                            case BOOLEAN:
                                System.out.print("布尔型" + j + "\t");
                                break;
                            case ERROR:
                                System.out.print("错误" + j + "\t");
                                break;
                            case _NONE:
                                System.out.print("没有" + j + "\t");
                                break;
                            default:
                                System.out.print("其他类型的数据" + j + "\t");
                                break;
                        }
                    } else {
                        System.out.print("空的" + j + "\t");
                        continue;
                    }
                }
                System.out.println();
            } else {
                System.out.println("第" + (i + 1) + "行是空行");
            }
        }
    }

    public static void checkBlankData(Workbook wb, String sheetName) {
        Sheet sheet = wb.getSheet(sheetName);
        
        for (int i = 3; i <= sheet.getLastRowNum(); i++) {
            if (sheet.getRow(i) != null) {
               int blank =0;
                for (int j = 0; j < 16; j++) {
                    Cell cell = sheet.getRow(i).getCell(j);
                    if (sheet.getRow(i).getCell(j) != null) {

                        switch (cell.getCellTypeEnum()) {
                            case NUMERIC:
                                //System.out.print("NUMERIC类型数据" + j + "\t");
                                break;
                            case BLANK:
                                blank++;
                                //System.out.print("空类型" + j + cell.getStringCellValue() + "\t");
                                break;
                            case STRING:
                               // System.out.print("字符串" + j + "\t");
                                break;
                            case FORMULA:
                                //System.out.print("公式" + j + "\t");
                                break;
                            case BOOLEAN:
                               // System.out.print("布尔型" + j + "\t");
                                break;
                            case ERROR:
                               // System.out.print("错误" + j + "\t");
                                break;
                            case _NONE:
                                blank++;
                                //System.out.print("没有" + j + "\t");
                                break;
                            default:
                                //System.out.print("其他类型的数据" + j + "\t");
                                break;
                        }
                    } else {
                        blank++;
                        //System.out.print("空的" + j + "\t");
                        continue;
                    }
                }
                //System.out.println();
                if(blank == 16){
               
                System.out.println("第" + (i + 1) + "行的16个单元格都是BLANK or NULL or _NONE");
                }
            } else {
                System.out.println("第" + (i + 1) + "行是空行");
            }
        }
    }

    public String getFileName(String filePath) {
        int startIndex = filePath.lastIndexOf("\\");
        int lastIndex = filePath.indexOf(".");
        String fileName = filePath.substring(startIndex + 1, lastIndex);
        return fileName;
    }

    public void getPropertys(String filePath) {
        try {
            final String filename = filePath;
            POIFSReader r = new POIFSReader();
            r.registerListener(new MyPOIFSReaderListener(),
                    "\005SummaryInformation");
            r.read(new FileInputStream(filename));

        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReadExl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ReadExl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    class MyPOIFSReaderListener implements POIFSReaderListener {

        @Override
        public void processPOIFSReaderEvent(POIFSReaderEvent event) {
            //SummaryInformation si = null;
            try {
                si = (SummaryInformation) PropertySetFactory.create(event.getStream());
            } catch (NoPropertySetStreamException | MarkUnsupportedException | IOException ex) {
                throw new RuntimeException("Property set stream \""
                        + event.getPath() + event.getName() + "\": " + ex);
            }
            /*final String title = si.getTitle();
            if (title != null) {
                System.out.println("Title: \"" + title + "\"");
            } else {
                System.out.println("Document has no title.");
            }
            System.out.println(si.getAuthor());*/
        }
    }

    public SummaryInformation getSummaryInformation(String filePath) {
        if (si == null) {
            this.getPropertys(filePath);
        }
        return si;
    }

    public void setSummaryInformation(String filePath) {
        this.getPropertys(filePath);
    }
}
