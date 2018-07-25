package br.com.alertafiscal.opusprimum.gui;

import br.com.alertafiscal.opusprimum.Componente;
import br.com.alertafiscal.opusprimum.gui.config.ConfiguracaoGUI;
import br.com.alertafiscal.opusprimum.utils.DiretorioUtils;
import java.io.File;

/**
 *
 * @author bastosbf
 */
public class Principal {

    public static final String TIPO_PRINCIPAL_GUI = "GUI";
    public static final String TIPO_PRINCIPAL_CMD = "CMD";

    public static final String TIPO_EXECUCAO_PARCIAL = "PARCIAL";
    public static final String TIPO_EXECUCAO_TOTAL = "TOTAL";

    public static void main(String[] args) {
        try {
           // String tipoPrincipal = TIPO_PRINCIPAL_GUI;
            String tipoExecucao = TIPO_EXECUCAO_PARCIAL;
           // if (args.length > 0) {
             //   tipoPrincipal = args[0];
            //}
            //if (args.length > 1) {
              //  tipoExecucao = args[1];
            //}
            //if (TIPO_PRINCIPAL_GUI.equalsIgnoreCase(tipoPrincipal)) {
                File arquivoConfiguracoesLocal = new File("config.properties");
                File arquivoConfiguracoesOculto = new File(new DiretorioUtils().folder(".opus").asFile(), "config.properties");
                if (!arquivoConfiguracoesLocal.exists() && !arquivoConfiguracoesOculto.exists()) {
                    ConfiguracaoGUI.rodar();
                }
                while (!arquivoConfiguracoesLocal.exists() && !arquivoConfiguracoesOculto.exists()) {
                    //devido a um problema com a atualizacao da interface tem que esperar
                    Thread.sleep(500);
                }
                
                PrincipalSingle.rodar(tipoExecucao);
                //PrincipalGUI.rodar(tipoExecucao);
                
            //} else if (TIPO_PRINCIPAL_CMD.equalsIgnoreCase(tipoPrincipal)) {
              //  PrincipalCMD.rodar(tipoExecucao);
            //}
        } catch (Exception e) {
            Componente.getLogger().exception(e);
        }
    }

}
