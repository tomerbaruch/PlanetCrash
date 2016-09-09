package PlanetCrash.core.Game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import PlanetCrash.core.config.Config;
import PlanetCrash.db.DatabaseHandler;

public class QuestionsGenerator {
	private ArrayList<Question> possibleQuestions;
	private String countryName;
	private int countryId;
	private DatabaseHandler dbh;
	private String dbname;
	
	Config config = new Config();
	
	public QuestionsGenerator(String countryName,DatabaseHandler dbh,String dbname){
		this.countryName=countryName;
		this.dbh = dbh;
		this.dbname = dbname;
		possibleQuestions = new ArrayList<Question>();
		String query = "SELECT Country.idCountry " +
                "FROM "+this.dbname+".Country "+
                "WHERE Country.Name='"+countryName+"';";
		
		try {
			ResultSet rs =this.dbh.executeQuery(query);//jdbc
			if(rs.next())
				countryId=rs.getInt("idCountry");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		generateUniversityQuestions(3);
		generateAgeQuestions(3);
		generatePopulationSizeQuestion();
		generateContinentQuestion();
		generateOfficialLanguageQuestion();
		generateOfficialCurrencyQuestion();
		generateCapitalCityQuestion();
		generateBornInQuestions();
		generatePrizeWinnerQuestions("Grammy Award",2);
		generatePrizeWinnerQuestions("Grammy Lifetime Achievement Award",2);
		generatePrizeWinnerQuestions("Academy Award for Best Actor",2);
		generatePrizeWinnerQuestions("Academy Award for Best Actress",2);
		generatePrizeWinnerQuestions("Nobel Peace Prize",2);
		generatePrizeWinnerQuestions("FIFA World Player of the Year",2);
		generatePrizeWinnerQuestions("Nobel Prize in Physics",2);
		generatePrizeWinnerQuestions("Nobel Prize in Chemistry",2);
		generateCommonProfessionQuestion(3);
		generateWhosDeadQuestion(3);

		
	}
	public void generateQuestionForCountry(String countryName, int numOfQuestions){

		
	}
	public ArrayList<Question> getPossibleQuestions(){
		return this.possibleQuestions;
	}

	private void generateUniversityQuestions(int i){
		String query_in = "SELECT University.Name " +
                "FROM "+this.dbname+".University "+
                "WHERE University.idCountry='"+countryId+"'"+
                " and University.Name IS NOT NULL"+
                " ORDER BY RAND()"+
                " LIMIT "+i+";";
		String query_out = "SELECT University.Name " +
                "FROM "+this.dbname+".University "+
                "WHERE University.idCountry!='"+countryId+"'"+
                " and University.Name IS NOT NULL"+
                " ORDER BY RAND()"+
                " LIMIT "+(4*i)+";";
		try {
			ResultSet rs_in = this.dbh.executeQuery(query_in);
			ResultSet rs_out = this.dbh.executeQuery(query_out);
			ArrayList<String> out=new ArrayList<String>();
			while(rs_out.next()){
				out.add(rs_out.getString("Name"));
			}
			if(out.size()<3){
				return;
			}
			String name;
			while(rs_in.next()){
				name=rs_in.getString("Name");
				Question q = new Question("Which of the following universities is located in "+this.countryName+"?");
				ArrayList<Integer> indexes = randomIndexes(out.size(), 3);
				q.setCorrectAnswer(new Answer(name));
				q.addPossibleAnswers(new Answer(name));
				for(int j=0;j<3;j++){
					q.addPossibleAnswers(new Answer(out.get(indexes.get(j))));
				}
				this.possibleQuestions.add(q);
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void generateAgeQuestions(int i){
		String query = "SELECT Person.Name, Person.yearOfBirth " +
                "FROM "+this.dbname+".City, "+this.dbname+".Person "+
                "WHERE City.idCountry='"+countryId+"' and Person.idPlaceOfBirth=City.idCity"+
                " and Person.Name IS NOT NULL and Person.yearOfBirth IS NOT NULL"+
                " and Person.yearOfDeath =0"+
                " ORDER BY RAND()"+
                " LIMIT "+i+";";
		try {
			ResultSet rs =this.dbh.executeQuery(query);
			String name="";
			int year;
			int currYear = Calendar.getInstance().get(Calendar.YEAR);
			Random r =new Random();
			while (rs.next()){
				name = rs.getString("Name");
				year = rs.getInt("yearOfBirth");
				
				Question q = new Question("How old is "+name+"?");
				q.setCorrectAnswer(new Answer(new Integer(currYear-year).toString()));
				q.addPossibleAnswers(new Answer(new Integer(currYear-year).toString()));
				q.addPossibleAnswers(new Answer(new Integer(currYear-year+r.nextInt(10)+1).toString()));
				q.addPossibleAnswers(new Answer(new Integer((currYear-year)-r.nextInt(10)-1).toString()));
				q.addPossibleAnswers(new Answer(new Integer(((currYear-year)+30)/(2+r.nextInt(2))).toString()));
				this.possibleQuestions.add(q);
			}

		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//jdbc
		
	}
	private void generateBornInQuestions(){
		String query_out = "SELECT Person.Name " +
                "FROM "+this.dbname+".City, "+this.dbname+".Person "+
                "WHERE City.idCountry!='"+countryId+"'"+" and Person.idPlaceOfBirth=City.idCity "+
                "and Person.Name IS NOT NULL "+
                "ORDER BY RAND() "+
                "LIMIT 3";
		String query_in = "SELECT Person.Name " +
                "FROM "+this.dbname+".City, "+this.dbname+".Person "+
                "WHERE City.idCountry='"+countryId+"'"+" and Person.idPlaceOfBirth=City.idCity "+
                "and Person.Name IS NOT NULL "+
                "ORDER BY RAND() "+
                "LIMIT 15";

		ArrayList<String> born_in= new ArrayList<String>();
		try {
			ResultSet rs_in =this.dbh.executeQuery(query_in);//jdbc
			ResultSet rs_out =this.dbh.executeQuery(query_out);//jdbc
			while(rs_in.next()){
				born_in.add(rs_in.getString("Name"));
			}
			if (born_in.size()<3){
				return;
			}
			while(rs_out.next()){
				Question q = new Question("Who of the following was not born in "+countryName+"?");
				q.setCorrectAnswer(new Answer(rs_out.getString("Name")));
				q.addPossibleAnswers(new Answer(rs_out.getString("Name")));
				ArrayList<Integer> indexes = randomIndexes(born_in.size(), 3);
				int i;
				for (i=0;i<3;i++){
					q.addPossibleAnswers(new Answer(born_in.get(indexes.get(i))));
				}
				q.setQuestionAsReady();
				possibleQuestions.add(q);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void generatePopulationSizeQuestion(){
		Question q = new Question("What is the population size of "+countryName+"?");
		String query = "SELECT Country.PopulationSize " +
                   "FROM "+this.dbname+".Country "+
                   "WHERE Country.idCountry='"+countryId+"'"+
                   " and Country.PopulationSize IS NOT NULL;";
		
		try {
			ResultSet rs =this.dbh.executeQuery(query);//jdbc
			if (rs.next()){	
				Random r =new Random();
				Integer populationSize = rs.getInt("PopulationSize");
				if(populationSize == 0)
					return;
				q.setCorrectAnswer(new Answer(populationSize.toString()));
				q.addPossibleAnswers(new Answer(populationSize.toString()));
				q.addPossibleAnswers(new Answer(((Integer)(populationSize/(r.nextInt(3)+2))).toString()));
				q.addPossibleAnswers(new Answer(((Integer)(populationSize*(r.nextInt(3)+2))).toString()));
				q.addPossibleAnswers(new Answer(((Integer)(populationSize/6)).toString()));
				q.setQuestionAsReady();
				possibleQuestions.add(q);
			}
			return;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void generateContinentQuestion(){
		Question q = new Question("In which continent is "+countryName+" located?");
		String query = "SELECT Continent.Name " +
                   "FROM "+this.dbname+".Country,"+this.dbname+".Continent "+
                   "WHERE Country.idCountry='"+countryId+"' and Country.idContinent=Continent.idContinent"
                   +" and Continent.Name IS NOT NULL;";

		try {
			ResultSet rs =this.dbh.executeQuery(query);//jdbc
			if (rs.next()){
				String locatedContinent = rs.getString("Name");
				q.setCorrectAnswer(new Answer(locatedContinent));
				q.addPossibleAnswers(new Answer(locatedContinent));
				query = "SELECT Distinct Continent.Name " +
			               "FROM "+this.dbname+".Continent "+
			               "WHERE Continent.Name!='"+locatedContinent+"'"+
			               " and Continent.Name IS NOT NULL "+
			               "ORDER BY RAND()"+
			               " LIMIT 3;";
				rs =this.dbh.executeQuery(query);//jdbc
				int i =0;
				while (rs.next()){
					i++;
					String continent = rs.getString("Name");
					q.addPossibleAnswers(new Answer(continent));
				}
				if (i==3){
					q.setQuestionAsReady();
					possibleQuestions.add(q);
				}
				return;
			}
			return;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void generateOfficialLanguageQuestion(){
		Question q = new Question("What is the official language in "+countryName+"?");
		String query = "SELECT Language.Name " +
                   "FROM "+this.dbname+".Country, "+this.dbname+".Language "+
                   "WHERE Country.idCountry='"+countryId+"' and Country.idLanguage=Language.idLanguage"+
                   " and Language.Name IS NOT NULL;";

		try {
			ResultSet rs =this.dbh.executeQuery(query);//jdbc
			if (rs.next()){
				String officialLanguage = rs.getString("Name").replace(" Language", "").replace(" language", "");
				q.setCorrectAnswer(new Answer(officialLanguage));
				q.addPossibleAnswers(new Answer(officialLanguage));
				query = "SELECT DISTINCT Language.Name " +
			               "FROM "+this.dbname+".Language ,"+this.dbname+".Country "+
			               "WHERE Language.Name!='"+officialLanguage+"' and Language.idLanguage = Country.idLanguage"
			               	+" and Language.Name IS NOT NULL"+               
			               " ORDER BY RAND()"+
			               " LIMIT 3;";
				rs =this.dbh.executeQuery(query);//jdbc
				int i =0;
				while (rs.next()){
					i++;
					String language = rs.getString("Name").replace(" Language", "").replace(" language", "");
					q.addPossibleAnswers(new Answer(language));
				}
				if (i==3){
					q.setQuestionAsReady();
					possibleQuestions.add(q);
				}
				return;
			}
			return;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void generateOfficialCurrencyQuestion(){
		Question q = new Question("What is the official currency of "+countryName+"?");
		String query = "SELECT Currency.Name " +
                   "FROM "+this.dbname+".Country,"+this.dbname+".Currency "+
                   "WHERE Country.idCountry='"+countryId+"' and Country.idCurrency=Currency.idCurrency"+
                   " and Currency.Name IS NOT NULL;";
		
		try {
			ResultSet rs =this.dbh.executeQuery(query);//jdbc
			if (rs.next()){
				String officialCurrency = rs.getString("Name");
				q.setCorrectAnswer(new Answer(officialCurrency));
				q.addPossibleAnswers(new Answer(officialCurrency));
				query = "SELECT DISTINCT Currency.Name " +
			               "FROM "+this.dbname+".Currency, "+this.dbname+".Country "+
			               "WHERE Currency.Name!='"+officialCurrency+"'"+
			               " and Currency.idCurrency=Country.idCurrency"+
			               " and Currency.Name IS NOT NULL"+
			               " ORDER BY RAND()"+
			               " LIMIT 3;";
				rs =this.dbh.executeQuery(query);//jdbc
				int i =0;
				while (rs.next()){
					i++;
					String currency = rs.getString("Name");
					q.addPossibleAnswers(new Answer(currency));
				}
				if (i==3){
					q.setQuestionAsReady();
					possibleQuestions.add(q);
				}
				return;
			}
			return;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void generateCapitalCityQuestion(){
		Question q = new Question("What is the capital city of "+countryName+"?");
		String query = "SELECT City.Name " +
                   "FROM "+this.dbname+".Country, "+this.dbname+".City "+
                   "WHERE City.idCountry='"+countryId+"' and Country.idCapital=City.idCity"+
                   " and City.Name IS NOT NULL;";
		
		try {
			ResultSet rs =this.dbh.executeQuery(query);//jdbc
			if (rs.next()){
				String capital = rs.getString("Name");
				q.setCorrectAnswer(new Answer(capital));
				q.addPossibleAnswers(new Answer(capital));
				query = "SELECT DISTINCT City.Name " +
			               "FROM "+this.dbname+".City "+
			               "WHERE City.idCountry='"+countryId+"' and City.Name!='"+capital+"' "+
			               "and City.Name IS NOT NULL "+
			               "ORDER BY RAND()"+
			               " LIMIT 3;";
				rs =this.dbh.executeQuery(query);//jdbc
				int i =0;
				while (rs.next()){
					i++;
					String city = rs.getString("Name");
					q.addPossibleAnswers(new Answer(city));
				}
				if (i==3){
					q.setQuestionAsReady();
					possibleQuestions.add(q);
				}
				return;
			}
			return;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void generatePrizeWinnerQuestions(String prizeName,int numberOfQuestion){
		String query ="SELECT DISTINCT Person.Name "+
					"FROM "+this.dbname+".Award, "+this.dbname+".AwardWinners, "+this.dbname+".Person, "+this.dbname+".City "+
				"WHERE City.idCountry ='"+countryId+"' and "+
					"Person.idPlaceOfBirth = City.idCity and "+
				"Person.idPerson = AwardWinners.idPerson and "+
					"AwardWinners.idAward = Award.idAward and "+
					"Award.Name ='"+prizeName+"' and Person.Name IS NOT NULL;";
		
		ArrayList<String> winners = new ArrayList<String>();
		ArrayList<String> nonWinners = new ArrayList<String>();
		try {
			ResultSet rs =this.dbh.executeQuery(query);//jdbc
			int i = 0;
			while(rs.next()){
				i++;
				winners.add(rs.getString("Name"));
				if(i==numberOfQuestion){
					break;
				}
			}
			if (i==0){
				return;
			}
			query ="SELECT DISTINCT Person.Name "+
					"FROM "+this.dbname+".Award, "+this.dbname+".AwardWinners, "+this.dbname+".Person, "+this.dbname+".City "+
				"WHERE City.idCountry ='"+countryId+"' and "+
					"Person.idPlaceOfBirth = City.idCity and "+
				"Person.idPerson = AwardWinners.idPerson and "+
				"AwardWinners.idAward = Award.idAward and "+
				"Award.Name !='"+prizeName+"' and Person.Name IS NOT NULL;";
			rs =this.dbh.executeQuery(query);//jdbc
			while(rs.next()){
				String nonWiner = rs.getString("Name");
				if (!winners.contains(nonWiner))
					nonWinners.add(nonWiner);
			}
			if (nonWinners.size()<3){
				return;
			}
			int j;
			for ( i=0;i<winners.size();i++){
				if (i==numberOfQuestion){
					break;
				}
				Question q = new Question("Who won the "+prizeName+"?");
				q.setCorrectAnswer(new Answer(winners.get(i)));
				q.addPossibleAnswers(new Answer(winners.get(i)));
				ArrayList<Integer> indexes = randomIndexes(nonWinners.size(), 3);
				for (j=0;j<3;j++){
					q.addPossibleAnswers(new Answer(nonWinners.get(indexes.get(j))));
				}
				q.setQuestionAsReady();
				possibleQuestions.add(q);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private void generateCommonProfessionQuestion(int numberOfQuestions){
		for(int j=0;j<numberOfQuestions;j++){
			Question q = new Question("");

			// get a random profession
			String query = String.format("SELECT idProfession, Name "
										+ "FROM %s.Profession "
										+ "ORDER BY RAND() "
										+ "LIMIT 1;", this.dbname);
			
			
			try {
				ResultSet rs = this.dbh.executeQuery(query);//jdbc
				if (!rs.first()){
					return;
				}
				String professionName = rs.getString("Name");
				int professionId = rs.getInt("idProfession");
				q.setCorrectAnswer(new Answer(professionName+"s"));
				q.addPossibleAnswers(new Answer(professionName+"s"));
				
				//get 3 random persons with that profession that were born at countryId"
				query =  String.format("SELECT Person.Name "
									+ "FROM %s.Person_Profession, %s.Person, %s.City "
									+ "WHERE Person_Profession.idProfession=%d "
									+ "AND Person.idPerson = Person_Profession.idPerson "
									+ "AND Person.idPlaceOfBirth = City.idCity "
									+ "AND City.idCountry = %d "
									+ "ORDER BY RAND() "
									+ "LIMIT 3;", this.dbname, this.dbname, this.dbname, professionId, countryId );
				
				rs =this.dbh.executeQuery(query);//jdbc
				String personsList[] = new String[3];
				int i =0;
				while (rs.next()){
					
					String person = rs.getString("Name");
					personsList[i] = person;
					i++;
				}
				if (i!=3){
					return; // not enough details to make a question
				}
				q.setQuestion(String.format("%s, %s and %s are all...", personsList[0],personsList[1],personsList[2]));
				
				//get 3 other random professions
				query =  String.format("SELECT Name"
						+ " FROM %s.Profession "
						+ "WHERE Profession.idProfession != %d "
						+ "ORDER BY RAND() "
						+ "LIMIT 3;", this.dbname, professionId);
				
				rs = this.dbh.executeQuery(query);//jdbc
				i=0;
				while (rs.next()){
					
					String profession = rs.getString("Name");
					q.addPossibleAnswers(new Answer(profession+"s"));
					i++;
				}
				if (i!=3){
					return; // not enough details to make a question
				}
				
				q.setQuestionAsReady();
				possibleQuestions.add(q);
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
	private void generateWhosDeadQuestion(int numberOfQuestions){
		for(int j=0;j<numberOfQuestions;j++){
			Question q = new Question("Which of the following people is no longer with us?");
			
			// get a random dead person
			String query = String.format("SELECT Person.Name	"
										+ "FROM %s.Person, %s.City "
										+ "WHERE yearOfDeath!=0 AND yearOfBirth!=0 "
										+ "AND idPlaceOfBirth=City.idCity "
										+ "AND City.idCountry=%d "
										+ "ORDER BY RAND() "
										+ "LIMIT 1;",this.dbname, config.get_db_name() ,countryId );
			
			
			try {
				ResultSet rs = this.dbh.executeQuery(query);//jdbc
				if (!rs.first()){
					return;
				}
				String deadMan = rs.getString("Name");
				q.setCorrectAnswer(new Answer(deadMan));
				q.addPossibleAnswers(new Answer(deadMan));
				
				//get 3 random persons that are still alive"
				query = String.format("SELECT Person.Name	"
						+ "FROM %s.Person, %s.City "
						+ "WHERE yearOfDeath=0 AND yearOfBirth!=0 "
						+ "AND idPlaceOfBirth=City.idCity "
						+ "AND City.idCountry=%d "
						+ "ORDER BY RAND() "
						+ "LIMIT 3;",this.dbname, config.get_db_name(), countryId );
	
				
				rs =this.dbh.executeQuery(query);//jdbc
				
				int i =0;
				while (rs.next()){
					String alive = rs.getString("Name");
					q.addPossibleAnswers(new Answer(alive));
					i++;
				}
				if (i!=3){
					return; // not enough details to make a question
				}
				q.setQuestionAsReady();
				possibleQuestions.add(q);
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private ArrayList<Integer> randomIndexes(int range, int numToPick){
		ArrayList<Integer> picked = new ArrayList<Integer>();
		Random r = new Random();
		while(picked.size()<numToPick){
			int pick = r.nextInt(range);
			if (!picked.contains(pick)){
				picked.add(pick);
			}
		}
		return picked;
	}
	
	public String getCountry() {
		return countryName;
	}
	
	public int getCountryId() {
		return countryId;
	}
}
