package br.edu.solutis.dev.trail.locadora.service;

import br.edu.solutis.dev.trail.locadora.mapper.MotoristaMapper;
import br.edu.solutis.dev.trail.locadora.model.dto.pessoa.MotoristaDTO;
import br.edu.solutis.dev.trail.locadora.model.entity.pessoa.MotoristaEntity;
import br.edu.solutis.dev.trail.locadora.repository.pessoa.MotoristaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class MotoristaService extends CrudService<MotoristaDTO> {
    @Autowired
    private MotoristaRepository repository;

    @Autowired
    private MotoristaMapper mapper;

    @Override
    public MotoristaDTO obterPorId(Long id) {
        return this.mapper.modelToDTO(this.repository.findById(id)
                .orElseThrow());
    }

    @Override
    public List<MotoristaDTO> obterTodos() {
        List<MotoristaEntity> colaboradores = this.repository.findAll();

        return colaboradores.stream().map(this.mapper::modelToDTO).toList();
    }

    @Override
    public MotoristaDTO salvar(MotoristaDTO payload) {
        MotoristaEntity motoristaSalvo = this.repository.save(this.mapper.dtoToModel(payload));
        return this.mapper.modelToDTO(motoristaSalvo);
    }

    @Override
    public void excluirPorId(Long id) {
        this.repository.deleteById(id);
    }
}
