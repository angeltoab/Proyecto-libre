package cat.institutmarianao.proyectows.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "COMANDA")
public class Comanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comanda")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_plat", nullable = false)
    private Plat plat;

    @ManyToOne
    @JoinColumn(name = "id_taula", nullable = false)
    private Taula taula;

    @ManyToOne
    @JoinColumn(name = "id_usuari", nullable = false)
    private Usuari usuari;

    @Enumerated(EnumType.STRING)
    @Column(name = "estat", nullable = false)
    private EstatComanda estat;

    public enum EstatComanda {
        ACTIVA, FINALITZADA
    }

    // Getters y setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Plat getPlat() {
        return plat;
    }

    public void setPlat(Plat plat) {
        this.plat = plat;
    }

    public Taula getTaula() {
        return taula;
    }

    public void setTaula(Taula taula) {
        this.taula = taula;
    }

    public Usuari getUsuari() {
        return usuari;
    }

    public void setUsuari(Usuari usuari) {
        this.usuari = usuari;
    }

    public EstatComanda getEstat() {
        return estat;
    }

    public void setEstat(EstatComanda estat) {
        this.estat = estat;
    }
    @Override
    public String toString() {
        return "Comanda{id=" + id +
               ", taula=" + (taula != null ? taula.getId() : null) +
               ", plat=" + (plat != null ? plat.getIdPlat() : null) +
               ", usuari=" + (usuari != null ? usuari.getIdUsuari() : null) +
               ", estat=" + estat + "}";
    }

}
