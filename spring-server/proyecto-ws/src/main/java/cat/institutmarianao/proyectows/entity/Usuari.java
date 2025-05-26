package cat.institutmarianao.proyectows.entity;

import jakarta.persistence.*;

@Entity
public class Usuari {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuari;

    private String correu;

    private String contrasenyaHash;

    @Enumerated(EnumType.STRING)
    private Newsletter newsletter;

    private Integer punts;

    public enum Newsletter {
        Si, No
    }

	public Integer getIdUsuari() {
		return idUsuari;
	}

	public void setIdUsuari(Integer idUsuari) {
		this.idUsuari = idUsuari;
	}

	public String getCorreu() {
		return correu;
	}

	public void setCorreu(String correu) {
		this.correu = correu;
	}

	public String getContrasenyaHash() {
		return contrasenyaHash;
	}

	public void setContrasenyaHash(String contrasenyaHash) {
		this.contrasenyaHash = contrasenyaHash;
	}

	public Newsletter getNewsletter() {
		return newsletter;
	}

	public void setNewsletter(Newsletter newsletter) {
		this.newsletter = newsletter;
	}

	public Integer getPunts() {
		return punts;
	}

	public void setPunts(Integer punts) {
		this.punts = punts;
	}

	public Usuari() {
		super();
	}
}
