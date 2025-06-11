package lv.venta.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@Table(name = "Macibu_rezultati")
public class MacibuRezultati {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MR_ID")
    private Long mrId;

    @ManyToOne
    @JoinColumn(name = "K_ID", nullable = false)
    private Kurss kurss;

    @NotNull
    @Column(name = "Macibu_rezultats")
    private String macibuRezultats;
    
    @Builder
    public MacibuRezultati(Kurss kurss, String macibuRezultats) {
        this.kurss = kurss;
        this.macibuRezultats = macibuRezultats;
    }
}
