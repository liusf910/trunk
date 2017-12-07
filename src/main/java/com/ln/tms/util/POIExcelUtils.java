package com.ln.tms.util;

import com.ln.tms.exception.FailRuntimeException;
import net.sf.ehcache.hibernate.management.impl.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class POIExcelUtils {
    /**
     * 读取excel
     *
     * @param file  导入的文件
     * @param title 文件标题
     * @return List
     */
    public static List<List<Object>> importExcel(MultipartFile file, String title) {
        Workbook workbook = null;
        try {
            String suff = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            if ("xls".equals(suff)) {
                workbook = new HSSFWorkbook(file.getInputStream());
            } else if ("xlsx".equals(suff)) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else {
                throw new FailRuntimeException("不支持的文件类型:" + suff);
            }
            //校验标题
            String readTitle = readTitle(workbook);
            if (title.equals(readTitle)) {
                int cellNum = StringUtils.split(title, ",").length;
                return readExcel(workbook, cellNum);
            } else {
                throw new FailRuntimeException("文件标题错误，正确标题：" + title);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 读取excel标题头
     *
     * @param workbook 表格
     * @return String
     */
    private static String readTitle(Workbook workbook) {
        Sheet sheetAt = workbook.getSheetAt(0);
        Row firstRow = sheetAt.getRow(0);

        StringBuffer title = new StringBuffer();
        for (Cell cell : firstRow) {
            title.append(cell.getStringCellValue() + ",");
        }
        return StringUtils.chop(title.toString());
    }

    /**
     * 读取Office excel
     *
     * @param workbook 表格
     * @return List<List<Object>>
     */
    private static List<List<Object>> readExcel(Workbook workbook, int cellNum) {
        List<List<Object>> list = new LinkedList<List<Object>>();

        Sheet sheet = workbook.getSheetAt(0);
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            List<Object> linked = new LinkedList<Object>();
            for (int j = 0; j < cellNum; j++) {
                Object value = null;
                Cell cell = row.getCell(j);
                if (cell == null) {
                    linked.add(value);
                    continue;
                }
                switch (cell.getCellType()) {
                    case XSSFCell.CELL_TYPE_STRING:
                        value = cell.getStringCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_NUMERIC:
                        if ("@".equals(cell.getCellStyle().getDataFormatString())) {
                            value = cell.getNumericCellValue();
                        } else if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                            value = cell.getNumericCellValue();
                        } else {
                            value = DateUtils.getDateformat(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                        }
                        break;
                    case XSSFCell.CELL_TYPE_BOOLEAN:
                        value = cell.getBooleanCellValue();
                        break;
                    case XSSFCell.CELL_TYPE_FORMULA:
                        value = cell.getCellFormula();
                    case XSSFCell.CELL_TYPE_BLANK:
                        value = "";
                        break;
                    default:
                        value = cell.toString();
                }
                linked.add(value);
            }
            list.add(linked);
        }
        return list;
    }

    /**
     * 导出excel头部标题
     *
     * @param title                  头部一级标题
     * @param cellRangeAddressLength 合并的行数
     * @param columnWidth            单元格列宽
     * @return HSSFWorkbook
     */
    public static HSSFWorkbook makeExcelHead(String title, int cellRangeAddressLength, int columnWidth) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFCellStyle styleTitle = createStyle(workbook, (short) 15);
        HSSFSheet sheet = workbook.createSheet(title);
        sheet.setDefaultColumnWidth(columnWidth);
        CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, cellRangeAddressLength);
        sheet.addMergedRegion(cellRangeAddress);
        HSSFRow rowTitle = sheet.createRow(0);
        HSSFCell cellTitle = rowTitle.createCell(0);
        // 为标题设置背景颜色
        styleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleTitle.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        cellTitle.setCellValue(title);
        cellTitle.setCellStyle(styleTitle);
        return workbook;
    }

    /**
     * 设定二级标题
     *
     * @param workbook
     * @param secondTitles 头部二级标题
     * @param sheetName    sheet名称
     * @return HSSFWorkbook
     */
    public static HSSFWorkbook makeSecondHead(HSSFWorkbook workbook, String[] secondTitles, String sheetName) {
        // 创建用户属性栏
        workbook.setSheetName(0, sheetName);
        HSSFSheet sheet = workbook.getSheetAt(0);
        HSSFRow rowField = sheet.createRow(1);
        HSSFCellStyle styleField = createStyle(workbook, (short) 13);
        for (int i = 0; i < secondTitles.length; i++) {
            HSSFCell cell = rowField.createCell(i);
            cell.setCellValue(secondTitles[i]);
            cell.setCellStyle(styleField);
        }
        return workbook;
    }

    /**
     * 提取公共的样式
     *
     * @param workbook
     * @param fontSize 字体大小
     * @return HSSFCellStyle
     */
    private static HSSFCellStyle createStyle(HSSFWorkbook workbook, short fontSize) {
        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 创建一个字体样式
        HSSFFont font = workbook.createFont();
        font.setFontHeightInPoints(fontSize);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
        return style;
    }

    /**
     * 导出数据
     *
     * @param workbook
     * @param dataList      导出的集合
     * @param beanPropertys 对应的实体bean属性
     * @return T
     */
    public static <T> HSSFWorkbook exportExcelData(HSSFWorkbook workbook, List<T> dataList, String[] beanPropertys) {
        HSSFSheet sheet = workbook.getSheetAt(0);
        // 填充数据
        HSSFCellStyle styleData = workbook.createCellStyle();
        styleData.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleData.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        for (int j = 0; j < dataList.size(); j++) {
            HSSFRow rowData = sheet.createRow(j + 2);
            T t = dataList.get(j);
            for (int k = 0; k < beanPropertys.length; k++) {
                Object value = BeanUtils.getBeanProperty(t, beanPropertys[k]);
                HSSFCell cellData = rowData.createCell(k);

                cellData.setCellValue(value != null ? value.toString() : null);

                cellData.setCellStyle(styleData);
            }
        }
        return workbook;
    }
}
