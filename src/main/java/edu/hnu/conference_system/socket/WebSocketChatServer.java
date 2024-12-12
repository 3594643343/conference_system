package edu.hnu.conference_system.socket;



import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hnu.conference_system.service.*;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@Slf4j
@ServerEndpoint("/chat/{userId}")
public class WebSocketChatServer {

    /**
     * 消息仓库
     */
    /*private static ConcurrentHashMap<Integer, List<String>> messageBuffer = new ConcurrentHashMap<>();*/

    private static ConcurrentHashMap<Integer, WebSocketChatServer> onlineUsers = new ConcurrentHashMap<>();
    private static CopyOnWriteArraySet<WebSocketChatServer> webSocketSet = new CopyOnWriteArraySet<>();


    private Session session;
    private Integer userId;

    private static UserInfoService userInfoService;
    private static ChatGroupService chatGroupService;
    private static UserAndGroupService userAndGroupService;
    private static UserContactService userContactService;
    private static FriendChatRecordService friendChatRecordService;
    private static GroupChatRecordService groupChatRecordService;
    private static CheckMessageRecordService checkMessageRecordService;


    @Autowired
    private void setUserInfoService(UserInfoService userInfoService) {
        WebSocketChatServer.userInfoService = userInfoService;
    }

    @Autowired
    private void setChatGroupService(ChatGroupService chatGroupService) {
        WebSocketChatServer.chatGroupService = chatGroupService;
    }

    @Autowired
    private void setUserAndGroupService(UserAndGroupService userAndGroupService) {
        WebSocketChatServer.userAndGroupService = userAndGroupService;
    }

    @Autowired
    private void setUserContactService(UserContactService userContactService) {
        WebSocketChatServer.userContactService = userContactService;
    }

    @Autowired
    private void setFriendChatRecordService(FriendChatRecordService friendChatRecordService) {
        WebSocketChatServer.friendChatRecordService = friendChatRecordService;
    }

    @Autowired
    private void setGroupChatRecordService(GroupChatRecordService groupChatRecordService) {
        WebSocketChatServer.groupChatRecordService = groupChatRecordService;
    }

    @Autowired
    private void setCheckMessageRecordService(CheckMessageRecordService checkMessageRecordService) {
        WebSocketChatServer.checkMessageRecordService = checkMessageRecordService;
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("userId") Integer userId) {
        this.session = session;
        this.userId = userId;
        webSocketSet.add(this);
        onlineUsers.put(userId, this);
        System.out.println("用户id: "+userId+" 进入了系统!");
        //检查消息仓库中是否有属于自己的消息,如果有,发送
        //messageBufferSend(userId);
    }

    @OnClose
    public void onClose() {
        userInfoService.exitSystem(userId);
        webSocketSet.remove(this);
        onlineUsers.remove(userId);
        System.out.println("用户id: "+userId+" 离开了系统!");
    }
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 接收用户发送的聊天消息
     * @param userId
     * @param message0
     */
    @OnMessage
    public void onMessage(@PathParam("userId") Integer userId, String message0) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,String> message = mapper.readValue(message0, Map.class);
        String receiverId = message.get("receiverId");
        String isGroup = message.get("isGroup");
        String time = message.get("time");
        String content = message.get("content");
        if(StringUtils.isAnyBlank(receiverId,isGroup,time,content)){
            session.getBasicRemote().sendText("异常: 输入含有空");
            System.out.println("接收信息含空");
            return;
        }

