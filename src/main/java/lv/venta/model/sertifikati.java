package lv.venta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Sertifikati")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class sertifikati {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Sert_ID")
    private long sertId;

    @ManyToOne
    @JoinColumn(name = "KD_ID", nullable = false)
    private KursaDalibnieks dalibnieks;

    @ManyToOne
    @JoinColumn(name = "KDat_ID", nullable = false)
    private KursaDatumi kursaDatums;

    @NotNull
    @Column(name = "Izsniegts_datums")
    private LocalDate izsniegtsDatums;

    @NotNull
    @Column(name = "Parakstits")
    private boolean parakstits;

    @Builder
    public sertifikati(KursaDalibnieks dalibnieks, KursaDatumi kursaDatums, LocalDate izsniegtsDatums, boolean parakstits) {
        this.dalibnieks = dalibnieks;
        this.kursaDatums = kursaDatums;
        this.izsniegtsDatums = izsniegtsDatums;
        this.parakstits = parakstits;
    }
}