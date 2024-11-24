package edu.hnu.conference_system.socket;


import cn.hutool.core.io.IoUtil;
import edu.hnu.conference_system.domain.User;
import edu.hnu.conference_system.holder.UserHolder;
import edu.hnu.conference_system.service.RoomService;
import edu.hnu.conference_system.service.UserInfoService;
import edu.hnu.conference_system.utils.PcmCovWavUtil;
import jakarta.annotation.Resource;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.apache.pdfbox.io.IOUtils.toByteArray;


@Component
@Slf4j
@ServerEndpoint("/meeting/audio/{userId}")
public class WebSocketAudioServer {
    private static ConcurrentHashMap<Long, WebSocketAudioServer> meetingUsers = new ConcurrentHashMap<>();
    private static CopyOnWriteArraySet<WebSocketAudioServer> webSocketSet = new CopyOnWriteArraySet<>();
    /**
     * 并发容器 存储 字节临时缓冲区
     */
    private static ConcurrentHashMap<Long, ByteArrayOutputStream> byteArrayOutputStreamConcurrentHashMap = new ConcurrentHashMap<>();

    private Session session;
    private Long userId;
    private Long meetingId;
    private String wavPath;

    private static UserInfoService userInfoService;
    private static RoomService roomService;

    @Autowired
    public void setUserInfoService(UserInfoService userInfoService) {
        WebSocketAudioServer.userInfoService = userInfoService;
    }

    @Autowired
    public void setRoomService(RoomService roomService) {
        WebSocketAudioServer.roomService = roomService;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Long userId) throws IOException {
        this.session = session;
        meetingUsers.put(userId, this);
        webSocketSet.add(this);
        Long meetingId = userInfoService.getMeetingIdByUserId(userId);
        this.meetingId = meetingId;
        this.userId = userId;
        String filePath = "C:/Users/lenovo/Desktop/ToWavTest" + File.separator;
        /*if(!new File(filePath).mkdirs()){
            System.out.println("创建文件夹鼠标!");
        }*/

        this.wavPath = filePath +userId + ".wav";
        File file = new File(wavPath);
        file.createNewFile();
        System.out.println("用户id:" + userId + "  加入会议:" + meetingId );
    }

    @OnClose
    public void onClose() throws IOException {
        go2Disk();
        audioMake();
        meetingUsers.remove(this.userId);
        webSocketSet.remove(this);
        byteArrayOutputStreamConcurrentHashMap.remove(userId);
        System.out.println("用户id:" + this.userId + "  离开会议:" + this.meetingId );
    }

    @OnMessage
    public void onMessage(@PathParam(value = "userId") Long userId, String message) throws IOException {
        //字符串分发
        broadcast2All(message);
    }

    @OnMessage(maxMessageSize = 5242880)
    public void onMessage(@PathParam(value = "userId") Long userId, ByteBuffer message) throws IOException {
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
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStreamConcurrentHashMap.put(this.userId, byteArrayOutputStream);

        }


    }
    /**
     * 并发容器 存储 字节临时缓冲区
     *
     * @param message ByteBuffer
     */
    private void appendBuffer(ByteBuffer message) throws IOException {
         ByteArrayOutputStream byteArrayOutputStream = byteArrayOutputStreamConcurrentHashMap.get(this.userId);
         if (byteArrayOutputStream == null) {
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
        byteArrayOutputStreamConcurrentHashMap.put(this.userId, byteArrayOutputStream);
    }

    /**
     * 将内存中音频转入外存,得到不可播放的wav
     */
    private void go2Disk() {
        try {
            //音频内存转外存
            ByteArrayOutputStream stream = byteArrayOutputStreamConcurrentHashMap.get(userId);
            if (stream != null) {
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

        String filePath = "C:/Users/lenovo/Desktop/ToWavTest" + File.separator;
        new File(filePath).mkdirs();
        FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath +userId + "_rel.wav"));
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
                }

            }
        } catch (IOException e) {
            try {
                this.getSession().close();
            } catch (IOException ignored) {
            }
            onClose();
        }
    }

    protected Session getSession() {
        return this.session;
    }
}
