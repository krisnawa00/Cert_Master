package lv.venta.model;

import java.time.LocalDate;
import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Sertifikati")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Sertifikati {

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

    @OneToMany(mappedBy = "sertifikati")
    @ToString.Exclude
    private Collection<Sertifikatu_registracijas_tabula> sertifikatuRegistracijasTabula;
    
    @OneToMany(mappedBy = "sertifikati")
    @ToString.Exclude
    private Collection<EParakstaLogs> EparakstsLogs ;
    
    
    @NotNull
    @Column(name = "Izsniegts_datums")
    private LocalDate izsniegtsDatums;

    @NotNull
    @Column(name = "Parakstits")
    private boolean parakstits;

    @Builder
    public Sertifikati(KursaDalibnieks dalibnieks, KursaDatumi kursaDatums, LocalDate izsniegtsDatums, boolean parakstits) {
        this.dalibnieks = dalibnieks;
        this.kursaDatums = kursaDatums;
        this.izsniegtsDatums = izsniegtsDatums;
        this.parakstits = parakstits;
    }
}
