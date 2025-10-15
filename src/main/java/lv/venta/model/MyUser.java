package lv.venta.model;

import java.util.Collection;

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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Table(name = "MyUserTable")
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class MyUser {
	@Column(name = "UId")
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(value = AccessLevel.NONE)
	private int uId;
	@Size(min = 3, max = 20)
	@Pattern(regexp = "[a-z0-9]+")
	@Column(name = "Username")
	@NotNull
	private String username;
	@NotNull
	@Column(name = "Password")
	private String password;
	
	@ManyToOne
	@JoinColumn(name = "AId")
	private MyAuthority authority;
	
	public MyUser( String username, String password, MyAuthority authority) {

		this.username = username;
		this.password = password;
		this.authority = authority;
	}
}
