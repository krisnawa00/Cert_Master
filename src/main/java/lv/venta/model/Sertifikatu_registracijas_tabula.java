package lv.venta.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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

    @ManyToOne
    @Column(name = "L_ID", nullable = false)
    private Lietotajs lietotajs;

    @ManyToOne
    @Column(name = "Sert_ID", nullable = false)
    private sertifikati sertifikati;


    @Builder
    public Sertifikatu_registracijas_tabula(Lietotajs lietotajs, sertifikati sertifikati) {
        this.lietotajs = lietotajs;
        this.sertifikati = sertifikati;
    }


}
