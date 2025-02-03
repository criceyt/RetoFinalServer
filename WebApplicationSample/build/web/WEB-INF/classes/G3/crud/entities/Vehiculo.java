package G3.crud.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import static javax.persistence.CascadeType.ALL;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ekain
 */
@NamedQueries({
    @NamedQuery(name = "cargarVehiculos", query = "SELECT a FROM Vehiculo a ORDER BY a.id DESC")
    ,
    @NamedQuery(name = "filtradoKmVehiculo", query = "SELECT a FROM Vehiculo a WHERE a.km <= :km ORDER BY a.km DESC")
    ,
    @NamedQuery(name = "filtradoColorVehiculo", query = "SELECT a FROM Vehiculo a WHERE a.color = :color")
    ,
    @NamedQuery(name = "filtradoPotenciaVehiculo", query = "SELECT a FROM Vehiculo a WHERE a.potencia <= :potencia ORDER BY a.potencia DESC")
    ,
    @NamedQuery(name = "filtradoMarcaVehiculo", query = "SELECT a FROM Vehiculo a WHERE a.marca = :marca")
    ,
    @NamedQuery(name = "filtradoPrecioVehiculo", query = "SELECT a FROM Vehiculo a WHERE a.precio <= :precio ORDER BY a.precio DESC")
    ,
    @NamedQuery(name = "filtradoDatePickerVehiculo", query = "SELECT a FROM Vehiculo a WHERE FUNCTION('DATE', a.fechaAlta) = FUNCTION('DATE', :fechaAlta)")
})

@Entity
@Table(name = "vehiculo", schema = "concesionariodb")
@XmlRootElement
public class Vehiculo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idVehiculo;
    private String marca;
    private String modelo;
    private String color;
    private String ruta;
    private Integer potencia;
    private Integer km;
    private Integer precio;
    @Temporal(TemporalType.DATE)
    private Date fechaAlta;
    @Enumerated(EnumType.STRING)
    private TipoVehiculo tipoVehiculo;

    // Relacion Vehiculo a Mantenimiento
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehiculo")
    private Set<Mantenimiento> mantenimientos;

    @XmlTransient
    public Set<Mantenimiento> getMantenimientos() {
        return mantenimientos;
    }

    public void setMantenimientos(Set<Mantenimiento> mantenimientos) {
        this.mantenimientos = mantenimientos;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "vehiculo_Proveedor", schema = "concesionariodb",
            joinColumns = @JoinColumn(name = "vehiculos_idVehiculo", referencedColumnName = "idVehiculo"),
            inverseJoinColumns = @JoinColumn(name = "proveedores_idProveedores", referencedColumnName = "idProveedor"))
    private Set<Proveedor> proveedores;

    public Set<Proveedor> getProveedores() {
        return proveedores;
    }

    public void setProveedores(Set<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    // Relacion de Vehiculo a Compra
    @OneToMany(cascade = ALL, mappedBy = "vehiculo", fetch = FetchType.EAGER)
    private Set<Compra> compras;

    @XmlTransient
    public Set<Compra> getCompras() {
        return compras;
    }

    public void setCompras(Set<Compra> compras) {
        this.compras = compras;
    }

    // Getters y Setters
    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Integer getPotencia() {
        return potencia;
    }

    public void setPotencia(Integer potencia) {
        this.potencia = potencia;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
}
