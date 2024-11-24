package edu.hnu.conference_system.utils;

import java.io.*;

/**
 * @author : YZD
 * @date : 2021-7-16 14:40
 */
public class PcmCovWavUtil {
    //录音的采样频率
    //private static int audioRate = 16000;
    private static int audioRate = 48000;
    //录音的声道，单声道
    private static int audioChannel = 1;
    //量化的深度
    private static int audioFormat = 16;

    //pcm 文件转 wav 文件
    public static void convertWaveFile(String inFileName, String outFileName) {

        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = audioRate;
        int channels = audioChannel;
        long byteRate = 16 * audioRate * channels / 8;
        if (audioFormat == 16) {
            byteRate = 16 * audioRate * channels / 8;
        } else if (audioFormat == 8) {
            byteRate = 8 * audioRate * channels / 8;
        }

        byte[] data = new byte[1024];
        try {
            in = new FileInputStream(inFileName);
            out = new FileOutputStream(new File(outFileName));
            totalAudioLen = in.getChannel().size();
            //由于不包括前面的8个字节RIFF和WAV
            totalDataLen = totalAudioLen + 36;
            addWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);
            while (in.read(data) != -1) {
                out.write(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert in != null;
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                assert out != null;
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //pcm 文件转 wav 文件
    public static void convertWaveFile(ByteArrayOutputStream byteArrayOutputStream, String outFileName) {

        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = audioRate;
        int channels = audioChannel;
        long byteRate = 16 * audioRate * channels / 8;
        if (audioFormat == 16) {
            byteRate = 16 * audioRate * channels / 8;
        } else if (audioFormat == 8) {
            byteRate = 8 * audioRate * channels / 8;
        }

        try {

            out = new FileOutputStream(new File(outFileName));
            totalAudioLen = byteArrayOutputStream.size();
            //由于不包括前面的8个字节RIFF和WAV
            totalDataLen = totalAudioLen + 36;
            addWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);
            out.write(byteArrayOutputStream.toByteArray());

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * pcm 流 转 wav 流
     *
     * @param byteArrayOutputStream
     * @param outputStream
     */
    public static void convertWaveFile(ByteArrayOutputStream byteArrayOutputStream, ByteArrayOutputStream outputStream) {

        long totalAudioLen = 0;
        long totalDataLen = totalAudioLen + 36;
        long longSampleRate = audioRate;
        int channels = audioChannel;
        long byteRate = 16 * audioRate * channels / 8;
        if (audioFormat == 16) {
            byteRate = 16 * audioRate * channels / 8;
        } else if (audioFormat == 8) {
            byteRate = 8 * audioRate * channels / 8;
        }

        try {

            totalAudioLen = byteArrayOutputStream.size();
            //由于不包括前面的8个字节RIFF和WAV
            totalDataLen = totalAudioLen + 36;
            addWaveFileHeader(outputStream, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);
            outputStream.write(byteArrayOutputStream.toByteArray());

        } catch (Exception e) {
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    //添加Wav头部信息
    private static void addWaveFileHeader(OutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate,
                                          int channels, long byteRate) throws IOException {
        byte[] header = new byte[44];
        // RIFF 头表示
        header[0] = 'R';
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        //数据大小，数据大小，真正大小是添加了8bit
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        //wave格式
        header[8] = 'W';//WAVE
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        //fmt Chunk
        header[12] = 'f'; // 'fmt '
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        //数据大小
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        //编码方式 10H为PCM编码格式
        header[20] = 1; // format = 1
        header[21] = 0;
        //通道数
        header[22] = (byte) channels;
        header[23] = 0;
        //采样率，每个通道的播放速度
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        //音频数据传送速率,采样率*通道数*采样深度/8
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        int tongdaowei = 1;
        if (audioChannel == 1) {
            tongdaowei = 1;
        } else {
            tongdaowei = 2;
        }
        // 确定系统一次要处理多少个这样字节的数据，确定缓冲区，通道数*采样位数
        if (audioFormat == 16) {
            header[32] = (byte) (tongdaowei * 16 / 8);
        } else if (audioFormat == 8) {
            header[32] = (byte) (tongdaowei * 8 / 8);
        }
        // header[32] = (byte) (1 * 16 / 8);
        header[33] = 0;
        //每个样本的数据位数
        if (audioFormat == 16) {
            header[34] = 16;
        } else if (audioFormat == 8) {
            header[34] = 8;
        }

        header[35] = 0;
        //Data chunk
        header[36] = 'd';//data
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
        out.write(header, 0, 44);
    }

}
