package edu.hnu.conference_system.socket;


import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import edu.hnu.conference_system.service.RoomService;
import edu.hnu.conference_system.service.UserInfoService;
import edu.hnu.conference_system.utils.PcmCovWavUtil;
import edu.hnu.conference_system.vo.FileShowVo;
import io.swagger.v3.core.util.Json;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
//import xunfei_api.IatModelZhMain;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.apache.pdfbox.io.IOUtils.toByteArray;


@Component
@Slf4j
@ServerEndpoint("/meeting/audio/{userId}")
public class WebSocketAudioServer {
    private static ConcurrentHashMap<Integer, WebSocketAudioServer> meetingUsers = new ConcurrentHashMap<>();
    private static CopyOnWriteArraySet<WebSocketAudioServer> webSocketSet = new CopyOnWriteArraySet<>();
    /**
     * 并发容器 存储 字节临时缓冲区
     */
    private static ConcurrentHashMap<Integer, ByteArrayOutputStream> byteArrayOutputStreamConcurrentHashMap = new ConcurrentHashMap<>();


    private Session session;
    private Integer userId;
    private Long meetingId;
    private String wavPath;

    private int voiceTime;

    private static String audioPath;
    private static UserInfoService userInfoService;
    private static RoomService roomService;

    /*@Value("${files-upload-url.audios}")
    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }*/


    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        WebSocketAudioServer.userInfoService = userInfoService;

    }

    @Autowired
    public void setRoomService(RoomService roomService) {
        WebSocketAudioServer.roomService = roomService;
        WebSocketAudioServer.audioPath= roomService.getAudioPath();
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId) throws IOException {
        this.voiceTime = 0;

        this.session = session;
        meetingUsers.put(userId, this);
        webSocketSet.add(this);
        Long meetingId = userInfoService.getMeetingIdByUserId(userId);
        this.meetingId = meetingId;
        this.userId = userId;
        String dirPath = audioPath+"/"+meetingId;
        //System.out.println(dirPath);
        File dir = new File(dirPath);
        if(!dir.exists()){
            if(!dir.mkdirs()){
                System.out.println("创建文件失败!");
            }
            /*else{
                System.out.println("创建文件成功");
            }*/
        }


        this.wavPath = dirPath+"/" +userId + ".wav";
        File file = new File(wavPath);
        file.createNewFile();
        broadcast2Others("SomeOneIn");
        System.out.println("用户id:" + userId + "  加入会议:" + meetingId );
    }

    @OnClose
    public void onClose() throws IOException {
        this.voiceTime = 0;
        go2Disk();
        audioMake();
        meetingUsers.remove(this.userId);
        webSocketSet.remove(this);
        byteArrayOutputStreamConcurrentHashMap.remove(userId);
        //broadcast2Others("SomeOneOut");
        System.out.println("用户id:" + this.userId + "  离开会议:" + this.meetingId );
    }

    @OnMessage
    public void onMessage(@PathParam(value = "userId") Integer userId, String message) throws IOException {
        //字符串分发
        broadcast2All(message);
    }

    @OnMessage(maxMessageSize = 5242880)
    public void onMessage(@PathParam(value = "userId") Integer userId, ByteBuffer message) throws Exception {
        //音频分发
        broadcast2All(message);
        //System.out.println(message.array().length);

        this.appendBuffer(message);
       // System.out.println(byteArrayOutputStreamConcurrentHashMap.get(userId).size());

        //内存占用过大,转入外存,清空内存
        //大于61071360时转入,约为58mb,相当于10min语音
        if(byteArrayOutputStreamConcurrentHashMap.get(userId).size()>61071360) {
            //System.out.println("过大");
            go2Disk();
            ByteArrayOutputStream newByteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStreamConcurrentHashMap.put(this.userId, newByteArrayOutputStream);

        }

        //临时存储,用于语音转文字
        /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bytes = message.array();
        try {
            byteArrayOutputStream.write(bytes);
        } catch (IOException e) {
            try {
                byteArrayOutputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return;
        }
        ByteArrayOutputStream pcmStream = new ByteArrayOutputStream();
        PcmCovWavUtil.convertWaveFile(byteArrayOutputStream, pcmStream);

        Audio2Text(pcmStream);*/



    }
    /**
     * 并发容器 存储 字节临时缓冲区
     *
     * @param message ByteBuffer
     */
    private void appendBuffer(ByteBuffer message) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = byteArrayOutputStreamConcurrentHashMap.get(userId);
        if (ObjectUtil.isEmpty(byteArrayOutputStream)) {
            byteArrayOutputStream = new ByteArrayOutputStream();
        }

        byte[] bytes = message.array();
        try {
            byteArrayOutputStream.write(bytes);
        } catch (IOException e) {
            try {
                byteArrayOutputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return;
        }

        byteArrayOutputStreamConcurrentHashMap.put(userId, byteArrayOutputStream);
    }

    /**
     * 将内存中音频转入外存,得到不可播放的wav
     */
    private void go2Disk() {
        try {
            //音频内存转外存
            ByteArrayOutputStream stream = byteArrayOutputStreamConcurrentHashMap.get(userId);
            if (stream != null) {
                File file0 = new File(wavPath);
                if (!file0.exists()){
                    file0.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(new File(wavPath),true);
                IoUtil.copy(new ByteArrayInputStream(stream.toByteArray()), fileOutputStream);

                fileOutputStream.flush();
                fileOutputStream.close();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 将外存中的wav读入并修改为可以播放的文件
     * @throws IOException
     */
    private void audioMake() throws IOException {
        InputStream in = new FileInputStream(this.wavPath);
        byte[] data = toByteArray(in);
        in.close();
        ByteArrayOutputStream read = new ByteArrayOutputStream();
        read.write(data);

        ByteArrayOutputStream tempStream = new ByteArrayOutputStream();
        PcmCovWavUtil.convertWaveFile(read, tempStream);

        FileOutputStream fileOutputStream = new FileOutputStream(new File(wavPath));
        IoUtil.copy(new ByteArrayInputStream(tempStream.toByteArray()), fileOutputStream);

        fileOutputStream.flush();
        fileOutputStream.close();

    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }


    /**
     * 广播给所有用户
     *
     * @param msg 消息
     */
    protected <T> void broadcast2All(Long meetingId,T msg) throws IOException {
        for (WebSocketAudioServer client : webSocketSet){
            if(Objects.equals(client.meetingId, meetingId)){
                client.call(msg);
            }
        }
    }

    /**
     * 广播给所有用户
     *
     * @param msg 消息
     */
    protected <T> void broadcast2All(T msg) throws IOException {
        for (WebSocketAudioServer client : webSocketSet){
            if(Objects.equals(client.meetingId, this.meetingId)){
                client.call(msg);
            }
        }
    }

    /**
     * 广播给除了自己外的用户
     *
     * @param msg 消息
     */
    protected <T> void broadcast2Others(Long meetingId,T msg) throws IOException {
        for (WebSocketAudioServer client : webSocketSet){
            if(Objects.equals(client.meetingId, meetingId)){
                if(!Objects.equals(client.userId, this.userId)){
                    client.call(msg);
                }
            }
        }
    }
    /**
     * 广播给除了自己外的用户
     *
     * @param msg 消息
     */
    protected <T> void broadcast2Others(T msg) throws IOException {
        for (WebSocketAudioServer client : webSocketSet){
            if(Objects.equals(client.meetingId, this.meetingId)){
                if(!Objects.equals(client.userId, this.userId)){
                    client.call(msg);
                }
            }
        }
    }

    /**
     * 同步方式向客户端发送字符串
     *
     * @param msg 参数类型为String或ByteBuffer
     */
    protected <T> void call(T msg) throws IOException {
        try {
            synchronized (this) {
                RemoteEndpoint.Basic remote = this.getSession().getBasicRemote();
                if (msg instanceof String) {
                    remote.sendText((String) msg);
                } else if (msg instanceof ByteBuffer) {
                    remote.sendBinary((ByteBuffer) msg);
                }else{
                    remote.sendObject(msg);
                }

            }
        } catch (IOException e) {
            try {
                this.getSession().close();
            } catch (IOException ignored) {
            }
            onClose();
        } catch (EncodeException e) {
            throw new RuntimeException(e);
        }
    }

    public void pushFile2All(Long meetingId,FileShowVo fileShowVo) throws IOException {
        JSON jsonObject = (JSON) JSON.toJSON(fileShowVo);

        broadcast2All(meetingId,jsonObject.toString());
    }

    public void tellAllFileUploaded(Long meetingId) throws IOException {
        broadcast2All(meetingId,"NEW_FILE");
    }

    public void kickOneOut(Integer id){
        for (WebSocketAudioServer client : webSocketSet){
            if(Objects.equals(client.userId, id)){
                try {
                    client.broadcast2Others(client.meetingId,"ONE_LEAVE");
                    client.session.getBasicRemote().sendText("END");
                    client.session.close();
                    //client.onClose();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void oneIn(Long meetingId, Integer id) {
        for (WebSocketAudioServer client : webSocketSet){
            if(Objects.equals(client.userId, id)){
                try {
                    client.broadcast2Others(meetingId,"IN");
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void oneLeave(Long meetingId,Integer id) {
        for (WebSocketAudioServer client : webSocketSet){
            if(Objects.equals(client.userId, id)){
                try {
                    client.broadcast2Others(meetingId,"ONE_LEAVE");
                    client.session.getBasicRemote().sendText("END");
                    client.session.close();
                    //client.onClose();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void roomEnd(Integer id) {
        for (WebSocketAudioServer client : webSocketSet){
            if(Objects.equals(client.userId, id)){
                try {
                    client.session.getBasicRemote().sendText("END");
                    client.session.close();
                    //client.onClose();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    protected Session getSession() {
        return this.session;
    }

    /*private void Audio2Text(ByteArrayOutputStream outputStream) throws Exception {
        IatModelZhMain iatModelZhMain = new IatModelZhMain();
        System.out.println(outputStream.toString().length());
        if(this.voiceTime ==0) {
            iatModelZhMain.VoiceToText(reSampling(outputStream.toByteArray()));
        }
        else{
            iatModelZhMain.nextVoice(reSampling(outputStream.toByteArray()));
        }
        this.voiceTime++;
    }*/

    private ByteArrayOutputStream reSampling(byte[] data) throws IOException, UnsupportedAudioFileException {

        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));

        AudioFormat srcFormat = audioIn.getFormat();
        int targetSampleRate = 16000;

        //System.out.println(srcFormat.getSampleRate());

        AudioFormat dstFormat = new AudioFormat(srcFormat.getEncoding(),
                targetSampleRate,
                srcFormat.getSampleSizeInBits(),
                srcFormat.getChannels(),
                srcFormat.getFrameSize(),
                srcFormat.getFrameRate(),
                srcFormat.isBigEndian());


        AudioInputStream convertedIn = AudioSystem.getAudioInputStream(dstFormat, audioIn);

        int numReads = -1;

        int BUFF_SIZE = targetSampleRate/2;

        byte [] buff = new byte[BUFF_SIZE];

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while ((numReads = convertedIn.read(buff)) !=-1)
        {
            System.out.println("读入字节数:"+ numReads);
            outputStream.write(buff);
        }
        return outputStream;
    }



}
