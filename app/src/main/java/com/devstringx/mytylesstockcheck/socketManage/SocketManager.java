package com.devstringx.mytylesstockcheck.socketManage;
import static io.socket.client.On.on;

import com.devstringx.mytylesstockcheck.BuildConfig;
import com.devstringx.mytylesstockcheck.common.Common;
import com.google.gson.Gson;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
public class SocketManager {

    private static SocketManager manager;
    public static Socket socket=null;

    public static SocketManager getInstance(){
        if(socket==null && manager==null) {
            manager=new SocketManager();
            try {
                String URL=BuildConfig.BASE_URL.replace("/api/v1/","");
                Common.showLog("SOCKERURL===="+URL);
                socket = IO.socket(URL);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return manager;
    }


    public void connect() {
        socket.connect();
    }

    public void disconnect() {
        socket.disconnect();
    }

    public static boolean isConnected() {
        return socket.connected() ? true : false;
    }

    public void onMessageReceived(MessageReceived received) {
        Common.showLog("SOCKET isConnected==="+isConnected());
        socket.on("Stock_Check", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(received!=null)
                    received.onMessageReceivedListner(args[0].toString());
            }
        }) ;
    }

    public void onPermissionCheck(MessageReceived received) {
        Common.showLog("SOCKET isConnected==="+isConnected());
        socket.on("Permission_Check", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if(received!=null)
                    received.onMessageReceivedListner(args[0].toString());
            }
        }) ;
    }


    public void sendMessage(String msg) {
        socket.emit("message", msg);
    }

  public interface MessageReceived{
        public void onMessageReceivedListner(String response);
  }

}
