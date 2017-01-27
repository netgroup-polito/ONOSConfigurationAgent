/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.frogsssa.service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.ws.rs.PathParam;

/**
 *
 * @author lara
 */
/*@javax.websocket.server.ServerEndpoint("/comm/{id}")
public class ServerEndpoint {
    
    private Session session;
    private Long idClient;
    
    @OnOpen
    public void onOpen(Session s, @PathParam("id") Long id){
        System.out.println("Opening Session " + s.getId());
        this.session = s;
        this.idClient = id;
        ConnectionModule.addServerEndpoint(this, id);
    }

    @OnMessage
    public String onMessage(String message) {
        ConnectionModule.messageReceived(idClient, message);
        return "ritorno onMessage";
    }
    
    public void sendMessage(String message){
        this.session.getAsyncRemote().sendText(message);
    }
    
    @OnClose
    public void onClose(){
        System.out.println("Closing session " + session.getId());
        ConnectionModule.deleteServerEndpoint(idClient);
    }
}*/
