import java.util.HashMap;
import java.util.Map;

public class Pagina {
	private char memoria;
	
	private Processos p;
	
	public Pagina (char memoria) {
		this.memoria = memoria;
	}
	
	public void setaProcesso(Processos p){
		this.p = p;
	}

}
