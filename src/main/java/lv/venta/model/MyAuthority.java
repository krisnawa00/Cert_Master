package lv.venta.model;

import java.util.Collection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "MyAuthorityTable")
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MyAuthority {
	@Column(name = "AId")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(value = AccessLevel.NONE)
	private int aId;
	@Column(name = "Title")
	@NotNull
	@Pattern(regexp = "[A-Z_]{3,10}")
	private String title;
	
	@OneToMany(mappedBy = "authority", fetch = FetchType.EAGER)
	@ToString.Exclude
	private Collection<MyUser> users;
	
	public MyAuthority(String title) {
		this.title = title;
	}
	
}
