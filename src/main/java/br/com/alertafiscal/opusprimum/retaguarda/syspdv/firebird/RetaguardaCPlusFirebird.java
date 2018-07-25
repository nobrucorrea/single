/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.alertafiscal.opusprimum.retaguarda.syspdv.firebird;

import br.com.alertafiscal.opusprimum.ConfiguracaoContexto;
import br.com.alertafiscal.opusprimum.retaguarda.Retaguarda;
import br.com.alertafiscal.opusprimum.utils.OpusUtils;
import br.com.alertafiscal.opusprimum.xml.parser.elemento.Produto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Bruno
 */
public class RetaguardaCPlusFirebird extends Retaguarda {

    //TRIBUTACOOA ECF
    String codigoTributacaoECFPadrao = "000000700";
    String codigoTributacaoECFST = "000000701";
    String codigoTributacaoECFIsento = "000000702";
    String codigoTributacaoECFNaoTributado = "000000703";
    String codigoTributacaoECFPadraoAliq7 = "000000707";
    String codigoTributacaoECFPadraoAliq13 = "000000713";
    String codigoTributacaoECFPadraoAliq14 = "000000714";
    String codigoTributacaoECFPadraoAliq20 = "000000720";
    String codigoTributacaoECFPadraoAliq27 = "000000727";

    //ICMS PROPERTIES
    String calculoPadrao;
    String calculoPadrao7;
    String calculoPadrao13;
    String calculoPadrao14;
    String calculoPadrao20;
    String calculoPadrao27;
    String calculoIsento;
    String calculoNaoTributado;
    String calculoSubstituicaoTributaria;

    String codCalculoIcmsEstadoAliq0Isento = "000000600";
    String codCalculoIcmsEstadoAliq0Ntributado = "000000601";
    String codCalculoIcmsEstadoAliq0calculoSubstituicaoTributaria = "000000602";
    String codCalculoIcmsEstadoAliq7 = "000000607";
    String codCalculoIcmsEstadoAliq13 = "000000613";
    String codCalculoIcmsEstadoAliq14 = "000000614";
    String codCalculoIcmsEstadoAliq20 = "000000620";
    String codCalculoIcmsEstadoAliq27 = "000000627";

