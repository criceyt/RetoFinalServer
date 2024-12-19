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
public class CompraId implements Serializable{
    
    private Integer usuarioId;
    private Integer vehiculoId;

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(Integer vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    @Override
    public String toString() {
        return "CompraId{" + "usuarioId=" + usuarioId + ", vehiculoId=" + vehiculoId + '}';
    }
    
    
}
