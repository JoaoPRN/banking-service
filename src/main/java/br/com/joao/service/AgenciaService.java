package br.com.joao.service;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import br.com.joao.domain.Agencia;
import br.com.joao.domain.http.AgenciaHttp;
import br.com.joao.domain.http.SituacaoCadastral;
import br.com.joao.exceptions.ErrosSistema;
import br.com.joao.repository.AgenciaRepository;
import br.com.joao.service.http.SituacaoCadastralHttpService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AgenciaService {
    
    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    private final AgenciaRepository agenciaRepository;

    AgenciaService(AgenciaRepository agenciaRepository) {

        this.agenciaRepository = agenciaRepository;
    }

    public void cadastrar(Agencia agencia){

        AgenciaHttp agenciaHttp = situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());
        if (agenciaHttp != null && agenciaHttp.getSituacaoCadastral().equals(SituacaoCadastral.ATIVO)){
            Log.info("A agência com o CNPJ" + agencia.getCnpj() + "foi cadastrada");
            agenciaRepository.persist(agencia);

        } else {
            Log.info("A agência com o CNPJ" + agencia.getCnpj() + "não foi cadastrada");
            throw new ErrosSistema.agenciaNaoAtivaOuNaoEncontradaException();
        }
    }

    public Agencia buscarPorId(Long id) {
        return agenciaRepository.findById(id);
    }

    public void deletar(Long id) {
        Log.info("A agência com o id" + id + "foi deletada");
        agenciaRepository.deleteById(id);
    }

    public void alterar(Agencia agencia) {
        Log.info("A agência com o CNPJ" + agencia.getCnpj() + " foi alterada");
        agenciaRepository.update("nome = ?1, razaoSocial = ?2, cnpj = ?3 where id = ?4", agencia.getNome(), agencia.getRazaoSocial(), agencia.getCnpj(), agencia.getId());

    }
}
