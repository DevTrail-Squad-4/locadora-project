package br.edu.solutis.dev.trail.locadora.model.dto.pessoa;

import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.NotFound;

public class FuncionarioDTO {
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

    @NotNull(message = "O genero é obrigatória)
            @Column(name = "sexo", nullable = false)
    private SexoEnum sexo;

    @NotNull(message = "A matricula é obrigatório)
    @NotBlank(message = "A matricula é obrigatório")
    @Size(max = 255, message = "A matricula deve ter menos de 255 letras")
    private String matricula;
}

