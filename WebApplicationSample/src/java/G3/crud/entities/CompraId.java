/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package G3.crud.entities;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author 2dam
 */
@Embeddable
public class CompraId implements Serializable {

    private Long idPersona;
    private Long idVehiculo;

    public Long getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    public Long getVehiculoId() {
        return idVehiculo;
    }

    public void setVehiculoId(Long vehiculoId) {
        this.idVehiculo = vehiculoId;
    }

    @Override
    public String toString() {
        return "CompraId{" + "idPersona=" + idPersona + ", vehiculoId=" + idVehiculo + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (idPersona != null ? idPersona.hashCode() : 0);
        hash = 31 * hash + (idVehiculo != null ? idVehiculo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CompraId other = (CompraId) obj;
        if ((this.idPersona == null && other.idPersona != null)
                || (this.idPersona != null && !this.idPersona.equals(other.idPersona))) {
            return false;
        }
        if ((this.idVehiculo == null && other.idVehiculo != null)
                || (this.idVehiculo != null && !this.idVehiculo.equals(other.idVehiculo))) {
            return false;
        }
        return true;
    }

}
