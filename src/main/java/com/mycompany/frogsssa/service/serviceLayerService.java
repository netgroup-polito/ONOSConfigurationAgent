/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.frogsssa.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * REST Web Service
 *
 * @author lara
 */
@Path("{AppId}")
public class serviceLayerService {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of serviceLayerService
     */
    public serviceLayerService() {
    }

    //get the resources
    @Path("{varId : .+}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getResources(@PathParam("AppId") String id, @PathParam("varId") String var){
        //String res = new String("asked for this variable " + var);
        //Object obj = ConnectionModule.getResVariable(id, var);
        //if(obj!=null){
          //  return (new Gson()).toJson(obj);
        //}
        //var = var.replace("/", ".");
        JsonNode obj = ((new ConnectionModule()).getValue(id, var));
        if(obj==null)
            return Response.ok("Object not found").build();
        return Response.ok(obj.toString()).build();
    }
    
    @GET
    @Path("DM")
    @Produces(MediaType.TEXT_PLAIN)
    public String getYang(@PathParam("AppId") String id){
        return ConnectionModule.getYang(id);
    }
    
    //configuration
    @Path("{varId: .+}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void setConf(@PathParam("AppId") String id, @PathParam("varId") String var, String Json){
        //controllo validità variabile
        //ConnectionModule.someConfiguration(id.toString(), "config " + var + " " + Json);
        System.out.println("It wants to set the variable " + var);
        //var = var.replace("/", ".");
        ConnectionModule.configVar(id, var, Json);
    }
    
    @Path("{varId: .+}")
    @DELETE
    public void deleteVar(@PathParam("AppId") String id, @PathParam("varId") String var){
        //var = var.replace("/", ".");
        ConnectionModule.deleteVar(id, var);
    }
}