    @Override
    public Map<String, String> mapeiaCodigoInternoEAN() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void preProcessamento(Produto produto) throws Exception {

        // VERIFICANDO SE CALCULOS JA ESTAO CADASTRADOS NO BANCO
        boolean noCalcAliq0Isento = ((List<String>) executeQuery("SELECT CODCALCULOICMSESTADO FROM calculoicmsestado WHERE CODCALCULOICMSESTADO = '" + codCalculoIcmsEstadoAliq0Isento + "';")).isEmpty();
        boolean noCalcNTributado = ((List<String>) executeQuery("SELECT CODCALCULOICMSESTADO FROM calculoicmsestado WHERE CODCALCULOICMSESTADO = '" + codCalculoIcmsEstadoAliq0Ntributado + "';")).isEmpty();
        boolean noCalcST = ((List<String>) executeQuery("SELECT CODCALCULOICMSESTADO FROM calculoicmsestado WHERE CODCALCULOICMSESTADO = '" + codCalculoIcmsEstadoAliq0calculoSubstituicaoTributaria + "';")).isEmpty();
        boolean noCalcAliq7 = ((List<String>) executeQuery("SELECT CODCALCULOICMSESTADO FROM calculoicmsestado WHERE CODCALCULOICMSESTADO = '" + codCalculoIcmsEstadoAliq7 + "';")).isEmpty();
        boolean noCalcAliq13 = ((List<String>) executeQuery("SELECT CODCALCULOICMSESTADO FROM calculoicmsestado WHERE CODCALCULOICMSESTADO = '" + codCalculoIcmsEstadoAliq13 + "';")).isEmpty();
        boolean noCalcAliq14 = ((List<String>) executeQuery("SELECT CODCALCULOICMSESTADO FROM calculoicmsestado WHERE CODCALCULOICMSESTADO = '" + codCalculoIcmsEstadoAliq14 + "';")).isEmpty();
        boolean noCalcAliq20 = ((List<String>) executeQuery("SELECT CODCALCULOICMSESTADO FROM calculoicmsestado WHERE CODCALCULOICMSESTADO = '" + codCalculoIcmsEstadoAliq20 + "';")).isEmpty();
        boolean noCalcAliq27 = ((List<String>) executeQuery("SELECT CODCALCULOICMSESTADO FROM calculoicmsestado WHERE CODCALCULOICMSESTADO = '" + codCalculoIcmsEstadoAliq27 + "';")).isEmpty();

        calculoPadrao = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("icms.padrao");
        calculoIsento = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("icms.isento");
        calculoNaoTributado = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("icms.naotributado");
        calculoSubstituicaoTributaria = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("icms.st");
        calculoPadrao7 = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("icms.padrao7");;
        calculoPadrao13 = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("icms.padrao13");;
        calculoPadrao14 = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("icms.padrao14");;
        calculoPadrao20 = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("icms.padrao20");;
        calculoPadrao27 = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("icms.padrao27");;

        //criar calculos na tabelas calculo icmsestado  e
        //icms estado
        //CODCALCULOICMSESTADO = NOT NULL
        //CFOP
        //CODCALCULOICMS not null
        //CODUFORIGEM = NOT NULL
        //CODUFDESTINO = NIT NULL
        //ALIQICMS = 7, 13, 14, 20, 27
        //CODSITUACAOTRIBUTARIA
        //CALCULOICMSESTADO
        //Aliq zero Isento
        //CSOSN 500
        StringBuilder builderAliq0Isento = new StringBuilder();
        builderAliq0Isento.append("INSERT INTO calculoicmsestado (");
        builderAliq0Isento.append("CODCALCULOICMSESTADO, CODCALCULOICMS, CODUFORIGEM, CODUFDESTINO, ALIQICMS,CSOSN) ");
        builderAliq0Isento.append("VALUES ('");
        builderAliq0Isento.append(codCalculoIcmsEstadoAliq0Isento);
        builderAliq0Isento.append("','");
        builderAliq0Isento.append(calculoIsento);
        builderAliq0Isento.append("','");
        builderAliq0Isento.append("RJ");
        builderAliq0Isento.append("','");
        builderAliq0Isento.append("RJ");
        builderAliq0Isento.append("',");
        builderAliq0Isento.append("0");
        builderAliq0Isento.append(", '500'");
        builderAliq0Isento.append(");");

        //Aliq zero Nao Tributado
        //csosn 500
        StringBuilder builderAliq0NTributado = new StringBuilder();
        builderAliq0NTributado.append("INSERT INTO calculoicmsestado (");
        builderAliq0NTributado.append("CODCALCULOICMSESTADO, CODCALCULOICMS, CODUFORIGEM, CODUFDESTINO, ALIQICMS, CSOSN) ");
        builderAliq0NTributado.append("VALUES ('");
        builderAliq0NTributado.append(codCalculoIcmsEstadoAliq0Ntributado);
        builderAliq0NTributado.append("','");
        builderAliq0NTributado.append(calculoNaoTributado);
        builderAliq0NTributado.append("','");
        builderAliq0NTributado.append("RJ");
        builderAliq0NTributado.append("','");
        builderAliq0NTributado.append("RJ");
        builderAliq0NTributado.append("',");
        builderAliq0NTributado.append("0");
        builderAliq0NTributado.append(", '500'");
        builderAliq0NTributado.append(");");

        //Aliq zero Substiruicao Tributaria
        //CSOSN 500
        StringBuilder builderAliq0SubTributaria = new StringBuilder();
        builderAliq0SubTributaria.append("INSERT INTO calculoicmsestado (");
        builderAliq0SubTributaria.append("CODCALCULOICMSESTADO, CODCALCULOICMS, CODUFORIGEM, CODUFDESTINO, ALIQICMS,CSOSN) ");
        builderAliq0SubTributaria.append("VALUES ('");
        builderAliq0SubTributaria.append(codCalculoIcmsEstadoAliq0calculoSubstituicaoTributaria);
        builderAliq0SubTributaria.append("','");
        builderAliq0SubTributaria.append(calculoSubstituicaoTributaria);
        builderAliq0SubTributaria.append("','");
        builderAliq0SubTributaria.append("RJ");
        builderAliq0SubTributaria.append("','");
        builderAliq0SubTributaria.append("RJ");
        builderAliq0SubTributaria.append("',");
        builderAliq0SubTributaria.append("0");
        builderAliq0SubTributaria.append(", '500'");
        builderAliq0SubTributaria.append(");");

        if (noCalcAliq0Isento) {
            executeUpdate(builderAliq0Isento.toString());
        }
        if (noCalcNTributado) {
            executeUpdate(builderAliq0NTributado.toString());
        }
        if (noCalcST) {
            executeUpdate(builderAliq0SubTributaria.toString());
        }

        //Calculo  padrao oi seja tributado com taxa de 7%
        //CSOSN 102
        StringBuilder builderTributado7 = new StringBuilder();
        builderTributado7.append("INSERT INTO calculoicmsestado (");
        builderTributado7.append("CODCALCULOICMSESTADO, CODCALCULOICMS, CODUFORIGEM, CODUFDESTINO, ALIQICMS, CSOSN) ");
        builderTributado7.append("VALUES ('");
        builderTributado7.append(codCalculoIcmsEstadoAliq7);
        builderTributado7.append("','");
        builderTributado7.append(calculoPadrao7);
        builderTributado7.append("','");
        builderTributado7.append("RJ");
        builderTributado7.append("','");
        builderTributado7.append("RJ");
        builderTributado7.append("',");
        builderTributado7.append("7");
        builderTributado7.append(", '012'");
        builderTributado7.append(");");

        //Calculo  padrao oi seja tributado com taxa de 13%
        StringBuilder builderTributado13 = new StringBuilder();
        builderTributado13.append("INSERT INTO calculoicmsestado (");
        builderTributado13.append("CODCALCULOICMSESTADO, CODCALCULOICMS, CODUFORIGEM, CODUFDESTINO, ALIQICMS, CSOSN) ");
        builderTributado13.append("VALUES ('");
        builderTributado13.append(codCalculoIcmsEstadoAliq13);
        builderTributado13.append("','");
        builderTributado13.append(calculoPadrao13);
        builderTributado13.append("','");
        builderTributado13.append("RJ");
        builderTributado13.append("','");
        builderTributado13.append("RJ");
        builderTributado13.append("',");
        builderTributado13.append("13");
        builderTributado13.append(", '102'");
        builderTributado13.append(");");

        //Calculo  padrao oi seja tributado com taxa de 14%
        StringBuilder builderTributado14 = new StringBuilder();
        builderTributado14.append("INSERT INTO calculoicmsestado (");
        builderTributado14.append("CODCALCULOICMSESTADO, CODCALCULOICMS, CODUFORIGEM, CODUFDESTINO, ALIQICMS, CSOSN) ");
        builderTributado14.append("VALUES ('");
        builderTributado14.append(codCalculoIcmsEstadoAliq14);
        builderTributado14.append("','");
        builderTributado14.append(calculoPadrao14);
        builderTributado14.append("','");
        builderTributado14.append("RJ");
        builderTributado14.append("','");
        builderTributado14.append("RJ");
        builderTributado14.append("',");
        builderTributado14.append("14");
        builderTributado14.append(", '102'");
        builderTributado14.append(");");

        //Calculo  padrao oi seja tributado com taxa de 20%
        StringBuilder builderTributado20 = new StringBuilder();
        builderTributado20.append("INSERT INTO calculoicmsestado (");
        builderTributado20.append("CODCALCULOICMSESTADO, CODCALCULOICMS, CODUFORIGEM, CODUFDESTINO, ALIQICMS, CSOSN) ");
        builderTributado20.append("VALUES ('");
        builderTributado20.append(codCalculoIcmsEstadoAliq20);
        builderTributado20.append("','");
        builderTributado20.append(calculoPadrao20);
        builderTributado20.append("','");
        builderTributado20.append("RJ");
        builderTributado20.append("','");
        builderTributado20.append("RJ");
        builderTributado20.append("',");
        builderTributado20.append("20");
        builderTributado20.append(", '102'");
        builderTributado20.append(");");

        //Calculo  padrao oi seja tributado com taxa de 27%
        StringBuilder builderTributado27 = new StringBuilder();
        builderTributado27.append("INSERT INTO calculoicmsestado (");
        builderTributado27.append("CODCALCULOICMSESTADO, CODCALCULOICMS, CODUFORIGEM, CODUFDESTINO, ALIQICMS, CSOSN) ");
        builderTributado27.append("VALUES ('");
        builderTributado27.append(codCalculoIcmsEstadoAliq27);
        builderTributado27.append("','");
        builderTributado27.append(calculoPadrao27);
        builderTributado27.append("','");
        builderTributado27.append("RJ");
        builderTributado27.append("','");
        builderTributado27.append("RJ");
        builderTributado27.append("',");
        builderTributado27.append("27");
        builderTributado27.append(", '102'");
        builderTributado27.append(");");

        if (noCalcAliq7) {
            executeUpdate(builderTributado7.toString());
        }
        //logger.info("comando %s executado", builderTributado7.toString());
        if (noCalcAliq13) {
            executeUpdate(builderTributado13.toString());
        }
        //logger.info("comando %s executado", builderTributado13.toString());
        if (noCalcAliq14) {
            executeUpdate(builderTributado14.toString());
        }
        //logger.info("comando %s executado", builderTributado14.toString());
        if (noCalcAliq20) {
            executeUpdate(builderTributado20.toString());
        }
        //logger.info("comando %s executado", builderTributado20.toString());
        if (noCalcAliq27) {
            executeUpdate(builderTributado27.toString());
        }
        //logger.info("comando %s executado", builderTributado27.toString());

        inicializarTributacaoECF(produto);

    }
    
