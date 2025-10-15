package lv.venta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Vērtējumi")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Vertejums {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "V_ID")
    private long vId;

    @ManyToOne
    @JoinColumn(name = "KDat_ID", nullable = false)
    private KursaDatumi kursaDatums;

    @ManyToOne
    @JoinColumn(name = "KD_ID", nullable = false)
    private KursaDalibnieks dalibnieks;

    @NotNull
    @Pattern(regexp = "^(10|[1-9])$", message = "Vertejumam jabut 1-10 skalā")
    @Column(name = "Vertejums")
    private String vertejums;

    @NotNull
    @Column(name = "Datums")
    private LocalDate datums;
    
    

    @Builder
    public Vertejums(KursaDatumi kursaDatums, KursaDalibnieks dalibnieks, String vertejums, LocalDate datums) {
        this.kursaDatums = kursaDatums;
        this.dalibnieks = dalibnieks;
        this.vertejums = vertejums;
        this.datums = datums;
    }
}

