/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import G3.crud.entities.Compra;
import G3.crud.entities.CompraId;
import static com.sun.xml.ws.spi.db.BindingContextFactory.LOGGER;
import java.util.List;
import java.util.logging.Level;
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
import javax.ws.rs.core.PathSegment;

/**
 *
 * @author 2dam
 */
@Stateless
@Path("compra")
public class CompraFacadeREST extends AbstractFacade<Compra> {

    @PersistenceContext(unitName = "WebApplicationSamplePU")
    private EntityManager em;

    private CompraId getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;usuarioId=usuarioIdValue;vehiculoId=vehiculoIdValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        G3.crud.entities.CompraId key = new G3.crud.entities.CompraId();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> idPersona = map.get("idPersona");
        if (idPersona != null && !idPersona.isEmpty()) {
            key.setIdPersona(new java.lang.Long(idPersona.get(0)));
        }
        java.util.List<String> idVehiculo = map.get("idVehiculo");
        if (idVehiculo != null && !idVehiculo.isEmpty()) {
            key.setIdVehiculo(new java.lang.Long(idVehiculo.get(0)));
        }
        return key;
    }

    public CompraFacadeREST() {
        super(Compra.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Compra entity) {
        try {
            super.create(entity);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "CompraFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") PathSegment id, Compra entity) {
        try {
            super.edit(entity);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "CompraFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        try {
            G3.crud.entities.CompraId key = getPrimaryKey(id);
            super.remove(super.find(key));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "CompraFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Compra find(@PathParam("id") PathSegment id) {
        try {
            G3.crud.entities.CompraId key = getPrimaryKey(id);
            return super.find(key);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "CompraFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Compra> findAll() {
        try {
            return super.findAll();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "CompraFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Compra> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        try {
            return super.findRange(new int[]{from, to});
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "CompraFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        try {
            return String.valueOf(super.count());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "CompraFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}