    private void inserirCest(String cest) {

        StringBuilder queryCest = new StringBuilder();
        queryCest.append("select codcesticms from cesticms where (");
        queryCest.append(" codcesticms='");
        queryCest.append(cest);
        queryCest.append("');");
        List<String> rCest = (List<String>) executeQuery(queryCest.toString());

        if (rCest.isEmpty()) {

            StringBuilder insertCEST = new StringBuilder(0);
            insertCEST.append("INSERT INTO cesticms ( codcesticms, descricao )");
            insertCEST.append("VALUES ('");
            insertCEST.append(cest);
            insertCEST.append("','");
            insertCEST.append(cest);
            insertCEST.append("');");

            System.out.println( insertCEST.toString());
            executeUpdate(insertCEST.toString());
        }

    }
    
    private void inserirNcm(String ncm, String cest) {

        //String aux = "ncm " + ncm  + " cest " + cest;
        //log.debug(aux);
        
        
        StringBuilder queryNCM = new StringBuilder();
        queryNCM.append("select codigoclassificacaofiscal from classificacaofiscal where (");
        queryNCM.append(" codigoclassificacaofiscal='");
        queryNCM.append(ncm);
        queryNCM.append("' AND CODCESTICMS = '");
        queryNCM.append(cest);
        queryNCM.append("')");
        List<String> rNCM = (List<String>) executeQuery(queryNCM.toString());

        if (rNCM.isEmpty()) {

            StringBuilder queryLastID = new StringBuilder();
            queryLastID.append("SELECT MAX(codclassificacaofiscal) FROM CLASSIFICACAOFISCAL;");
            List<String> listLastID = (List<String>) executeQuery(queryLastID.toString());
            String lastIdString = listLastID.get(0).trim();
            int lastIDInt = Integer.parseInt(lastIdString);
            lastIDInt++;

            String newId = String.valueOf(lastIDInt);

            //logger.info("NCM [%s] nao existe no banco " , codigoNcm);
            //cadastrar ncm no banco porque ele nao existe la
            StringBuilder insertNCM = new StringBuilder();
            insertNCM.append("INSERT INTO CLASSIFICACAOFISCAL ( CODCLASSIFICACAOFISCAL, CODIGOCLASSIFICACAOFISCAL, CODIGO, DESCRICAO, CODCESTICMS) VALUES('");
            insertNCM.append(newId);
            insertNCM.append("' , '");
            insertNCM.append(ncm);
            insertNCM.append("' , '");
            insertNCM.append(newId);
            insertNCM.append("' , '");
            insertNCM.append(ncm);
            insertNCM.append("' , '");
            insertNCM.append(cest);
            insertNCM.append("');");

            executeUpdate(insertNCM.toString());           
            
            

        } else {

            
            logger.info("[NCM]= " + ncm + " ja  estava cadastrado no banco. IGNORADO. \n");
            
        }
    }

