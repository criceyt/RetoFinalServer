/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import G3.crud.entities.Vehiculo;
import java.util.Date;
//import static G3.crud.entities.Vehiculo_.km;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author 2dam
 */



@Stateless
@Path("g3.crud.entities.vehiculo")
public class VehiculoFacadeREST extends AbstractFacade<Vehiculo> {

    
    private Logger LOGGER = Logger.getLogger(VehiculoFacadeREST.class.getName());

    @PersistenceContext(unitName = "WebApplicationSamplePU")
    private EntityManager em;

    public VehiculoFacadeREST() {
        super(Vehiculo.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Vehiculo entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Vehiculo entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Vehiculo find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Vehiculo> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    // Filtrado de datePicker
    @GET
    @Path("fechaAlta/{fechaAlta}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtradoDatePickerVehiculo(@PathParam("fechaAlta") Date fechaAlta) {
        List<Vehiculo> vehiculos=null;
        try {
            LOGGER.log(Level.INFO,"UserRESTful service: find users by profile {0}.",fechaAlta);
             vehiculos=em.createNamedQuery("filtradoDatePicker")
                     .setParameter("fechaAlta", fechaAlta)
                     .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "UserRESTful service: Exception reading users by profile, {0}",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return vehiculos;
    }
    
    // Filtrado de marca
    @GET
    @Path("marca/{marca}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtradoMarcaVehiculo(@PathParam("marca") String marca) {
        List<Vehiculo> vehiculos=null;
        try {
            LOGGER.log(Level.INFO,"UserRESTful service: find users by profile {0}.",marca);
             vehiculos=em.createNamedQuery("filtradoMarcaVehiculo")
                     .setParameter("marca", marca)
                     .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "UserRESTful service: Exception reading users by profile, {0}",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return vehiculos;
    }
    
    // Filtrado de color
    @GET
    @Path("color/{color}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtradoColorVehiculo(@PathParam("color") String color) {
        List<Vehiculo> vehiculos=null;
        try {
            LOGGER.log(Level.INFO,"UserRESTful service: find users by profile {0}.",color);
             vehiculos=em.createNamedQuery("filtradoColorVehiculo")
                     .setParameter("color", color)
                     .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "UserRESTful service: Exception reading users by profile, {0}",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return vehiculos;
    }
    
    // Filtrado de precio
    @GET
    @Path("precio/{precio}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtradoPrecioVehiculo(@PathParam("precio") Integer precio) {
        List<Vehiculo> vehiculos=null;
        try {
            LOGGER.log(Level.INFO,"UserRESTful service: find users by profile {0}.",precio);
             vehiculos=em.createNamedQuery("filtradoPrecioVehiculo")
                     .setParameter("precio", precio)
                     .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "UserRESTful service: Exception reading users by profile, {0}",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return vehiculos;
    }
    
    // Filtrado de potencia
    @GET
    @Path("potencia/{potencia}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtradoPotenciaVehiculo(@PathParam("potencia") Integer potencia) {
        List<Vehiculo> vehiculos=null;
        try {
            LOGGER.log(Level.INFO,"UserRESTful service: find users by profile {0}.",potencia);
             vehiculos=em.createNamedQuery("filtradoPotenciaVehiculo")
                     .setParameter("potencia", potencia)
                     .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "UserRESTful service: Exception reading users by profile, {0}",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return vehiculos;
    }

    // Filtrado de km
    @GET
    @Path("km/{km}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtrarPorKm(@PathParam("km") Integer km) {
        List<Vehiculo> vehiculos=null;
        try {
            LOGGER.log(Level.INFO,"UserRESTful service: find users by profile {0}.",km);
             vehiculos=em.createNamedQuery("filtradoKmVehiculo")
                     .setParameter("km", km)
                     .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "UserRESTful service: Exception reading users by profile, {0}",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return vehiculos;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
