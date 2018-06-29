
public class Processo {
	int id;
	int nextBloco;
	
	public Processo(int id) {
		this.id = id;
		this.nextBloco = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNextBloco() {
		return nextBloco;
	}

	public void setNextBloco(int nextBloco) {
		this.nextBloco = nextBloco;
	}
	
	
}
