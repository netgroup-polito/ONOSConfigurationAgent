/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.frogsssa.service;

import com.google.gson.Gson;
import com.mycompany.frogsssa.Resources;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseFeature;
import org.glassfish.jersey.server.ResourceConfig;
import com.mycompany.frogsssa.testDD;
import java.util.Arrays;


/**
 *
 * @author lara
 */
@Stateless
@Path("ConnectionModule")
public class ConnectionModule extends AbstractFacade<Resources> {

    @PersistenceContext(unitName = "com.mycompany_frogsssa_war_1.0-SNAPSHOTPU")
    private EntityManager em;
    private static HashMap<Long, Resources> res = new HashMap<Long, Resources>();
    private static HashMap<Long, EventOutput> SSEClients = new HashMap<>();
    private static HashMap<Long, testDD> DDClients = new HashMap<>();
    private static sseResource conn = new sseResource();
    
    public ConnectionModule() {
        super(Resources.class);
//        final ResourceConfig config = new ResourceConfig();
//        config.register(SseFeature.class);
    }
    
    public static void addEndpoint(EventOutput se, Long id){
        if(!SSEClients.containsKey(id)){
            SSEClients.put(id, se);
        }
    }
    
    public static void deleteEndpoint(Long id){
        if(SSEClients.containsKey(id))
            SSEClients.remove(id);
    }
    
    public static EventOutput getEndpoint(Long id){
        return SSEClients.get(id);
    }

//    public static void messageReceived(Long id, String m){
//        System.out.println("Message received from " + id + " : " + m);
//    }
    
    private Resources findR(Long id){
        if(res.containsKey(id))
            return res.get(id);
        return null;
    }
    
    @Path("events/{id}")
    public static class sseResource{
        final EventOutput evOut = new EventOutput();
        
        private static Resources findR(Long id){
        for(int i = 0; i < res.size(); i++)
            if(id.longValue()==res.get(i).getId().longValue())
                return res.get(i);
        return null;
    }
        
        @GET
        @Produces(SseFeature.SERVER_SENT_EVENTS)
        public EventOutput getServerSentEvents(@PathParam("id") final Long id){
            new Thread(new Runnable() {
                public void run() {
                    try{
                        for(int i=0;i<10;i++){
                            final OutboundEvent.Builder builder = new OutboundEvent.Builder();
                            builder.name("message-to-client");
                            builder.data(String.class, "Hello world " + i);
                            final OutboundEvent event = builder.build();
                            evOut.write(event);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ConnectionModule.class.getName()).log(Level.SEVERE, null, ex);
                        throw new RuntimeException("Exception in write " + ex);
                    }
                }
            }).start();
            ConnectionModule.addEndpoint(evOut, id);
            //writeEvents(id);
            sendMessage(id, "messaggio iniziale");
            return evOut;
        }
        
        
        public static void writeEvents(final Long id){
            new Thread(new Runnable() {
                public void run() {
                    try{
                        EventOutput e = ConnectionModule.getEndpoint(id);
                        for(int i=0;i<10;i++){
                            final OutboundEvent.Builder builder = new OutboundEvent.Builder();
                            builder.name("message-to-client");
                            builder.data(String.class, "Events " + i);
                            final OutboundEvent event = builder.build();
                            e.write(event);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ConnectionModule.class.getName()).log(Level.SEVERE, null, ex);
                        throw new RuntimeException("Exception in write " + ex);
                    }
                }
            }).start();
        }
        
        public static void sendMessage(final Long id, final String message){
                final EventOutput e = ConnectionModule.getEndpoint(id);
                if(e!=null){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final OutboundEvent.Builder builder = new OutboundEvent.Builder();
                                builder.name("message-to-client");
                                builder.data(String.class, message);
                                final OutboundEvent event = builder.build();
                                e.write(event);
                            } catch (IOException ex) {
                                Logger.getLogger(ConnectionModule.class.getName()).log(Level.SEVERE, null, ex);
                                System.err.println("Error while sending the message " + message +  "to the client " + id);
                                ConnectionModule.deleteEndpoint(id);
                                new RuntimeException("Error while sending the message " + message +  "to the client " + id);
                            }
                        }
                    }).start();
                }
            }
        
    }
    
    private static boolean SendData(Long id, String message){
        final Long ident = id;
        final String m = message;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
            try {
                final OutboundEvent.Builder builder = new OutboundEvent.Builder();
                builder.name("sse sever - client");
                builder.data(String.class, m);
                final OutboundEvent event = builder.build();
                if(SSEClients.containsKey(ident))
                {
                    EventOutput e = SSEClients.get(ident);
                    if(e!=null)
                        e.write(event);
                }
            } catch (IOException ex) {
                Logger.getLogger(ConnectionModule.class.getName()).log(Level.SEVERE, null, ex);
            }            
            }
        });
        t.start();
