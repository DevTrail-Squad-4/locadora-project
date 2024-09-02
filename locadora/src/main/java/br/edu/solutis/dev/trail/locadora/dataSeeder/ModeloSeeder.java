package br.edu.solutis.dev.trail.locadora.dataSeeder;

import br.edu.solutis.dev.trail.locadora.model.entity.carro.Modelo;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Fabricante;
import br.edu.solutis.dev.trail.locadora.model.enums.ModeloCategoriaEnum;
import br.edu.solutis.dev.trail.locadora.repository.carro.ModeloRepository;
import br.edu.solutis.dev.trail.locadora.repository.carro.FabricanteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Order(2)
public class ModeloSeeder implements CommandLineRunner {

    private final ModeloRepository modeloRepository;
    private final FabricanteRepository fabricanteRepository;

    public ModeloSeeder(ModeloRepository modeloRepository, FabricanteRepository fabricanteRepository) {
        this.modeloRepository = modeloRepository;
        this.fabricanteRepository = fabricanteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (modeloRepository.count() > 0) {
            return; // Se já houver dados, não faz nada
        }

        // Carregar os fabricantes para referência
        Fabricante fabricante1 = fabricanteRepository.findById(1L).orElseThrow(() -> new RuntimeException("Fabricante não encontrado"));
        Fabricante fabricante2 = fabricanteRepository.findById(2L).orElseThrow(() -> new RuntimeException("Fabricante não encontrado"));

        // Criar e salvar os modelos
        Modelo modelo1 = new Modelo(null, "Sedan Compacto", ModeloCategoriaEnum.COMPACT_SEDAN, fabricante1, null, false, LocalDateTime.now(), LocalDateTime.now());
        Modelo modelo2 = new Modelo(null, "Hatchback Compacto", ModeloCategoriaEnum.COMPACT_HATCHBACK, fabricante2, null, false, LocalDateTime.now(), LocalDateTime.now());

        modeloRepository.saveAll(List.of(modelo1, modelo2));
    }
}
