package compositestructure.model;

import compositestructure.model.associations.AB;
import compositestructure.model.associations.AC;
import hu.elte.txtuml.api.model.Connector;
import hu.elte.txtuml.api.model.Delegation;

public class abConnector extends Delegation {
  public class connEnd1 extends One<AB.b, B.PB> {}
  public class connEnd2 extends One<AB.a, A.P> {}
}
