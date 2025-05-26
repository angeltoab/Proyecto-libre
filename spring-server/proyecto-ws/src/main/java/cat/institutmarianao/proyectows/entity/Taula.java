package cat.institutmarianao.proyectows.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "TAULA")
public class Taula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_taula")
    private Integer id;

    @Column(name = "capacitat", nullable = false)
    private Integer capacitat;

    // Getters y setters

    public Integer getId() {
        return id;
    }

    public Taula() {
		super();
	}

	public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCapacitat() {
        return capacitat;
    }

    public void setCapacitat(Integer capacitat) {
        this.capacitat = capacitat;
    }
}
