package edu.hnu.conference_system.utils;


import com.aspose.slides.ISlide;
import com.aspose.slides.License;
import com.aspose.slides.Presentation;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xwpf.usermodel.XWPFDocument;



public class FileToPicUtils {

    /**
     * word转图片
     */
    /*public static void wordToImage(String wordPath) throws IOException {
        XWPFDocument document = new XWPFDocument(new FileInputStream(wordPath));

        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(wordPath));
    }*/

    /**
     * pdf转图片
     * @throws Exception
     */
    public static int pdfToPic(String pdfPath)throws Exception {
        String source= pdfPath;
        File file = new File(source);
        if (!file.exists()) {
            throw new Exception("文件不存在!");
        }
        String desFilePath= file.getParent()+File.separator+getFileNameNoEx(file.getName())+"_PIC";

        String desFileName="";


        String imageType="jpg";



        return  pdfToImage(source,desFilePath,desFileName,imageType);
    }

    /**
     * @param source      原文件
     * @param desFilePath 生成图片的路径
     * @param desFileName 生成图片的名称（多页文档时会变成：从1开始的数字）
     * @param imageType   图片类型
     * @return
     */
    private static int pdfToImage(String source, String desFilePath, String desFileName, String imageType) throws Exception {
        //通过给定的源路径名字符串创建一个File实例
        File file = new File(source);
        if (!file.exists()) {
            throw new Exception("文件不存在!");
        }
        //目录不存在则创建目录
        File destination = new File(desFilePath);
        if (!destination.exists()) {
            boolean flag = destination.mkdirs();
            System.out.println("创建文件夹结果：" + flag);
        }
        PDDocument doc = null;
        int pageCount = 0;
        try {
            //加载PDF文件
            doc = PDDocument.load(file);
            PDFRenderer renderer = new PDFRenderer(doc);
            //获取PDF文档的页数
            pageCount = doc.getNumberOfPages();
            System.out.println("文档一共" + pageCount + "页");
            List<Object> fileList = new ArrayList<>();
            for (int i = 0; i < pageCount; i++) {
                //只有一页的时候文件名为传入的文件名，大于一页的文件名为：文件名_自增加数字(从1开始)
                String realFileName = String.valueOf(i + 1);
                //每一页通过分辨率和颜色值进行转化
                BufferedImage bufferedImage = renderer.renderImageWithDPI(i, 96 * 2, ImageType.RGB);
                String filePath = desFilePath + File.separator + realFileName + "." + imageType;
                //写入文件
                ImageIO.write(bufferedImage, imageType, new File(filePath));
                //文件名存入list
                fileList.add(filePath);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("文件转化异常!");
        } finally {
            try {
                if (doc != null) {
                    doc.close();
                }
            } catch (IOException e) {
                System.out.println("关闭文档失败");
                e.printStackTrace();
            }
        }
        return pageCount;
    }

    private static InputStream license;
    /**
     * 获取license
     *
     * @return
     */
    public static boolean getLicense() {

        boolean result = false;
        license = FileToPicUtils.class.getClassLoader().getResourceAsStream("license.xml");
        if (license != null) {
            License aposeLic = new License();
            aposeLic.setLicense(license);
            result = true;
        }
        return result;
    }

    /**
     * 转Image
     *
     * @return
     */
    public static int pptToPic(String pptPath) throws Exception {
        // 验证License
        if (!getLicense()) {
            throw new Exception("license验证失败!!");
        }
        String fileName =pptPath;
        File file = new File(fileName);
        if (!file.exists()) {
            throw new Exception("文件不存在!");
        }
        String filePath = file.getParent()+File.separator;
        String dest = filePath + getFileNameNoEx(file.getName())+"_PIC";
        File destPath = new File(dest);
        if (!destPath.exists()) {
            destPath.mkdir();
        }
        int pageNum = 0;
        try {
            FileInputStream fileInput = new FileInputStream(fileName);
            Presentation pres = new Presentation(fileInput);
            pageNum = pres.getSlides().size();
            int i;
            for (i = 0; i < pres.getSlides().size(); i++) {
                ISlide slide = pres.getSlides().get_Item(i);
                int height = (int)(pres.getSlideSize().getSize().getHeight()-150);
                int width = (int)(pres.getSlideSize().getSize().getWidth()-150);
                BufferedImage image = slide.getThumbnail(new Dimension(width, height));
                //每一页输出一张图片
                File outImage = new File(dest+File.separator + (i+1) + ".jpg");
                ImageIO.write(image, "jpg", outImage);
            }
        } catch (Exception e) {
            throw e;
        }
        return pageNum;
    }
    /**
     * 获取文件名，去除扩展名的
     *
     * @param filename
     * @return
     */
    private static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

}
