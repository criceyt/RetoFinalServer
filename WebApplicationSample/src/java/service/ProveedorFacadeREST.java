/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import G3.crud.entities.Proveedor;
import G3.crud.entities.TipoVehiculo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author 2dam
 */
@Stateless
@Path("proveedor")
public class ProveedorFacadeREST extends AbstractFacade<Proveedor> {

    private Logger LOGGER = Logger.getLogger(VehiculoFacadeREST.class.getName());

    @PersistenceContext(unitName = "WebApplicationSamplePU")
    private EntityManager em;

    public ProveedorFacadeREST() {
        super(Proveedor.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Proveedor entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Proveedor entity) {
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
    public Proveedor find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Proveedor> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Proveedor> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    // Filtrado por DatePicker para Proveedores
    @GET
    @Path("ultimaActividad/{ultimaActividad}")
    @Produces({"application/xml"})
    public List<Proveedor> filtradoPorDatePickerProveedores(@PathParam("ultimaActividad") String ultimaActividad) throws NotFoundException{
        List<Proveedor> proveedores = null;
        
        if(ultimaActividad == null){
            throw new BadRequestException();
        }
        
        try {
            // Parsear la fecha desde el String recibido en el formato 'yyyy-MM-dd'
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parseo = sdf.parse(ultimaActividad);

            LOGGER.log(Level.INFO, "Buscando proveedores de los que la ultimaActividad sea igual a la introducida", parseo);
            proveedores = em.createNamedQuery("filtradoPorDatePickerProveedores")
                    .setParameter("ultimaActividad", parseo)
                    .getResultList();
        } catch (ParseException e) {
            LOGGER.log(Level.INFO, "Error al intentar parsear la ultimaActividad");
            throw new InternalServerErrorException(e);   
        } catch(NoResultException e){
            LOGGER.log(Level.INFO, "ERROR 404, No se ha encontrado ningun proveedor con esa fecha de ultimaActividad");    
            throw new NotFoundException(e);    
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO,
                    "ERROR 500, Error interno en el servidor",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return proveedores;
    }
    
    // Filtrado de nombre
    @GET
    @Path("nombreProveedor/{nombreProveedor}")
    @Produces({"application/xml"})
    public List<Proveedor> filtradoPorNombre(@PathParam("nombreProveedor") String nombreProveedor) throws NotFoundException{
        List<Proveedor> proveedores=null;
        
        if(nombreProveedor == null){
            throw new BadRequestException();
        }
        
        try {
            LOGGER.log(Level.INFO,"Buscando proveedores por nombre",nombreProveedor);
             proveedores=em.createNamedQuery("filtradoPorNombre")
                     .setParameter("nombreProveedor", nombreProveedor)
                     .getResultList();
        }catch(NoResultException e){
            LOGGER.log(Level.INFO, "ERROR 404, El nombre del proveedor introducido no existe");    
            throw new NotFoundException(e);
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO,
                    "ERROR 500, Error interno en el servidor",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return proveedores;
    }

    // Filtrado de TipoVehiculo
    @GET
    @Path("tipoVehiculo/{tipoVehiculo}")
    @Produces({"application/xml"})
    public List<Proveedor> filtradoPorTipoVehiculo(@PathParam("tipoVehiculo") String tipoVehiculo) throws NotFoundException{
        List<Proveedor> proveedores = null;
        
        if (tipoVehiculo == null) {
            LOGGER.log(Level.SEVERE, "El tipoVehiculo es nulo");
            throw new BadRequestException();
        }
        
        try {
            // Convertir el String recibido a un valor del enum TipoVehiculo
            TipoVehiculo tipo = TipoVehiculo.fromString(tipoVehiculo);

            // Log para verificar el tipo de vehículo
            LOGGER.log(Level.INFO, "Buscando proveedores por el tipo de vehiculo", tipo);

            // Ejecutar la consulta usando el parámetro convertido
            proveedores = em.createNamedQuery("filtradoPorTipoVehiculo")
                    .setParameter("tipoVehiculo", tipo)
                    .getResultList();
        } catch (IllegalArgumentException e) {
            // Manejar el caso cuando el tipo de vehículo no es válido
            LOGGER.log(Level.SEVERE, "UserRESTful service: Invalid TipoVehiculo {0}.", tipoVehiculo);
            throw new WebApplicationException("Tipo de vehículo no válido.", Response.Status.BAD_REQUEST);
         }catch(NoResultException e){
            LOGGER.log(Level.INFO, "ERROR 404, El tipo de vehiculo introducido no existe");    
            throw new NotFoundException(e);
        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.INFO,
                    "ERROR 500, Error interno en el servidor",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return proveedores;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
