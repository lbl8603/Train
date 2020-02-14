package com.wherewego.train.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;

/**
 * 二维码操作类
 * @Author:lubeilin
 * @Date:Created in 15:41 2020/2/8
 * @Modified By:
 */
public class QRCodeUtils {
    public static String  readQRCode(BufferedImage image) throws Exception{
        image = ImageUtils.binarization(image);//二值化处理
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
        HashMap properties = new HashMap();
        properties.put(EncodeHintType.CHARACTER_SET, "utf-8");//字符集
        MultiFormatReader formatReader = new MultiFormatReader();
        Result result = formatReader.decode(binaryBitmap, properties);
        String originalURL = result.getText();
        return originalURL;
    }
    public static BufferedImage createQRCodeImage(String content, int width, int height, String format) throws Exception{
        byte[] bytes = createQRCodeBytes(content,width,height,format);
        return ImageIO.read(new ByteArrayInputStream(bytes));
    }
    public static byte[] createQRCodeBytes(String content, int width, int height, String format) throws Exception{
        HashMap<EncodeHintType, Object> hints = new HashMap<>(4);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 2);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageConfig config = new MatrixToImageConfig(0xFF000000, 0xFFF1F1F1);
//        MatrixToImageWriter.writeToPath(bitMatrix, "JPG", imageFile.toPath(), config);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, format, byteArrayOutputStream,config);
        return byteArrayOutputStream.toByteArray();
    }
}
