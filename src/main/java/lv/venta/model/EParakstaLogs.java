package lv.venta.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

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
    private sertifikati sertifikati;

    @NotNull
    @Column(name = "Parakstisanas_datums")
    private LocalDate parakstisanasDatums;

    @NotNull
    @Column(name = "Statuss")
    private String statuss;

    @Builder
    public EParakstaLogs(sertifikati sertifikati,
                           LocalDate parakstisanasDatums,
                           String statuss) {
        this.sertifikati = sertifikati;
        this.parakstisanasDatums = parakstisanasDatums;
        this.statuss = statuss;
    }
}