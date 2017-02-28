/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.frogsssa.service;

import org.codehaus.jackson.JsonNode;

/**
 *
 * @author lara
 */
class CommandMsg {
    Long id;
    command act;
    String var;
    JsonNode objret;
    Object obj;
    public enum command{GET, CONFIG, DELETE};
}
