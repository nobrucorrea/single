package br.com.alertafiscal.opusprimum.gui;

import br.com.alertafiscal.opusprimum.Componente;
import br.com.alertafiscal.opusprimum.ConfiguracaoContexto;
import br.com.alertafiscal.opusprimum.FluxoPrincipal;
import br.com.alertafiscal.opusprimum.log.LoggerFachada;
import br.com.alertafiscal.opusprimum.log.impl.LoggerGUI;
import br.com.alertafiscal.opusprimum.retaguarda.Retaguarda;
//import br.com.alertafiscal.opusprimum.retaguarda.alterdata.postgresql.RetaguardaAlterdataPostgreSQL;
import br.com.alertafiscal.opusprimum.retaguarda.alterdata.postgresql.RetaguardaAlterdataPostgresSQLSingle;
import br.com.alertafiscal.opusprimum.ws.ConsultarTributacoesRJFinalResponse.ConsultarTributacoesRJFinalResult;
import br.com.alertafiscal.opusprimum.ws.ConsultarTributacoesRJFinalResponse.ConsultarTributacoesRJFinalResult.CategoriaProduto.Detalhamento;
import br.com.alertafiscal.opusprimum.ws.ConsultarTributacoesRJFinalResponse.ConsultarTributacoesRJFinalResult.CategoriaProduto.Federal;
import br.com.alertafiscal.opusprimum.ws.ConsultarTributacoesRJFinalResponse.ConsultarTributacoesRJFinalResult.CategoriaProduto.IcmsInterno;
import br.com.alertafiscal.opusprimum.ws.ConsultarTributacoesRJFinalResponse.ConsultarTributacoesRJFinalResult.CategoriaProduto.Saida;
//import br.com.alertafiscal.opusprimum.ws.ConsultarTributacoesRJResponse.ConsultarTributacoesRJFinalResult.CategoriaProduto.Detalhamento;
import br.com.alertafiscal.opusprimum.ws.WsIntegracaoSoap;
import br.com.alertafiscal.opusprimum.ws.WsIntegracaoSoapSoap;
import br.com.alertafiscal.opusprimum.ws.WsIntegracaoConsulta;
import br.com.alertafiscal.opusprimum.xml.parser.elemento.Produto;
//import java.util.logging.Level;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
//http://soap.alertafiscalintranet.com.br/WSIntegracaoSoap.asmx?wsdl

/**
 *
 * @author Bruno
 */
public class PrincipalSingle extends javax.swing.JFrame {

    /**
     * Creates new form PrincipalSingle
     */
    LoggerGUI logger = null;
    LoggerFachada loggerFachada = null;
    String wsToken = null;
    String wsId = null;
    Produto produto = null;
    