       private boolean associarNCMCest(String ncm, String cest) {

        boolean ncmOK = true;
        boolean cestOK = true;
        boolean ncmCestOK = false;


        //verifica ncm cest
        StringBuilder builder = new StringBuilder();
        builder.append("select codcesticms, codigoclassificacaofiscal ");
        builder.append("from cesticmsclassificacaofiscal where ");
        builder.append("codcesticms= '");
        builder.append(cest);
        builder.append("' AND codigoclassificacaofiscal = '");
        builder.append(ncm);
        builder.append("';");

        List<String> rNCM_CEST = (List<String>) executeQuery(builder.toString());

        if (rNCM_CEST.isEmpty()) {
            ncmCestOK = true;
        }
        

        if (ncmCestOK) {

            StringBuilder ncmCestRela = new StringBuilder();
            ncmCestRela.append("INSERT INTO CESTICMSCLASSIFICACAOFISCAL ");
            ncmCestRela.append("( CODCESTICMS, CODIGOCLASSIFICACAOFISCAL, SISTEMA, FLAGATIVO )");
            ncmCestRela.append(" VALUES ( '");
            ncmCestRela.append(cest);
            ncmCestRela.append("' , '");
            ncmCestRela.append(ncm);
            ncmCestRela.append("' ,");
            ncmCestRela.append("'Y', 'Y');");

            executeUpdate(ncmCestRela.toString());

            String aux ="[NCM] " + ncm + " [CST] " + cest + " relacionado ";
            logger.debug("associado %s", aux);
           
        }

        return true;
    }


    @Override
    public boolean atualizarNCM(Produto produto){
    
        String codigoInterno = OpusUtils.completeToLeft(produto.getCodigoInterno(), '0', 9);
        String codigoNcm = produto.getNcmLimpo();
        String descricao = produto.getDescricaoNcm();
        String cest = produto.getCestLimpo();
        
        StringBuilder queryNCM = new StringBuilder();
        queryNCM.append("SELECT codclassificacaoFiscal FROM CLASSIFICACAOFISCAL WHERE codigoclassificacaofiscal = '");
        queryNCM.append(codigoNcm);
        queryNCM.append("' AND codcesticms = '");
        queryNCM.append(cest);
        queryNCM.append("';");
        
        List<String> listCodNcm = (List<String>) executeQuery(queryNCM.toString());
        
        
        if( listCodNcm.isEmpty() ){
            
            //inserir cest
            inserirCest(cest);
            //inserir ncm com cest
            inserirNcm(codigoNcm, cest);
            //relacioanr cest icms
            associarNCMCest(codigoNcm, cest);
            
        
            StringBuilder query = new StringBuilder();
            query.append("SELECT codclassificacaoFiscal FROM CLASSIFICACAOFISCAL WHERE codigoclassificacaofiscal = '");
            query.append(codigoNcm);
            query.append("';");
        
            List<String> list = (List<String>)executeQuery(query.toString());
            String newCodigo = list.get(0).trim();
            
            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE produto SET codclassificacaofiscal = '");
            builder.append(newCodigo);
            builder.append("' WHERE codprod = '");
            builder.append(codigoInterno);
            builder.append("';");

            executeUpdate(builder.toString());
            
            return true;
        
        
        }
        
         
        String ncm = listCodNcm.get(0).trim();
         StringBuilder builder = new StringBuilder();
         builder.append("UPDATE produto SET codclassificacaofiscal = '");
         builder.append(ncm);
         builder.append("' WHERE codprod = '");
         builder.append(codigoInterno);
         builder.append("';");

         executeUpdate(builder.toString());
        
        
         
        return true;

        

    }
    
    /*@Override
    public boolean atualizarNCM(Produto produto) throws Exception {

        String codigoInterno = OpusUtils.completeToLeft(produto.getCodigoInterno(), '0', 9);
        String codigoNcm = produto.getNcmLimpo();
        String descricao = produto.getDescricaoNcm();

        //procuro pelo ncm no banco
        StringBuilder queryNCM = new StringBuilder();
        queryNCM.append("SELECT codclassificacaoFiscal FROM CLASSIFICACAOFISCAL WHERE codigoclassificacaofiscal = '");
        queryNCM.append(codigoNcm);
        queryNCM.append("';");

        List<String> listaNCM = (List<String>) executeQuery(queryNCM.toString());

        if (listaNCM.isEmpty()) {

            //ultimo id inserido
            StringBuilder queryLastID = new StringBuilder();
            queryLastID.append("SELECT MAX(codclassificacaofiscal) FROM CLASSIFICACAOFISCAL;");
            List<String> listLastID = (List<String>) executeQuery(queryLastID.toString());
            String lastIdString = listLastID.get(0).trim();
            int lastIDInt = Integer.parseInt(lastIdString);
            lastIDInt++;

            String newId = String.valueOf(lastIDInt);

            //logger.info("NCM [%s] nao existe no banco " , codigoNcm);
            //cadastrar ncm no banco porque ele nao existe la
            StringBuilder insertNCM = new StringBuilder();
            insertNCM.append("INSERT INTO CLASSIFICACAOFISCAL ( CODCLASSIFICACAOFISCAL, CODIGOCLASSIFICACAOFISCAL, CODIGO, DESCRICAO) VALUES('");
            insertNCM.append(newId);
            insertNCM.append("' , '");
            insertNCM.append(codigoNcm);
            insertNCM.append("' , '");
            insertNCM.append(newId);
            insertNCM.append("' , '");
            insertNCM.append(descricao);
            insertNCM.append("');");

            executeUpdate(insertNCM.toString());

            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE produto SET codclassificacaofiscal = '");
            builder.append(newId);
            builder.append("' WHERE codprod = '");
            builder.append(codigoInterno);
            builder.append("';");

            executeUpdate(builder.toString());
            return true;

        } else {

            //logger.info("NCM [%s] JA existe no banco " , codigoNcm);
            //associar codNCM ao produto
            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE produto SET codclassificacaofiscal = '");
            builder.append(listaNCM.get(0));
            builder.append("' WHERE codprod = '");
            builder.append(codigoInterno);
            builder.append("';");

            executeUpdate(builder.toString());
            return true;

        }

    }*/

