package cat.institutmarianao.proyectows.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "VALORACIO")
public class Valoracio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_valoracio")
    private Integer id;

    @Column(name = "valor", nullable = false)
    private Double valor;

    @Column(name = "comentari", nullable = false)
    private String comentari;

    @ManyToOne
    @JoinColumn(name = "id_plat", nullable = false)
    private Plat plat;

    @ManyToOne
    @JoinColumn(name = "id_usuari", nullable = false)
    private Usuari usuari;

    // Getters y setters

    public Integer getId() {
        return id;
    }

    public Valoracio() {
		super();
	}

	public void setId(Integer id) {
        this.id = id;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getComentari() {
        return comentari;
    }

    public void setComentari(String comentari) {
        this.comentari = comentari;
    }

    public Plat getPlat() {
        return plat;
    }

    public void setPlat(Plat plat) {
        this.plat = plat;
    }

    public Usuari getUsuari() {
        return usuari;
    }

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }
}
