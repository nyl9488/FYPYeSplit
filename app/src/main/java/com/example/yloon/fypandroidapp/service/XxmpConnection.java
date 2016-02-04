package com.example.yloon.fypandroidapp.service;

/**
 * Created by YLoon on 31/10/2015.
 */

import android.os.StrictMode;

import com.example.yloon.fypandroidapp.server.config;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XxmpConnection {

    private static XMPPConnection con = null;
    private static void openConnection() {
        try {
            // url、端口，也可以设置连接的服务器名字，地址，端口，用户。
            ConnectionConfiguration connConfig = new ConnectionConfiguration(config.domain_name, 5222);

//   configuration设置
            // connConfig.setSASLAuthenticationEnabled(false);
            //connConfig.setCompressionEnabled(true);
            //connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
            connConfig.setSelfSignedCertificateEnabled(true);
            connConfig.setSASLAuthenticationEnabled(false);
            connConfig.setDebuggerEnabled(false);
            connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.required);
            con = new XMPPConnection(connConfig);

            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            con.connect();


            /** 开启读写线程，并加入到管理类中*/
            //ClientSendThread cst=new ClientSendThread(connection);
            //cst.start();
            //ManageClientThread.addClientSendThread(a, cst);


        } catch (XMPPException xe) {
            xe.printStackTrace();
        }
    }
    public static XMPPConnection getConnection() {
        if (con == null) {
            openConnection();
        }
        return con;
    }
    public static void closeConnection() {
        con.disconnect();
        con = null;
    }
}