    public PrincipalSingle() {
        initComponents();

        logger = new LoggerGUI(txtComunicacoes);
        loggerFachada = new LoggerFachada(logger);
        Componente.setLogger(loggerFachada);
       
        
        wsToken = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("webservice.token");
        wsId= ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("webservice.id");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanelCodigoInterno = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCodigoInterno = new javax.swing.JTextField();
        btnBuscarCodInterno = new javax.swing.JButton();
        btnCoInternoUpdate = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtComunicacoes = new javax.swing.JTextArea();
        jPanelNCM = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtCodigoNCM = new javax.swing.JTextField();
        btnBuscarNCM = new javax.swing.JButton();
        btnNCMUpdate = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtOutpuNCM = new javax.swing.JTextArea();
        jPanelCodigoBarra = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtCodigoBarra = new javax.swing.JTextField();
        btnBuscarCodBarra = new javax.swing.JButton();
        btnCodBarraUpdate = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtOutputCodigoBarra = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Código Interno");

        btnBuscarCodInterno.setText("BUSCAR");
        btnBuscarCodInterno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarCodInternoActionPerformed(evt);
            }
        });

        btnCoInternoUpdate.setText("ATUALIZAR");
        btnCoInternoUpdate.setEnabled(false);
        btnCoInternoUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCoInternoUpdateActionPerformed(evt);
            }
        });

        txtComunicacoes.setColumns(20);
        txtComunicacoes.setRows(5);
        jScrollPane1.setViewportView(txtComunicacoes);

        javax.swing.GroupLayout jPanelCodigoInternoLayout = new javax.swing.GroupLayout(jPanelCodigoInterno);
        jPanelCodigoInterno.setLayout(jPanelCodigoInternoLayout);
        jPanelCodigoInternoLayout.setHorizontalGroup(
            jPanelCodigoInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCodigoInternoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCodigoInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCodigoInterno, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelCodigoInternoLayout.createSequentialGroup()
                        .addComponent(btnBuscarCodInterno)
                        .addGap(18, 18, 18)
                        .addComponent(btnCoInternoUpdate)))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanelCodigoInternoLayout.setVerticalGroup(
            jPanelCodigoInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCodigoInternoLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCodigoInterno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanelCodigoInternoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarCodInterno)
                    .addComponent(btnCoInternoUpdate))
                .addGap(20, 20, 20))
        );

        jTabbedPane1.addTab("Código Interno", jPanelCodigoInterno);

        jLabel3.setText("Código NCM");

        btnBuscarNCM.setText("BUSCAR");
        btnBuscarNCM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarNCMActionPerformed(evt);
            }
        });

        btnNCMUpdate.setText("ATUALIZAR");
        btnNCMUpdate.setEnabled(false);

        txtOutpuNCM.setColumns(20);
        txtOutpuNCM.setRows(5);
        jScrollPane3.setViewportView(txtOutpuNCM);

        javax.swing.GroupLayout jPanelNCMLayout = new javax.swing.GroupLayout(jPanelNCM);
        jPanelNCM.setLayout(jPanelNCMLayout);
        jPanelNCMLayout.setHorizontalGroup(
            jPanelNCMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNCMLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelNCMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCodigoNCM)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelNCMLayout.createSequentialGroup()
                        .addComponent(btnBuscarNCM)
                        .addGap(18, 18, 18)
                        .addComponent(btnNCMUpdate)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanelNCMLayout.setVerticalGroup(
            jPanelNCMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelNCMLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCodigoNCM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addGroup(jPanelNCMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarNCM)
                    .addComponent(btnNCMUpdate))
                .addContainerGap())
        );

        jTabbedPane1.addTab("NCM", jPanelNCM);

        jLabel2.setText("Código de Barra");

        btnBuscarCodBarra.setText("BUSCAR");
        btnBuscarCodBarra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarCodBarraActionPerformed(evt);
            }
        });

        btnCodBarraUpdate.setText("ATUALIZAR");
        btnCodBarraUpdate.setEnabled(false);

        txtOutputCodigoBarra.setColumns(20);
        txtOutputCodigoBarra.setRows(5);
        jScrollPane2.setViewportView(txtOutputCodigoBarra);

        javax.swing.GroupLayout jPanelCodigoBarraLayout = new javax.swing.GroupLayout(jPanelCodigoBarra);
        jPanelCodigoBarra.setLayout(jPanelCodigoBarraLayout);
        jPanelCodigoBarraLayout.setHorizontalGroup(
            jPanelCodigoBarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCodigoBarraLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnBuscarCodBarra)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCodBarraUpdate)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanelCodigoBarraLayout.createSequentialGroup()
                .addGroup(jPanelCodigoBarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCodigoBarra, javax.swing.GroupLayout.PREFERRED_SIZE, 481, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(0, 47, Short.MAX_VALUE))
        );
        jPanelCodigoBarraLayout.setVerticalGroup(
            jPanelCodigoBarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCodigoBarraLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCodigoBarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanelCodigoBarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBuscarCodBarra)
                    .addComponent(btnCodBarraUpdate))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Código de Barra", jPanelCodigoBarra);

        jButton1.setText("Update");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Saida");

        jLabel5.setText("Saida Atualizada");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
                    .addComponent(jTextField2))
                .addContainerGap(308, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addGap(5, 5, 5)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 207, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(26, 26, 26))
        );

        jTabbedPane1.addTab("tab4", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarCodInternoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarCodInternoActionPerformed

        if (txtCodigoInterno.getText().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, "Por favor, preencher o campo Codigo do Produto");

        } else {
            
                        
            WsIntegracaoConsulta objConsulta = new WsIntegracaoConsulta();
            objConsulta.setCodigoInterno(txtCodigoInterno.getText());
            objConsulta.setToken(this.wsToken);
            objConsulta.setId(Integer.valueOf(this.wsId));

            loggerFachada.info("Chamando Web Service ....");
            loggerFachada.info("Token %s", this.wsToken);
            loggerFachada.info("ID %s",this.wsId);
            
            ConsultarTributacoesRJFinalResult result = getWSData(objConsulta);

            if (result == null) {
                
                JOptionPane.showMessageDialog(this, "Produto não possuí atualização");
                txtComunicacoes.setText("");

            } else {
                
                loggerFachada.info("Processando resposta Web Service");
                Detalhamento detalhamento = result.getCategoriaProduto().get(0).getDetalhamento();
                Federal impostoFederal = result.getCategoriaProduto().get(0).getFederal();
                IcmsInterno icmsInterno = result.getCategoriaProduto().get(0).getIcmsInterno();
                Saida saida = result.getCategoriaProduto().get(0).getSaida();

                
                //NCM
                loggerFachada.info("NCM ");
                loggerFachada.info("Codigo NCM: %s ", detalhamento.getNcm());
                loggerFachada.info("Descrição NCM:  %s ", detalhamento.getDescricaoNcm());
                loggerFachada.info("Codigo Cest %s", saida.getCodigoCest());
                
                loggerFachada.info("Impostos Federais ");
                loggerFachada.info("Codigo CST PIS COFINS Entrada: %s ", impostoFederal.getCodigoCstPisCofinsEntrada());
                loggerFachada.info("Cofins Entrada: %s ", impostoFederal.getCofinsEntrada() );
                loggerFachada.info("Cofins Saida %s ", impostoFederal.getCofinsSaida());
                loggerFachada.info("Pis Entrada %s ",impostoFederal.getPisEntrada());
                loggerFachada.info("Pis Saida %s ", impostoFederal.getPisSaida() );
                
         
                
                
                loggerFachada.info("ICMS");
                loggerFachada.info("ICMS Interno %s: ", icmsInterno.getAliquotaIcmsInterno());
                
                
               

                btnCoInternoUpdate.setEnabled(true);
                
                produto = new Produto();
                produto.setCodigoInterno(txtCodigoInterno.getText());
                produto.setNcm(detalhamento.getNcm());
                produto.setCest(saida.getCodigoCest());
                produto.setIpi(String.valueOf(saida.getIpi()));
                produto.setPisEntrada(String.valueOf(impostoFederal.getPisEntrada()));
                produto.setCofinsEntrada(String.valueOf(impostoFederal.getCofinsEntrada()));
                produto.setPisSaida(String.valueOf(impostoFederal.getPisSaida()));
                produto.setCofinsSaida(String.valueOf(impostoFederal.getCofinsSaida()));
                

            }
        }

    }//GEN-LAST:event_btnBuscarCodInternoActionPerformed

    private void btnBuscarCodBarraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarCodBarraActionPerformed
       
        
        if(txtCodigoBarra.getText().isEmpty()){
        
        
            JOptionPane.showMessageDialog(this, "Por favor, preencher o campo Codigo de Barra");
            
        }else{
        
         WsIntegracaoConsulta objConsulta = new WsIntegracaoConsulta();
         objConsulta.setEan(txtCodigoBarra.getText());
         
         
         
         loggerFachada.info("Chamando Web Service ....");
            
         ConsultarTributacoesRJFinalResult result = getWSData(objConsulta);

         
         loggerFachada.info("Processando resposta Web Service");
                Detalhamento detalhamento = result.getCategoriaProduto().get(0).getDetalhamento();
                Federal impostoFederal = result.getCategoriaProduto().get(0).getFederal();
                IcmsInterno icmsInterno = result.getCategoriaProduto().get(0).getIcmsInterno();
                Saida saida = result.getCategoriaProduto().get(0).getSaida();
                
         loggerFachada.info("NCM %s", detalhamento.getNcm());
         loggerFachada.info("Descrição NCM ", detalhamento.getDescricaoNcm());
         loggerFachada.info("Impostos Federais ");
         loggerFachada.info("Codigo CST PIS COFINS Entrada %s", impostoFederal.getCodigoCstPisCofinsEntrada());

                btnCodBarraUpdate.setEnabled(true);
                
        }
    }//GEN-LAST:event_btnBuscarCodBarraActionPerformed

    private void btnBuscarNCMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarNCMActionPerformed

        if(txtCodigoNCM.getText().isEmpty()){
        
        
            JOptionPane.showMessageDialog(this, "Por favor, preencher o campo NCM");
            
        }else{
        
         WsIntegracaoConsulta objConsulta = new WsIntegracaoConsulta();
         objConsulta.setEan(txtCodigoBarra.getText());
         
         
         
         loggerFachada.info("Chamando Web Service ....");
            
         ConsultarTributacoesRJFinalResult result = getWSData(objConsulta);

         
         loggerFachada.info("Processando resposta Web Service");
                Detalhamento detalhamento = result.getCategoriaProduto().get(0).getDetalhamento();
                Federal impostoFederal = result.getCategoriaProduto().get(0).getFederal();
                IcmsInterno icmsInterno = result.getCategoriaProduto().get(0).getIcmsInterno();
                Saida saida = result.getCategoriaProduto().get(0).getSaida();
                
                loggerFachada.info("NCM %s", detalhamento.getNcm());
                loggerFachada.info("Descrição NCM ", detalhamento.getDescricaoNcm());
                loggerFachada.info("Impostos Federais ");
                loggerFachada.info("Codigo CST PIS COFINS Entrada %s", impostoFederal.getCodigoCstPisCofinsEntrada());

              btnNCMUpdate.setEnabled(true);
                
        }
        
    }//GEN-LAST:event_btnBuscarNCMActionPerformed

    private void btnCoInternoUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCoInternoUpdateActionPerformed

        try {
            Retaguarda retaguarda = new RetaguardaAlterdataPostgresSQLSingle();
            retaguarda.openTransaction();
            retaguarda.atualizarNCM(this.produto);
            retaguarda.atualizarCEST(this.produto);
            //retaguarda.atualizarIPI(this.produto);
            //retaguarda.atualizarNatureza(this.produto);
            //retaguarda.atualizarPISCOFINS(this.produto);
            //retaguarda.atualizarICMS(this.produto);
            
            
            retaguarda.commit();
            
            JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!!");
            
            
        } catch (Exception ex) {
            loggerFachada.exception(ex);
            ex.printStackTrace();
        }
        
        
    }//GEN-LAST:event_btnCoInternoUpdateActionPerformed

