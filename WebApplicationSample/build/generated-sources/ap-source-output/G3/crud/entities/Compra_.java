package G3.crud.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Compra.class)
public abstract class Compra_ {

	public static volatile SingularAttribute<Compra, Date> fechaCompra;
	public static volatile SingularAttribute<Compra, CompraId> idCompra;
	public static volatile SingularAttribute<Compra, String> matricula;
	public static volatile SingularAttribute<Compra, Usuario> usuario;
	public static volatile SingularAttribute<Compra, Vehiculo> vehiculo;

}

