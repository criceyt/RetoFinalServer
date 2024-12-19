/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package G3.crud.entities;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author 2dam
 */
@Entity
@Table(name="usuario",schema="concesionariodb")
@XmlRootElement
public class Usuario extends Persona implements Serializable {

    private static final long serialVersionUID = 1L;
    
    // Atributos
    private boolean premium;

   // Relacion de Usuario a Compra
    @OneToMany(mappedBy="usuario", cascade=ALL)
    private Set<Compra> compra;
    
    
   // getters and setters
    public boolean isPremium() {
        return premium;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }

    @XmlTransient
    public Set<Compra> getCompra() {
        return compra;
    }

    public void setCompra(Set<Compra> compra) {
        this.compra = compra;
    }

}