    @Override
    public boolean atualizarCEST(Produto produto) throws Exception {
        //BUSCAR POR CEST NO BANCO
        //SE NAO EXISTIR - CADASTRAR NA TABELA CESTICMS CAMPOS CODIGOCEST  E DESCRICAO
        //EM SEGUIDA ASSOCIAR O CODIGOCEST COM O CLASSIFICACAO FISCAL NA TABELA CESTICMSCLASSIFICACAFISCAL

        String codigoInterno = OpusUtils.completeToLeft(produto.getCodigoInterno(), '0', 9);
        String codigoCest = produto.getCestLimpo();
        String codigoNCM = produto.getNcmLimpo();
        String descricaoCest = produto.getDescricaoCest();

        StringBuilder queryCEST = new StringBuilder();
        queryCEST.append("SELECT codcesticms FROM CESTICMS WHERE codcesticms = '");
        queryCEST.append(codigoCest);
        queryCEST.append("';");

        List<String> listaCEST = (List<String>) executeQuery(queryCEST.toString());

        if (listaCEST.isEmpty()) {

            StringBuilder insertCEST = new StringBuilder(0);
            insertCEST.append("INSERT INTO cesticms ( codcesticms, descricao )");
            insertCEST.append("VALUES ('");
            insertCEST.append(codigoCest);
            insertCEST.append("','");
            insertCEST.append(descricaoCest);
            insertCEST.append("');");

            executeUpdate(insertCEST.toString());

        }

        //verificar se existe o relacionameto cest ncms
        StringBuilder queryCESTNCM = new StringBuilder();
        queryCESTNCM.append("SELECT codcesticms FROM CESTICMSCLASSIFICACAOFISCAL WHERE( codcesticms = '");
        queryCESTNCM.append(codigoCest);
        queryCESTNCM.append("' AND codigoclassificacaofiscal = '");
        queryCESTNCM.append(codigoNCM);
        queryCESTNCM.append("');");

        List<String> listaCESTNCM = (List<String>) executeQuery(queryCESTNCM.toString());

        if (listaCESTNCM.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("INSERT INTO CESTICMSCLASSIFICACAOFISCAL( ");
            builder.append("codcesticms, codigoclassificacaofiscal, sistema)");
            builder.append(" VALUES ('");
            builder.append(codigoCest);
            builder.append("','");
            builder.append(codigoNCM);
            builder.append("','Y');");

            executeUpdate(builder.toString());
        }

        return true;
    }

    @Override
    public boolean atualizarIPI(Produto produto) throws Exception {
        String codigoInterno = OpusUtils.completeToLeft(produto.getCodigoInterno(), '0', 9);
        BigDecimal ipi = new BigDecimal(produto.getIpi());

        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE produto SET valoripi = ");
        builder.append(ipi);
        builder.append(" WHERE codprod = '");
        builder.append(codigoInterno);
        builder.append("';");

        executeUpdate(builder.toString());
        
        
        //ATUALIZAR CST SAIDA E ENTRADO DO BANCO
        StringBuilder builderCST = new StringBuilder();
        builderCST.append("UPDATE produto SET ");
        builderCST.append("cstipientrada = '03' , ");
        builderCST.append("cstipisaida = '53' ");
        builderCST.append(" WHERE codprod = '");
        builderCST.append(codigoInterno);
        builderCST.append("';");
        
        
        executeUpdate( builderCST.toString() );

        
        

        return true;

    }

    @Override
    public boolean atualizarPISCOFINS(Produto produto) throws Exception {

        String codigoInterno = OpusUtils.completeToLeft(produto.getCodigoInterno(), '0', 9);
        BigDecimal aliquotaPis = produto.getCofinsSaidaDecimal();
        BigDecimal aliquotaConfins = produto.getCofinsSaidaDecimal();
        String cstPisCofinsSaida = produto.getPiscofinsCstSaida();
        String cstPisCofinsEntrada = produto.getPiscofinsCstEntrada();

        StringBuilder builder = new StringBuilder();
        builder.append("UPDATE produto SET aliqpis = ");
        builder.append(aliquotaPis);
        builder.append(", aliqcofins = ");
        builder.append(aliquotaConfins);
        builder.append(", cstpisentrada =  '");
        builder.append(cstPisCofinsEntrada.substring(cstPisCofinsEntrada.length() - 2, cstPisCofinsEntrada.length()));
        builder.append("', cstcofinsentrada = '");
        builder.append(cstPisCofinsEntrada.substring(cstPisCofinsEntrada.length() - 2, cstPisCofinsEntrada.length()));
        builder.append("', cstpissaida = '");
        builder.append(cstPisCofinsSaida.substring(cstPisCofinsSaida.length() - 2, cstPisCofinsSaida.length()));
        builder.append("', cstcofinssaida = '");
        builder.append(cstPisCofinsSaida.substring(cstPisCofinsSaida.length() - 2, cstPisCofinsSaida.length()));
        builder.append("' WHERE codprod = '");
        builder.append(codigoInterno);
        builder.append("';");

        executeUpdate(builder.toString());

        return true;
    }

