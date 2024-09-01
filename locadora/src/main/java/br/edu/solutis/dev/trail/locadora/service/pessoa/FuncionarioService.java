package br.edu.solutis.dev.trail.locadora.service.pessoa;

import br.edu.solutis.dev.trail.locadora.exception.pessoa.funcionario.FuncionarioException;
import br.edu.solutis.dev.trail.locadora.exception.pessoa.funcionario.FuncionarioNotFoundException;
import br.edu.solutis.dev.trail.locadora.mapper.GenericMapper;
import br.edu.solutis.dev.trail.locadora.model.dto.pessoa.FuncionarioDTO;
import br.edu.solutis.dev.trail.locadora.model.entity.pessoa.Funcionario;
import br.edu.solutis.dev.trail.locadora.repository.pessoa.FuncionarioRepository;
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
public class FuncionarioService implements CrudService<FuncionarioDTO> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FuncionarioService.class);
    private final FuncionarioRepository employeeRepository;
    private final GenericMapper<FuncionarioDTO, Funcionario> modelMapper;

    public FuncionarioDTO findById(Long id) {
        LOGGER.info("Encontrando funcionario com id: {}", id);
        Funcionario employee = getEmployee(id);

        return modelMapper.mapModelToDto(employee, FuncionarioDTO.class);
    }

    public PageResponse<FuncionarioDTO> findAll(int pageNo, int pageSize) {
        LOGGER.info("Encontrando funcionario com o numero da página {} ae tamanho da pagina {}.", pageNo, pageSize);

        try {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            Page<Funcionario> pagedEmployees = employeeRepository.findByDeletedFalse(paging);

            List<FuncionarioDTO> employeeDtos = modelMapper
                    .mapList(pagedEmployees.getContent(), FuncionarioDTO.class);

            PageResponse<FuncionarioDTO> pageResponse = new PageResponse<>();
            pageResponse.setContent(employeeDtos);
            pageResponse.setCurrentPage(pagedEmployees.getNumber());
            pageResponse.setTotalItems(pagedEmployees.getTotalElements());
            pageResponse.setTotalPages(pagedEmployees.getTotalPages());

            return pageResponse;

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FuncionarioException("Um erro ocorreu ao carregar um funcionario.", e);
        }
    }

    public FuncionarioDTO add(FuncionarioDTO payload) {
        try {
            LOGGER.info("Adicionando funcionario: {}", payload);

            Funcionario funcionario = employeeRepository
                    .save(modelMapper.mapDtoToModel(payload, Funcionario.class));

            return modelMapper.mapModelToDto(funcionario, FuncionarioDTO.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FuncionarioException("Um erro ocorreu ao adicionar funcionario.", e);
        }
    }

    public FuncionarioDTO update(FuncionarioDTO payload) {
        Funcionario existingEmployee = getEmployee(payload.getId());
        if (existingEmployee.isDeleted()) throw new FuncionarioNotFoundException(existingEmployee.getId());

        try {
            LOGGER.info("Editando funcionario: {}", payload);
            FuncionarioDTO funcionarioDTO = modelMapper
                    .mapModelToDto(existingEmployee, FuncionarioDTO.class);

            updateEmployeeFields(payload, funcionarioDTO);

            Funcionario funcionario = employeeRepository
                    .save(modelMapper.mapDtoToModel(funcionarioDTO, Funcionario.class));

            return modelMapper.mapModelToDto(funcionario, FuncionarioDTO.class);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FuncionarioException("Um erro ocorreu ao editar o funcionario.", e);
        }
    }

    public void deleteById(Long id) {
        FuncionarioDTO funcionarioDTO = findById(id);

        try {
            LOGGER.info("Soft deleting Employee with ID: {}", id);

            Funcionario funcionario = modelMapper.mapDtoToModel(funcionarioDTO, Funcionario.class);
            funcionario.setDeleted(true);

            employeeRepository.save(funcionario);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new FuncionarioException("Um erro ocorreu ao deletar funcionario.", e);
        }
    }

    private void updateEmployeeFields(FuncionarioDTO payload, FuncionarioDTO existingEmployee) {
        if (payload.getNome() != null) {
            existingEmployee.setNome(payload.getNome());
        }
        if (payload.getMatricula() != null) {
            existingEmployee.setMatricula(payload.getMatricula());
        }
        if (payload.getAniversario() != null) {
            existingEmployee.setAniversario(payload.getAniversario());
        }
        if (payload.getSexo() != null) {
            existingEmployee.setSexo(payload.getSexo());
        }
    }

    private Funcionario getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Funcionario com o id {} nao achado.", id);
            return new FuncionarioNotFoundException(id);
        });
    }
}
