package com.longlong.common.utils;

import net.lingala.zip4j.ZipFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileZipUtil {
    private static final String xmlFile = "<file><filename>savefileName</filename></file>";
    public static final String did = ".did";
    public static final String zip = ".zip";

    /**
     * 文件压缩 用于加密
     *
     * @param list 压缩的文件数据
     * @param path 路径 压缩生产的路径 全路径 /home/1.exe
     */
    public static File compression(List<byte[]> list, String path,String newFileName) throws IOException {
        File file = new File(path);
        OutputStream outputStream = Files.newOutputStream(file.toPath());
        //创建一个压缩输出流对象
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        //设置压缩方式
        zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
        //遍历list集合
        for (int i = 0; i < list.size(); i++) {
            //获取文件名
            String fileName = i + "";
            //创建一个压缩实体
            zipOutputStream.putNextEntry(new ZipEntry(fileName));
            //创建一个输入流对象
            InputStream is = new ByteArrayInputStream(list.get(i));
            //创建一个字节数组
            byte[] b = list.get(i);
            System.out.println("大小" + b.length / 1024 / 1024);
            //定义一个长度
//            int length = 0;
//            //循环读取输入流中的数据
//            while ((length = is.read(b)) > -1) {
            //将数据写入压缩输出流
            zipOutputStream.write(b);
//            }
            //关闭压缩实体
            zipOutputStream.closeEntry();
        }
        zipOutputStream.putNextEntry(new ZipEntry("fileSettings.xml"));
        zipOutputStream.write(xmlFile.replace("savefileName",newFileName).getBytes());
        //关闭压缩输出流
        zipOutputStream.close();
        //返回文件
        return file;
    }


    /**
     * 文件压缩
     *
     * @param list 压缩的文件数据
     * @param path 路径 压缩生产的路径 全路径 /home/1.exe
     */
    public static File compression(List<byte[]> list, String path, List<String> fileNames) throws IOException {
        File file = new File(path);
        OutputStream outputStream = Files.newOutputStream(file.toPath());
        //创建一个压缩输出流对象
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        //设置压缩方式
        zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
        //遍历list集合
        for (int i = 0; i < list.size(); i++) {
            //获取文件名
            String fileName = fileNames.get(i);
            //创建一个压缩实体
            zipOutputStream.putNextEntry(new ZipEntry(fileName));
            //创建一个输入流对象
            InputStream is = new ByteArrayInputStream(list.get(i));
            //创建一个字节数组
            byte[] b = list.get(i);
            System.out.println("大小" + b.length / 1024 / 1024);
            //定义一个长度
//            int length = 0;
//            //循环读取输入流中的数据
//            while ((length = is.read(b)) > -1) {
            //将数据写入压缩输出流
            zipOutputStream.write(b);
//            }
            //关闭压缩实体
            zipOutputStream.closeEntry();
        }
        //关闭压缩输出流
        zipOutputStream.close();
        //返回文件
        return file;
    }


    /**
     * 文件解密
     **/
    // 定义一个方法，用于解压缩指定路径的文件
    public static Map<String, byte[]> decompress(String path) throws IOException {
        Map<String, byte[]> map = new HashMap<>(16);

        //转换成io流
        InputStream inputStream = new FileInputStream(path);
        ZipInputStream zipInputStream = new ZipInputStream(inputStream, Charset.forName("UTF-8"));
        List<InputStream> list = new ArrayList<>();
        ZipEntry zipEntry = null;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            BufferedReader in = null;
            byte[] byte_s = new byte[1];
            int num = -1;
            while ((num = zipInputStream.read(byte_s, 0, byte_s.length)) > -1) {
                byteArrayOutputStream.write(byte_s);
            }
            map.put(zipEntry.getName(), byteArrayOutputStream.toByteArray());
            byteArrayOutputStream.close();
        }
        zipInputStream.close();
        inputStream.close();
        return map;
    }

    public static File compression(File file, String password) {
        ZipFile zipFile = new ZipFile(file, password.toCharArray());

        return null;

    }


}
