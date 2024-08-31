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
        LOGGER.info("Finding cart with ID: {}", id);

        Carrinho carrinho = carrinhoRepository.encontrarPeloId(id).orElseThrow(() -> new CarrinhoNotFoundException(id));

        return modelMapperResponse.mapModelToDto(carrinho, CarrinhoDtoResponse.class);
    }

    public CarrinhoDtoResponse encontrarPeloIdMotorista(Long motoristaId) {
        LOGGER.info("Finding cart with driver ID: {}", motoristaId);

        Carrinho carrinho = carrinhoRepository.encontrarCarrinhoPorMotoristaId(motoristaId);

        if (carrinho == null) {
            LOGGER.error("Carrinho com o ID de motorista {} not found.", motoristaId);
            throw new CarrinhoNotFoundException(motoristaId);
        }

        return modelMapperResponse.mapModelToDto(carrinho, CarrinhoDtoResponse.class);
    }

    public PageResponse<CartDtoResponse> encontrarTodos(int numeroPagina, int tamanhoPagina) {
        try {
            LOGGER.info("Fetching carts with page number {} and page size {}.", numeroPagina, tamanhoPagina);

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
            LOGGER.error("An error occurred while fetching insurance policies: {}", e.getMessage());
            throw new CarrinhoException("An error occurred while fetching cart.", e);
        }
    }

    public CarrinhoDtoResponse add(CarrinhoDto pagamento) {
        try {
            LOGGER.info("Adding a new cart: {}", pagamento);

            Carrinho carrinho = carrinhoRepository
                    .save(modelMapper.mapDtoToModel(pagamento, Carrinho.class));

            return modelMapperResponse.mapModelToDto(carrinho, CarrinhoDtoResponse.class);
        } catch (Exception e) {
            LOGGER.error("An error occurred while adding a new cart: {}", e.getMessage());
            throw new CarrinhoException("An error occurred while adding a new cart.", e);
        }
    }

    public CarrinhoDto update(CarrinhoDto pagamento) {
        try {
            LOGGER.info("Updating cart: {}", pagamento);

            Carrinho carrinhoMotorista = carrinhoRepository.findBymotoristaId(pagamento.getIdMotorista());
            if (carrinhoMotorista.isDeleted()) throw new CarrinhoNotFoundException(carrinhoMotorista.getId());

            Carrinho carrinho = carrinhoRepository.save(carrinhoMotorista);

            return modelMapper.mapModelToDto(carrinho, CarrinhoDto.class);
        } catch (Exception e) {
            LOGGER.error("An error occurred while updating cart: {}", e.getMessage());
            throw new CarrinhoException("An error occurred while updating cart.", e);
        }
    }

    public void deletarPorID(Long id) {
        Carrinho carrinho = carrinhoRepository.encontrarPeloId(id).orElseThrow();

        try {
            LOGGER.info("Soft deleting cart with ID: {}", id);

            carrinho.setDeleted(true);

            carrinhoRepository.save(carrinho);
        } catch (Exception e) {
            LOGGER.error("An error occurred while deleting cart: {}", e.getMessage());
            throw new CarrinhoException("An error occurred while deleting cart.", e);
        }
    }

    public CarrinhoDto adicionarPeloIdMotorista(long motoristaId) {
        try {
            LOGGER.info("Adding cart with driver ID: {}", motoristaId);

            Motorista motorista = getMotoristaPeloId(motoristaId);

            Carrinho carrinho = new Carrinho();
            cart.setDriver(motorista);

            Carrinho savedCart = carrinhoRepository.save(carrinho);

            return modelMapper.mapModelToDto(savedCart, CarrinhoDto.class);
        } catch (Exception e) {
            LOGGER.error("An error occurred while adding cart: {}", e.getMessage());
            throw new CarrinhoException("An error occurred while adding cart.", e);
        }
    }

    public void deletarPeloIdMotorista(long motoristaId) {
        try {
            LOGGER.info("Deleting cart with driver ID: {}", motoristaId);

            Carrinho carrinho = carrinhoRepository.encontrarPeloIdMotorista(motoristaId);

            if (carrinho != null) {
                carrinho.setDeleted(true);

                carrinhoRepository.save(carrinho);
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while deleting cart: {}", e.getMessage());
            throw new CarrinhoException("An error occurred while deleting cart.", e);
        }
    }

    public Aluguel encontrarAluguelnoCarrinhoPeloIdMotoristaEIdAluguel (long motoristaId, long aluguelId) {
        try {
            LOGGER.info("Finding rent with ID {} in cart with driver ID: {}", aluguelId, motoristaId);

            Carrinho carrinho = carrinhoRepository.findBymotoristaId(motoristaId);
            Aluguel aluguel = getAluguelPeloID(aluguelId);

            if (carrinho.getAluguels().contains(aluguel)) {
                return aluguel;
            }

            throw new AluguelNotFoundException(aluguelId);
        } catch (Exception e) {
            LOGGER.error("An error occurred while finding rent in cart: {}", e.getMessage());
            throw new CarrinhoException("An error occurred while finding rent in cart.", e);
        }
    }

    addRentToCartBymotoristaId
    public CarrinhoDto addAluguelAUmCarrinhoPeloIdMotorista (long motoristaId, long aluguelId) {
        try {
            LOGGER.info("Adding rent with ID {} to cart with driver ID: {}", aluguelId, motoristaId);

            Carrinho carrinho = carrinhoRepository.encontrarPeloIdMotorista(motoristaId);
            Aluguel aluguel = getAluguelPeloID(aluguelId);
            carrinho.getAluguels().add(aluguel);

            Carrinho updatedCarrinho = carrinhoRepository.save(carrinho);

            return modelMapper.mapModelToDto(updatedCarrinho, CarrinhoDto.class);
        } catch (Exception e) {
            LOGGER.error("An error occurred while adding rent to cart: {}", e.getMessage());
            throw new CarrinhoException("An error occurred while adding rent to cart.", e);
        }
    }

    public CarrinhoDtoResponse removerAluguelDoCarrinhoPeloIdMotorista(long motoristaId, long aluguelId) {
        try {
            LOGGER.info("Removing rent with ID {} from cart with driver ID: {}", aluguelId, motoristaId);

            Carrinho carrinho = carrinhoRepository.encontrarCarrinhoPorMotoristaId(motoristaId);
            Aluguel aluguel = getAluguelPeloID(aluguelId);
            aluguel.setCarrinho(null);
            carrinho.getAluguels().remove(aluguel);

            aluguelRepository.save(aluguel);
            Carrinho updatedCarrinho = carrinhoRepository.save(carrinho);

            return modelMapperResponse.mapModelToDto(updatedCarrinho, CarrinhoDtoResponse.class);
        } catch (Exception e) {
            LOGGER.error("An error occurred while removing rent from cart: {}", e.getMessage());
            throw new CartException("An error occurred while removing rent from cart.", e);
        }
    }

    private Motorista getMotoristaPeloIdID(long motoristaId) {
        return motoristaRepository.encontrarPeloId(motoristaId).orElseThrow(() -> {
            LOGGER.error("Driver with ID {} not found.", motoristaId);
            return new DriverNotFoundException(motoristaId);
        });
    }

    private Aluguel getAluguelPeloID(long aluguelId) {
        return aluguelRepository.encontrarPeloId(aluguelId).orElseThrow(() -> {
            LOGGER.error("Rent with ID {} not found.", aluguelId);
            return new RentNotFoundException(aluguelId);
        });
    }
}