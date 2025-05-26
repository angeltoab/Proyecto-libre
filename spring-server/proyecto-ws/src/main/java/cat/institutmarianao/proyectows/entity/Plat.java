package cat.institutmarianao.proyectows.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
public class Plat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id_plat")
    private Integer idPlat;

    @JsonProperty("nom")
    private String nom;

    @JsonProperty("descripcio")
    private String descripcio;

    @Enumerated(EnumType.STRING)
    @JsonProperty("categoria")
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    @JsonProperty("recomanacio")
    private Recomanacio recomanacio;

    @JsonProperty("preu")
    private Double preu;

    @JsonProperty("puntuacio")
    private Double puntuacio;



    public Plat() {
		super();
	}

	public Integer getIdPlat() {
		return idPlat;
	}

	public void setIdPlat(Integer idPlat) {
		this.idPlat = idPlat;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getDescripcio() {
		return descripcio;
	}

	public void setDescripcio(String descripcio) {
		this.descripcio = descripcio;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Recomanacio getRecomanacio() {
		return recomanacio;
	}

	public void setRecomanacio(Recomanacio recomanacio) {
		this.recomanacio = recomanacio;
	}

	public Double getPreu() {
		return preu;
	}

	public void setPreu(Double preu) {
		this.preu = preu;
	}

	public Double getPuntuacio() {
		return puntuacio;
	}

	public void setPuntuacio(Double puntuacio) {
		this.puntuacio = puntuacio;
	}

	public enum Categoria {
        Entrant, Principal, Postre, Beguda
    }

    public enum Recomanacio {
        Si, No
    }
}
