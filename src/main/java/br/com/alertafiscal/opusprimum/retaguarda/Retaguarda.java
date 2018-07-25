package br.com.alertafiscal.opusprimum.retaguarda;

import br.com.alertafiscal.opusprimum.Componente;
import br.com.alertafiscal.opusprimum.ConfiguracaoContexto;
import br.com.alertafiscal.opusprimum.persistencia.HibernateContexto;
import br.com.alertafiscal.opusprimum.utils.DiretorioUtils;
import br.com.alertafiscal.opusprimum.utils.OpusUtils;
import br.com.alertafiscal.opusprimum.xml.parser.elemento.Produto;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

public abstract class Retaguarda extends Componente {
    
    private PrintWriter pw;
    private Session session;
    
    public Retaguarda() {
        session = HibernateContexto.getSession();
        boolean desenvolvimento = ConfiguracaoContexto.getInstance().isDesenvolvimento();
        if (desenvolvimento) {
            File scriptSQL = new File(new DiretorioUtils().folder(".opus").asFile(), "script.sql");
            if (scriptSQL.exists()) {
                scriptSQL.delete();
            }
            try {
                pw = new PrintWriter(new FileWriter(scriptSQL, true));
            } catch (IOException ex) {
                logger.exception(ex);
            }
        }
    }
    
    public void atualizar(List<Produto> produtos) {

        //lista com porduto do bancod e dados
        //testar se produto esta no no banco ; se estiver faço processamento senao ignoro
        //essa qeuery serve para o syspdv taebla produto
        List<String> produtosDB = (List<String>) executeQuery("SELECT codprod FROM produto");
        logger.info("Guardando o ID de todos os produtos do banco", produtos.size());
        
        try {
            preProcessamento(null);
        } catch (Exception ex) {
            logger.exception(  ex, "ERRO NO PRE PROCESSAMENTO" );
        }
        
        int contador = 0;
        int ausente = 0;
        int tamanho = produtos.size();
        logger.info("Iniciando atualização de %d produtos: ", produtos.size());
        
        for (Produto produto : produtos) {
            String codigoProduto = OpusUtils.completeToLeft(produto.getCodigoInterno(), '0', 9);
            if (produtosDB.contains(codigoProduto)) {
                session.beginTransaction();
                try {
                    atualizarNCM(produto);
                } catch (Exception ex) {
                    logger.exception(ex, "Erro em atualizar NCM [%s]", produto.getCodigoInterno());
                }
                try {
                    atualizarCEST(produto);
                } catch (Exception ex) {
                    logger.exception(ex, "Erro em atualizar CEST [%s]", produto.getCodigoInterno());
                }
                try {
                    atualizarIPI(produto);
                } catch (Exception ex) {
                    logger.exception(ex, "Erro em atualizar IPI [%s]", produto.getCodigoInterno());
                }
                try {
                    atualizarPISCOFINS(produto);
                } catch (Exception ex) {
                    logger.exception(ex, "Erro em atualizar PIS/COFINS [%s]", produto.getCodigoInterno());
                }
                try {
                    atualizarICMS(produto);
                } catch (Exception ex) {
                    logger.exception(ex, "Erro em atalizar ICMS [%s]", produto.getCodigoInterno());
                }
                
                try {
                    atualizarNatureza(produto);
                } catch (Exception ex) {
                    logger.exception(ex, "Erro em atualizar Natureza da Receita  [%s]", produto.getCodigoInterno());
                }
                
                try {
                    posProcessamento(produto);
                } catch (Exception ex) {
                    logger.exception(ex, "Erro no pós-processamento [%s]", produto.getCodigoInterno());
                }
                logger.trace("Atualização " + (++contador) + " de " + tamanho + " executada.");
                session.getTransaction().commit();
            } else {
                ausente++;
                logger.info("[%s] {%s} nao se enontra no banco de dos foi ignorado", produto.getCodigoInterno(), codigoProduto);
            }
        }
        session.close();
        logger.info("Atualizações realizadas!");
        logger.info("%d produtos ausentes no xml", ausente);
        logger.info("Opus finalizado...");
        
        boolean desenvolvimento = ConfiguracaoContexto.getInstance().isDesenvolvimento();
        if (desenvolvimento) {
            pw.flush();
            pw.close();
        }
    }
    
    public boolean openTransaction(){
        session.beginTransaction();
        return true;
    }
    public boolean commit(){
        session.getTransaction().commit();
        return true;
    }
    public abstract Map<String, String> mapeiaCodigoInternoEAN() throws Exception;
    
    public abstract void preProcessamento(Produto produto) throws Exception;
    
    public abstract boolean atualizarNCM(Produto produto) throws Exception;
    
    public abstract boolean atualizarCEST(Produto produto) throws Exception;
    
    public abstract boolean atualizarIPI(Produto produto) throws Exception;
    
    public abstract boolean atualizarPISCOFINS(Produto produto) throws Exception;
    
    public abstract boolean atualizarICMS(Produto produto) throws Exception;
    
    public abstract boolean atualizarNatureza(Produto produto) throws Exception;
    
    public abstract void posProcessamento(Produto produto) throws Exception;
    
    protected int executeUpdate(String sql) {
        boolean desenvolvimento = ConfiguracaoContexto.getInstance().isDesenvolvimento();
        if (desenvolvimento) {
            pw.print(sql);
            pw.println(";");
        }
        SQLQuery query = session.createSQLQuery(sql);
        return query.executeUpdate();
    }
    
    protected List<?> executeQuery(String sql) {
        boolean desenvolvimento = ConfiguracaoContexto.getInstance().isDesenvolvimento();
        if (desenvolvimento) {
            pw.print(sql);
            pw.println(";");
        }
        SQLQuery query = session.createSQLQuery(sql);
        return query.list();
    }
}