//        if(SEClients.containsKey(id))
//            SEClients.get(id).sendMessage(message);
        if (t.isInterrupted())
            return false;
        return true;
    }
    
    @POST
    @Path("{id}/dataModel")
    @Consumes(MediaType.TEXT_PLAIN)
    //@Produces(MediaType.TEXT_PLAIN)
    public void DataModel(@PathParam("id") Long id, String DM){
        String result = DM;
        //from xml to yang?
        Resources r = findR(id); 
        if(r!=null)
            r.setDataModel(result);
        //return result;
    }
    
    @GET
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    public String create() {
        Resources entity = new Resources();
        Long id = (new Random()).nextLong();
        entity.setId(id);
        //create resources entrance
        res.put(id, entity);
        //SSE
        //DDClient
        if(!DDClients.containsKey(id)){
            testDD c = new testDD("tcp://127.0.0.1:5555", "/home/lara/GIT/DoubleDecker/keys/a-keys.json", id.toString(), "connMod");
            DDClients.put(id, c);
            //public the id in the "all topic"
            c.publish("all", id.toString());
        }
        return entity.getId().toString();
    }
    
    public static void someConfiguration(String id, String msg){
        if(SSEClients.containsKey(Long.parseLong(id))){
            System.out.println("Traduzione corretta da DDClient a SSEClient per " + id);
            SendData(Long.parseLong(id), msg);
        }
    }
    
    
    @POST
    @Path("{id}/change")
    @Consumes(MediaType.TEXT_PLAIN)
    public void somethingChanged(@PathParam("id") Long id, String m){
        if(DDClients.containsKey(id)){
            testDD c = DDClients.get(id);
            c.publish(id.toString(), m);
        }
    }
    
    @Override
    public void create(Resources entity){
        super.create(entity);
    }

    /*@PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Resources entity) {
        super.edit(entity);
    }*/
    
    @POST
    @Path("{id}/{x}/DM")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String Correspondence(@PathParam("id") final Long id, @PathParam("x") final String x, String c){
       Resources r = findR(id);
       if(r==null)
           Boolean.toString(false);
       boolean result = r.setCorrespondence(x,c);
       //sseResource.sendMessage(id, "Modified DM of " + id);
       new Thread(new Runnable() {
           @Override
           public void run() {
               SendData(id, "corrispondenza per " + x + " settata");
           }
       }).start();
       return Boolean.toString(result);
    }
    
    @POST
    @Path("{id}/{x}/Value")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String Value(@PathParam("id") Long id, @PathParam("x") String x, String o){
       Resources r = findR(id);
       if(r==null)
           return Boolean.toString(false);
       //from string to object
       return Boolean.toString(r.setValue(x,o));
    }
    
    
    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        Resources r = findR(id);
        if(r!=null)
            res.remove(r);
        SSEClients.remove(id);
        DDClients.remove(id);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Resources find(@PathParam("id") Long id) {
        sseResource.sendMessage(id, "ricevo id");
        for(int i=0;i<res.size();i++){
            Long idConf = res.get(i).getId();
            if(idConf.longValue()==id.longValue())
                return res.get(i);
        }
        return null;
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Resources> findAll() {
        return new ArrayList(res.values());
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Resources> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    
    //methods to be accessed by serviceLayerService
    public Resources getRes(Long id){
        if(res.containsKey(id))
            return res.get(id);
        else return null;
    }
    
    public static Object getResVariable(Long id, String var){
        if(res.containsKey(id)){
            Resources r = res.get(id);
            return r.getValue(var);
        }
        return null;
    }
}

