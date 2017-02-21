/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.frogsssa.service;

/**
 *
 * @author lara
 */
class CommandMsg {
    Long id;
    action act;
    String var;
    Object obj;
    public enum action{GET, CONFIG};
}
