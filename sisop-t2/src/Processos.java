import java.util.ArrayList;

public class Processos {

	private ArrayList paginas;
	private String id;
	private int tam;
	private int tamPagina;

	public Processos(String id, int tam, int tamPagina) {
		this.id = id;
		this.tam = tam;
		this.tamPagina = tamPagina;
		this.paginas = new ArrayList<>();
	}
	

	
	public void addPagina(Pagina p){
		paginas.add(p);
	}
	
	
	
	public String toString(){return id;}
}
