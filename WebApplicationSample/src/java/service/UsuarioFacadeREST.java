/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import G3.crud.entities.Usuario;
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
@Path("g3.crud.entities.usuario")
public class UsuarioFacadeREST extends AbstractFacade<Usuario> {
    
    private Logger LOGGER = Logger.getLogger(UsuarioFacadeREST.class.getName());

    @PersistenceContext(unitName = "WebApplicationSamplePU")
    private EntityManager em;

    public UsuarioFacadeREST() {
        super(Usuario.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Usuario entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Usuario entity) {
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
    public Usuario find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }
    
    
    //Consulta para carga de datos usuario
    @GET
    @Path("idPersona/{idPersona}")
    @Produces({"application/xml"})
    public List<Usuario> mostrarDatosUser(@PathParam("idPersona") Long idPersona) {
        List<Usuario> usuarios=null;
        try {
            LOGGER.log(Level.INFO,"UserRESTful service: find users by idPersona ",idPersona);
             usuarios=em.createNamedQuery("cargarDatosUsuario")
                     .setParameter("idPersona", idPersona)
                     .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "UserRESTful service: Exception reading users by idPersona",
                    e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return usuarios;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}

