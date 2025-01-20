/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import G3.crud.entities.Vehiculo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//import static G3.crud.entities.Vehiculo_.km;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
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
@Path("vehiculo")
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
    
    // Filtrado de marca
    @GET
    @Path("marca/{marca}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtradoMarcaVehiculo(@PathParam("marca") String marca) throws NotFoundException{
        List<Vehiculo> vehiculos=null;
        
        if(marca == null){
             LOGGER.log(Level.SEVERE, "La marca es nula");
            throw new BadRequestException();
        }
        
        try {
            LOGGER.log(Level.INFO,"UserRESTful service: find users by profile {0}.",marca);
             vehiculos=em.createNamedQuery("filtradoMarcaVehiculo")
                     .setParameter("marca", marca)
                     .getResultList();
        } catch(NoResultException e){
            LOGGER.log(Level.INFO, "ERROR 404, No se ha encontrado ningun vehiculo con esa marca");    
            throw new NotFoundException(e);       
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO,
                    "ERROR 500, Error interno en el servidor",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return vehiculos;
    }
    
    // Filtrado de color
    @GET
    @Path("color/{color}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtradoColorVehiculo(@PathParam("color") String color) throws NotFoundException{
        List<Vehiculo> vehiculos=null;
        
        if(color == null){
             LOGGER.log(Level.SEVERE, "El color es nulo");
            throw new BadRequestException();
        }
        
        try {
            LOGGER.log(Level.INFO,"Buscando vehiculos por color",color);
             vehiculos=em.createNamedQuery("filtradoColorVehiculo")
                     .setParameter("color", color)
                     .getResultList();
        } catch(NoResultException e){
            LOGGER.log(Level.INFO, "ERROR 404, No se ha encontrado ningun vehiculo con ese color");    
            throw new NotFoundException(e);       
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO,
                    "ERROR 500, Error interno en el servidor",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return vehiculos;
    }
    
    // Filtrado de precio
    @GET
    @Path("precio/{precio}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtradoPrecioVehiculo(@PathParam("precio") Integer precio) throws NotFoundException{
        List<Vehiculo> vehiculos=null;
        
        if(precio == null){
             LOGGER.log(Level.SEVERE, "El precio es nulo");
            throw new BadRequestException();
        }
        
        try {
            LOGGER.log(Level.INFO,"Buscando vehiculos por precio",precio);
             vehiculos=em.createNamedQuery("filtradoPrecioVehiculo")
                     .setParameter("precio", precio)
                     .getResultList();
        } catch(NoResultException e){
            LOGGER.log(Level.INFO, "ERROR 404, No se ha encontrado ningun vehiculo con ese precio");    
            throw new NotFoundException(e);       
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO,
                    "ERROR 500, Error interno en el servidor",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return vehiculos;
    }
    
    // Filtrado de potencia
    @GET
    @Path("potencia/{potencia}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtradoPotenciaVehiculo(@PathParam("potencia") Integer potencia) throws NotFoundException{
        List<Vehiculo> vehiculos=null;
        
        if(potencia == null){
             LOGGER.log(Level.SEVERE, "La potencia es nula");
            throw new BadRequestException();
        }
        
        try {
            LOGGER.log(Level.INFO,"Buscando vehiculos por potencia",potencia);
             vehiculos=em.createNamedQuery("filtradoPotenciaVehiculo")
                     .setParameter("potencia", potencia)
                     .getResultList();
        } catch(NoResultException e){
            LOGGER.log(Level.INFO, "ERROR 404, No se ha encontrado ningun vehiculo con esa potencia");    
            throw new NotFoundException(e);       
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO,
                    "ERROR 500, Error interno en el servidor",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return vehiculos;
    }

    // Filtrado de km
    @GET
    @Path("km/{km}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtrarPorKm(@PathParam("km") Integer km) throws NotFoundException{
        List<Vehiculo> vehiculos=null;
        
        if(km == null){
             LOGGER.log(Level.SEVERE, "Los km son nulos");
            throw new BadRequestException();
        }
        
        try {
            LOGGER.log(Level.INFO,"Buscando vehiculos por kilometros",km);
             vehiculos=em.createNamedQuery("filtradoKmVehiculo")
                     .setParameter("km", km)
                     .getResultList();
        } catch(NoResultException e){
            LOGGER.log(Level.INFO, "ERROR 404, No se ha encontrado ningun vehiculo con esos km");    
            throw new NotFoundException(e);       
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO,
                    "ERROR 500, Error interno en el servidor",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return vehiculos;
    }
    
    // Filtrado por DatePicker
    @GET
    @Path("fechaAlta/{fechaAlta}")
    @Produces({"application/xml"})
    public List<Vehiculo> filtradoDatePickerVehiculo(@PathParam("fechaAlta") String fechaAltaString) throws NotFoundException{
        List<Vehiculo> vehiculos = null;
        
        if(fechaAltaString == null){
             LOGGER.log(Level.SEVERE, "La fechaAlta es nula");
            throw new BadRequestException();
        }
        
        try {
            // Parsear la fecha desde el String recibido en el formato 'yyyy-MM-dd'
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parseo = sdf.parse(fechaAltaString);

            LOGGER.log(Level.INFO, "Buscando vehiculos en los que la fecha de alta sea igual a la introducida", parseo);
            vehiculos = em.createNamedQuery("filtradoDatePicker")
                    .setParameter("fechaAlta", parseo)
                    .getResultList();
        } catch (ParseException e) {
            LOGGER.log(Level.INFO, "Error al intentar parsear la fechaAlta");
            throw new InternalServerErrorException(e);   
        } catch(NoResultException e){
            LOGGER.log(Level.INFO, "ERROR 404, No se ha encontrado ningun vehiculo con esa fecha de alta");    
            throw new NotFoundException(e);
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO,
                    "ERROR 500, Error interno en el servidor",
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