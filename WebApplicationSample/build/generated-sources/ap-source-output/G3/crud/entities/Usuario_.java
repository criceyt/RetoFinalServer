package G3.crud.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Usuario.class)
public abstract class Usuario_ extends G3.crud.entities.Persona_ {

	public static volatile SetAttribute<Usuario, Compra> compras;
	public static volatile SingularAttribute<Usuario, Boolean> premium;

}

