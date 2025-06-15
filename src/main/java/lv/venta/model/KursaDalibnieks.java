package lv.venta.model;

import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lv.venta.model.enums.Pilseta;
import lv.venta.model.enums.Valsts;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@Table(name = "Kursa_dalibnieki")
public class KursaDalibnieks {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "KD_ID")
    private long kdId;

    @NotNull
    @Pattern(regexp = "[A-ZĀČĒĢĪĶĻŅŠŪŽ][a-zāčēģīķļņšūž]+", message = "Vārds must start with a capital letter and contain only letters")
    @Column(name = "Vards")
    private String vards;

    @NotNull
    @Pattern(regexp = "[A-ZĀČĒĢĪĶĻŅŠŪŽ][a-zāčēģīķļņšūž]+", message = "Uzvārds must start with a capital letter and contain only letters")
    @Column(name = "Uzvards")
    private String uzvards;

    @NotNull
    @Email
    @Column(name = "E_pasts")
    private String ePasts;

    @NotNull
    @Pattern(regexp = "\\+?[0-9]{8,15}", message = "Invalid phone number format")
    @Column(name = "Telefona_nr")
    private String telefonaNr;

    @NotNull
    @Pattern(regexp = "[0-9]{11}", message = "Personas kods must be exactly 11 digits")
    @Column(name = "Personas_id")
    private String personasId;

    @NotNull
    @Column(name = "Pilseta")
    Pilseta pilseta;

    @NotNull
    @Column(name = "Valsts")
    Valsts valsts;

    @NotNull
    @Column(name = "Iela_nosaukums_numurs")
    private String ielaNosaukumsNumurs;

    @NotNull
    @Column(name = "Dzivokla_nummurs")
    private String dzivoklaNummurs;

    @NotNull
    @Column(name = "Pasta_indekss")
    private String pastaIndekss;

    
    
    
    @OneToMany(mappedBy = "dalibnieks")
    @ToString.Exclude
    private Collection<Vertejums> vertejumi;

    @OneToMany(mappedBy = "dalibnieks")
    @ToString.Exclude
    private Collection<sertifikati> sertifikati;
    
    @Builder
    public KursaDalibnieks(String vards, String uzvards, String ePasts, String telefonaNr,
                            String personasId, @NotNull Pilseta pilseta, @NotNull Valsts valsts,
                            String ielaNosaukumsNumurs, String dzivoklaNummurs, String pastaIndekss) {
    	this.vards = vards;
    	this.uzvards = uzvards;
    	this.ePasts = ePasts;
    	this.telefonaNr = telefonaNr;
    	this.personasId = personasId;
    	this.pilseta = pilseta;
    	this.valsts = valsts;
    	this.ielaNosaukumsNumurs = ielaNosaukumsNumurs;
    	this.dzivoklaNummurs = dzivoklaNummurs;
    	this.pastaIndekss = pastaIndekss;

    }
}