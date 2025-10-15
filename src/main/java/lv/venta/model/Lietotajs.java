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
import jakarta.validation.constraints.Size;
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
@Table(name = "Lietotajs")

public class Lietotajs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "L_ID")
    private long lId;


    @NotNull
    @Pattern(regexp = "[A-ZĀČĒĢĪĶĻŅŠŪŽ a-zāčēģīķļņšūž@#$%^&*:;<>?!0-9]+", message = "Parole nedrikst būt īsāka nekā 5 rakstzimes vai garāka nekā 40 rakstzimes")
    @Column(name = "Parole")
    @Size(min = 5, max = 40)
    private String parole;

    @NotNull
    @Email
    @Column(name = "E_pasts")
    private String ePasts;

    @OneToMany(mappedBy = "lietotajs")
    @ToString.Exclude
    private Collection<Sertifikatu_registracijas_tabula> sertifikatuRegistracijasTabula;





    @Builder
    public Lietotajs(String parole, String ePasts) {
        this.parole = parole;
        this.ePasts = ePasts;
    }




}