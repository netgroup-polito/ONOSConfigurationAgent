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
        System.out.println(obj);
        if(obj==null)
            return Response.status(Response.Status.NOT_FOUND).build();
        if(obj.asText().equals("null"))
            return Response.status(Response.Status.PARTIAL_CONTENT).build();
        return Response.ok(obj.toString()).build();
    }
    
    @GET
    @Path("DM")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getYang(@PathParam("AppId") String id){
        String yang = ConnectionModule.getYang(id);
        if(yang==null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(yang).build();
    }
    
    //configuration
    @Path("{varId: .+}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setConf(@PathParam("AppId") String id, @PathParam("varId") String var, String Json){
        //0 ok
        //1 variable not setted - error
        //2 variable not found - error
        //3 internal server error
        //4 app not found
        //controllo validità variabile
        //ConnectionModule.someConfiguration(id.toString(), "config " + var + " " + Json);
        System.out.println("It wants to set the variable " + var);
        //var = var.replace("/", ".");
        Integer configured = ConnectionModule.configVar(id, var, Json);
        Response res;
        switch(configured){
            case 0:
                res = Response.ok().build();
                break;
            case 1:
                res = Response.status(Response.Status.BAD_REQUEST).build();
                break;
            case 2:
                res = Response.status(Response.Status.NOT_FOUND).build();
                break;
            case 3:
                res = Response.serverError().build();
                break;
            case 4:
                res = Response.status(Response.Status.NOT_FOUND).build();
                break;
            default:
                res = Response.serverError().build();
        }
        return res;
    }
    
    @Path("{varId: .+}")
    @DELETE
    public Response deleteVar(@PathParam("AppId") String id, @PathParam("varId") String var){
        //var = var.replace("/", ".");
        Integer deleted = ConnectionModule.deleteVar(id, var);
                Response res;
        switch(deleted){
            case 0:
                res = Response.noContent().build();
                break;
            case 1:
                res = Response.status(Response.Status.BAD_REQUEST).build();
                break;
            case 2:
                res = Response.status(Response.Status.NOT_FOUND).build();
                break;
            case 3:
                res = Response.serverError().build();
                break;
            case 4:
                res = Response.status(Response.Status.NOT_FOUND).build();
                break;
            default:
                res = Response.serverError().build();
        }
        return res;
    }
}
