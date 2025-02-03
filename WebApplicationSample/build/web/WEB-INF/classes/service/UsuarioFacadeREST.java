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
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotAcceptableException;
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
@Path("usuario")
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
        try {
            if (entity == null) {
                LOGGER.log(Level.SEVERE, "El objeto Usuario recibido es nulo.");
                throw new BadRequestException("El objeto Usuario es nulo.");
            }

            // Verificar si el correo ya existe
            if (verificarEmailExistente(entity.getEmail()) || verificarDniExistente(entity.getDni())) {
                LOGGER.log(Level.SEVERE, "El correo electrónico o el DNI ya está registrado.");

                // Exception
                throw new NotAcceptableException();
            }

            // Si el correo y el dni no existe, proceder con la creación
            super.create(entity);

        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.SEVERE, "Error al crear el usuario", e);
            throw new InternalServerErrorException(e);
        }
    }

    // Método para verificar si el correo electrónico ya existe
    private boolean verificarEmailExistente(String email) {
        Long count = 0L;
        try {
            count = (Long) em.createNamedQuery("verificarEmailExistente")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al verificar la existencia del correo", e);
            throw new InternalServerErrorException(e);
        }
        return count > 0; // Devuelve true si el correo existe
    }

    // Método para verificar si el DNI ya existe
    private boolean verificarDniExistente(String dni) {
        Long count = 0L;
        try {
            count = (Long) em.createNamedQuery("verificarDniExistente")
                    .setParameter("dni", dni)
                    .getSingleResult();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al verificar la existencia del DNI", e);
            throw new InternalServerErrorException(e);
        }
        return count > 0; // Devuelve true si el DNI existe
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Long id, Usuario entity) {
        try {
            super.edit(entity);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "UsurioFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Long id) {
        try {
            super.remove(super.find(id));
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "UsurioFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Usuario find(@PathParam("id") Long id) {
        try {
            return super.find(id);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "UsurioFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findAll() {
        try {
            return super.findAll();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "UsurioFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Usuario> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        try {
            return super.findRange(new int[]{from, to});
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "UsurioFacadeRESTful service: Exception logging up .", ex.getMessage());
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
            LOGGER.log(Level.SEVERE, "UsurioFacadeRESTful service: Exception logging up .", ex.getMessage());
            throw new InternalServerErrorException(ex);
        }
    }

    //Consulta para carga de datos usuario
    @GET
    @Path("idPersona/{idPersona}")
    @Produces({"application/xml"})
    public List<Usuario> mostrarDatosUser(@PathParam("idPersona") Long idPersona) {
        List<Usuario> usuarios = null;
        try {
            LOGGER.log(Level.INFO, "UserRESTful service: find users by idPersona ", idPersona);
            usuarios = em.createNamedQuery("cargarDatosUsuario")
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
