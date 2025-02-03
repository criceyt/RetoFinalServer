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
import javax.ws.rs.WebApplicationException;
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
            if (existeCorreo(entity.getEmail())) {
                LOGGER.log(Level.SEVERE, "El correo electrónico ya está registrado.");

                throw new NotAcceptableException();
            }

            // Verificar si el correo ya existe
            if (existeDni(entity.getDni())) {
                LOGGER.log(Level.SEVERE, "El correo electrónico ya está registrado.");

                throw new NotAcceptableException();
            }

            // Si el correo y el dni no existe, proceder con la creación
            super.create(entity);

        } catch (InternalServerErrorException e) {
            LOGGER.log(Level.SEVERE, "Error al crear el usuario", e);
            throw new InternalServerErrorException(e);
        }
    }

    // Método auxiliar que verifica si el correo ya existe
    private boolean existeCorreo(String email) {
        try {
            // Aquí hacemos la consulta en la base de datos para ver si ya existe un usuario con ese correo
            Integer count = (Integer) em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.email = :email")
                    .setParameter("email", email)
                    .getSingleResult();
            return count > 0;  // Si el conteo es mayor a 0, significa que el correo ya está registrado
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al verificar si el correo ya existe", e);
            throw new NotAcceptableException("Error al verificar el correo");
        }
    }

    private boolean existeDni(String dni) {
        try {
            // Aquí hacemos la consulta en la base de datos para ver si ya existe un usuario con ese correo
            Integer count = (Integer) em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.dni = :dni")
                    .setParameter("dni", dni)
                    .getSingleResult();
            return count > 0;  // Si el conteo es mayor a 0, significa que el correo ya está registrado
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al verificar si el dni ya existe", e);
            throw new NotAcceptableException("Error al verificar el dni");
        }
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
