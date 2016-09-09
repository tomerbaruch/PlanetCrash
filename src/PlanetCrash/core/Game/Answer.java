package PlanetCrash.core.Game;

public class Answer {
	private String answer;
	
	public Answer(String answer) {
		this.answer=answer;
	}
	
	public String getAnswer() {
		return this.answer;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Answer && ((Answer) obj).getAnswer().equals(this.answer))
			return true;
		
		return false;
	}
}
