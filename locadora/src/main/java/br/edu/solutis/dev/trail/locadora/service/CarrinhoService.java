package br.edu.solutis.dev.trail.locadora.service;

import br.edu.solutis.dev.trail.locadora.exception.person.driver.DriverNotFoundException;
import br.edu.solutis.dev.trail.locadora.exception.rent.RentNotFoundException;
import br.edu.solutis.dev.trail.locadora.exception.rent.cart.CartException;
import br.edu.solutis.dev.trail.locadora.exception.rent.cart.CartNotFoundException;
import br.edu.solutis.dev.trail.locadora.mapper.GenericMapper;
import br.edu.solutis.dev.trail.locadora.model.dto.CarrinhoDtoResponse;
import br.edu.solutis.dev.trail.locadora.model.dto.aluguel.CarrinhoDto;
import br.edu.solutis.dev.trail.locadora.model.entity.person.Driver;
import br.edu.solutis.dev.trail.locadora.model.entity.Carrinho;
import br.edu.solutis.dev.trail.locadora.model.entity.Aluguel;
import br.edu.solutis.dev.trail.locadora.repository.person.DriverRepository;
import br.edu.solutis.dev.trail.locadora.repository.rent.CartRepository;
import br.edu.solutis.dev.trail.locadora.repository.rent.RentRepository;
import br.edu.solutis.dev.trail.locadora.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.REQUIRED)
public class CarrinhoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CarrinhoService.class);
    private final CarrinhoRepository carrinhoRepository;
    private final AluguelRepository aluguelRepository;
    private final MotoristaRepository motoristaRepository;
    private final GenericMapper<CarrinhoDto, Carrinho> modelMapper;
    private final GenericMapper<CarrinhoDtoResponse,Carrinho> modelMapperResponse;

    public CarrinhoDtoResponse encontrarPeloId(Long id) {
        LOGGER.info("Procurando carrinho com o ID: {}", id);

        Carrinho carrinho = carrinhoRepository.encontrarPeloId(id).orElseThrow(() -> new CarrinhoNotFoundException(id));

        return modelMapperResponse.mapModelToDto(carrinho, CarrinhoDtoResponse.class);
    }

    public CarrinhoDtoResponse encontrarPeloIdMotorista(Long motoristaId) {
        LOGGER.info("Buscando carrinho com o ID do motorista: {}", motoristaId);

        Carrinho carrinho = carrinhoRepository.encontrarCarrinhoPorMotoristaId(motoristaId);

        if (carrinho == null) {
            LOGGER.error("Carrinho com o ID de motorista {} nao encontrado.", motoristaId);
            throw new CarrinhoNotFoundException(motoristaId);
        }

        return modelMapperResponse.mapModelToDto(carrinho, CarrinhoDtoResponse.class);
    }

    public PageResponse<CarrinhoDtoResponse> encontrarTodos(int numeroPagina, int tamanhoPagina) {
        try {
            LOGGER.info("Buscando carrinhos com número da página {} e tamanho da página {}.", numeroPagina, tamanhoPagina);

            Pageable paginacao = PageRequest.of(numeroPagina, tamanhoPagina);
            Page<Carrinho> carrinhosPaginados = carrinhoRepository.encontrarTodos(paginacao);

            List<CarrinhoDtoResponse> carrinhosDtoResponse = modelMapperResponse.
                    mapList(carrinhosPaginados.getContent(), CarrinhoDtoResponse.class);

            PageResponse<CarrinhoDtoResponse> pageResponse = new PageResponse<>();
            pageResponse.setContent(carrinhosDtoResponse);
            pageResponse.setCurrentPage(carrinhosPaginados.getNumber());
            pageResponse.setTotalItems(carrinhosPaginados.getTotalElements());
            pageResponse.setTotalPages(carrinhosPaginados.getTotalPages());

            return pageResponse;
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao buscar apólices de seguro: {}", e.getMessage());
            throw new CarrinhoException("Ocorreu um erro ao buscar o carrinho.", e);
        }
    }

    public CarrinhoDtoResponse add(CarrinhoDto payload) {
        try {
            LOGGER.info("Adicionando um novo carrinho: {}", payload);

            Carrinho carrinho = carrinhoRepository
                    .save(modelMapper.mapDtoToModel(payload, Carrinho.class));

            return modelMapperResponse.mapModelToDto(carrinho, CarrinhoDtoResponse.class);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao adicionar um novo carrinho: {}", e.getMessage());
            throw new CarrinhoException("Ocorreu um erro ao adicionar um novo carrinho.", e);
        }
    }

    public CarrinhoDto update(CarrinhoDto payload) {
        try {
            LOGGER.info("Atualizando carrinho: {}", payload);

            Carrinho carrinhoMotorista = carrinhoRepository.findBymotoristaId(payload.getIdMotorista());
            if (carrinhoMotorista.isDeleted()) throw new CarrinhoNotFoundException(carrinhoMotorista.getId());

            Carrinho carrinho = carrinhoRepository.save(carrinhoMotorista);

            return modelMapper.mapModelToDto(carrinho, CarrinhoDto.class);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao atualizar o carrinho: {}", e.getMessage());
            throw new CarrinhoException("Ocorreu um erro ao atualizar o carrinho.", e);
        }
    }

    public void deletarPorID(Long id) {
        Carrinho carrinho = carrinhoRepository.encontrarPeloId(id).orElseThrow();

        try {
            LOGGER.info("Excluindo o carrinho com ID: {}", id);

            carrinho.setDeleted(true);

            carrinhoRepository.save(carrinho);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao excluir o carrinho: {}", e.getMessage());
            throw new CarrinhoException("Ocorreu um erro ao excluir o carrinho.", e);
        }
    }

    public CarrinhoDto adicionarPeloIdMotorista(long motoristaId) {
        try {
            LOGGER.info("Adicionando carrinho com ID do motorista: {}", motoristaId);

            Motorista motorista = getMotoristaPeloId(motoristaId);

            Carrinho carrinho = new Carrinho();
            cart.setDriver(motorista);

            Carrinho savedCart = carrinhoRepository.save(carrinho);

            return modelMapper.mapModelToDto(savedCart, CarrinhoDto.class);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao adicionar o carrinho: {}", e.getMessage());
            throw new CarrinhoException("Ocorreu um erro ao adicionar o carrinho.", e);
        }
    }

    public void deletarPeloIdMotorista(long motoristaId) {
        try {
            LOGGER.info("Excluindo carrinho com ID do motorista: {}", motoristaId);

            Carrinho carrinho = carrinhoRepository.encontrarPeloIdMotorista(motoristaId);

            if (carrinho != null) {
                carrinho.setDeleted(true);

                carrinhoRepository.save(carrinho);
            }
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao excluir o carrinho: {}", e.getMessage());
            throw new CarrinhoException("Ocorreu um erro ao excluir o carrinho.", e);
        }
    }

    public Aluguel encontrarAluguelnoCarrinhoPeloIdMotoristaEIdAluguel (long motoristaId, long aluguelId) {
        try {
            LOGGER.info("Buscando aluguel com ID {} no carrinho com ID do motorista: {}", aluguelId, motoristaId);

            Carrinho carrinho = carrinhoRepository.findBymotoristaId(motoristaId);
            Aluguel aluguel = getAluguelPeloID(aluguelId);

            if (carrinho.getAluguels().contains(aluguel)) {
                return aluguel;
            }

            throw new AluguelNotFoundException(aluguelId);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao buscar o aluguel no carrinho: {}", e.getMessage());
            throw new CarrinhoException("Ocorreu um erro ao buscar o aluguel no carrinho.", e);
        }
    }

    addRentToCartBymotoristaId
    public CarrinhoDto addAluguelAUmCarrinhoPeloIdMotorista (long motoristaId, long aluguelId) {
        try {
            LOGGER.info("Adicionando aluguel com ID {} ao carrinho com ID do motorista: {}", aluguelId, motoristaId);

            Carrinho carrinho = carrinhoRepository.encontrarPeloIdMotorista(motoristaId);
            Aluguel aluguel = getAluguelPeloID(aluguelId);
            carrinho.getAluguels().add(aluguel);

            Carrinho updatedCarrinho = carrinhoRepository.save(carrinho);

            return modelMapper.mapModelToDto(updatedCarrinho, CarrinhoDto.class);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao adicionar o aluguel ao carrinho: {}", e.getMessage());
            throw new CarrinhoException("Ocorreu um erro ao adicionar o aluguel ao carrinho.", e);
        }
    }

    public CarrinhoDtoResponse removeAluguelFromCarrinhoByMotoristaId(long motoristaId, long aluguelId) {
        try {
            LOGGER.info("Removendo aluguel com ID {} do carrinho com ID do motorista: {}", aluguelId, motoristaId);

            Carrinho carrinho = carrinhoRepository.encontrarCarrinhoPorMotoristaId(motoristaId);
            Aluguel aluguel = getAluguelPeloID(aluguelId);
            aluguel.setCarrinho(null);
            carrinho.getAluguels().remove(aluguel);

            aluguelRepository.save(aluguel);
            Carrinho updatedCarrinho = carrinhoRepository.save(carrinho);

            return modelMapperResponse.mapModelToDto(updatedCarrinho, CarrinhoDtoResponse.class);
        } catch (Exception e) {
            LOGGER.error("Ocorreu um erro ao remover o aluguel do carrinho: {}", e.getMessage());
            throw new CarrinhoException("Ocorreu um erro ao remover o aluguel do carrinho.", e);
        }
    }

    private Motorista getMotoristaPeloIdID(long motoristaId) {
        return motoristaRepository.encontrarPeloId(motoristaId).orElseThrow(() -> {
            LOGGER.error("Motorista com ID {} não encontrado.", motoristaId);
            return new DriverNotFoundException(motoristaId);
        });
    }

    private Aluguel getAluguelPeloID(long aluguelId) {
        return aluguelRepository.encontrarPeloId(aluguelId).orElseThrow(() -> {
            LOGGER.error("Aluguel com ID {} não encontrado.", aluguelId);
            return new RentNotFoundException(aluguelId);
        });
    }
}