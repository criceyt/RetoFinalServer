/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import G3.crud.entities.Persona;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author 2dam
 */
@Stateless
@Path("persona")
public class PersonaFacadeREST extends AbstractFacade<Persona> {

    @PersistenceContext(unitName = "WebApplicationSamplePU")
    private EntityManager em;

    private Logger LOGGER = Logger.getLogger(PersonaFacadeREST.class.getName());

    public PersonaFacadeREST() {
        super(Persona.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Persona entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Persona entity) {
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
    public Persona find(@PathParam("id") Long id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Persona> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Persona> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    // Consulta para Inicio de Sesión de Usuarios y Trabajadores
    @GET
    @Path("inicioSesionPersona/{email}/{contrasena}")
    @Produces({"application/xml"})
    public Persona inicioSesionPersona(@PathParam("email") String email, @PathParam("contrasena") String contrasena) {
        Persona persona = null;
        try {
            LOGGER.log(Level.INFO, "UserRESTful service: find user by email and password");

            // Ejecuta la NamedQuery 'inicioSesionPersona' y pasa los parámetros email y contrasena
            persona = (Persona) em.createNamedQuery("inicioSesionPersona")
                    .setParameter("email", email)
                    .setParameter("contrasena", contrasena)
                    .getSingleResult(); // Usar getSingleResult() para un único resultado
        } catch (NoResultException e) {
            LOGGER.log(Level.INFO, "UserRESTful service: No user found with provided email and password");
            persona = null; // Si no se encuentra usuario, devolver null o manejar el caso según tu lógica
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "UserRESTful service: Exception reading user by email and password", e.getMessage());
            throw new InternalServerErrorException(e);
        }
        return persona;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
