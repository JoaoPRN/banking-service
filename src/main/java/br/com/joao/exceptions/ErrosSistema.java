package br.com.joao.exceptions;

public class ErrosSistema{
    
    public static final class agenciaNaoAtivaOuNaoEncontradaException extends RuntimeException{
        
        public static final String codigoErro = "001";

        public agenciaNaoAtivaOuNaoEncontradaException(){
            super(codigoErro);
        }
    }
}