//tab4 Update 
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        //Atualizar
         /*try {
            Retaguarda retaguarda = new RetaguardaAlterdataPostgresSQLSingle();
            retaguarda.openTransaction();
            retaguarda.atualizarNCM(this.produto);
            retaguarda.atualizarCEST(this.produto);
            //retaguarda.atualizarIPI(this.produto);
            //retaguarda.atualizarNatureza(this.produto);
            //retaguarda.atualizarPISCOFINS(this.produto);
            //retaguarda.atualizarICMS(this.produto);
            
            
            retaguarda.commit();
            
          //  JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!!");
            
            
        } catch (Exception ex) {
            loggerFachada.exception(ex);
            ex.printStackTrace();
        }
        
        // Buscar informações 
        if (txtCodigoInterno.getText().isEmpty()) {
            
            //JOptionPane.showMessageDialog(this, "Por favor, preencher o campo Codigo do Produto");

        } else {
            
                        
            WsIntegracaoConsulta objConsulta = new WsIntegracaoConsulta();
            objConsulta.setCodigoInterno(txtCodigoInterno.getText());
            objConsulta.setToken(this.wsToken);
            objConsulta.setId(Integer.valueOf(this.wsId));

            loggerFachada.info("Chamando Web Service ....");
            loggerFachada.info("Token %s", this.wsToken);
            loggerFachada.info("ID %s",this.wsId);
            
            ConsultarTributacoesRJFinalResult result = getWSData(objConsulta);

            if (result == null) {
                
                JOptionPane.showMessageDialog(this, "Produto não possuí atualização");
                jTextField1.setText("");// troquei o txtComunicoes pelo jTextField1

            } else {
                
                loggerFachada.info("Processando resposta Web Service");
                Detalhamento detalhamento = result.getCategoriaProduto().get(0).getDetalhamento();
              //  Federal impostoFederal = result.getCategoriaProduto().get(0).getFederal();
                //IcmsInterno icmsInterno = result.getCategoriaProduto().get(0).getIcmsInterno();
                Saida saida = result.getCategoriaProduto().get(0).getSaida();

                
                //NCM
                loggerFachada.info("NCM ");
                loggerFachada.info("Codigo NCM: %s ", detalhamento.getNcm());
                loggerFachada.info("Descrição NCM:  %s ", detalhamento.getDescricaoNcm());
                loggerFachada.info("Codigo Cest %s", saida.getCodigoCest());
                
                loggerFachada.info("Impostos Federais ");
               // loggerFachada.info("Codigo CST PIS COFINS Entrada: %s ", impostoFederal.getCodigoCstPisCofinsEntrada());
               // loggerFachada.info("Cofins Entrada: %s ", impostoFederal.getCofinsEntrada() );
               //loggerFachada.info("Cofins Saida %s ", impostoFederal.getCofinsSaida());
               // loggerFachada.info("Pis Entrada %s ",impostoFederal.getPisEntrada());
                //loggerFachada.info("Pis Saida %s ", impostoFederal.getPisSaida() );
                
         
                
                
                loggerFachada.info("ICMS");
                //loggerFachada.info("ICMS Interno %s: ", icmsInterno.getAliquotaIcmsInterno());
                
                
               

                btnCoInternoUpdate.setEnabled(true);
                
                produto = new Produto();
                produto.setCodigoInterno(txtCodigoInterno.getText());
                produto.setNcm(detalhamento.getNcm());
                produto.setCest(saida.getCodigoCest());
                produto.setIpi(String.valueOf(saida.getIpi()));
//                produto.setPisEntrada(String.valueOf(impostoFederal.getPisEntrada()));
  //              produto.setCofinsEntrada(String.valueOf(impostoFederal.getCofinsEntrada()));
    //            produto.setPisSaida(String.valueOf(impostoFederal.getPisSaida()));
      //          produto.setCofinsSaida(String.valueOf(impostoFederal.getCofinsSaida()));
                

            }
        }*/
