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
@Table(name = "Macibu_rezultati")
public class MacibuRezultati {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MR_ID")
    private long mrId;

    @ManyToOne
    @JoinColumn(name = "K_ID", nullable = false)
    private Kurss kurss;

    @NotNull
    @Column(name = "Macibu_rezultats")
    private boolean  macibuRezultats;
    
    @Builder
    public MacibuRezultati(Kurss kurss, boolean
     macibuRezultats) {
        this.kurss = kurss;
        this.macibuRezultats = macibuRezultats;
    }
}