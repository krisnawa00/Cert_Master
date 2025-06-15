package lv.venta.model;

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
@Table(name = "Sertifikatu_registracijas_tabula")
public class Sertifikatu_registracijas_tabula {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SRT_ID")
    private long srtId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "L_ID", nullable = false)
    private Lietotajs lietotajs;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Sert_ID", nullable = false)
    private sertifikati sertifikati;

    @NotNull
    @Column(name = "Registracijas_datums")
    private String registracijas_datums;

    @Builder
    public Sertifikatu_registracijas_tabula(Lietotajs lietotajs, sertifikati sertifikati, String registracijas_datums) {
        this.lietotajs = lietotajs;
        this.sertifikati = sertifikati;
        this.registracijas_datums = registracijas_datums;
    }
}

