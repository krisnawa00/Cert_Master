package lv.venta.model;

import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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

    @OneToMany(mappedBy = "kurss")
    @ToString.Exclude
    private Collection<KursaDatumi> kursaDatumi; 
    
    @OneToMany(mappedBy = "kurss")
    @ToString.Exclude
    private Collection<MacibuRezultati> maciburezultati ; 


    @Builder
    public Kurss(String nosaukums, int stundas, Limenis līmenis) {
        this.nosaukums = nosaukums;
        this.stundas = stundas;
        this.limenis = līmenis;
    }


    
}

