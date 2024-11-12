package edu.hnu.conference_system.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

public class Base64Utils {

    /**
     * 图片转Base64字符串
     * @param image_path
     * @return
     */
    public static String encode(String image_path) {
        String bs64 = null;
        FileInputStream in =null;
        byte[] buffer = null;
        Base64.Encoder encoder = Base64.getEncoder();
        try{
            in = new FileInputStream(image_path);
            buffer = new byte[in.available()];
            in.read(buffer);
            bs64 = encoder.encodeToString(buffer);
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try {
                in.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return bs64;
    }

    /**
     * Base64字符串转图片
     * @param input image_path
     * @return
     */
    public static void decode(String input,String image_path) throws IOException {
        if(input.length() == 0){
            return;
        }
        Base64.Decoder decoder = Base64.getDecoder();
        OutputStream output = null;
        try {
            output = new FileOutputStream(image_path);

            byte[] decodedBytes = decoder.decode(input);
            for(int i = 0; i < decodedBytes.length; i++){
                if(decodedBytes[i] <0){
                    decodedBytes[i] += 256;
                }
            }
            output.write(decodedBytes);
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            output.flush();
            output.close();
        }
    }
}
