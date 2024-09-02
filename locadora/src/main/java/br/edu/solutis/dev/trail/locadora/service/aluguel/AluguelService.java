package br.edu.solutis.dev.trail.locadora.service.aluguel;



import br.edu.solutis.dev.trail.locadora.exception.carro.CarroAlreadyRentedException;
import br.edu.solutis.dev.trail.locadora.exception.carro.CarroException;
import br.edu.solutis.dev.trail.locadora.exception.carro.CarroNotRentedException;
import br.edu.solutis.dev.trail.locadora.exception.pessoa.motorista.MotoristaException;
import br.edu.solutis.dev.trail.locadora.exception.pessoa.motorista.MotoristaNotAuthorizedException;
import br.edu.solutis.dev.trail.locadora.exception.aluguel.AluguelAlreadyConfirmedException;
import br.edu.solutis.dev.trail.locadora.exception.aluguel.AluguelException;
import br.edu.solutis.dev.trail.locadora.exception.aluguel.AluguelNotConfirmedException;
import br.edu.solutis.dev.trail.locadora.exception.aluguel.AluguelNotFoundException;
import br.edu.solutis.dev.trail.locadora.mapper.GenericMapper;
import br.edu.solutis.dev.trail.locadora.model.dto.aluguel.AluguelDto;
import br.edu.solutis.dev.trail.locadora.model.dto.aluguel.AluguelDtoResponse;
import br.edu.solutis.dev.trail.locadora.model.entity.aluguel.Aluguel;
import br.edu.solutis.dev.trail.locadora.model.entity.aluguel.ApoliceSeguro;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Carro;
import br.edu.solutis.dev.trail.locadora.model.entity.pessoa.Motorista;
import br.edu.solutis.dev.trail.locadora.repository.carro.CarroRepository;
import br.edu.solutis.dev.trail.locadora.repository.pessoa.MotoristaRepository;
import br.edu.solutis.dev.trail.locadora.repository.aluguel.ApoliceSeguroRepository;
import br.edu.solutis.dev.trail.locadora.repository.aluguel.AluguelRepository;
import br.edu.solutis.dev.trail.locadora.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class AluguelService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AluguelService.class);

    private final AluguelRepository aluguelRepository;
    private final GenericMapper<AluguelDto, Aluguel> modelMapper;
    private final GenericMapper<AluguelDtoResponse, Aluguel> modelMapperResponse;
    private final ApoliceSeguroRepository apoliceSeguroRepository;
    private final CarroRepository carroRepository;
    private final MotoristaRepository motoristaRepository;

    public AluguelDtoResponse findById(Long id) {
        LOGGER.info("Buscando aluguel com ID: {}", id);

        Aluguel aluguel = getAluguel(id);

        return modelMapperResponse.mapModelToDto(aluguel, AluguelDtoResponse.class);
    }

    public PageResponse<AluguelDtoResponse> findAll(int pageNo, int pageSize) {
        try {
            LOGGER.info("Buscando todos os alugueis");

            Page<Aluguel> alugueisPaginados = aluguelRepository.findAll(PageRequest.of(pageNo, pageSize));

            List<AluguelDtoResponse> aluguelsDto = modelMapper.mapList(alugueisPaginados.getContent(), AluguelDtoResponse.class);

            PageResponse<AluguelDtoResponse> pageResponse = new PageResponse<>();
            pageResponse.setContent(aluguelsDto);
            pageResponse.setCurrentPage(alugueisPaginados.getNumber());
            pageResponse.setTotalItems(alugueisPaginados.getTotalElements());
            pageResponse.setTotalPages(alugueisPaginados.getTotalPages());

            return pageResponse;
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao buscar todos os alugueis.", e);
            throw new RuntimeException("Ocorreu um erro ao buscar todos os aluguéis.", e);
        }
    }

    public AluguelDto add(AluguelDto payload) {
        try {
            ApoliceSeguro apoliceSeguro = apoliceSeguroRepository.findById(payload.getApoliceSeguroId()).orElseThrow();
            Carro carro = carroRepository.findById(payload.getCarroId()).orElseThrow();
            Motorista motorista = motoristaRepository.findById(payload.getMotoristaId()).orElseThrow();

            if (carro.isAlugado()) {
                throw new CarroAlreadyRentedException(carro.getId());
            }

            payload.setCarrinhoId(motorista.getCarrinho().getId());
            calculoAluguel(payload, carro.getValorDiario(), apoliceSeguro.getValorFranquia());

            Aluguel aluguel = aluguelRepository.save(modelMapper.mapDtoToModel(payload, Aluguel.class));

            return modelMapper.mapModelToDto(aluguel, AluguelDto.class);
        } catch (CarroAlreadyRentedException e) {
            LOGGER.error("Ocorreu um erro ao adicionar o aluguel.", e);
            throw new CarroException("O carro com o Id " + payload.getCarroId() + " ja esta alugado.", e);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao adicionar o aluguel.", e);
            throw new AluguelException("Ocorreu um erro ao adicionar o aluguel.", e);
        }
    }

    public AluguelDtoResponse confirmAluguel(Long id) {
        LOGGER.info("Confirmando aluguel com ID {}", id);

        Aluguel aluguel = getAluguel(id);

        try {
            if (aluguel.isConfirmado()) {
                throw new AluguelAlreadyConfirmedException(id);
            } else if (aluguel.isDeleted()) {
                throw new AluguelNotFoundException(id);
            } else if (aluguel.getCarro().isAlugado()) {
                throw new CarroAlreadyRentedException(aluguel.getCarro().getId());
            }

            aluguel.setConfirmado(true);
            aluguel.getCarro().setAlugado(true);

            return modelMapperResponse.mapModelToDto(aluguelRepository.save(aluguel), AluguelDtoResponse.class);
        } catch (AluguelAlreadyConfirmedException e) {
            LOGGER.error("Ocorreu um erro ao confirmar o aluguel com ID {}", id, e);
            throw new AluguelException("O aluguel com ID " + id + " já está confirmado.", e);
        } catch (AluguelNotFoundException e) {
            LOGGER.error("Ocorreu um erro ao confirmar o aluguel com ID {}", id, e);
            throw new AluguelException("O aluguel com o ID " + id + " nao foi encontrado.", e);
        } catch (CarroAlreadyRentedException e) {
            LOGGER.error("Ocorreu um erro ao confirmar o aluguel com ID {}", id, e);
            throw new CarroException("O carro com o ID " + aluguel.getCarro().getId() + " ja está alugado.", e);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao confirmar o aluguel com ID {}", id, e);
            throw new AluguelException("Ocorreu um erro ao confirmar o aluguel.", e);
        }
    }
        
    public AluguelDtoResponse finishAluguel(long motoristaId, long aluguelId) {
        LOGGER.info("Finalizando aluguel com ID: {}", aluguelId);

        Aluguel aluguel = getAluguel(aluguelId);

        try {
            if (!aluguel.isConfirmado()) {
                throw new AluguelNotConfirmedException(aluguelId);
            } else if (aluguel.isDeleted()) {
                throw new AluguelNotFoundException(aluguelId);
            } else if (!aluguel.getCarro().isAlugado()) {
                throw new CarroNotRentedException(aluguel.getCarro().getId());
            } else if (aluguel.getMotorista().getId() != motoristaId) {
                throw new MotoristaNotAuthorizedException(motoristaId);
            }

            aluguel.setFinalizado(true);
            aluguel.setDataFinalizada(LocalDate.now());
            aluguel.getCarro().setAlugado(false);

            return modelMapperResponse.mapModelToDto(aluguelRepository.save(aluguel), AluguelDtoResponse.class);
        } catch (AluguelNotConfirmedException e) {
            LOGGER.error("Ocorreu um erro ao confirmar o aluguel com ID {}", aluguelId, e);
            throw new AluguelException("O aluguel com o ID " + aluguelId + " nao foi confirmado.", e);
        } catch (AluguelNotFoundException e) {
            LOGGER.error("Ocorreu um erro ao confirmar o aluguel com ID {}", aluguelId, e);
            throw new AluguelException("O aluguel com o ID " + aluguelId + " nao foi encontrado.", e);
        } catch (CarroNotRentedException e) {
            LOGGER.error("Ocorreu um erro ao confirmar o aluguel com ID {}", aluguelId, e);
            throw new CarroException("O carro com o ID " + aluguel.getCarro().getId() + " nao esta alugado.", e);
        } catch (MotoristaNotAuthorizedException e) {
            LOGGER.error("Ocorreu um erro ao confirmar o aluguel com ID {}", motoristaId, e);
            throw new MotoristaException("O motorista com o ID " + motoristaId + " nao esta autorizado.", e);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao confirmar o aluguel com ID {}", aluguelId, e);
            throw new AluguelException("Ocorreu um erro ao confirmar o aluguel.", e);
        }
    }

    public List<AluguelDtoResponse> findAlugueisAtivos(){
        List<Aluguel> alugueisAtivos = aluguelRepository.findByFinalizadoFalse();

        try {
            return modelMapperResponse.mapList(alugueisAtivos, AluguelDtoResponse.class);
        } catch (Exception e) {
            throw new AluguelException("Ocorreu um erro ao buscar alugueis ativos.", e);
        }
    }

    public List<AluguelDtoResponse> findAlugueisFinalizados() {
        List<Aluguel> alugueisFinalizados = aluguelRepository.findByFinalizadoTrue();

        try {
            return modelMapperResponse.mapList(alugueisFinalizados, AluguelDtoResponse.class);
        } catch (Exception e) {
            throw new AluguelException("Ocorreu um erro ao buscar aluguéis finalizados.", e);
        }
    }

    public AluguelDtoResponse update(AluguelDto payload) {
        try {
            LOGGER.info("Atualizando aluguel com ID: {}", payload.getId());

            Aluguel aluguel = getAluguel(payload.getId());
            if (aluguel.isConfirmado()) {
                throw new Exception("Esse aluguel ja esta confirmado.");
            } else if (aluguel.isDeleted()) {
                throw new AluguelNotFoundException(payload.getId());
            }

            updateCampos(aluguel, payload);

            return modelMapperResponse.mapModelToDto(aluguelRepository.save(aluguel), AluguelDtoResponse.class);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao atualizar o aluguel com ID: {}", payload.getId(), e);
            throw new AluguelException("Ocorreu um erro ao atualizar o aluguel.", e);
        }
    }

    public void deleteById(Long id) {
        LOGGER.info("Excluindo aluguel com ID: {}", id);

        Aluguel aluguel = getAluguel(id);

        try {

            aluguel.setDeleted(true);
            aluguel.getCarro().setAlugado(false);

            aluguelRepository.save(aluguel);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao excluir o aluguel com ID: {}", id, e);
            throw new AluguelException("Ocorreu um erro ao excluir o aluguel.", e);
        }
    }

    private void calculoAluguel(AluguelDto payload, BigDecimal valorDiario, BigDecimal valorFranquia) throws Exception {
        long entreOsDias = ChronoUnit.DAYS.between(payload.getDataInicial(), payload.getDataFinal());
        entreOsDias = entreOsDias == 0 ? 1 : entreOsDias;

        if (entreOsDias < 1) {
            throw new Exception("O período de locação deve ser de pelo menos um dia.");
        }

        BigDecimal entreOsDiasDecimal = new BigDecimal(String.valueOf(entreOsDias));
        BigDecimal aluguelTotal = valorDiario.multiply(entreOsDiasDecimal).add(valorFranquia);

        payload.setValor(aluguelTotal);
    }

    private Aluguel getAluguel(Long id) {
        return aluguelRepository.findById(id).orElseThrow(() -> new AluguelNotFoundException(id));
    }

    private void updateCampos(Aluguel aluguel, AluguelDto payload) {
        aluguel.setDataInicial(payload.getDataInicial());
        aluguel.setDataFinal(payload.getDataFinal());
        aluguel.setValor(payload.getValor());
    }
}