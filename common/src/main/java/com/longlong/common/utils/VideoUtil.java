package com.longlong.common.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class VideoUtil {

    /**
     * 获取视频长度
     *
     * @param videoPath 视频文件地址
     */
    public static Long videoLength(String videoPath) {
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(videoPath);
        return videoLength(frameGrabber);
    }

    /**
     * 获取视频长度
     *
     * @param inputStream 视频流
     */
    public static Long videoLength(InputStream inputStream) {
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(inputStream);
        return videoLength(frameGrabber);
    }

    /**
     * 获取视频长度
     *
     * @param frameGrabber 对象
     */
    public static Long videoLength(FFmpegFrameGrabber frameGrabber) {
        try {
            // 开始抓取视频帧
            frameGrabber.start();
            // 获取视频时长（单位：毫秒）
            Double duration = frameGrabber.getLengthInTime() / frameGrabber.getFrameRate() / 1000;
            return duration.longValue();
        } catch (Exception e) {
            // 如果发生异常，则输出异常信息

        }
        // 返回视频时长（单位：毫秒）
        return 0L;
    }

    /**
     * 获取视频帧数
     */
    public static double videoFrameRate(FFmpegFrameGrabber frameGrabber) {
        return frameGrabber.getFrameRate();

    }

    /**
     * 获取视频帧数
     */
    public static double videoFrameRate(InputStream inputStream) {
        FFmpegFrameGrabber frameGrabber = new FFmpegFrameGrabber(inputStream);
        return (videoFrameRate(frameGrabber));

    }

    static final String suffixJPG = ".jpg";
    static final String JPG = "jpg";

    /**
     * @param videoPath 视频地址
     * @param savePath  保存图片路径
     */

    @SneakyThrows
    public static String getScreenshot(String videoPath, String savePath) {
        // 创建一个FFmpegFrameGrabber对象，用于抓取视频帧
        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(videoPath);
        // 启动抓取器
        grabber.start();
        // 抓取一帧图像
        Frame frame = grabber.grabImage();
        // 创建Java2DFrameConverter对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
// 使用Java2DFrameConverter对象的getBufferedImage方法，获取帧转换后的BufferedImage对象
        BufferedImage bi = converter.getBufferedImage(frame);
        String pngName = savePath+ RandomStringUtils.randomAlphanumeric(10) + System.currentTimeMillis()+suffixJPG;
        File output = new File(pngName);
        boolean write = ImageIO.write(bi, JPG, output);
        if (write){
            return pngName;
        }else {
            return "";
        }

//        System.out.println("截取视频截图开始："+ System.currentTimeMillis());
//        Map<String, Object> result = new HashMap<>();
//        FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(filePath);
//
//        // 第一帧图片存储位置
//        String targerFilePath = filePath.substring(0, filePath.lastIndexOf("\\"));
//        // 视频文件名
//        String fileName = filePath.substring(filePath.lastIndexOf("\\") + 1);
//        // 图片名称
//        String targetFileName = fileName.substring(0, fileName.lastIndexOf("."));
//        System.out.println("视频路径是：" + targerFilePath);
//        System.out.println("视频文件名：" + fileName);
//        System.out.println("图片名称是：" + targetFileName);
//
//        grabber.start();
//        //设置视频截取帧（默认取第一帧）
//        Frame frame = grabber.grabImage();
//        //视频旋转度
//        String rotate = grabber.getVideoMetadata("rotate");
//        Java2DFrameConverter converter = new Java2DFrameConverter();
//        //绘制图片
//        BufferedImage bi = converter.getBufferedImage(frame);
//        if (rotate != null) {
//            // 旋转图片
//            bi = rotate(bi, Integer.parseInt(rotate));
//        }
//        //图片的类型
//        String imageMat = "jpg";
//        //图片的完整路径
//        String imagePath = targerFilePath + File.separator + targetFileName + "." + imageMat;
//        //创建文件
//        File output = new File(imagePath);
//        ImageIO.write(bi, imageMat, output);
//
//        //拼接Map信息
//        result.put("videoWide", bi.getWidth());
//        result.put("videoHigh", bi.getHeight());
//        long duration = grabber.getLengthInTime() / (1000 * 1000);
//        result.put("rotate", StringUtils.isBlank(rotate)? "0" : rotate);
//        result.put("format", grabber.getFormat());
//        result.put("imgPath", output.getPath());
//        System.out.println("视频的宽:" + bi.getWidth());
//        System.out.println("视频的高:" + bi.getHeight());
//        System.out.println("视频的旋转度：" + rotate);
//        System.out.println("视频的格式：" + grabber.getFormat());
//        System.out.println("此视频时长（s/秒）：" + duration);
//        grabber.stop();
//        System.out.println("截取视频截图结束："+ System.currentTimeMillis());
//        return result;
    }

//    /**
//     * @Description: 根据视频旋转度来调整图片
//     * @param src
//     * @param angel	视频旋转度
//     * @return  BufferedImage
//     */
//    public static BufferedImage rotate(BufferedImage src, int angel) {
//        int src_width = src.getWidth(null);
//        int src_height = src.getHeight(null);
//        int type = src.getColorModel().getTransparency();
//        Rectangle rect_des = calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);
//        BufferedImage bi = new BufferedImage(rect_des.width, rect_des.height, type);
//        Graphics2D g2 = bi.createGraphics();
//        g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
//        g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
//        g2.drawImage(src, 0, 0, null);
//        g2.dispose();
//        return bi;
//    }
//
//
//    /**
//     * @Description: 计算图片旋转大小
//     * @param src
//     * @param angel
//     * @return  Rectangle
//     */
//    public static Rectangle calcRotatedSize(Rectangle src, int angel) {
//        if (angel >= 90) {
//            if (angel / 90 % 2 == 1) {
//                int temp = src.height;
//                src.height = src.width;
//                src.width = temp;
//            }
//            angel = angel % 90;
//        }
//        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
//        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
//        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
//        double angel_dalta_width = Math.atan((double) src.height / src.width);
//        double angel_dalta_height = Math.atan((double) src.width / src.height);
//        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
//        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
//        int des_width = src.width + len_dalta_width * 2;
//        int des_height = src.height + len_dalta_height * 2;
//        return new java.awt.Rectangle(new Dimension(des_width, des_height));
//    }

}
