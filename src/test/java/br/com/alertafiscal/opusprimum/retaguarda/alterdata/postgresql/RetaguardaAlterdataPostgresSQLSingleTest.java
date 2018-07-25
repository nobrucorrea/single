/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.alertafiscal.opusprimum.retaguarda.alterdata.postgresql;

import br.com.alertafiscal.opusprimum.xml.parser.elemento.Produto;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
//import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bruno
 */
public class RetaguardaAlterdataPostgresSQLSingleTest {
    
    public RetaguardaAlterdataPostgresSQLSingleTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of mapeiaCodigoInternoEAN method, of class RetaguardaAlterdataPostgresSQLSingle.
     */
  @org.junit.Test
      
   public void testMapeiaCodigoInternoEAN() throws Exception {
        System.out.println("mapeiaCodigoInternoEAN");
        RetaguardaAlterdataPostgresSQLSingle instance = new RetaguardaAlterdataPostgresSQLSingle();
        Map<String, String> expResult = null;
        Map<String, String> result = instance.mapeiaCodigoInternoEAN();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of preProcessamento method, of class RetaguardaAlterdataPostgresSQLSingle.
     */
   @org.junit.Test
    public void testPreProcessamento() throws Exception {
        System.out.println("preProcessamento");
        Produto produto = null;
        RetaguardaAlterdataPostgresSQLSingle instance = new RetaguardaAlterdataPostgresSQLSingle();
        instance.preProcessamento(produto);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of atualizarNCM method, of class RetaguardaAlterdataPostgresSQLSingle.
     */
    @org.junit.Test
    public void testAtualizarNCM() throws Exception {
        System.out.println("atualizarNCM");
        Produto produto = null;
        RetaguardaAlterdataPostgresSQLSingle instance = new RetaguardaAlterdataPostgresSQLSingle();
        boolean expResult = false;
        boolean result = instance.atualizarNCM(produto);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of atualizarCEST method, of class RetaguardaAlterdataPostgresSQLSingle.
     */
    @org.junit.Test
    public void testAtualizarCEST() throws Exception {
        System.out.println("atualizarCEST");
        Produto produto = null;
        RetaguardaAlterdataPostgresSQLSingle instance = new RetaguardaAlterdataPostgresSQLSingle();
        boolean expResult = false;
        boolean result = instance.atualizarCEST(produto);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of atualizarIPI method, of class RetaguardaAlterdataPostgresSQLSingle.
     */
   @org.junit.Test
    public void testAtualizarIPI() throws Exception {
        System.out.println("atualizarIPI");
        Produto produto = null;
        RetaguardaAlterdataPostgresSQLSingle instance = new RetaguardaAlterdataPostgresSQLSingle();
        boolean expResult = false;
        boolean result = instance.atualizarIPI(produto);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of atualizarPISCOFINS method, of class RetaguardaAlterdataPostgresSQLSingle.
     */
    @org.junit.Test
    public void testAtualizarPISCOFINS() throws Exception {
        System.out.println("atualizarPISCOFINS");
        Produto produto = null;
        RetaguardaAlterdataPostgresSQLSingle instance = new RetaguardaAlterdataPostgresSQLSingle();
        boolean expResult = false;
        boolean result = instance.atualizarPISCOFINS(produto);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of atualizarICMS method, of class RetaguardaAlterdataPostgresSQLSingle.
     */
    @org.junit.Test
    public void testAtualizarICMS() throws Exception {
        System.out.println("atualizarICMS");
        Produto produto = null;
        RetaguardaAlterdataPostgresSQLSingle instance = new RetaguardaAlterdataPostgresSQLSingle();
        boolean expResult = false;
        boolean result = instance.atualizarICMS(produto);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of atualizarNatureza method, of class RetaguardaAlterdataPostgresSQLSingle.
     */
    @org.junit.Test
    public void testAtualizarNatureza() throws Exception {
        System.out.println("atualizarNatureza");
        Produto produto = null;
        RetaguardaAlterdataPostgresSQLSingle instance = new RetaguardaAlterdataPostgresSQLSingle();
        boolean expResult = false;
        boolean result = instance.atualizarNatureza(produto);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of posProcessamento method, of class RetaguardaAlterdataPostgresSQLSingle.
     */
    @org.junit.Test
    public void testPosProcessamento() throws Exception {
        System.out.println("posProcessamento");
        Produto produto = null;
        RetaguardaAlterdataPostgresSQLSingle instance = new RetaguardaAlterdataPostgresSQLSingle();
        instance.posProcessamento(produto);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
