import java.util.ArrayList;

public class Memoria {

	ArrayList<Pagina> paginas;

	public Memoria(ArrayList<Pagina> paginas) {
		this.paginas = paginas;
	}

	public void limpar(int idProcesso) {
		for (Pagina p : paginas) {
			for (Endereco e : p.enderecos) {
				if (e.id != null && e.id == idProcesso) {
					e.id = null;
				}
			}
		}
	}

	/*
	 * 
	 */
	public void print() {
		for (Pagina p : this.paginas) {
			System.out.print("\n" + p.indice + " =>\t");
			for (Endereco e : p.enderecos) {
				if (e.id == null) {
					System.out.print("- -\t");
				} else {
					System.out.print("P" + e.id + "\t");
				}
			}
		}
	}
}
