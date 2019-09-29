package com.llvision.face.utils;

import com.llvision.face.entity.Person;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExcelUtil {
    // 默认单元格内容为数字时格式
    public static DecimalFormat df = new DecimalFormat("0");
    // 默认单元格格式化日期字符串
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // 格式化数字
    public static DecimalFormat nf = new DecimalFormat("0.00");

    public static ArrayList<Person> readExcel(InputStream inputStream, String name, int keyField) {
        Workbook book = null;
        try {
            if (name.endsWith("xlsx")) {
                // 处理ecxel2007
                book = new XSSFWorkbook(inputStream);

            }
            if (name.endsWith("xls")) {
                // 处理ecxel2003
                book = new HSSFWorkbook(inputStream);
            }
        } catch (IOException e) {

        }
        return readWorkbook(book, keyField);
    }

    /*public static ArrayList<Car> readExcelWithCar(InputStream inputStream, String name, int keyField) {
        Workbook book = null;
        try {
            if (name.endsWith("xlsx")) {
                // 处理ecxel2007
                book = new XSSFWorkbook(inputStream);

            }
            if (name.endsWith("xls")) {
                // 处理ecxel2003
                book = new HSSFWorkbook(inputStream);
            }
        } catch (IOException e) {

        }
        return readWorkbookWithCar(book, keyField);
    }*/

    public static int getKeyFieldWithCar(InputStream inputStream, String name) {
        Workbook book = null;
        int type = 0;
        try {
            if (name.endsWith("xlsx")) {
                // 处理ecxel2007
                type = 1;
                book = new XSSFWorkbook(inputStream);

            }
            if (name.endsWith("xls")) {
                // 处理ecxel2003
                type = 2;
                book = new HSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            return 0;

        }
        Sheet sheet = book.getSheetAt(0);
        Row row = sheet.getRow(1);
        Boolean flag = validateExcelWithCar(row);
        int i = 0;
        if (flag) {
            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if (type == 1) {
                    // 如果是2007版的红色是0
                    if (0 == book.getFontAt(cell.getCellStyle().getFontIndex()).getColor()) {
                        i += j + 1;
                    }
                }
                // 如果是2003版的红色是10
                if (type == 2) {
                    if (10 == book.getFontAt(cell.getCellStyle().getFontIndex()).getColor()) {
                        i += j + 1;
                    }
                }
            }
        }
        // 如果i不是1也不是2这说明唯一不能为空的列不是姓名也不是身份证号
        if (i != 1 && i != 2) {
            i = 0;
        }
        return i;
    }

    public static int getKeyField(InputStream inputStream, String name) {
        Workbook book = null;
        int type = 0;
        try {
            if (name.endsWith("xlsx")) {
                // 处理ecxel2007
                type = 1;
                book = new XSSFWorkbook(inputStream);

            }
            if (name.endsWith("xls")) {
                // 处理ecxel2003
                type = 2;
                book = new HSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            return 0;

        }
        Sheet sheet = book.getSheetAt(0);
        Row row = sheet.getRow(1);
        Boolean flag = validateExcel(row);
        int i = 0;
        if (flag) {
            for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if (type == 1) {
                    // 如果是2007版的红色是0
                    if (0 == book.getFontAt(cell.getCellStyle().getFontIndex()).getColor()) {
                        i += j + 1;
                    }
                }
                // 如果是2003版的红色是10
                if (type == 2) {
                    if (10 == book.getFontAt(cell.getCellStyle().getFontIndex()).getColor()) {
                        i += j + 1;
                    }
                }
            }
        }
        // 如果i不是1也不是2这说明唯一不能为空的列不是姓名也不是身份证号
        if (i != 1 && i != 2) {
            i = 0;
        }
        return i;
    }

    public static boolean validateExcel(Row row) {
        List<String> readRow = readRow(row, 1);
        String[] fieldArr = new String[]{"姓名", "身份证号", "性别", "生日", "住址", "是否警示"};
        String[] fieldEnArr = new String[]{"Name", "ID Number", "Gender", "Birthday", "Address", "If Alert"};
        String[] fieldPuArr = new String[]{"Nome", "Número de ID", "Gênero", "Data de nascimento", "Endereço", "Recebeu o aviso?"};
        Boolean flag = Arrays.toString(readRow.toArray()).equals(Arrays.toString(fieldArr))
                || Arrays.toString(readRow.toArray()).equals(Arrays.toString(fieldEnArr))
                || Arrays.toString(readRow.toArray()).equals(Arrays.toString(fieldPuArr));
        return flag;
    }

    public static boolean validateExcelWithCar(Row row) {
        List<String> readRow = readRow(row, 1);
        String[] fieldArr = new String[]{"车牌号码", "所有人", "身份证号", "车辆型号", "车身颜色", "是否警示"};
        String[] fieldEnArr = new String[]{"License plate", "Owner", "ID Number", "Vehicle model", "Color", "If Alert"};
        Boolean flag = Arrays.toString(readRow.toArray()).equals(Arrays.toString(fieldArr)) || Arrays.toString(readRow.toArray()).equals(Arrays.toString(fieldEnArr));
        return flag;
    }

    public static ArrayList<Person> readWorkbook(Workbook book, int keyField) {
		ArrayList<Person> rowList = new ArrayList<>();
        try {
            Person person;
            Sheet sheet = book.getSheetAt(0);
            Row row;
            // 判断标题头
            for (int i = 2, rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                if (null != row) {
                    List<String> readRow = readRow(row, keyField);
                    if (readRow != null && readRow.size() == 6
                            && (!readRow.get(0).equals("姓名") || !readRow.get(0).equals("Name") || !readRow.get(0).equals("Nome"))) {
                        person = new Person();
                        int sex = 2;
                        person.setName(readRow.get(0));
                        person.setCard(readRow.get(1));
                        String sexStr = readRow.get(2).trim();
                        if (sexStr.equals("男") || sexStr.equals("man") || sexStr.equals("Masculino")) {
                            sex = 1;
                        }
                        if (sexStr.equals("女") || sexStr.equals("woman") || sexStr.equals("Feminino")) {
                            sex = 0;
                        }
                        person.setSex(sex);
                        String birthday = readRow.get(3);
                        if (StringUtils.isNotEmpty(birthday) && StringUtils.regexDateStr(birthday)) {
                            person.setBirthday(birthday);
                        }
                        person.setAddress(readRow.get(4));
                        int isWarning = 0;
                        String isWarningStr = readRow.get(5).trim();
                        if (StringUtils.isNotEmpty(isWarningStr) && (isWarningStr.equals("是") || isWarningStr.equals("yes") || isWarningStr.equals("Sim"))) {
                            isWarning = 1;
                        }
                        person.setIsWarning(isWarning);
                        rowList.add(person);
                    }
                } else {
                    rowCount++;
                }
            }
            return rowList;
        } catch (Exception e) {
            return null;
        }
    }


    /*public static ArrayList<Car> readWorkbookWithCar(Workbook book, int keyField) {
        ArrayList<Car> rowList = new ArrayList<>();
        try {
            Car car;
            Sheet sheet = book.getSheetAt(0);
            Row row;
            // 判断标题头
            for (int i = 2, rowCount = 0; rowCount < sheet.getPhysicalNumberOfRows(); i++) {
                row = sheet.getRow(i);
                if (null != row) {
                    List<String> readRow = readRow(row, keyField);
                    if (readRow != null) {
                        if (readRow.size() == 6 && (!readRow.get(0).equals("车牌号码")) || !readRow.get(0).equals("License plate")) {
                            car = new Car();
                            car.setPlateNum(readRow.get(0));
                            car.setOwner(readRow.get(1));
                            car.setOwnerCard(readRow.get(2));
                            car.setModel(readRow.get(3));
                            car.setColor(readRow.get(4));
                            int isWarning = 0;
                            String isWarningStr = readRow.get(5).trim();
                            if (StringUtils.isNotEmpty(isWarningStr) && (isWarningStr.equals("是") || isWarningStr.equals("yes") || isWarningStr.equals("Sim"))) {
                                isWarning = 1;
                            }
                            car.setIsWarning(isWarning);
                            rowList.add(car);
                        }
                    }
                } else {
                    rowCount++;
                }
            }
            return rowList;
        } catch (Exception e) {
            return null;
        }
    }*/


    public static List<String> readRow(Row row, int keyField) {
        List<String> result = new ArrayList<>();
        String value;
        int num = 0;
		for (int j = 0; j < 6; j++) {
            Cell cell = row.getCell(j);
            if (cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                // 当该单元格为空
                if ((keyField == 1 && j == 0) || (keyField == 2 && j == 1))
                    return null;
                num += 1;
                value = "";
            } else {
                if (j != 3)
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                value = cell.toString();
            }
            if (cell != null && cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC && j == 3) {
                if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                    value = df.format(cell.getNumericCellValue());
                } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    value = nf.format(cell.getNumericCellValue());
                } else {
                    value = sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                }
            }
            result.add(value);

        }
        if (num == 6)
            return new ArrayList<>();
        return result;
    }

    public static List<String> readRowWithCar(Row row, int keyField) {
        List<String> result = new ArrayList<>();
        String value;
        int num = 0;
        for (int j = row.getFirstCellNum(); j < 6; j++) {
            Cell cell = row.getCell(j);
            if (cell == null || cell.getCellType() == HSSFCell.CELL_TYPE_BLANK) {
                // 当该单元格为空
                if ((keyField == 1 && j == 0) || (keyField == 2 && j == 1))
                    return null;
                num += 1;
                value = "";
            } else {
                if (j != 3)
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                value = cell.toString();
            }
            result.add(value);

        }
        if (num == 6)
            return new ArrayList<>();
        return result;
    }


    public static void main(String[] args) throws Exception {
		File file1 = new File("d:/card.xlsx");
        int keyField = getKeyField(new FileInputStream(file1), file1.getName());
		ArrayList<Person> result1 = ExcelUtil.readExcel(new FileInputStream(file1), file1.getName(), 2);
        for (Person person : result1) {
            System.out.println("姓名：" + person.getName() + "\t身份证号：" + person.getCard() + "\t性别：" + person.getSex()
                    + "\t生日：" + (person.getBirthday() != null ? person.getBirthday() : "暂无")
                    + "\t民族：" + person.getNation() + "\t地址：" + person.getAddress() + "\t是否警示：" + person.getIsWarning());
        }
    }
}
