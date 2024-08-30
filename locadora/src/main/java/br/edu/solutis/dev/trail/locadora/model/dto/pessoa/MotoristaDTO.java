package br.edu.solutis.dev.trail.locadora.model.dto.pessoa;

import br.edu.solutis.dev.trail.locadora.model.enums.SexoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.NotFound;

import java.time.LocalDate;

@Data
public class MotoristaDTO {
    private Long id;

    @NotNull(message = "O nome é obrigatório")
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 4, max = 255, message = "O nome deve ter entre 4 e 255 letras")
    private String nome;

    @NotFound(message = "O CNH é obrigatório")
    @NotBlank(message = "O CNH é obrigatório")
    @Size(max = 10, message = "CNH must be less than 10 characters long")
    private String cnh;

    @NotNull(message = "CPF é obrigatório")
    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 14, max = 14, message = "CPF deve ter 14 caracteres")
    private String cpf;

    @NotNull(message = "A data de aniversário é obrigatória")
    private LocalDate aniversario;

    @NotNull(message = "O genero é obrigatória)
    @Column(name = "sexo", nullable = false)
    private SexoEnum sexo;
}
