

package com.longlong.common.utils;

import com.longlong.common.file.WindowsFileProperties;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;
import org.dom4j.DocumentException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 文件工具类
 */
@UtilityClass
public class FileUtil extends org.springframework.util.FileCopyUtils {

    static Long KB = 1024L;
    static Long MB = 1024L * KB;
    static Long GB = 1024L * MB;

    static int fileEncryptionSize = (int) (50 * MB);


    /**
     * 默认为true
     */
    public static class TrueFilter implements FileFilter, Serializable {
        private static final long serialVersionUID = -6420452043795072619L;

        public final static TrueFilter TRUE = new TrueFilter();

        @Override
        public boolean accept(File pathname) {
            return true;
        }
    }

    /**
     * 扫描目录下的文件
     *
     * @param path 路径
     * @return 文件集合
     */
    public static List<File> list(String path) {
        File file = new File(path);
        return list(file, TrueFilter.TRUE);
    }

    /**
     * 扫描目录下的文件
     *
     * @param path            路径
     * @param fileNamePattern 文件名 * 号
     * @return 文件集合
     */
    public static List<File> list(String path, final String fileNamePattern) {
        File file = new File(path);
        return list(file, pathname -> {
            String fileName = pathname.getName();
            return PatternMatchUtils.simpleMatch(fileNamePattern, fileName);
        });
    }

    /**
     * 扫描目录下的文件
     *
     * @param path   路径
     * @param filter 文件过滤
     * @return 文件集合
     */
    public static List<File> list(String path, FileFilter filter) {
        File file = new File(path);
        return list(file, filter);
    }

    /**
     * 扫描目录下的文件
     *
     * @param file 文件
     * @return 文件集合
     */
    public static List<File> list(File file) {
        List<File> fileList = new ArrayList<>();
        return list(file, fileList, TrueFilter.TRUE);
    }

    /**
     * 扫描目录下的文件
     *
     * @param file            文件
     * @param fileNamePattern Spring AntPathMatcher 规则
     * @return 文件集合
     */
    public static List<File> list(File file, final String fileNamePattern) {
        List<File> fileList = new ArrayList<>();
        return list(file, fileList, pathname -> {
            String fileName = pathname.getName();
            return PatternMatchUtils.simpleMatch(fileNamePattern, fileName);
        });
    }

    /**
     * 扫描目录下的文件
     *
     * @param file   文件
     * @param filter 文件过滤
     * @return 文件集合
     */
    public static List<File> list(File file, FileFilter filter) {
        List<File> fileList = new ArrayList<>();
        return list(file, fileList, filter);
    }

