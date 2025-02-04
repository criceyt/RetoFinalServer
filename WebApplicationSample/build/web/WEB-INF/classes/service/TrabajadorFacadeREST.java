/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import G3.crud.entities.Trabajador;
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
@Path("trabajador")
public class TrabajadorFacadeREST extends AbstractFacade<Trabajador> {

    @PersistenceContext(unitName = "WebApplicationSamplePU")
    private EntityManager em;
    private Logger LOGGER = Logger.getLogger(TrabajadorFacadeREST.class.getName());

    public TrabajadorFacadeREST() {
        super(Trabajador.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Trabajador entity) {
        try {
            super.create(entity);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "TrabajadorFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }

    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Trabajador entity) {
        try {
            super.edit(entity);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "TrabajadorFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        try {
            super.remove(super.find(id));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "TrabajadorFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Trabajador find(@PathParam("id") Long id) {
        try {
            return super.find(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "TrabajadorFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Trabajador> findAll() {
        try {
            return super.findAll();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "TrabajadorFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Trabajador> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        try {
            return super.findRange(new int[]{from, to});
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "TrabajadorFacadeRESTful service: Exception logging up .", ex.getMessage());
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
            LOGGER.log(Level.SEVERE, "TrabajadorFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
}