package G3.crud.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Persona.class)
public abstract class Persona_ {

	public static volatile SingularAttribute<Persona, Integer> codigoPostal;
	public static volatile SingularAttribute<Persona, Date> fechaRegistro;
	public static volatile SingularAttribute<Persona, String> direccion;
	public static volatile SingularAttribute<Persona, String> contrasena;
	public static volatile SingularAttribute<Persona, String> nombreCompleto;
	public static volatile SingularAttribute<Persona, Integer> telefono;
	public static volatile SingularAttribute<Persona, Long> idPersona;
	public static volatile SingularAttribute<Persona, String> dni;
	public static volatile SingularAttribute<Persona, String> email;

}

