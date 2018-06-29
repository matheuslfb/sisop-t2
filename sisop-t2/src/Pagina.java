import java.util.ArrayList;

public class Pagina {
	ArrayList<Endereco> enderecos;
	int indice;

	public Pagina(int indice) {
		enderecos = new ArrayList<>();
		this.indice = indice;
	}
}
