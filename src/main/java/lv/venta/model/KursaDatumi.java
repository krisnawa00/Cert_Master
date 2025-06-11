package lv.venta.model;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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

    
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Sakuma_datums")
    private String sakumaDatums;


    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "Beiga_datums")
    private String beigadatums;



    public KursaDatumi(String sakumaDatums, String beigadatums) {
        this.sakumaDatums = sakumaDatums;
        this.beigadatums = beigadatums;
    }

}
