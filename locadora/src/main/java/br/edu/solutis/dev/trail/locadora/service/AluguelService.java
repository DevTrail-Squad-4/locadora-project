package br.edu.solutis.dev.trail.locadora.service;



import br.edu.solutis.dev.trail.locadora.exception.car.CarAlreadyRentedException;
import br.edu.solutis.dev.trail.locadora.exception.car.CarException;
import br.edu.solutis.dev.trail.locadora.exception.car.CarNotRentedException;
import br.edu.solutis.dev.trail.locadora.exception.person.driver.DriverException;
import br.edu.solutis.dev.trail.locadora.exception.person.driver.DriverNotAuthorizedException;
import br.edu.solutis.dev.trail.locadora.exception.rent.RentAlreadyConfirmedException;
import br.edu.solutis.dev.trail.locadora.exception.rent.RentException;
import br.edu.solutis.dev.trail.locadora.exception.rent.RentNotConfirmedException;
import br.edu.solutis.dev.trail.locadora.exception.rent.RentNotFoundException;
import br.edu.solutis.dev.trail.locadora.mapper.GenericMapper;
import br.edu.solutis.dev.trail.locadora.model.dto.aluguel.AluguelDto;
import br.edu.solutis.dev.trail.locadora.model.dto.rent.RentDtoResponse;
import br.edu.solutis.dev.trail.locadora.model.entity.car.Car;
import br.edu.solutis.dev.trail.locadora.model.entity.person.Driver;
import br.edu.solutis.dev.trail.locadora.model.entity.rent.InsurancePolicy;
import br.edu.solutis.dev.trail.locadora.model.entity.Aluguel;
import br.edu.solutis.dev.trail.locadora.model.entity.ApoliceSeguro;
import br.edu.solutis.dev.trail.locadora.repository.car.CarRepository;
import br.edu.solutis.dev.trail.locadora.repository.person.DriverRepository;
import br.edu.solutis.dev.trail.locadora.repository.rent.InsurancePolicyRepository;
import br.edu.solutis.dev.trail.locadora.repository.rent.RentRepository;
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

    private final AluguelRepository AluguelRepository;
    private final GenericMapper<AluguelDto, Aluguel> modelMapper;
    private final GenericMapper<AluguelDtoResponse, Aluguel> modelMapperResponse;
    private final ApoliceSeguroRepository apoliceSeguroRepository;
    private final CarroRepository carroRepository;
    private final MotoristaRepository MotoristaRepository;

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

            PageResponse<RentDtoResponse> pageResponse = new PageResponse<>();
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
            calculoAluguel(payload, carro.getValorDiário(), apoliceSeguro.getValorFranquia());

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
            throw new CareoException("O carro com o ID " + aluguel.getCarro().getId() + " ja está alugado.", e);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao confirmar o aluguel com ID {}", id, e);
            throw new AluguelException("Ocorreu um erro ao confirmar o aluguel.", e);
        }
    }
        
    public AluguelDtoResponse finishedAluguel(long motoristaId, long aluguelId) {
        LOGGER.info("Finalizando aluguel com ID: {}", aluguelId);

        Aluguel aluguel = getAluguel(aluguelId);

        try {
            if (!aluguel.isConfirmado()) {
                throw new AluguelNotConfirmedException(aluguelId);
            } else if (aluguel.isDeleted()) {
                throw new AluguelNotFoundException(aluguelId);
            } else if (!aluguel.getCar().isAlugado()) {
                throw new CarroNotRentedException(aluguel.getCarro().getId());
            } else if (aluguel.getMotorista().getId() != motoristaId) {
                throw new MotoristaNotAuthorizedException(motoristaId);
            }

            aluguel.setFinalizado(true);
            aluguel.setDataFinalizada(LocalDate.now());
            aluguel.getCar().setAlugado(false);

            return modelMapperResponse.mapModelToDto(aluguelRepository.save(aluguel), AluguelDtoResponse.class);
        } catch (RentNotConfirmedException e) {
            LOGGER.error("Ocorreu um erro ao confirmar o aluguel com ID {}", aluguelId, e);
            throw new RentException("O aluguel com o ID " + aluguelId + " nao foi confirmado.", e);
        } catch (RentNotFoundException e) {
            LOGGER.error("Ocorreu um erro ao confirmar o aluguel com ID {}", aluguelId, e);
            throw new RentException("O aluguel com o ID " + aluguelId + " nao foi encontrado.", e);
        } catch (CarNotRentedException e) {
            LOGGER.error("Ocorreu um erro ao confirmar o aluguel com ID {}", aluguelId, e);
            throw new CarException("O carro com o ID " + aluguel.getCarro().getId() + " nao esta alugado.", e);
        } catch (DriverNotAuthorizedException e) {
            LOGGER.error("Ocorreu um erro ao confirmar o aluguel com ID {}", motoristaId, e);
            throw new DriverException("O motorista com o ID " + motoristaId + " nao esta autorizado.", e);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao confirmar o aluguel com ID {}", aluguelId, e);
            throw new RentException("Ocorreu um erro ao confirmar o aluguel.", e);
        }
    }

    public List<AluguelDtoResponse> findAlugueisAtivos(){
        List<Rent> alugueisAtivos = aluguelRepository.findByNaoFinalizado();

        try {
            return modelMapperResponse.mapList(alugueisAtivos, AluguelDtoResponse.class);
        } catch (Exception e) {
            throw new RentException("Ocorreu um erro ao buscar alugueis ativos.", e);
        }
    }

    public List<RentDtoResponse> findAlugueisFinalizados() {
        List<Rent> alugueisFinalizados = alugueisRepository.findByFinalizado();

        try {
            return modelMapperResponse.mapList(alugueisFinalizados, AluguelDtoResponse.class);
        } catch (Exception e) {
            throw new RentException("Ocorreu um erro ao buscar aluguéis finalizados.", e);
        }
    }

    public AluguelDtoResponse update(AluguelDto payload) {
        try {
            LOGGER.info("Atualizando aluguel com ID: {}", payload.getId());

            Aluguel aluguel = getAlugado(payload.getId());
            if (aluguel.isConfirmado()) {
                throw new Exception("Esse aluguel ja esta confirmado.");
            } else if (rent.isDeleted()) {
                throw new AluguelNotFoundException(payload.getId());
            }

            updateCampos(aluguel, payload);

            return modelMapperResponse.mapModelToDto(rentRepository.save(aluguel), AluguelDtoResponse.class);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao atualizar o aluguel com ID: {}", payload.getId(), e);
            throw new RentException("Ocorreu um erro ao atualizar o aluguel.", e);
        }
    }

    public void deleteByID(Long id) {
        LOGGER.info("Excluindo aluguel com ID: {}", id);

        Aluguel aluguel = getAluguel(id);

        try {

            aluguel.setDeleted(true);
            aluguel.getCarro().setAlugado(false);

            rentRepository.save(aluguel);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao excluir o aluguel com ID: {}", id, e);
            throw new RentException("Ocorreu um erro ao excluir o aluguel.", e);
        }
    }

    private void aluguelCalculator(AluguelDto payload, BigDecimal valorDiario, BigDecimal valorFranquia) throws Exception {
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
        return aluguelRepository.encontrarPeloId(id).orElseThrow(() -> new AluguelNotFoundException(id));
    }

    private void updateCampos(Aluguel aluguel, AluguelDto payload) {
        aluguel.setDataInicial(payload.getDataInicial());
        aluguel.setDataFinal(payload.getDataFinal());
        aluguel.setValor(payload.getValor());
    }
}