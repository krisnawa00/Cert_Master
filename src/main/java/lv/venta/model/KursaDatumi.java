package lv.venta.model;

import java.util.Collection;

import org.springframework.format.annotation.DateTimeFormat;

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

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@Table(name = "Kursa_datumi")

public class KursaDatumi {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "KDAT_ID")
    private long kdatId;

    
    @ManyToOne
    @JoinColumn(name = "K_ID", nullable = false) 
    private Kurss kurss;
    
    @ManyToOne
    @JoinColumn(name = "P_ID", nullable = false) 
    private Pasniedzeji pasniedzeji;

    
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Sakuma_datums")
    private String sakumaDatums;


    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Beiga_datums")
    private String beigadatums;

    @OneToMany(mappedBy = "kursaDatums")
    @ToString.Exclude
    private Collection<Vertejums> vertejumi;

    @OneToMany(mappedBy = "kursaDatums")
    @ToString.Exclude
    private Collection<sertifikati> sertifikati;




    @Builder
    public KursaDatumi(Kurss kurss, Pasniedzeji pasniedzeji, String sakumaDatums, String beigadatums) {
        this.kurss = kurss;
        this.pasniedzeji = pasniedzeji;
        this.sakumaDatums = sakumaDatums;
        this.beigadatums = beigadatums;
    }
}
