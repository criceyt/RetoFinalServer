package java.G3.crud.entities;

import G3.crud.entities.Mantenimiento;
import G3.crud.entities.Proveedores;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Vehiculo", schema = "concesionariodb")
public class Vehiculo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVehiculo")
    private Long idVehiculo;
    @Column(name = "matricula", nullable = false, unique = true)
    private String matricula;
    @Column(name = "marca", nullable = false)
    private String marca;
    @Column(name = "modelo", nullable = false)
    private String modelo;
    @Column(name = "color")
    private String color;
    @Column(name = "potencia")
    private Integer potencia;
    @Temporal(TemporalType.DATE)
    @Column(name = "fechaAlta", nullable = false)
    private Date fechaAlta;
    @Column(name = "tipoVehiculo")
    private String tipoVehiculo;

    @OneToMany
    @JoinColumn(name = "idMantenimiento")
    private Mantenimiento proveedor;

    @ManyToMany
    @JoinTable(
            name = "Vehiculo_Proveedores",
            joinColumns = @JoinColumn(name = "idVehiculo"),
            inverseJoinColumns = @JoinColumn(name = "idProveedor")
    )
    private List<Proveedores> proveedores;

    @ManyToMany(mappedBy = "vehiculosComprados")
    private List<Usuario> usuarios;

    // Constructores
    public Vehiculo() {
    }

    public Vehiculo(String matricula, String marca, String modelo, String color, Integer potencia, Date fechaAlta, String tipoVehiculo) {
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.potencia = potencia;
        this.fechaAlta = fechaAlta;
        this.tipoVehiculo = tipoVehiculo;
    }

    // Getters y Setters
    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
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

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<Mantenimiento> getMantenimientos() {
        return mantenimientos;
    }

    public void setMantenimientos(List<Mantenimiento> mantenimientos) {
        this.mantenimientos = mantenimientos;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}