    @Override
    public boolean atualizarICMS(Produto produto) throws Exception {

        logger.debug("Atualizando icms produto %s", produto.getCodigoInterno());

        //inserir  0000  o xml de revisao nao possui zeros a esquerda, mas no banco possui 9 caracteres
        // muitos zeros a esquerda  
        String codigoInterno = OpusUtils.completeToLeft(produto.getCodigoInterno(), '0', 9);
        String codigoCsosn = produto.getCodigoCsosn();
        String icmsSaidaConsumidorFinal = produto.getIcmsSaidaConsumidorFinal();
        Double icmsSaidaConsumidorFinalDouble = Double.parseDouble(produto.getIcmsSaidaConsumidorFinal());
        String icmsCstSadiaConsumidorFinal = produto.getIcmsCstSaidaConsumidorFinal();
        String modalidade = ConfiguracaoContexto.getInstance().getConfiguracao().getProperty("opus.modalidade.juridica");
        String cfopDentro = "102";
        String cfopFora = "102";
        //t07
        StringBuilder builder = new StringBuilder();

        //Lucro Real
        if (modalidade.equals("lucro_real") || modalidade.equals("lucro_presumido")) {

            //cesta basica
            //isento 000000002
            if (icmsCstSadiaConsumidorFinal.equals("040")) {

                //calculoicms estado é a tabela
                builder.append("UPDATE produto SET codcalculoicms = '");
                builder.append(calculoIsento);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFIsento);
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");

            }
            //nao tributado
            //N00            
            if (icmsCstSadiaConsumidorFinal.equals("041")) {
                builder.append("UPDATE produto SET codcalculoicms =  '");
                builder.append(calculoNaoTributado);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFNaoTributado);                
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");

            }
            //subtituicao tributaria
            //F10
            if (icmsCstSadiaConsumidorFinal.equals("060")) {
                builder.append("UPDATE produto SET codcalculoicms = '");
                builder.append(calculoSubstituicaoTributaria);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFST);                
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");

