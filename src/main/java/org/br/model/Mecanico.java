package org.br.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mecanicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Mecanico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mecanico")
    private Long id;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 14, nullable = false, unique = true)
    private String cpf;

    @Column(length = 15)
    private String telefone;

    @Column(length = 50)
    private String especialidade;

    @Builder.Default
    private Boolean ativo = true;
}
