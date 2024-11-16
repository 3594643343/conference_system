package edu.hnu.conference_system.utils;


import java.io.*;

public class TxtUtils {
    public static String readTxt(String txtPath) throws IOException {
        File file = new File(txtPath);
        if(!file.exists()){
            throw new FileNotFoundException(txtPath);
        }
        String s = "";
        InputStreamReader in = new InputStreamReader(new FileInputStream(file), "UTF-8");
        BufferedReader br = new BufferedReader(in);
        StringBuffer content = new StringBuffer();
        while ((s = br.readLine()) != null) {
            content = content.append(s);
        }
        return content.toString();
    }
}