                cfopDentro = "405";
                cfopFora = "403";
            }

            if (icmsCstSadiaConsumidorFinal.equals("000") && icmsSaidaConsumidorFinalDouble.intValue() == 7) {//0 ou 00               
                builder.append("UPDATE produto SET codcalculoicms =  '");
                builder.append(calculoPadrao7);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFPadraoAliq7);                
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (icmsCstSadiaConsumidorFinal.equals("000") && icmsSaidaConsumidorFinalDouble.intValue() == 13) {//0 ou 00               
                builder.append("UPDATE produto SET codcalculoicms =  '");
                builder.append(calculoPadrao13);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFPadraoAliq13);                
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (icmsCstSadiaConsumidorFinal.equals("000") && icmsSaidaConsumidorFinalDouble.intValue() == 14) {//0 ou 00               
                builder.append("UPDATE produto SET codcalculoicms =  '");
                builder.append(calculoPadrao14);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFPadraoAliq14);                
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (icmsCstSadiaConsumidorFinal.equals("000") && icmsSaidaConsumidorFinalDouble.intValue() == 20) {//0 ou 00               
                builder.append("UPDATE produto SET codcalculoicms =  '");
                builder.append(calculoPadrao20);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFPadraoAliq20);                
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (icmsCstSadiaConsumidorFinal.equals("000") && icmsSaidaConsumidorFinalDouble.intValue() == 27) {//0 ou 00               
                builder.append("UPDATE produto SET codcalculoicms =  '");
                builder.append(calculoPadrao27);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFPadraoAliq27);                
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

        }

        //SIMPLES
        if (modalidade.equals("simples")) {

            if (codigoCsosn.equals("300") || codigoCsosn.equals("500")) {
                builder.append("UPDATE produto SET codcalculoicms = '");
                builder.append(calculoIsento);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFIsento);                
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");

                cfopDentro = "405";
                cfopFora = "403";

            }

            if (codigoCsosn.equals("500") && icmsCstSadiaConsumidorFinal.equals("060")) {
                builder.setLength(0);
                builder.append("UPDATE produto SET codcalculoicms = '");
                builder.append(calculoSubstituicaoTributaria);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFST);                
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");

            }

            if (codigoCsosn.equals("102") && icmsSaidaConsumidorFinalDouble.intValue() == 0) {
                builder.append("UPDATE produto SET codcalculoicms = '");
                builder.append(calculoIsento);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFIsento);                
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (codigoCsosn.equals("102") && icmsSaidaConsumidorFinalDouble.intValue() == 7) {
                builder.append("UPDATE produto SET codcalculoicms = '");
                builder.append(calculoPadrao7);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFPadraoAliq7);                
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");

            }

            if (codigoCsosn.equals("102") && icmsSaidaConsumidorFinalDouble.intValue() == 13) {
                builder.append("UPDATE produto SET codcalculoicms = '");
                builder.append(calculoPadrao13);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFPadraoAliq13);                
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (codigoCsosn.equals("102") && icmsSaidaConsumidorFinalDouble.intValue() == 14) {
                builder.append("UPDATE produto SET codcalculoicms = '");
                builder.append(calculoPadrao14);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFPadraoAliq14);                
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (codigoCsosn.equals("102") && icmsSaidaConsumidorFinalDouble.intValue() == 20) {
                builder.append("UPDATE produto SET codcalculoicms = '");
                builder.append(calculoPadrao20);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFPadraoAliq20);                
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");
            }

            if (codigoCsosn.equals("102") && icmsSaidaConsumidorFinalDouble.intValue() == 27) {

                builder.append("UPDATE produto SET codcalculoicms = '");
                builder.append(calculoPadrao27);
                builder.append("', codtributacaoecf = '");
                builder.append(codigoTributacaoECFPadraoAliq27);                
                builder.append("' WHERE codprod = '");
                builder.append(codigoInterno);
                builder.append("';");

                logger.debug("**** 000005491 interno  %s csosn %s saidaconsumidorfinal %d query %s", codigoInterno, codigoCsosn, icmsSaidaConsumidorFinalDouble.intValue(), builder.toString());
            }

        }

        //ATUALIARC CFOP
        StringBuilder builderCFOP = new StringBuilder();
        builderCFOP.append("UPDATE produto SET cfopdentrouf = '");
        builderCFOP.append(cfopDentro);
        builderCFOP.append("', cfopforauf = '");
        builderCFOP.append(cfopFora);
        builderCFOP.append("' WHERE codprod = '");
        builderCFOP.append(codigoInterno);
        builderCFOP.append("';");

        //logger.debug("eEXECUTANDO ICMS [%s]", builder.toString());
        executeUpdate(builder.toString());

        executeUpdate(builderCFOP.toString());

        return true;
    }

    private boolean inicializarTributacaoECF(Produto produto) {

        //verificand se as tributacoes ECF ja foram criadas
        boolean noTribECFPadrao = ((List<String>) executeQuery("SELECT CODTRIBUTACAOECF FROM TRIBUTACAOECF WHERE CODTRIBUTACAOECF = '" + codigoTributacaoECFPadrao + "';")).isEmpty();
        boolean noTribECFST = ((List<String>) executeQuery("SELECT CODTRIBUTACAOECF FROM TRIBUTACAOECF WHERE CODTRIBUTACAOECF = '" + codigoTributacaoECFST + "';")).isEmpty();
        boolean noTribECFIsento = ((List<String>) executeQuery("SELECT CODTRIBUTACAOECF FROM TRIBUTACAOECF WHERE CODTRIBUTACAOECF = '" + codigoTributacaoECFIsento + "';")).isEmpty();
        boolean noTribECFNaoTributado = ((List<String>) executeQuery("SELECT CODTRIBUTACAOECF FROM TRIBUTACAOECF WHERE CODTRIBUTACAOECF = '" + codigoTributacaoECFNaoTributado + "';")).isEmpty();
        boolean noTribECFPadraoAliq7 = ((List<String>) executeQuery("SELECT CODTRIBUTACAOECF FROM TRIBUTACAOECF WHERE CODTRIBUTACAOECF = '" + codigoTributacaoECFPadraoAliq7 + "';")).isEmpty();
        boolean noTribECFPadraoAliq13 = ((List<String>) executeQuery("SELECT CODTRIBUTACAOECF FROM TRIBUTACAOECF WHERE CODTRIBUTACAOECF = '" + codigoTributacaoECFPadraoAliq13 + "';")).isEmpty();
        boolean noTribECFPadraoAliq14 = ((List<String>) executeQuery("SELECT CODTRIBUTACAOECF FROM TRIBUTACAOECF WHERE CODTRIBUTACAOECF = '" + codigoTributacaoECFPadraoAliq14 + "';")).isEmpty();
        boolean noTribECFPadraoAliq20 = ((List<String>) executeQuery("SELECT CODTRIBUTACAOECF FROM TRIBUTACAOECF WHERE CODTRIBUTACAOECF = '" + codigoTributacaoECFPadraoAliq20 + "';")).isEmpty();
        boolean noTribECFPadraoAliq27 = ((List<String>) executeQuery("SELECT CODTRIBUTACAOECF FROM TRIBUTACAOECF WHERE CODTRIBUTACAOECF = '" + codigoTributacaoECFPadraoAliq27 + "';")).isEmpty();

        //TABELA TRIBTACAOECF
        //CODTRIBUTACAOECF
        //CODIGO - 6 caracteres
        //TIPOTRIBUTACAO
        //csosn
        //alitributacao
        StringBuilder builderECFPadrao = new StringBuilder();
        builderECFPadrao.append("INSERT INTO tributacaoecf ( ");
        builderECFPadrao.append("CODTRIBUTACAOECF, codigo, nometributacaoecf, codcalculoicms, tipotributacao, csosn, aliqtributacao ) VALUES ('");
        builderECFPadrao.append(codigoTributacaoECFPadrao);
        builderECFPadrao.append("',");
        builderECFPadrao.append("'000700',");
        builderECFPadrao.append("'Calculo Padrão','");
        builderECFPadrao.append(calculoPadrao);
        builderECFPadrao.append("','T','");
        builderECFPadrao.append("102");
        builderECFPadrao.append("',");
        builderECFPadrao.append("0.0);");

        StringBuilder builderECFIST = new StringBuilder();
        builderECFIST.append("INSERT INTO tributacaoecf ( ");
        builderECFIST.append("CODTRIBUTACAOECF, codigo, nometributacaoecf, codcalculoicms, tipotributacao, csosn, aliqtributacao ) VALUES ('");
        builderECFIST.append(codigoTributacaoECFST);
        builderECFIST.append("',");
        builderECFIST.append("'000701',");
        builderECFIST.append("'Calculo ST','");
        builderECFIST.append(calculoSubstituicaoTributaria);
        builderECFIST.append("','F','");
        builderECFIST.append("500");
        builderECFIST.append("',");
        builderECFIST.append("0.0);");

        StringBuilder builderECFISento = new StringBuilder();
        builderECFISento.append("INSERT INTO tributacaoecf ( ");
        builderECFISento.append("CODTRIBUTACAOECF, codigo, nometributacaoecf, codcalculoicms, tipotributacao, csosn, aliqtributacao ) VALUES ('");
        builderECFISento.append(codigoTributacaoECFIsento);
        builderECFISento.append("',");
        builderECFISento.append("'000702',");
        builderECFISento.append("'Calculo Isento','");
        builderECFISento.append(calculoIsento);
        builderECFISento.append("','I','");
        builderECFISento.append("102");
        builderECFISento.append("',");
        builderECFISento.append("0.0);");

        StringBuilder builderECFNaoTributado = new StringBuilder();
        builderECFNaoTributado.append("INSERT INTO tributacaoecf ( ");
        builderECFNaoTributado.append("CODTRIBUTACAOECF, codigo, nometributacaoecf, codcalculoicms, tipotributacao, csosn, aliqtributacao ) VALUES ('");
        builderECFNaoTributado.append(codigoTributacaoECFNaoTributado);
        builderECFNaoTributado.append("',");
        builderECFNaoTributado.append("'000703',");
        builderECFNaoTributado.append("'Calculo Não Tributado','");
        builderECFNaoTributado.append(calculoNaoTributado);
        builderECFNaoTributado.append("', 'N','");
        builderECFNaoTributado.append("102");
        builderECFNaoTributado.append("',");
        builderECFNaoTributado.append("0.0);");

        //logger.info("COMANDO com erro %s", builderECFISento.toString());
        if (noTribECFPadrao) {
            executeUpdate(builderECFPadrao.toString());
        }
        if (noTribECFST) {
            executeUpdate(builderECFIST.toString());
        }
        if (noTribECFIsento) {
            executeUpdate(builderECFISento.toString());
        }
        if (noTribECFNaoTributado) {
            executeUpdate(builderECFNaoTributado.toString());
        }

        StringBuilder builderECFIPadraoAliq7 = new StringBuilder();
        builderECFIPadraoAliq7.append("INSERT INTO tributacaoecf ( ");
        builderECFIPadraoAliq7.append("CODTRIBUTACAOECF, codigo, nometributacaoecf, codcalculoicms,  tipotributacao, csosn, aliqtributacao ) VALUES ('");
        builderECFIPadraoAliq7.append(codigoTributacaoECFPadraoAliq7);
        builderECFIPadraoAliq7.append("',");
        builderECFIPadraoAliq7.append("'000707',");
        builderECFIPadraoAliq7.append("'Caculo Padrão 7%','");
        builderECFIPadraoAliq7.append(calculoPadrao7);
        builderECFIPadraoAliq7.append("','T','");
        builderECFIPadraoAliq7.append("102");
        builderECFIPadraoAliq7.append("',");
        builderECFIPadraoAliq7.append("7.0);");

        StringBuilder builderECFIPadraoAliq13 = new StringBuilder();
        builderECFIPadraoAliq13.append("INSERT INTO tributacaoecf ( ");
        builderECFIPadraoAliq13.append("CODTRIBUTACAOECF, codigo, nometributacaoecf, codcalculoicms, tipotributacao, csosn, aliqtributacao ) VALUES ('");
        builderECFIPadraoAliq13.append(codigoTributacaoECFPadraoAliq13);
        builderECFIPadraoAliq13.append("',");
        builderECFIPadraoAliq13.append("'000713',");
        builderECFIPadraoAliq13.append("'Calculo Padrão 13%','");
        builderECFIPadraoAliq13.append(calculoPadrao13);
        builderECFIPadraoAliq13.append("', 'T','");
        builderECFIPadraoAliq13.append("102");
        builderECFIPadraoAliq13.append("',");
        builderECFIPadraoAliq13.append("13.0);");

        StringBuilder builderECFIPadraoAliq14 = new StringBuilder();
        builderECFIPadraoAliq14.append("INSERT INTO tributacaoecf ( ");
        builderECFIPadraoAliq14.append("CODTRIBUTACAOECF, codigo, nometributacaoecf, codcalculoicms, tipotributacao, csosn, aliqtributacao ) VALUES ('");
        builderECFIPadraoAliq14.append(codigoTributacaoECFPadraoAliq14);
        builderECFIPadraoAliq14.append("',");
        builderECFIPadraoAliq14.append("'000714',");
        builderECFIPadraoAliq14.append("'Calculo Padrão 14%','");
        builderECFIPadraoAliq14.append(calculoPadrao14);
        builderECFIPadraoAliq14.append("','T','");
        builderECFIPadraoAliq14.append("102");
        builderECFIPadraoAliq14.append("',");
        builderECFIPadraoAliq14.append("14.0);");

        StringBuilder builderECFIPadraoAliq20 = new StringBuilder();
        builderECFIPadraoAliq20.append("INSERT INTO tributacaoecf ( ");
        builderECFIPadraoAliq20.append("CODTRIBUTACAOECF, codigo, nometributacaoecf, codcalculoicms, tipotributacao, csosn, aliqtributacao ) VALUES ('");
        builderECFIPadraoAliq20.append(codigoTributacaoECFPadraoAliq20);
        builderECFIPadraoAliq20.append("',");
        builderECFIPadraoAliq20.append("'000720',");
        builderECFIPadraoAliq20.append("'Calculo Padrão 20%','");
        builderECFIPadraoAliq20.append(calculoPadrao20);
        builderECFIPadraoAliq20.append("','T','");
        builderECFIPadraoAliq20.append("102");
        builderECFIPadraoAliq20.append("',");
        builderECFIPadraoAliq20.append("20.0);");

        StringBuilder builderECFIPadraoAliq27 = new StringBuilder();
        builderECFIPadraoAliq27.append("INSERT INTO tributacaoecf ( ");
        builderECFIPadraoAliq27.append("CODTRIBUTACAOECF, codigo, nometributacaoecf, codcalculoicms, tipotributacao, csosn, aliqtributacao ) VALUES ('");
        builderECFIPadraoAliq27.append(codigoTributacaoECFPadraoAliq27);
        builderECFIPadraoAliq27.append("',");
        builderECFIPadraoAliq27.append("'000727',");
        builderECFIPadraoAliq27.append("'Calculo Padrão 27%','");
        builderECFIPadraoAliq27.append(calculoPadrao27);
        builderECFIPadraoAliq27.append("','T','");
        builderECFIPadraoAliq27.append("102");
        builderECFIPadraoAliq27.append("',");
        builderECFIPadraoAliq27.append("27.0);");

        if (noTribECFPadraoAliq7) {
            executeUpdate(builderECFIPadraoAliq7.toString());
        }
        if (noTribECFPadraoAliq13) {
            executeUpdate(builderECFIPadraoAliq13.toString());
        }
        if (noTribECFPadraoAliq14) {
            executeUpdate(builderECFIPadraoAliq14.toString());
        }
        if (noTribECFPadraoAliq20) {
            executeUpdate(builderECFIPadraoAliq20.toString());
        }
        if (noTribECFPadraoAliq27) {
            executeUpdate(builderECFIPadraoAliq27.toString());
        }

        return true;
    }

    @Override
    public void posProcessamento(Produto produto) throws Exception {

    }

    @Override
    public boolean atualizarNatureza(Produto produto) throws Exception {
        return true;
    }

}
