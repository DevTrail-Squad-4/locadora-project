package br.edu.solutis.dev.trail.locadora.service;


import br.edu.solutis.dev.trail.locadora.exception.rent.insurace.InsurancePolicyException;
import br.edu.solutis.dev.trail.locadora.exception.rent.insurace.InsurancePolicyNotFoundException;
import br.edu.solutis.dev.trail.locadora.mapper.GenericMapper;
import br.edu.solutis.dev.trail.locadora.model.dto.aluguel.ApoliceSeguroDto;
import br.edu.solutis.dev.trail.locadora.model.entity.ApoliceSeguro;
import br.edu.solutis.dev.trail.locadora.repository.rent.InsurancePolicyRepository;
import br.edu.solutis.dev.trail.locadora.response.PageResponse;
import br.edu.solutis.dev.trail.locadora.service.CrudService;
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
public class ApoliceSeguroService implements CrudService<ApoliceSeguroDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApoliceSeguroService.class);
    private final ApoliceSeguroRepository apoliceSeguroRepository;
    private final GenericMapper<ApoliceSeguroDto, ApoliceSeguro> modelMapper;

    public ApoliceSeguroDto encontrarPeloId(Long id) {
        LOGGER.info("Buscando apólice de seguro com ID: {}", id);
        ApoliceSeguro apoliceSeguro = getApoliceSeguro(id);

        return modelMapper.mapModelToDto(apoliceSeguro, ApoliceSeguroDto.class);
    }

    public PageResponse<ApoliceSeguroDto> encontrarTodos(int numeroPagina, int tamanhoPagina) {
        try {
            LOGGER.info("Buscando apólices de seguro com número da página {} e tamanho da página {}.", numeroPagina, tamanhoPagina);

            Pageable paginacao = PageRequest.of(numeroPagina, tamanhoPagina);
            Page<ApoliceSeguro> apoliceSeguroPaginado = apoliceSeguroRepository.encontrarPeloNaoDeletado(paginacao);

            List<ApoliceSeguroDto> apoliceSeguroDtos = modelMapper.
                    mapList(ApoliceSeguroPaginado.getContent(), ApoliceSeguroDto.class);

            PageResponse<ApoliceSeguroDto> pageResponse = new PageResponse<>();
            pageResponse.setContent(apoliceSeguroDtos);
            pageResponse.setCurrentPage(apoliceSeguroPaginado.getNumber());
            pageResponse.setTotalItems(apoliceSeguroPaginado.getTotalElements());
            pageResponse.setTotalPages(apoliceSeguroPaginado.getTotalPages());

            return pageResponse;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ApoliceSeguroException("Ocorreu um erro ao buscar apolices de seguro.", e);
        }
    }

    public ApoliceSeguroDto add(ApoliceSeguroDto payload) {
        try {
            LOGGER.info("Adicionando apólice de seguro: {}", payload);

            ApoliceSeguro apoliceSeguro = apoliceSeguroRepository
                    .save(modelMapper.mapDtoToModel(payload, ApoliceSeguro.class));

            return modelMapper.mapModelToDto(apoliceSeguro, ApoliceSeguro.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ApoliceSeguroException("Ocorreu um erro ao adicionar a apólice de seguro.", e);
        }
    }

    public ApoliceSeguroDto update(ApoliceSeguroDto payload) {
        ApoliceSeguro apoliceSeguroExiste = getApoliceSeguro(payload.getId());
        if (apoliceSeguroExiste.isDeletado())
            throw new ApoliceSeguroNotFoundException(apoliceSeguroExiste.getId());

        try {
            LOGGER.info("Atualizando apólice de seguro: {}", payload);
            ApoliceSeguroDto apoliceSeguroDto = modelMapper
                    .mapModelToDto(apoliceSeguroExiste, ApoliceSeguroDto.class);

            updateModelFields(payload, apoliceSeguroDto);

            ApoliceSeguro apoliceSeguro = apoliceSeguroRepository
                    .save(modelMapper.mapDtoToModel(apoliceSeguroDto, ApoliceSeguro.class));

            return modelMapper.mapModelToDto(apoliceSeguro, ApoliceSeguroDto.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ApoliceSeguroException("Ocorreu um erro ao atualizar a apólice de seguro.", e);
        }
    }

    public void deleteById(Long id) {
        ApoliceSeguroDto apoliceSeguroDto = encontrarPeloId(id);

        try {
            LOGGER.info("Excluindo a apólice de seguro com ID: {}", id);

            ApoliceSeguro apoliceSeguro = modelMapper.mapDtoToModel(apoliceSeguroDto, ApoliceSeguro.class);
            apoliceSeguro.setDeletado(true);

            apoliceSeguroRepository.save(apoliceSeguro);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ApoliceSeguroException("Ocorreu um erro ao excluir a apólice de seguro.", e);
        }
    }

    private void updateModelFields(ApoliceSeguroDto payload, ApoliceSeguroDto apoliceSeguroExiste) {
        if (payload.getValorFranquia() != null) {
            apoliceSeguroExiste.setValorFranquia(payload.getValorFranquia());
        }

        apoliceSeguroExiste.setProtecaoTerceiro(payload.isProtecaoTerceiro());
        apoliceSeguroExiste.setProtecaoCausasNaturais(payload.isProtecaoCausasNaturais());
        apoliceSeguroExiste.setProtecaoRoubo(payload.isProtecaoRoubo());
    }

    private ApoliceSeguro getApoliceSeguro(Long id) {
        return apoliceSeguroRepository.encontrarPeloId(id).orElseThrow(() -> {
            LOGGER.error("Apólice de seguro com ID {} não encontrada.", id);
            return new ApoliceSeguroNotFoundException(id);
        });
    }
}