package lv.venta.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@Table(name = "Eparaksta_logs")
public class EParakstaLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "E_ID")
    private long eId;

    @ManyToOne
    @JoinColumn(name = "Sert_ID", nullable = false)
    private Sertifikati sertifikati;

    @NotNull
    @Column(name = "Parakstisanas_datums")
    private LocalDate parakstisanasDatums;

    @NotNull
    @Column(name = "Statuss")
    private String statuss;

    @Builder
    public EParakstaLogs(Sertifikati sertifikati,
                           LocalDate parakstisanasDatums,
                           String statuss) {
        this.sertifikati = sertifikati;
        this.parakstisanasDatums = parakstisanasDatums;
        this.statuss = statuss;
    }
}
