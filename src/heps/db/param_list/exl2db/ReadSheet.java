/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heps.db.param_list.exl2db;

import heps.db.param_list.api.ParameterAPI;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author lv
 * @author chu
 */
public class ReadSheet {

    ArrayList<String> rowLabels = new ArrayList<>();
    ArrayList rowLabels1 = new ArrayList();

    SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yy", Locale.CHINESE);
    int x = 0;

    public ArrayList getParamList(Workbook wb, String sheetName) throws ParseException {

        if (wb == null) {
            System.out.println("Error!");
        } else {
            Sheet sheet = wb.getSheet(sheetName);
            ArrayList<ParameterAPI> dataList = new ArrayList<>();
            boolean labelRow1;
            boolean labelRow2;

            for (Iterator<Row> rit = sheet.rowIterator(); rit.hasNext();) {
                Row row = (Row) rit.next();
                ParameterAPI pam = new ParameterAPI();
                labelRow1 = false;
                labelRow2 = false;

                if (getValue(row.getCell(0)).equals("系统")) {
                    labelRow1 = true;
                    labelRow2 = false;

                }

                if (getValue(row.getCell(0)).equals("System")) {
                    labelRow1 = false;
                    labelRow2 = true;
                    System.out.println("System get");

                }
                if (!labelRow1 && !labelRow2) {
                    Cell sys = row.getCell(0);
                    pam.setSys(getValue(sys));

                    Cell team = row.getCell(1);
                    pam.setTeam(getValue(team));

                    Cell team_manager = row.getCell(2);
                    pam.setTeam_manager(getValue(team_manager));

                    Cell parent_team = row.getCell(3);
                    pam.setParent_team(getValue(parent_team));

                    Cell parent_sys = row.getCell(4);
                    pam.setParent_sys(getValue(parent_sys));

                    Cell param_name = row.getCell(5);
                    pam.setParam_name(getValue(param_name).replaceAll(" +", ""));

                    Cell att = row.getCell(6);
                    pam.setAtt(getValue(att));

                    Cell unit = row.getCell(7);
                    pam.setUnit(getValue(unit));

                    Cell data = row.getCell(8);
                    pam.setData(getValue(data));

                    Cell date = row.getCell(9);
                    pam.setDate(sdf.parse(getValue(date)));
                    System.out.println("读出时间：" + getValue(date) + "x:" + (x++) + "参数：" + getValue(param_name)+"组长："+getValue(team_manager)+"所属组："+getValue(parent_team));

                    Cell def = row.getCell(10);
                    pam.setDef(getValue(def));

                    Cell ref_title = row.getCell(11);
                    pam.setRef_title(getValue(ref_title));

                    Cell ref_author = row.getCell(12);
                    pam.setRef_author(getValue(ref_author));

                    Cell ref_publication = row.getCell(13);
                    pam.setRef_publication(getValue(ref_publication));

                    Cell ref_url = row.getCell(14);
                    pam.setRef_url(getValue(ref_url));

                    Cell keywords = row.getCell(15);
                    pam.setKeyword(getValue(keywords));

                    dataList.add(pam);
                }
            }
            return dataList;
        }
        return null;
    }

    public ArrayList getRowLabels() {
        return rowLabels;
    }

    public static String formatNumericCell(Double value, Cell cell) {

        //isCellDateFormatted判断该单元格是"时间格式"或者该"单元格的公式算出来的是时间格式"
        if (DateUtil.isCellDateFormatted(cell)) {
            //cell.getDateCellValue()碰到单元格是公式,会自动计算出Date结果
            Date date = cell.getDateCellValue();
            DataFormatter dataFormatter = new DataFormatter();
            Format format = dataFormatter.createFormat(cell);
            return format.format(date);
        } else {
            DataFormatter dataFormatter = new DataFormatter();
            Format format = dataFormatter.createFormat(cell);
            return format.format(cell.getNumericCellValue());
        }
    }

    private String getValue(Cell cell) {
        if (cell != null) {
            switch (cell.getCellTypeEnum()) {
                case NUMERIC:
                    return formatNumericCell(cell.getNumericCellValue(), cell);

                case BLANK:
                    cell.setCellType(STRING);
                    cell.setCellValue("");//System.out.println("空格");
                    return cell.getStringCellValue();

                case STRING:
                    return String.valueOf(cell.getStringCellValue());

                case FORMULA:
                    return formatNumericCell(cell.getNumericCellValue(), cell);

                default:
                    return "aaa";
            }
        } else {
            return "";
        }
    }

}
