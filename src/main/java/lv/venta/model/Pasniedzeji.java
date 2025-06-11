package lv.venta.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@Table(name = "Pasniedzeji")
public class Pasniedzeji {
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
    
    @Builder
    public Pasniedzeji(String vards, String uzvards) {
        this.vards = vards;
        this.uzvards = uzvards;
    }
}
