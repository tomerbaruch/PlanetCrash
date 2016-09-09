package PlanetCrash.parsing.Yago;

/*
 * A yago entry in the format:
 * _id		rentity		relation	lentity
 */
public class YagoEntry {
	public String _id;
	public String rentity; //Right entity
	public String relation;
	public String lentity; //Left entity
	
	public YagoEntry(String _id, String lentity, String relation, String rentity) {
		this._id=_id;
		this.rentity=rentity;
		this.relation=relation;
		this.lentity=lentity;
	}
	
	public String getId() {
		return this._id;
	}
	
	public String getRightEntity() {
		return this.rentity;
	}
	
	public String getRelation() {
		return this.relation;
	}
	
	public String getLeftEntity() {
		return this.lentity;
	}
}
