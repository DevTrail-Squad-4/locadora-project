package br.edu.solutis.dev.trail.locadora.model.dto.pessoa;

import br.edu.solutis.dev.trail.locadora.model.enums.SexoEnum;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MotoristaDTO {
    private Long id;

    @NotNull(message = "Insira o email")
    @NotBlank(message = "Insira o email")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 5 e 50 caracteres")
    private String email;

    @NotNull(message = "Coloque o nome")
    @NotBlank(message = "Coloque o nome")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotNull(message = "O CPF é obrigatório")
    @NotBlank(message = "O CPF é obrigatório")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 digitos")
    private String cpf;

    @NotNull(message = "O aniversátio é obrigatorio")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate aniversario;

    @NotNull(message = "Voce deve escolher o sexo")
    @Column(name = "sexo", nullable = false)
    private SexoEnum sexo;


    @NotNull(message = "O CNH é obrigatório")
    @NotBlank(message = "O CNH é obrigatório")
    @Size(min = 11, max = 11, message = "O CNH deve ter menos de 10 caracteres")
    private String cnh;
}
