package G3.crud.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author oier
 */
@Entity
@Table(name = "proveedor", schema = "concesionariodb")

@NamedQueries({
    //@NamedQuery(name = "buscarTodosProveedores",query = "SELECT a FROM Proveedor a ORDER BY a.idProveedor DESC"),
    @NamedQuery(name = "filtradoPorDatePickerProveedores", query = "SELECT a FROM Proveedor a WHERE FUNCTION('DATE', a.ultimaActividad) = FUNCTION('DATE', :ultimaActividad)")
    ,
    //@NamedQuery(name = "buscarProveedorPorFechaVehiculo", query = "SELECT p FROM Proveedor p " + "JOIN p.vehiculos v WHERE p.ultimaActividad = v.fechaAlta"),
    @NamedQuery(name = "filtradoPorNombre", query = "SELECT a FROM Proveedor a WHERE a.nombreProveedor = :nombreProveedor ORDER BY a.nombreProveedor DESC")
    ,
    @NamedQuery(name = "filtradoPorTipoVehiculo", query = "SELECT a FROM Proveedor a WHERE a.tipoVehiculo = :tipoVehiculo ORDER BY a.tipoVehiculo DESC"),})

@XmlRootElement
public class Proveedor implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idProveedor;
    private String nombreProveedor;

    @Enumerated(EnumType.STRING)
    private TipoVehiculo tipoVehiculo;

    private String especialidad;

    @Temporal(TemporalType.DATE)
    @JsonSerialize(as = Date.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date ultimaActividad;

    // Relacion
    @ManyToMany(mappedBy = "proveedores", fetch = FetchType.EAGER)
    private Set<Vehiculo> vehiculos;

    @XmlTransient
    public Set<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(Set<Vehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }

    // getters and setters
    public Long getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Long idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Date getUltimaActividad() {
        return ultimaActividad;
    }

    public void setUltimaActividad(Date ultimaActividad) {
        this.ultimaActividad = ultimaActividad;
    }

    // getters and setters de la lista
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProveedor != null ? idProveedor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Proveedor)) {
            return false;
        }
        Proveedor other = (Proveedor) object;
        if ((this.idProveedor == null && other.idProveedor != null) || (this.idProveedor != null && !this.idProveedor.equals(other.idProveedor))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "G3.crud.entities.Proveedores[ id=" + idProveedor + " ]";
    }

}