    /**
     * 扫描目录下的文件
     *
     * @param file   文件
     * @param filter 文件过滤
     * @return 文件集合
     */
    private static List<File> list(File file, List<File> fileList, FileFilter filter) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    list(f, fileList, filter);
                }
            }
        } else {
            // 过滤文件
            boolean accept = filter.accept(file);
            if (file.exists() && accept) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     * 获取文件后缀名
     *
     * @param fullName 文件全名
     * @return {String}
     */
    public static String getFileExtension(String fullName) {
        Assert.notNull(fullName, "file fullName is null.");
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * 获取文件名，去除后缀名
     *
     * @param file 文件
     * @return {String}
     */
    public static String getNameWithoutExtension(String file) {
        Assert.notNull(file, "file is null.");
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf(CharPool.DOT);
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    /**
     * Returns the path to the system temporary directory.
     *
     * @return the path to the system temporary directory.
     */
    public static String getTempDirPath() {
        return System.getProperty("java.io.tmpdir");
    }

    /**
     * Returns a {@link File} representing the system temporary directory.
     *
     * @return the system temporary directory.
     */
    public static File getTempDir() {
        return new File(getTempDirPath());
    }

    /**
     * Reads the contents of a file into a String.
     * The file is always closed.
     *
     * @param file the file to read, must not be {@code null}
     * @return the file contents, never {@code null}
     */
    public static String readToString(final File file) {
        return readToString(file, Charsets.UTF_8);
    }

    /**
     * Reads the contents of a file into a String.
     * The file is always closed.
     *
     * @param file     the file to read, must not be {@code null}
     * @param encoding the encoding to use, {@code null} means platform default
     * @return the file contents, never {@code null}
     */
    public static String readToString(final File file, final Charset encoding) {
        try (InputStream in = Files.newInputStream(file.toPath())) {
            return IoUtil.toString(in, encoding);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }


    public static byte[] readToByteArray(final File file) {
        try (InputStream in = Files.newInputStream(file.toPath())) {
            return IoUtil.toByteArray(in);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    public static File readToFile(byte[] bytes, String path) {
        File file = new File(path);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file the file to write
     * @param data the content to write to the file
     */
    public static void writeToFile(final File file, final String data) {
        writeToFile(file, data, Charsets.UTF_8, false);
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file   the file to write
     * @param data   the content to write to the file
     * @param append if {@code true}, then the String will be added to the
     *               end of the file rather than overwriting
     */
    public static void writeToFile(final File file, final String data, final boolean append) {
        writeToFile(file, data, Charsets.UTF_8, append);
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file     the file to write
     * @param data     the content to write to the file
     * @param encoding the encoding to use, {@code null} means platform default
     */
    public static void writeToFile(final File file, final String data, final Charset encoding) {
        writeToFile(file, data, encoding, false);
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file     the file to write
     * @param data     the content to write to the file
     * @param encoding the encoding to use, {@code null} means platform default
     * @param append   if {@code true}, then the String will be added to the
     *                 end of the file rather than overwriting
     */
    public static void writeToFile(final File file, final String data, final Charset encoding, final boolean append) {
        try (OutputStream out = new FileOutputStream(file, append)) {
            IoUtil.write(data, out, encoding);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 转成file
     *
     * @param multipartFile MultipartFile
     * @param file          File
     */
    public static void toFile(MultipartFile multipartFile, final File file) {
        try {
            FileUtil.toFile(multipartFile.getInputStream(), file);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * 转成file
     *
     * @param in   InputStream
     * @param file File
     */
    public static void toFile(InputStream in, final File file) {
        try (OutputStream out = new FileOutputStream(file)) {
            FileUtil.copy(in, out);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Moves a file.
     * <p>
     * When the destination file is on another file system, do a "copy and delete".
     *
     * @param srcFile  the file to be moved
     * @param destFile the destination file
     * @throws NullPointerException if source or destination is {@code null}
     * @throws IOException          if source or destination is invalid
     * @throws IOException          if an IO error occurs moving the file
     */
    public static void moveFile(final File srcFile, final File destFile) throws IOException {
        Assert.notNull(srcFile, "Source must not be null");
        Assert.notNull(destFile, "Destination must not be null");
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' is a directory");
        }
        if (destFile.exists()) {
            throw new IOException("Destination '" + destFile + "' already exists");
        }
        if (destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' is a directory");
        }
        final boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            FileUtil.copy(srcFile, destFile);
            if (!srcFile.delete()) {
                FileUtil.deleteQuietly(destFile);
                throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
            }
        }
    }

    /**
     * 删除文件
     */
    public static boolean deleteQuietly(@Nullable final File file) {
        if (file == null) {
            return false;
        }
        try {
            if (file.isDirectory()) {
                FileSystemUtils.deleteRecursively(file);
            }
        } catch (final Exception ignored) {
        }

        try {
            return file.delete();
        } catch (final Exception ignored) {
            return false;
        }
    }

    /**
     * 判断文件路径是否存在不存在则创建
     */
    public static void createFilePath(String path) {

        File file = new File(path);
        createFilePath(file);
    }

    /**
     * 判断文件路径是否存在不存在则创建
     */
    public static void createFilePath(File file) {
        System.out.println("创建路径"+file.getPath() );
        if (!file.exists()) {
            String path = file.getPath();
            ;
            //win出现的问题保存文件则没问题 默认文件创建在C盘
            if (System.getProperty("os.name").contains("Windows")) {
                file = new File(path);
                System.out.println(file.getPath() + "创建成功");
                boolean mkdir = file.mkdir();
                System.out.println(mkdir);
            } else {
                file.mkdir();
            }
        }
    }

    /**
     * 生产文件加密密码
     */
    public static String getPassword() {
        return StringUtil.random(16);
    }

    /**
     * 文件加密
     *
     * @param path        加密路径
     * @param key         密钥
     * @param encryptPath 压缩生产的路径 全路径 /home/1.exe
     */
    public static File FileEncrypt(String path, String key, String encryptPath, String newFileName) throws IOException {

//        Assert.isTrue(key.length() == 32, "密钥长度不对");
        //加密文件大小
        int encrypt = 50;
        File file = new File(path);
        BufferedInputStream in = new BufferedInputStream(Files.newInputStream(file.toPath()));
        List<byte[]> list = new ArrayList<>();
        int len = -1;
        if (file.isFile()) {
//            file.length()/MB;
            //要循环的次数
            int forI = (int) Math.ceil((double) file.length() / fileEncryptionSize);

            for (int i = 0; i < forI; i++) {
                if (forI == 1) {
                    //小于50MB
                    byte[] byteArray = readToByteArray(file);

                    list.add(AesUtil.encrypt(byteArray, key));

                    System.out.println("小于50MB");

                } else {
                    if (i + 1 != forI) {
                        byte[] byteArray = new byte[(int) (fileEncryptionSize)];
                        String fileName = RandomStringUtils.randomAlphanumeric(10) + System.currentTimeMillis();
                        File tempFile = new File(fileName);
                        FileOutputStream outputStream = new FileOutputStream(tempFile);
                        BufferedOutputStream out = new BufferedOutputStream(outputStream);
                        int count = 0;
                        while ((len = in.read(byteArray)) != -1) {
                            out.write(byteArray, 0, len);
                            count += len;
                            if (count >= MB * encrypt) {
                                break;//每次读取1M，然后写入到文件中
                            }
                        }
                        list.add(AesUtil.encrypt(byteArray, key));

                        out.flush();
                        out.close();
                        outputStream.close();
                        System.out.println(forI);
                        System.out.println(file.length());
                    } else {
                        byte[] byteArray = new byte[(int) (file.length() % fileEncryptionSize)];
                        String fileName = RandomStringUtils.randomAlphanumeric(10) + System.currentTimeMillis();
                        File tempFile = new File(fileName);
                        FileOutputStream outputStream = new FileOutputStream(tempFile);
                        BufferedOutputStream out = new BufferedOutputStream(outputStream);
                        int count = 0;
                        while ((len = in.read(byteArray)) != -1) {
                            out.write(byteArray, 0, len);
                            count += len;
                            if (count >= file.length() % fileEncryptionSize) {
                                break;//每次读取1M，然后写入到文件中
                            }
                        }
                        list.add(AesUtil.encrypt(byteArray, key));

                        out.flush();
                        out.close();
                        outputStream.close();
                        System.out.println(forI);
                        System.out.println(file.length() / fileEncryptionSize);
                    }
                }
            }
        }
        File compression = FileZipUtil.compression(list, encryptPath, newFileName);
        return compression;
    }

    public static byte[] toByteArray(File file) {
        try (InputStream inputStream = new FileInputStream(file)) {
            long length = file.length();
            if (length > Integer.MAX_VALUE) {
                throw new IOException("File is too large: " + file.getName());
            }

            byte[] bytes = new byte[(int) length];
            int offset = 0;
            int numRead;

            while (offset < bytes.length && (numRead = inputStream.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }

            if (offset < bytes.length) {
                throw new IOException("Could not completely read file " + file.getName());
            }

            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 文件解密
     *
     * @param path     解密路径
     * @param key      密钥
     * @param fileName 文件名称
     */
    public static File decrypt(String path, String key, String fileName) throws IOException {
        List<byte[]> list = new LinkedList<>();
        Map<String, byte[]> decompress = FileZipUtil.decompress(path);
        decompress.forEach((mapKey, value) -> {
            if (!mapKey.contains(".xml")) {
                list.add(AesUtil.decrypt(value, key));
            } else {
                //解析xml
                try {
                    XmlUtil xmlUtil = new XmlUtil(value);
                    xmlUtil.getElementValue("filename");
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        byte[] bytes = listToByteArray(list);

        return readToFile(bytes, fileName);
    }


    public static File decryptV1(String path, String key, String decryptFilePath) throws IOException {
        AtomicReference<String> fileName = new AtomicReference<>("");
        List<byte[]> list = new LinkedList<>();
        Map<String, byte[]> decompress = FileZipUtil.decompress(path);
        decompress.forEach((mapKey, value) -> {
            if (!mapKey.contains(".xml")) {
                list.add(AesUtil.decrypt(value, key));
            } else {
                //解析xml
                try {
                    XmlUtil xmlUtil = new XmlUtil(value);
                    fileName.set(xmlUtil.getElementText("filename"));
                    System.out.println(xmlUtil.getElementText("filename"));
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        byte[] bytes = listToByteArray(list);

        return readToFile(bytes, decryptFilePath + fileName.get());
    }

    public static byte[] listToByteArray(List<byte[]> list) {
        int length = 0;
        for (byte[] bytes : list) {
            length += bytes.length;
        }

        byte[] byteArray = new byte[length];
        int index = 0;
        for (byte[] bytes : list) {
            System.arraycopy(bytes, 0, byteArray, index, bytes.length);
            index += bytes.length;
        }

        return byteArray;
    }

    /**
     * 获取文件的MD5
     */
    public static String md5(final byte[] bytes) {
        return DigestUtil.md5Hex(bytes);
    }
    /**
     * 获取文件的sha256
     */
    public static String sha256(byte[] bytes) {
        return DigestUtil.sha256(bytes);
    }

}
