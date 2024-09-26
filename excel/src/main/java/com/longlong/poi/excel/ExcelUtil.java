package com.longlong.poi.excel;

import io.micrometer.core.instrument.internal.TimedExecutorService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.task.TaskExecutor;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ExcelUtil<T> {

    /**
     * 工作薄
     */
    private Workbook wb;

    /**
     * 工作表
     */
    private Sheet sheet;

    /**
     * 需要导出的数据
     */
    private List<T> exportList;

    /**
     * 对象的class对象
     */
    private Class<T> clazz;

    /**
     * 被选中需要导出的字段名称
     */
    private Map<String, Object> checkedFieldsName;

    /**
     * 被选中需要导出的字段对象
     */
    private List<Field> checkedFields;

    /**
     * 包含需要字典转换的字段对象
     */
    private List<Field> fieldsContainDict;

    /**
     * 对象中的字典值
     */
    private Map<String, Map<String, String>> dicts;

    private CellStyle header;
    private CellStyle data;

    private ExcelUtil() {
    }

    public ExcelUtil(Class<T> clazz) {
        this.clazz = clazz;
    }


    /**
     * 导出Excel
     *
     * @param list      需要导出的数据
     * @param sheetName 工作表名称
     */
    public Workbook exportExcel(List<T> list, String sheetName) {
        //初始化
        init(list, sheetName, null);
        try {
            //字段
            convertDict();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // sheet第一行加入名称数据
        createTopRow();

        try {
            createOtherRow();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return wb;
    }


    /**
     * 导出Excel
     *
     * @param list       需要导出的数据
     * @param sheetName  工作表名称
     * @param fieldsName 被选中需要导出的字段名称
     */
    public Workbook exportExcel(List<T> list, Map<String, Object> fieldsName, String sheetName) {
        //初始化
        init(list, sheetName, fieldsName);
        try {
            //字段
            convertDict();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // sheet第一行加入名称数据
        createTopRow();

        try {
            createOtherRow();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return wb;
    }


    /**
     * 导出Excel
     *
     * @param list       需要导出的数据
     * @param sheetName  工作表名称
     * @param fileName   文件名称
     * @param fieldsName 被选中需要导出的字段名称
     */
    public void exportExcel(List<T> list, Map<String, Object> fieldsName, String sheetName, String fileName, HttpServletResponse response) {
        //初始化
        init(list, sheetName, fieldsName);

        try {
            //字段
            convertDict();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // sheet第一行加入名称数据
        createTopRow();

        try {
            createOtherRow();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // 导出wb
        try {
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("content-disposition",
                    URLEncoder.encode(fileName + ".xlsx", "utf-8"));
            //     response.setHeader("Content-disposition",String.format("attachment; filename=\"%s\"", fileName+"-"+System.currentTimeMillis() + ".xlsx"));
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 导出Excel
     *
     * @param list      需要导出的数据
     * @param sheetName 工作表名称
     * @param fileName  文件名称
     */
    public void exportExcel(List<T> list, String sheetName, String fileName, HttpServletResponse response) {
        //初始化
        init(list, sheetName, null);

        try {
            //字段
            convertDict();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // sheet第一行加入名称数据
        createTopRow();

        try {
            createOtherRow();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // 导出wb
        try {
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("content-disposition",
                    URLEncoder.encode(fileName + ".xlsx", "utf-8"));
            //     response.setHeader("Content-disposition",String.format("attachment; filename=\"%s\"", fileName+"-"+System.currentTimeMillis() + ".xlsx"));
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            wb.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 初始化
     */
    public void init(List<T> list, String sheetName, Map<String, Object> fieldsName) {
        this.checkedFieldsName = fieldsName;
        // 防止导出过程中出现空指针
        if (Objects.isNull(list)) {
            this.exportList = new ArrayList<>();
        } else {
            this.exportList = list;
        }
        //初始化工作簿
        initWorkbook();
        if (fieldsName == null) {
            // 初始化工作表
            initSheet(sheetName);
        } else {
            // 初始化工作表
            initSheet(sheetName, fieldsName);
        }
        initFields();
        // 根据注解生成生成字典
        generateObjDict();
        //生成样式
        createStyles(wb);

    }


    /**
     * 初始化工作簿
     */
    private void initWorkbook() {
        this.wb = new SXSSFWorkbook();
    }


    /**
     * 初始化工作表
     */
    //初始化表格
    private void initSheet(String sheetName, Map<String, Object> fieldsName) {
        //创建标题样式
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 15);
        titleStyle.setFont(titleFont);
        //创建工作表
        this.sheet = wb.createSheet();
        //创建表头
        Row sheetRow = this.sheet.createRow(0);
        sheetRow.createCell(0).setCellValue(sheetName);
        sheetRow.getCell(0).setCellStyle(titleStyle);
        //合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, fieldsName.size() - 1));
    }


    /**
     * 初始化工作表
     */
    //初始化表格
    private void initSheet(String sheetName) {
        //创建标题样式
        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) 15);
        titleStyle.setFont(titleFont);
        //创建工作表
        this.sheet = wb.createSheet();
        //创建表头
        Row sheetRow = this.sheet.createRow(0);
        sheetRow.createCell(0).setCellValue(sheetName);
        sheetRow.getCell(0).setCellStyle(titleStyle);
        //获取Class 包含@excel的长度
        Field[] declaredFields = clazz.getDeclaredFields();
        int lastCol = 1;
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(Excel.class)) {
                lastCol++;
            }
        }

        //合并单元格
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, lastCol - 1));
    }

    /**
     * 初始化checkedFields(选中需要导出的字段), fieldsContainDict(字典)
     * fieldsContainDict含有字典表达式的字段
     * checkedFields用户选中的字段
     * 如果用户自定义存在则走自定义的 如果不存在则走有Excel注解的
     */
    private void initFields() {
        // 获取对象所有字段对象
        Field[] fields = clazz.getDeclaredFields();
        // 过滤出checkedFields
        this.checkedFields = Arrays.stream(fields).
                filter(item -> {
                    //判断用户自定是否存在
                    if (!Objects.isNull(this.checkedFieldsName)) {
                        if (item.isAnnotationPresent(Excel.class)) {
                            return checkedFieldsName.containsKey(item.getName());
                        }
                    } else {
                        return item.isAnnotationPresent(Excel.class);
                    }
                    return false;
                })
                .collect(Collectors.toList());

        // 过滤出fieldsContainDict
        this.fieldsContainDict = Arrays.stream(clazz.getDeclaredFields())
                .filter(item -> !"".equals(item.getAnnotation(Excel.class) != null ? item.getAnnotation(Excel.class).dictExp() : ""))
                .collect(Collectors.toList());
    }

    /**
     * 通过扫描字段注解生成字典数据
     */
    //遍历所有包含字典注解的字段
    private void generateObjDict() {
        //如果没有包含字典注解的字段，则直接返回
        if (fieldsContainDict.isEmpty()) {
            return;
        }
        //如果dicts为空，则初始化dicts
        if (dicts == null) {
            dicts = new HashMap<>();
        }
        //遍历所有包含字典注解的字段
        for (Field field : fieldsContainDict) {
            //获取字段名，作为字典的key
            String dictKey = field.getName();
            //获取字典注解的值
            String exps = field.getAnnotation(Excel.class).dictExp();
            //以逗号分割字符串
            String[] exp = exps.split(",");
            //创建一个Map，用于存储字典的key-value
            Map<String, String> keyV = new HashMap<>();
            //将Map存储到dicts中
            dicts.put(dictKey, keyV);
            //遍历分割后的字符串数组
            for (String s : exp) {
                //以等号分割字符串
                String[] out = s.split(":");
                //将分割后的key-value存储到Map中
                keyV.put(out[0], out[1]);
            }
        }

    }

    /**
     * 转换字典值
     * 将数据中字典值转化为对应的值(注:字典值应为String格式)
     */
    private void convertDict() throws IllegalAccessException {
        for (Field field : fieldsContainDict) {
            Excel annotation = field.getAnnotation(Excel.class);
            String dictKey = field.getName();
            field.setAccessible(true);
            for (T t : exportList) {
                // 获取字段值
                String o = (String) field.get(t);
                field.set(t, dicts.get(dictKey).get(o));
            }
        }
    }

    /**
     * sheet第一行加入名称数据
     *
     * @param
     */
    private void createTopRow() {
        Row row = this.sheet.createRow(1);
        //设置样式
        for (int index = 0; index < checkedFields.size(); index++) {
            Cell cell = row.createCell(index);
            Sheet sheet = cell.getSheet();
            sheet.setColumnWidth(index, 5000);
            cell.setCellValue(checkedFields.get(index).getAnnotation(Excel.class).name());
            cell.setCellStyle(header);
        }
    }

    /**
     * 创建表格样式
     *
     * @param wb 工作薄对象
     * @return 样式列表 header 为表头样式 data 为内容样式
     */
    private void createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        // 数据格式
        CellStyle style = wb.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);
        data = style;
        // 表头格式
        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBold(true);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        style.setBorderLeft(BorderStyle.DASHED);
        styles.put("header", style);
        header = style;
    }

    /**
     * 添加导出数据
     */
    @SneakyThrows
    private void createOtherRow() throws IllegalAccessException {
        for (int rowNum = 0; rowNum < exportList.size(); rowNum++) {

            int finalRowNum = rowNum;

                Row row = sheet.createRow(finalRowNum + 2);
                row.setHeight((short) 600);
                T t = exportList.get(finalRowNum);
                for (int colNum = 0; colNum < checkedFields.size(); colNum++) {
                    Cell cell = row.createCell(colNum);
                    cell.setCellStyle(data);
                    Field field = checkedFields.get(colNum);
                    field.setAccessible(true);
                    // 单元格设置值
                    try {
                        addCell(cell, field, t);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }

            }


        }





    /**
     * 单元格中添加数据
     *
     * @param cell  单元格
     * @param field 字段
     * @param t     list中的一条数据
     */
    private void addCell(Cell cell, Field field, T t) throws IllegalAccessException {
        Class<?> fieldType = field.getType();
        if (String.class == fieldType) {
            if (field.get(t) != null && field.get(t) != "") {
                int isFile = field.getAnnotation(Excel.class).isFile();
                //文本
                String cellValue = (String) field.get(t);
                if (isFile == 0) {
                    if (("").equals(cellValue)) {
                        cell.setCellValue("-");
                    } else {
                        cell.setCellValue((String) field.get(t));
                    }
                } else if (isFile == 1) {

                } else if (isFile == 2) {

                }
            } else {
                cell.setCellValue("-");
            }
        } else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
            if (field.get(t) != null && field.get(t) != "") {
                cell.setCellValue((Integer) field.get(t));
            } else {
                cell.setCellValue("-");
            }
        } else if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
            if (field.get(t) != null && field.get(t) != "") {
                cell.setCellValue((Long) field.get(t));
            } else {
                cell.setCellValue("-");
            }
        } else if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
            if (field.get(t) != null && field.get(t) != "") {
                cell.setCellValue((Double) field.get(t));
            } else {
                cell.setCellValue("-");
            }
        } else if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
            if (field.get(t) != null && field.get(t) != "") {
                cell.setCellValue((Float) field.get(t) == null);
            } else {
                cell.setCellValue("-");
            }
        } else if (Date.class == fieldType) {
            if (field.get(t) != null && field.get(t) != "") {
                String dateFormat = field.getAnnotation(Excel.class).dateFormat();
                cell.setCellValue(dateFormat((Date) field.get(t), dateFormat));
            } else {
                cell.setCellValue("-");
            }
        } else {
            cell.setCellValue("-");
        }
    }

    /**
     * 时间格式转换
     *
     * @param date       日期
     * @param dateFormat 日期格式
     * @return
     */
    private String dateFormat(Date date, String dateFormat) {
        if (dateFormat == null || dateFormat.isEmpty()) {
            dateFormat = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(date);
    }


}