        Integer toWho = Integer.valueOf(receiverId);
        if(isGroup.equals("1")){
            //群发
            send2group(toWho,groupSendJson(toWho,userId,time,content));
            //记录存储
            groupChatRecordService.insertRecord(toWho,this.userId,content,time);
        }
        else{
            //私聊
            send2one(toWho,pairSendJson(userId,time,content));
            //记录储存
            friendChatRecordService.insertRecord(this.userId,toWho,content,time);
        }

    }

    /**
     * 单发消息
     * @param toWho
     * @param sendJson
     * @throws IOException
     */
    private void send2one(Integer toWho, String sendJson) throws IOException {
        if(userContactService.beforeSendCheck(this.userId,toWho)){
            for(WebSocketChatServer webSocketChatServer : webSocketSet){
                if(webSocketChatServer.userId.equals(toWho)){
                    webSocketChatServer.session.getBasicRemote().sendText(sendJson);
                    break;
                }
            }
            //没找到,说明不在线,放入消息仓库
            //go2buffer(toWho,sendJson);
        }
        else{
            //不是好友或删除了好友
            this.session.getBasicRemote().sendText(sendJsonError("不是好友!"));
        }
    }

    /**
     * 群发消息
     * @param toWho
     * @param sendJson
     */
    private void send2group(Integer toWho, String sendJson) {
        if(!userAndGroupService.beforeSendCheck(this.userId,toWho)){
            //不在群中
            try {
                this.session.getBasicRemote().sendText(sendJsonError("不在群聊中!"));
            }catch (Exception e){
                e.printStackTrace();
            }
            return;
        }
        //在群中
        List<Integer> groupMembersId = userAndGroupService.getMembersId(toWho);
        for(Integer groupMemberId : groupMembersId){
            if(groupMemberId.equals(userId)) continue;

            if(onlineUsers.containsKey(groupMemberId)){
                //在线用户中有此人
                try {
                    onlineUsers.get(groupMemberId).session.getBasicRemote().sendText(sendJson);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else{
                //不在线,存入仓库
                //go2buffer(groupMemberId,sendJson);
            }

        }
    }

    /**
     * 发送添加好友验证
     */
    public void sendAddFriendCheckMessage(Integer userId, Integer friendId, String checkWords) {
        //储存记录
        Integer recordId = checkMessageRecordService.initFriendCheck(userId,friendId,checkWords);

        String message = checkSendJson(recordId,userId, LocalDateTime.now().toString(),checkWords);
        for(WebSocketChatServer webSocketChatServer : webSocketSet){
            if(webSocketChatServer.userId.equals(friendId)){
                try {
                    webSocketChatServer.session.getBasicRemote().sendText(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
        }

        //没在线,转存仓库
        //go2buffer(friendId,message);
    }

    /**
     * 发送添加群聊验证
     */
    public void sendAddGroupCheckMessage(Integer userId, Integer groupId, String checkWords) {
        Integer creatorId = chatGroupService.getGroupCreatorId(groupId);
        //储存记录
        Integer recordId = checkMessageRecordService.initGroupCheck(userId,groupId,creatorId,checkWords);

        String message = groupCheckSendJson(recordId,userId,groupId,LocalDateTime.now().toString(),checkWords);
        for(WebSocketChatServer webSocketChatServer : webSocketSet){
            if(webSocketChatServer.userId.equals(creatorId)){
                try {
                    webSocketChatServer.session.getBasicRemote().sendText(message);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
        }

        //没在线,转存仓库
        //go2buffer(creatorId,message);
    }

    /**
     * 构造错误消息JSON code == 0
     * @param message
     * @return
     */
    private String sendJsonError(String message) {
        JSONObject innerJson = new JSONObject();
        innerJson.set("message", message);
        return buildSendJson(0,innerJson);
    }

    /**
     * 构造私聊消息JSON, code == 1
     * @param senderId
     * @param time
     * @param content
     * @return
     */
    private String pairSendJson(Integer senderId,String time,String content) {
        JSONObject innerJson = new JSONObject();
        innerJson.set("senderId", senderId);
        innerJson.set("time", time);
        innerJson.set("content", content);
        return buildSendJson(1,innerJson);
    }

    /**
     * 构造群聊消息JSON, code == 2
     * @param toWho
     * @param userId
     * @param time
     * @param content
     * @return
     */
    private String groupSendJson(Integer toWho, Integer userId, String time, String content) {
        JSONObject innerJson = new JSONObject();
        innerJson.set("groupId", toWho);
        innerJson.set("senderId", userId);
        innerJson.set("time", time);
        innerJson.set("content", content);
        return buildSendJson(2,innerJson);
    }

    /**
     * 构造好友验证消息 code == 3
     * @param senderId
     * @param time
     * @param content
     * @return
     */
    private String checkSendJson(Integer recordId,Integer senderId, String time, String content) {
        JSONObject innerJson = new JSONObject();
        innerJson.set("recordId", recordId);
        innerJson.set("senderId", senderId);
        innerJson.set("time", time);
        innerJson.set("content", content);
        return buildSendJson(3,innerJson);
    }

    /**
     * 构造群聊验证消息 code == 4
     * @param senderId
     * @param groupId
     * @param time
     * @param content
     * @return
     */
    private String groupCheckSendJson(Integer recordId,Integer senderId,Integer groupId ,String time, String content) {
        JSONObject innerJson = new JSONObject();
        innerJson.set("recordId",recordId);
        innerJson.set("senderId", senderId);
        innerJson.set("groupId", groupId);
        innerJson.set("time", time);
        innerJson.set("content", content);
        return buildSendJson(4,innerJson);
    }


    /**
     * 构造JSON
     * @param code
     * @param innerJson
     * @return
     */
    private String buildSendJson(Integer code,JSONObject innerJson){
        JSONObject outerJson = new JSONObject();
        outerJson.set("code", code);
        outerJson.set("data", innerJson);
        return outerJson.toString();
    }

    /**
     * 消息存入消息仓库
     * @param toWho
     * @param sendJson
     */
    /*private void go2buffer(Integer toWho, String sendJson) {
        if( !messageBuffer.containsKey(toWho)){
            List<String> list = new ArrayList<>();
            list.add(sendJson);
            messageBuffer.put(toWho,list);
        }
        else{
            List<String> list = messageBuffer.get(toWho);
            list.add(sendJson);
            messageBuffer.put(toWho,list);
        }
    }*/

    /**
     * 消息仓库中消息分发, 用户登录时调用
     * @param userId
     */
    /*private void messageBufferSend(Integer userId){
        if(messageBuffer.isEmpty()){
            return;
        }
        for(Integer key: messageBuffer.keySet()){
            if(userId.equals(key)){
                List<String> list = messageBuffer.get(key);
                for(String str : list){
                    try {
                        onlineUsers.get(userId).session.getBasicRemote().sendText(str);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                messageBuffer.remove(key);
                break;
            }
        }
    }*/
}