// termina tab4        
     
    }//GEN-LAST:event_jButton1ActionPerformed

    private ConsultarTributacoesRJFinalResult getWSData(WsIntegracaoConsulta objConsulta) {

        try { // Call Web Service Operation
            WsIntegracaoSoap service = null;
            service = new WsIntegracaoSoap();

            WsIntegracaoSoapSoap port = null;
            port = service.getWsIntegracaoSoapSoap12();

            // TODO initialize WS operation arguments here
            //WsIntegracaoConsulta objConsulta = null; 
            //objConsulta = new WsIntegracaoConsulta();
            // TODO process result here
            ConsultarTributacoesRJFinalResult result = null;
            result = port.consultarTributacoesRJFinal(objConsulta);

            return result;

        } catch (Exception ex) {
            return null;
        }
    }

    public void executa(String tipoExecucao) {
        LoggerGUI logger = new LoggerGUI(txtComunicacoes);
        LoggerFachada loggerFachada = new LoggerFachada(logger);
        Componente.setLogger(loggerFachada);
        try {
            FluxoPrincipal.rodar(tipoExecucao, loggerFachada);
        } catch (Exception ex) {
            loggerFachada.exception(ex);
            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao tentar executar o Opus, entre em contato com o suporte!");
        } finally {
            //       botaoFechar.setEnabled(true);
        }
    }

    public static void rodar(String tipoExecucao) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PrincipalGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrincipalGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrincipalGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrincipalGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        PrincipalSingle gui = new PrincipalSingle();
        gui.setVisible(true);
        //gui.executa(tipoExecucao);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarCodBarra;
    private javax.swing.JButton btnBuscarCodInterno;
    private javax.swing.JButton btnBuscarNCM;
    private javax.swing.JButton btnCoInternoUpdate;
    private javax.swing.JButton btnCodBarraUpdate;
    private javax.swing.JButton btnNCMUpdate;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelCodigoBarra;
    private javax.swing.JPanel jPanelCodigoInterno;
    private javax.swing.JPanel jPanelNCM;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField txtCodigoBarra;
    private javax.swing.JTextField txtCodigoInterno;
    private javax.swing.JTextField txtCodigoNCM;
    private javax.swing.JTextArea txtComunicacoes;
    private javax.swing.JTextArea txtOutpuNCM;
    private javax.swing.JTextArea txtOutputCodigoBarra;
    // End of variables declaration//GEN-END:variables
}
