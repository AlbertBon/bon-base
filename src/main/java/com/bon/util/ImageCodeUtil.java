package com.bon.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @program: bon基础项目
 * @description: 图形验证码工具类
 * @author: Bon
 * @create: 2018-05-16 11:23
 **/
public class ImageCodeUtil {

    // 图片的宽度。
    private int width = 160;
    // 图片的高度。
    private int height = 40;
    // 验证码字符个数
    private int codeCount = 5;
    // 验证码干扰线数
    private int lineCount = 150;
    // 验证码
    private String code = null;
    // 验证码图片Buffer
    private BufferedImage buffImg=null;

    private static Random random = new Random();

    private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N',  'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z',  '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    public ImageCodeUtil() {
        this.createCode();
    }

    /**
     *
     * @param width 图片宽
     * @param height 图片高
     */
    public ImageCodeUtil(int width, int height) {
        this.width=width;
        this.height=height;
        this.createCode();
    }
    /**
     *
     * @param width 图片宽
     * @param height 图片高
     * @param codeCount 字符个数
     * @param lineCount 干扰线条数
     */
    public ImageCodeUtil(int width, int height, int codeCount, int lineCount) {
        this.width=width;
        this.height=height;
        this.codeCount=codeCount;
        this.lineCount=lineCount;
        this.createCode();
    }

    public void createCode() {
        int x = 0,fontHeight=0,codeY=0;
        int red = 0, green = 0, blue = 0;

        x = width / (codeCount +2);//每个字符的宽度
        fontHeight = height - 2;//字体的高度
        codeY = height - 4;

        // 图像buffer
        buffImg = new BufferedImage(width, height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 生成随机数
        Random random = new Random();

        Color c = getRandColor(200, 250);
        g.setColor(c);// 设置背景色
        g.fillRect(0, 0, width, height);
        // 创建字体
        ImgFontByte imgFont=new ImgFontByte();
        Font font =imgFont.getFont(fontHeight);
        g.setFont(font);

        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs+random.nextInt(width/8);
            int ye = ys+random.nextInt(height/8);
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawLine(xs, ys, xe, ye);
        }

        // randomCode记录随机产生的验证码
        StringBuffer randomCode = new StringBuffer();
        // 随机产生codeCount个字符的验证码。
        for (int i = 0; i < codeCount; i++) {
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            // 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
            g.setColor(getRandColor(0, 200));// 设置线条的颜色
            g.drawString(strRand, (i + 1) * x, codeY);
            // 将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }
        // 将四位数字的验证码保存到Session中。
        code=randomCode.toString();
    }

    private static Color getRandColor(int fc, int bc) {
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    public void write(String path) throws IOException {
        OutputStream sos = new FileOutputStream(path);
        this.write(sos);
    }

    public void write(OutputStream sos) throws IOException {
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }
    public BufferedImage getBuffImg() {
        return buffImg;
    }

    public String getCode() {
        return code;
    }

    /**
     * ttf字体文件
     * @author dsna
     *
     */
    public class ImgFontByte {
        public Font getFont(int fontHeight){
            try {
                Font baseFont = Font.createFont(Font.TRUETYPE_FONT, new ByteArrayInputStream(hex2byte(getFontByteStr())));
                return baseFont.deriveFont(Font.PLAIN, fontHeight);
            } catch (Exception e) {
                return new Font("Arial",Font.PLAIN, fontHeight);
            }
        }

        private  byte[] hex2byte(String str) {
            if (str == null)
                return null;
            str = str.trim();
            int len = str.length();
            if (len == 0 || len % 2 == 1)
                return null;

            byte[] b = new byte[len / 2];
            try {
                for (int i = 0; i < str.length(); i += 2) {
                    b[i / 2] = (byte) Integer
                            .decode("0x" + str.substring(i, i + 2)).intValue();
                }
                return b;
            } catch (Exception e) {
                return null;
            }
        }
        /**
         * ttf字体文件的十六进制字符串
         * @return
         */
        private String getFontByteStr(){
            return PropertyUtil.getProperty("fontByte");
        }
    }

}
