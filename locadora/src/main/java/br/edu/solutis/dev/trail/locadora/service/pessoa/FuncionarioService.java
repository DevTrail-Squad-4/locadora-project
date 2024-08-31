package br.com.solutis.locadora.service.person;

import br.edu.solutis.dev.trail.locadora.model.dto.pessoa.FuncionarioDTO;
import br.edu.solutis.dev.trail.locadora.model.entity.pessoa.FuncionarioEntity;
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
    private final GenericMapper<FuncionarioDTO, FuncionarioEntity> modelMapper;

    public FuncionarioDTO> findById(Long id) {
        LOGGER.info("Encontrando funcionario com id: {}", id);
        FuncionarioEntity employee = getEmployee(id);

        return modelMapper.mapModelToDto(employee, FuncionarioDTO>.class);
    }

    public PageResponse<FuncionarioDTO> findAll(int pageNo, int pageSize) {
        LOGGER.info("Encontrando funcionario com o numero da página {} ae tamanho da pagina {}.", pageNo, pageSize);

        try {
            Pageable paging = PageRequest.of(pageNo, pageSize);
            Page<FuncionarioEntity> pagedEmployees = employeeRepository.findByDeletedFalse(paging);

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
            throw new EmployeeException("Um erro ocorreu ao carregar um funcionario.", e);
        }
    }

    public FuncionarioDTO add(FuncionarioDTO payload) {
        try {
            LOGGER.info("Adicionando funcionario: {}", payload);

            FuncionarioEntity Employee = employeeRepository
                    .save(modelMapper.mapDtoToModel(payload, Employee.class));

            return modelMapper.mapModelToDto(Employee, FuncionarioDTO.class);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new EmployeeException("Um erro ocorreu ao adicionar funcionario.", e);
        }
    }

    public FuncionarioDTO update(FuncionarioDTO payload) {
        FuncionarioEntity existingEmployee = getEmployee(payload.getId());
        if (existingEmployee.isDeleted()) throw new EmployeeNotFoundException(existingEmployee.getId());

        try {
            LOGGER.info("Editando funcionario: {}", payload);
            FuncionarioDTO EmployeeDto = modelMapper
                    .mapModelToDto(existingEmployee, FuncionarioDTO.class);

            updateEmployeeFields(payload, FuncionarioDTO);

            FuncionarioEntity Employee = employeeRepository
                    .save(modelMapper.mapDtoToModel(FuncionarioDTO, Employee.class));

            return modelMapper.mapModelToDto(Employee, FuncionarioDTO.class);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new EmployeeException("Um erro ocorreu ao editar o funcionario.", e);
        }
    }

    public void deleteById(Long id) {
        FuncionarioDTO EmployeeDto = findById(id);

        try {
            LOGGER.info("Soft deleting Employee with ID: {}", id);

            FuncionarioEntity Employee = modelMapper.mapDtoToModel(EmployeeDto, Employee.class);
            Employee.setDeleted(true);

            employeeRepository.save(Employee);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new EmployeeException("Um erro ocorreu ao delter funcionario.", e);
        }
    }

    private void updateEmployeeFields(FuncionarioDTO payload, FuncionarioDTO existingEmployee) {
        if (payload.getName() != null) {
            existingEmployee.setName(payload.getName());
        }
        if (payload.getRegistration() != null) {
            existingEmployee.setRegistration(payload.getRegistration());
        }
        if (payload.getBirthDate() != null) {
            existingEmployee.setBirthDate(payload.getBirthDate());
        }
        if (payload.getGender() != null) {
            existingEmployee.setGender(payload.getGender());
        }
    }

    private FuncionarioEntity getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> {
            LOGGER.error("Funcionario com o id {} nao achado.", id);
            return new EmployeeNotFoundException(id);
        });
    }
}
