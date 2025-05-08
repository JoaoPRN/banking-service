package br.com.joao.service;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import br.com.joao.domain.Agencia;
import br.com.joao.domain.Endereco;
import br.com.joao.domain.http.AgenciaHttp;
import br.com.joao.exceptions.ErrosSistema;
import br.com.joao.repository.AgenciaRepository;
import br.com.joao.service.http.SituacaoCadastralHttpService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

@QuarkusTest
public class AgenciaServiceTest {
    
    @InjectMock
    private AgenciaRepository agenciaRepository;

    @InjectMock
    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    @Inject
    private AgenciaService agenciaService;

    @Test
    public void deveNaoCadastrarQuandoClientRetornarNull() {

        Agencia agencia = criarAgencia();
        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(null);
        Assertions.assertThrows(ErrosSistema.agenciaNaoAtivaOuNaoEncontradaException.class, () -> agenciaService.cadastrar(agencia));
        
        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia);
    }

    @Test
    public void deveCadastrarQuandoClientRetornarSituacaoCadastralAtiva() {
        Agencia agencia = criarAgencia();
        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123")).thenReturn(criarAgenciaHttp());
        
        agenciaService.cadastrar(agencia);
        Mockito.verify(agenciaRepository).persist(agencia);
    }

    private Agencia criarAgencia(){
        Endereco endereco = new Endereco(1, "teste", "teste", "teste", 5);
        return new Agencia(1, "Agencia Teste", "Razao Agencia Teste", "123", endereco);
    
    }

    private AgenciaHttp criarAgenciaHttp() {
        return new AgenciaHttp("Agencia Teste", "Razao Agencia Teste", "123", "ATIVO");
}
} 
