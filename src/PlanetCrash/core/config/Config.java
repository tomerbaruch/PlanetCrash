package PlanetCrash.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/* cofiguration handler class*/ 
public class Config {

	private Properties configFile = new Properties();

	public Config() 
	{
		try {
			InputStream in = new FileInputStream("config/config.properties");
			configFile.load(in);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}


	/** get the path of the yago files directory */
	public String get_yago_files_directory(){
		return configFile.getProperty("YagoFilesDirectory");
	}

	/** get the name of the file yago_transitive_types.tsv */
	public String get_yago_transitive_types_name(){		
		return configFile.getProperty("YagoTransitiveTypeFileName");
	}

	/** get the name of the file yagoFacts.tsv */
	public String get_yago_facts_name(){
		return configFile.getProperty("YagoFactsFileName");
	}




	/** get the name of the file yagoLiteralFacts.tsv */
	public String get_yago_literal_facts_name(){
		return configFile.getProperty("YagoLiteralFactsFileName");
	}

	/** get the name of the file yagoDateFacts.tsv */
	public String get_yago_date_facts_name(){
		return configFile.getProperty("YagoDateFactsFileName");
	}

	/** get the path of the file YagoWikipediaInfo.tsv */
	public String get_yago_wikipedia_info_name(){
		return configFile.getProperty("YagoWikipediaInfoName");
	}

	/** get the server address */
	public String get_host_address(){
		return configFile.getProperty("Host");
	}
	/** get the port of the server */
	public String get_port(){
		return configFile.getProperty("Port");
	}

	/** get the dbName(Schema name) */
	public String get_db_name(){
		return configFile.getProperty("DbName");
	}

	/** get the user name */
	public String get_user_name(){
		return configFile.getProperty("UserName");
	}

	/** get user password */
	public String get_password(){
		return configFile.getProperty("Password");
	}

	/** get number of connections to create **/
	public int get_number_connection(){
		return Integer.parseInt(configFile.getProperty("NumOfConnections"));
	}

	/*parsing*/



	/** get the yago tag name for "country" type **/
	public String get_yago_tag_country(){
		return configFile.getProperty("COUNTRY");
	}
	/** get the yago tag name for "country" type **/
	public String get_yago_tag_university(){
		return configFile.getProperty("UNIVERSITY");
	}
	/** get the yago tag name for "city" type **/
	public String get_yago_tag_city(){
		return configFile.getProperty("CITY");
	}

	/** get the yago tag name for "language" type **/
	public String get_yago_tag_language(){
		return configFile.getProperty("LANGUAGE");
	}
	/** get the yago tag name for "language" type **/
	public String get_yago_tag_language2(){
		return configFile.getProperty("LANGUAGE2");
	}
	/** get the yago tag name for "currency" type **/
	public String get_yago_tag_currency(){
		return configFile.getProperty("CURRENCY");
	}
	/** get the yago tag name for "currency" type **/
	public String get_yago_tag_currency2(){
		return configFile.getProperty("CURRENCY2");
	}
	/** get the yago tag name for "population" type **/
	public String get_yago_tag_population(){
		return configFile.getProperty("POPULATION");
	}

	/** get the yago tag name for "musician" type **/
	public String get_yago_tag_musician(){
		return configFile.getProperty("MUSICIAN");
	}
	/** get the yago tag name for "scientist" type **/
	public String get_yago_tag_scientist(){
		return configFile.getProperty("SCIENTIST");
	}
	/** get the yago tag name for "politician" type **/
	public String get_yago_tag_politician(){
		return configFile.getProperty("POLITICIAN");
	}
	/** get the yago tag name for "actor" type **/
	public String get_yago_tag_actor(){
		return configFile.getProperty("ACTOR");
	}
	/** get the yago tag name for "athlete" type **/
	public String get_yago_tag_athlete(){
		return configFile.getProperty("ATHLETHE");
	}

	/** get the yago tag name for "date of birth" fact **/
	public String get_yago_tag_birth_date(){
		return configFile.getProperty("BORNDATE");
	}
	/** get the yago tag name for "date of death" fact **/
	public String get_yago_tag_death_date(){
		return configFile.getProperty("DEATHDATE");
	}
	/** get the yago tag name for "locatedin" fact **/
	public String get_yago_tag_located_in(){
		return configFile.getProperty("FACT_LOCATION");
	}
	/** get the yago tag name for "has currency" fact **/
	public String get_yago_tag_has_currency(){
		return configFile.getProperty("FACT_CURRENCY");
	}
	/** get the yago tag name for "official language" fact **/
	public String get_yago_tag_official_language(){
		return configFile.getProperty("FACT_LANGUAGE");
	}
	/** get the yago tag name for "capital city" fact **/
	public String get_yago_tag_capital_city(){
		return configFile.getProperty("FACT_CAPITAL");
	}
	/** get the yago tag name for "capital city" fact **/
	public String get_yago_tag_leader(){
		return configFile.getProperty("FACT_LEADER");
	}
	public String get_yago_tag_prize(){
		return configFile.getProperty("FACT_PRIZE");
	}
	public String get_yago_tag_birth_place(){
		return configFile.getProperty("FACT_BIRTHPLACE");
	}
	public String get_yago_tag_wiki_len(){
		return configFile.getProperty("FACT_WIKILEN");
	}
	public int get_min_wiki_len(){
		return Integer.parseInt(configFile.getProperty("MIN_WIKILEN"));
	}
	public int get_files_size(){
		return Integer.parseInt(configFile.getProperty("TOTAL_FILES_SIZE"));
	}
	public int get_high_graphics() {
		int ret =0;
		try{
			ret = Integer.parseInt(configFile.getProperty("highGraphics"));
		} catch (NumberFormatException e) {
			System.out.println("Bad config value: "+configFile.getProperty("highGraphics"));
		}
		return ret;
	}
}