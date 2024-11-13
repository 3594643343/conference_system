package edu.hnu.conference_system;

import edu.hnu.conference_system.utils.FileToPicUtils;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

@SpringBootTest
class ConferenceSystemApplicationTests {


	/*@Test
	void pdfToPic() throws Exception {
		String source= "C:\\Users\\lenovo\\Desktop\\系统详细部署文档.pdf";
		String desFilePath="C:\\Users\\lenovo\\Desktop\\PDF转换加";
		String desFileName="testpdf";
		String imageType="png";
		FileToPicUtils.pdfToImage(source, desFilePath, desFileName, imageType);
		Pair<Boolean, Object> pair = FileToPicUtils.pdfToImage(source, desFilePath, desFileName, imageType);
		System.out.println("PDF形式的发票转化为图片结果：" + pair.getLeft());
		if (!pair.getLeft()) {
			System.out.println("" + pair.getRight());
		} else {
			List<String> fileList = (List<String>) pair.getRight();
			System.out.println("转化的文件的内容：");
			fileList.forEach(System.out::println);
		}

	}*/

	/*@Test
	void pdft() throws Exception {
		List<String> l = FileToPicUtils.pdfToPic("C:\\Users\\lenovo\\Desktop\\bbb.pdf");
		for(String s : l){
			System.out.println(s);
		}
	}*/
	/*@Test
	void pptt() throws Exception {
		FileToPicUtils.pptToPic("C:\\Users\\lenovo\\Desktop\\人工智能和机器学习驱动的计算机网络3.pptx");
	}*/




}
