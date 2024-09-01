package br.edu.solutis.dev.trail.locadora.dataSeeder;

import br.edu.solutis.dev.trail.locadora.model.entity.carro.Carro;
import br.edu.solutis.dev.trail.locadora.model.entity.carro.Modelo;
import br.edu.solutis.dev.trail.locadora.repository.carro.CarroRepository;
import br.edu.solutis.dev.trail.locadora.repository.carro.ModeloRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Order(3)
public class CarroSeeder implements CommandLineRunner {

    private final CarroRepository carroRepository;
    private final ModeloRepository modeloRepository;

    public CarroSeeder(CarroRepository carroRepository, ModeloRepository modeloRepository) {
        this.carroRepository = carroRepository;
        this.modeloRepository = modeloRepository;
    }

    @Override
    public void run(String... args) {
        // Verificar se já existem carros
        if (carroRepository.count() == 0) {
            // Buscar modelos
            Optional<Modelo> modeloA = modeloRepository.findById(1L);
            Optional<Modelo> modeloB = modeloRepository.findById(2L);

            if (modeloA.isPresent() && modeloB.isPresent()) {
                // Inserir carros de exemplo
                List<Carro> carros = List.of(
                    new Carro(null, "ABC1234", "CH123456789", "Preto", new BigDecimal("120.00"), "http://example.com/image1.jpg", false, modeloA.get(), null, null, false, LocalDateTime.now(), LocalDateTime.now()),
                    new Carro(null, "XYZ5678", "CH987654321", "Branco", new BigDecimal("150.00"), "http://example.com/image2.jpg", false, modeloB.get(), null, null, false, LocalDateTime.now(), LocalDateTime.now()),
                    new Carro(null, "DEF9012", "CH456789123", "Vermelho", new BigDecimal("180.00"), "http://example.com/image3.jpg", false, modeloA.get(), null, null, false, LocalDateTime.now(), LocalDateTime.now()),
                    new Carro(null, "GHI3456", "CH321654987", "Azul", new BigDecimal("200.00"), "http://example.com/image4.jpg", false, modeloB.get(), null, null, false, LocalDateTime.now(), LocalDateTime.now()),
                    new Carro(null, "JKL7890", "CH654321987", "Verde", new BigDecimal("220.00"), "http://example.com/image5.jpg", false, modeloA.get(), null, null, false, LocalDateTime.now(), LocalDateTime.now()),
                    new Carro(null, "MNO2345", "CH789654321", "Amarelo", new BigDecimal("250.00"), "http://example.com/image6.jpg", false, modeloB.get(), null, null, false, LocalDateTime.now(), LocalDateTime.now()),
                    new Carro(null, "PQR6789", "CH987123456", "Roxo", new BigDecimal("280.00"), "http://example.com/image7.jpg", false, modeloA.get(), null, null, false, LocalDateTime.now(), LocalDateTime.now()),
                    new Carro(null, "STU0123", "CH654987321", "Laranja", new BigDecimal("300.00"), "http://example.com/image8.jpg", false, modeloB.get(), null, null, false, LocalDateTime.now(), LocalDateTime.now()),
                    new Carro(null, "VWX4567", "CH321789654", "Cinza", new BigDecimal("350.00"), "http://example.com/image9.jpg", false, modeloA.get(), null, null, false, LocalDateTime.now(), LocalDateTime.now()),
                    new Carro(null, "YZA8901", "CH987321456", "Prata", new BigDecimal("400.00"), "http://example.com/image10.jpg", false, modeloB.get(), null, null, false, LocalDateTime.now(), LocalDateTime.now())
                );

                carroRepository.saveAll(carros);
            } else {
                throw new RuntimeException("Modelos não encontrados. Certifique-se de que os modelos foram inseridos antes de adicionar carros.");
            }
        }
    }
}
