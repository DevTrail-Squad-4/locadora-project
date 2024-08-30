package br.edu.solutis.dev.trail.locadora.model.dto.pessoa;

import br.edu.solutis.dev.trail.locadora.model.enums.SexoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FuncionarioDTO {
    private Long id;

    @NotNull(message = "O CNH é obrigatorio")
    @NotBlank(message = "O CNH é obrigatorio")
    @Size(max = 10, message = "O CNH deve ter menos de 10 caracteres")
    private String cnh;

    @NotNull(message = "Coloque o nome")
    @NotBlank(message = "Coloque o nome")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String nome;

    @NotNull(message = "O CPF é obrigatório")
    @NotBlank(message = "O CPF é obrigatório")
    @Size(min = 14, max = 14, message = "O CPF deve ter 14 numeros")
    private String cpf;

    @NotNull(message = "O aniversátio é obrigatorio")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDate aniversario;

    @NotNull(message = "Voce deve escolher o sexo")
    @Column(name = "sexo", nullable = false)
    private SexoEnum sexo;

    @NotNull(message = "A matrícula é obrigatoria")
    @NotBlank(message = "A matrícula é obrigatoria")
    @Size(max = 255, message = "A matrícula deve ter menos de 255 caracteres")
    private String matricula;
}
