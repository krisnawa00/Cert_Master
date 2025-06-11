package lv.venta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import lv.venta.model.enums.Limenis;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Kurss")
public class Kurss {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "K_ID")
    private long kId;

    @NotNull
    @Column(name = "Nosaukums")
    private String nosaukums;

    @NotNull
    @Min(1)
    @Max(100)
    @Column(name = "Stundas")
    private int stundas;

    @NotNull
    @Column(name = "limenis")
    private Limenis limenis;

    @Builder
    public Kurss(String nosaukums, int stundas, Limenis limenis) {
        this.nosaukums = nosaukums;
        this.stundas = stundas;
        this.limenis = limenis;
    }
}

