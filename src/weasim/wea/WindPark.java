package weasim.wea;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "WindPark.findAll", query = "select o from WindPark o")
public class WindPark implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WEASEQ")
	private Integer id;

	@OneToMany(cascade = CascadeType.ALL)
	List<WEA> weas;

	public WindPark() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setWeas(List<WEA> param) {
		this.weas = param;
	}

	public List<WEA> getWeas() {
		return weas;
	}
}
