package br.edu.solutis.dev.trail.locadora.dataSeeder;

import br.edu.solutis.dev.trail.locadora.model.entity.carro.Fabricante;
import br.edu.solutis.dev.trail.locadora.repository.carro.FabricanteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class FabricanteSeeder implements CommandLineRunner {

    private final FabricanteRepository fabricanteRepository;

    public FabricanteSeeder(FabricanteRepository fabricanteRepository) {
        this.fabricanteRepository = fabricanteRepository;
    }

    @Override
    public void run(String... args) {
        // Verificar se já existem fabricantes
        if (fabricanteRepository.count() == 0) {
            // Inserir fabricantes de exemplo
            List<Fabricante> fabricantes = List.of(
                new Fabricante(null, "Toyota", null, false, null, null),
                new Fabricante(null, "Hyundai", null, false, null, null),
                new Fabricante(null, "Ford", null, false, null, null),
                new Fabricante(null, "Volkswagen", null, false, null, null)
            );

            fabricanteRepository.saveAll(fabricantes);
        }
    }
}
