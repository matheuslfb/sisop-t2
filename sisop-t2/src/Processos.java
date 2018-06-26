import java.util.ArrayList;
import java.util.List;

public class Processos {

	private List<Pagina> paginas;
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
		this.paginas.add(p);
	}
	
	public Pagina getPagina(int posicaoMemoria){
		return paginas.get( posicaoMemoria / tamPagina );
	} 
	
	public boolean estaNaRam(int posicaoMemoria){
		return getPagina(posicaoMemoria).ram();
	} 
	
	public String toString(){return id;}
}
