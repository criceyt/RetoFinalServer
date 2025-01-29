/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package G3.crud.entities;

import G3.crud.crypto.Hash;
import G3.crud.crypto.Servidor;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author 2dam
 */
@NamedQueries({
    @NamedQuery(name = "reiniciarContrasena", query = "UPDATE Persona p SET p.contrasena = :nuevaContrasena WHERE p.email = :email")
    ,
   @NamedQuery(name = "findEmailPersona", query = "SELECT p FROM Persona p WHERE p.email = :email")
    ,
  @NamedQuery(name = "inicioSesionPersona", query = "SELECT p FROM Persona p WHERE p.email = :email AND p.contrasena = :contrasena")

})

@Entity
@Table(name = "persona", schema = "concesionariodb")
@Inheritance(strategy = InheritanceType.JOINED)
@XmlRootElement
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idPersona;

    private String dni;
    private String email;
    private String nombreCompleto;
    private Date fechaRegistro;
    private Integer codigoPostal;
    private Integer telefono;
    private String direccion;
    private String contrasena;

    public Long getId() {
        return idPersona;
    }

    public void setId(Long idPersona) {
        this.idPersona = idPersona;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(Integer codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        // Si la contraseña recibida está cifrada en Base64, la desencriptamos
        if (Servidor.esBase64Valido(contrasena)) {
            // Desencriptar la contraseña
            String decryptedPassword = Servidor.desencriptarContraseña(contrasena);

            // Verificar que la desencriptación fue exitosa
            if (decryptedPassword != null && !decryptedPassword.isEmpty()) {
                // Hashear la contraseña desencriptada
                this.contrasena = Hash.hashText(decryptedPassword);
            } else {
                throw new IllegalArgumentException("Error al desencriptar la contraseña");
            }
        } else {
            // Si no está cifrada, simplemente asignamos el valor hasheado
            this.contrasena = Hash.hashText(contrasena);
        }
    }

    public void setContrasenaReset(String constrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPersona != null ? idPersona.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Persona)) {
            return false;
        }
        Persona other = (Persona) object;
        if ((this.idPersona == null && other.idPersona != null) || (this.idPersona != null && !this.idPersona.equals(other.idPersona))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "G3.crud.entities.Persona[ idPersona=" + idPersona + " ]";
    }

}
