/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.alertafiscal.opusprimum.log.impl;

import java.io.PrintStream;

/**
 *
 * @author Bruno
 */
public class LoggerCMD extends LoggerAbstrato {

    private PrintStream out;
    
    public LoggerCMD(PrintStream out) {
        this.out = out;
    }

    @Override
    public void info(String string, Object... os) {
        super.info(string, os);
        out.println(String.format(string, os) + "\n");
    }

}
