package G3.crud.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 2dam
 */
@NamedQueries({
    @NamedQuery(name = "cargarDatosUsuario", query = "SELECT p FROM Usuario p WHERE p.idPersona = :idPersona")
})

@Entity
@Table(name = "usuario", schema = "concesionariodb")
@XmlRootElement
public class Usuario extends Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos
    private boolean premium;
    private ArrayList<Vehiculo> tusVehiculos = new ArrayList<>(); // Se inicializa aquí

    // Relación de Usuario a Compra
    @OneToMany(cascade = ALL, mappedBy = "usuario", fetch = FetchType.EAGER)
    private Set<Compra> compras;

    public Usuario() {
        // Aseguramos que el ArrayList esté siempre inicializado
        this.tusVehiculos = new ArrayList<>();
    }

    // Getters y setters
    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    public Set<Compra> getCompras() {
        return compras;
    }

    public void setCompras(Set<Compra> compras) {
        this.compras = compras;
    }

    public ArrayList<Vehiculo> getTusVehiculos() {
        return tusVehiculos;
    }

    public void setTusVehiculos(ArrayList<Vehiculo> tusVehiculos) {
        this.tusVehiculos = tusVehiculos;
    }
}
